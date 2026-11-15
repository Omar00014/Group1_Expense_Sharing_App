package com.example.payment_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.payment_app.data.model.Receipt
import com.example.payment_app.data.model.ReceiptExpense
import com.example.payment_app.data.model.ReceiptFileType
import com.example.payment_app.data.repository.FakeReceiptRepository
import com.example.payment_app.data.repository.ReceiptRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ReceiptUiState(
    val expense: ReceiptExpense? = null,
    val receipt: Receipt? = null,
    val showRemoveConfirmation: Boolean = false,
)

class ReceiptViewModel(
    expenseId: String,
    repository: ReceiptRepository = FakeReceiptRepository(),
    fallbackExpense: ReceiptExpense? = null,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ReceiptUiState(
            expense = repository.expense(expenseId) ?: fallbackExpense,
            receipt = repository.receipt(expenseId),
        )
    )
    val uiState: StateFlow<ReceiptUiState> = _uiState.asStateFlow()

    fun onReceiptPicked(fileUri: String, fileName: String, mimeType: String?) {
        val receipt = Receipt(
            fileName = fileName,
            fileType = if (mimeType == "application/pdf") ReceiptFileType.PDF else ReceiptFileType.IMAGE,
            fileUri = fileUri,
        )
        _uiState.update { it.copy(receipt = receipt) }
    }

    fun onRemoveClick() {
        _uiState.update { it.copy(showRemoveConfirmation = true) }
    }

    fun onRemoveConfirmed() {
        _uiState.update { it.copy(receipt = null, showRemoveConfirmation = false) }
    }

    fun onRemoveDismissed() {
        _uiState.update { it.copy(showRemoveConfirmation = false) }
    }
}
