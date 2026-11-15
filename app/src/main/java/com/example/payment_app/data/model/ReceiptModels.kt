package com.example.payment_app.data.model

data class ReceiptExpense(
    val expenseId: String,
    val title: String,
    val groupName: String,
    val amountCents: Long,
    val paidByName: String,
    val splitCount: Int,
)

enum class ReceiptFileType { PDF, IMAGE }

data class ReceiptLineItem(
    val label: String,
    val amountCents: Long,
)

data class Receipt(
    val fileName: String,
    val fileType: ReceiptFileType,
    val fileUri: String? = null,
    val merchant: String? = null,
    val dateLabel: String? = null,
    val lineItems: List<ReceiptLineItem> = emptyList(),
) {
    val lineItemsTotalCents: Long get() = lineItems.sumOf { it.amountCents }
}
