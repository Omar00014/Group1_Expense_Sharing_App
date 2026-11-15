package com.example.payment_app

import com.example.payment_app.ui.screens.payment.CardBrand
import com.example.payment_app.ui.screens.payment.PaymentCard
import com.example.payment_app.ui.screens.payment.PaymentReceipt
import com.example.payment_app.ui.screens.payment.PaymentShareContext
import com.example.payment_app.ui.screens.payment.defaultSavedCards
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PaymentModelsTest {

    @Test
    fun paymentCard_displaysCorrectFormattedStrings() {
        val card = PaymentCard(
            id = "test_1",
            cardholderName = "Alex Taylor",
            cardNumberMasked = "•••• •••• •••• 4242",
            last4 = "4242",
            expiryDate = "12/28",
            brand = CardBrand.VISA,
            isDefault = true
        )

        assertEquals("Visa ending •••• 4242", card.displayTitle)
        assertEquals("Visa •••• 4242", card.displayCompact)
        assertTrue(card.isDefault)
        assertFalse(card.isBackup)
    }

    @Test
    fun paymentShareContext_displaysExpectedSummaryAndAmount() {
        val context = PaymentShareContext(
            groupName = "Apartment",
            membersCount = 3,
            expensesCount = 7,
            recipientName = "Maya",
            recipientHandle = "@maya",
            amount = 14.50
        )

        assertEquals("$14.50", context.formattedAmount)
        assertEquals("3 members · 7 expenses", context.groupSubtitle)
        assertEquals("You owe Maya $14.50 for Apartment.", context.contextSummary)
        assertEquals("Maya @maya", context.fullRecipient)
    }

    @Test
    fun paymentReceipt_formatsCorrectly() {
        val receipt = PaymentReceipt(
            referenceId = "REF-AP-2026-0424",
            amount = 14.50,
            remainingDebt = 0.00
        )

        assertEquals("REF-AP-2026-0424", receipt.referenceId)
        assertEquals("$14.50", receipt.formattedAmount)
        assertEquals("$0.00", receipt.formattedRemainingDebt)
        assertEquals("Paid", receipt.status)
    }

    @Test
    fun defaultCards_containExpectedInitialCards() {
        assertEquals(2, defaultSavedCards.size)

        val defaultCard = defaultSavedCards.first { it.isDefault }
        assertEquals("4242", defaultCard.last4)
        assertEquals(CardBrand.VISA, defaultCard.brand)

        val backupCard = defaultSavedCards.first { it.isBackup }
        assertEquals("1881", backupCard.last4)
        assertEquals(CardBrand.MASTERCARD, backupCard.brand)
    }
}
