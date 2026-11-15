package com.example.payment_app

import com.example.payment_app.data.model.ReceiptFileType
import com.example.payment_app.data.repository.FakeReceiptRepository
import com.example.payment_app.ui.screens.receipt.ReceiptRoutes
import com.example.payment_app.ui.viewmodel.ReceiptViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ReceiptViewModelTest {
    private val repository = FakeReceiptRepository()

    @Test
    fun loadsExpenseAndSampleReceipt() {
        val state = ReceiptViewModel("a1", repository).uiState.value
        assertEquals("Dinner", state.expense?.title)
        val receipt = assertNotNullAndGet(state.receipt)
        assertEquals(8_800L, receipt.lineItemsTotalCents)
    }

    @Test
    fun expenseWithoutReceipt_hasNoReceipt() {
        val state = ReceiptViewModel("a2", repository).uiState.value
        assertNotNull(state.expense)
        assertNull(state.receipt)
    }

    @Test
    fun unknownExpense_hasNoExpense() {
        assertNull(ReceiptViewModel("does-not-exist", repository).uiState.value.expense)
    }

    @Test
    fun pickingAFile_attachesOrReplacesTheReceipt() {
        val viewModel = ReceiptViewModel("a2", repository)

        viewModel.onReceiptPicked("content://files/groceries.pdf", "groceries.pdf", "application/pdf")
        val pdf = assertNotNullAndGet(viewModel.uiState.value.receipt)
        assertEquals("groceries.pdf", pdf.fileName)
        assertEquals(ReceiptFileType.PDF, pdf.fileType)
        assertEquals("content://files/groceries.pdf", pdf.fileUri)

        viewModel.onReceiptPicked("content://files/photo.jpg", "photo.jpg", "image/jpeg")
        val image = assertNotNullAndGet(viewModel.uiState.value.receipt)
        assertEquals("photo.jpg", image.fileName)
        assertEquals(ReceiptFileType.IMAGE, image.fileType)
    }

    @Test
    fun remove_asksForConfirmationFirst() {
        val viewModel = ReceiptViewModel("a1", repository)

        viewModel.onRemoveClick()
        assertTrue(viewModel.uiState.value.showRemoveConfirmation)
        assertNotNull(viewModel.uiState.value.receipt)

        viewModel.onRemoveDismissed()
        assertFalse(viewModel.uiState.value.showRemoveConfirmation)
        assertNotNull(viewModel.uiState.value.receipt)

        viewModel.onRemoveClick()
        viewModel.onRemoveConfirmed()
        assertFalse(viewModel.uiState.value.showRemoveConfirmation)
        assertNull(viewModel.uiState.value.receipt)
    }

    private fun <T> assertNotNullAndGet(value: T?): T {
        assertNotNull(value)
        return value!!
    }
}

class ReceiptRoutesTest {
    @Test
    fun forExpense_fillsInTheExpenseId() {
        assertEquals("receipt/a1", ReceiptRoutes.forExpense("a1"))
        assertEquals("receipt/{expenseId}", ReceiptRoutes.ROUTE)
    }
}
