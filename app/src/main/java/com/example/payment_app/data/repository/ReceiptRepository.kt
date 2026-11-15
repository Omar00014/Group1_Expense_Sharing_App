package com.example.payment_app.data.repository

import com.example.payment_app.data.model.Receipt
import com.example.payment_app.data.model.ReceiptExpense
import com.example.payment_app.data.model.ReceiptFileType
import com.example.payment_app.data.model.ReceiptLineItem

interface ReceiptRepository {
    fun expense(expenseId: String): ReceiptExpense?
    fun receipt(expenseId: String): Receipt?
}

class FakeReceiptRepository : ReceiptRepository {

    private val dinnerReceipt = Receipt(
        fileName = "Dinner receipt",
        fileType = ReceiptFileType.PDF,
        merchant = "Northside Bistro",
        dateLabel = "12 Mar 2026",
        lineItems = listOf(
            ReceiptLineItem("Starter", 1_400),
            ReceiptLineItem("Main course", 4_200),
            ReceiptLineItem("Drinks", 1_800),
            ReceiptLineItem("Tip", 1_400),
        ),
    )

    private val expenses = listOf(
        ReceiptExpense("e1", "Dinner", "Weekend Trip", 8_800, "John Doe", 4),
        ReceiptExpense("e2", "Groceries", "Apartment", 4_350, "Jordan", 3),
        ReceiptExpense("a1", "Dinner", "Weekend Trip", 8_800, "Jordan", 4),
        ReceiptExpense("a2", "Groceries", "Apartment", 4_350, "You", 3),
        ReceiptExpense("a4", "Internet", "Apartment", 3_000, "Maya", 3),
        ReceiptExpense("a5", "Fuel", "Weekend Trip", 6_400, "You", 4),
    ).associateBy { it.expenseId }

    private val receipts = mapOf(
        "e1" to dinnerReceipt,
        "a1" to dinnerReceipt,
    )

    override fun expense(expenseId: String): ReceiptExpense? = expenses[expenseId]

    override fun receipt(expenseId: String): Receipt? = receipts[expenseId]
}
