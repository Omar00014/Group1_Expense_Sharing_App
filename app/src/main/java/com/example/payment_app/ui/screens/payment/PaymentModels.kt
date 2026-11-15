package com.example.payment_app.ui.screens.payment

import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.Saver

enum class CardBrand(val displayName: String) {
    VISA("Visa"),
    MASTERCARD("Mastercard"),
    AMEX("American Express"),
    OTHER("Card")
}

data class PaymentCard(
    val id: String,
    val cardholderName: String,
    val cardNumberMasked: String,
    val last4: String,
    val expiryDate: String,
    val brand: CardBrand = CardBrand.VISA,
    val isDefault: Boolean = false,
    val isBackup: Boolean = false
) {
    val displayTitle: String
        get() = "${brand.displayName} ending •••• $last4"

    val displayCompact: String
        get() = "${brand.displayName} •••• $last4"
}

val PaymentCardSaver: Saver<PaymentCard, Any> = mapSaver(
    save = {
        mapOf(
            "id" to it.id,
            "cardholderName" to it.cardholderName,
            "cardNumberMasked" to it.cardNumberMasked,
            "last4" to it.last4,
            "expiryDate" to it.expiryDate,
            "brand" to it.brand.name,
            "isDefault" to it.isDefault,
            "isBackup" to it.isBackup
        )
    },
    restore = { map ->
        PaymentCard(
            id = map["id"] as String,
            cardholderName = map["cardholderName"] as String,
            cardNumberMasked = map["cardNumberMasked"] as String,
            last4 = map["last4"] as String,
            expiryDate = map["expiryDate"] as String,
            brand = CardBrand.valueOf(map["brand"] as String),
            isDefault = map["isDefault"] as Boolean,
            isBackup = map["isBackup"] as Boolean
        )
    }
)

data class PaymentShareContext(
    val groupName: String = "Apartment",
    val membersCount: Int = 3,
    val expensesCount: Int = 7,
    val recipientName: String = "Maya",
    val recipientHandle: String = "@maya",
    val amount: Double = 14.50,
    val note: String = ""
) {
    val formattedAmount: String
        get() = String.format("$%.2f", amount)

    val groupSubtitle: String
        get() = "$membersCount members · $expensesCount expenses"

    val contextSummary: String
        get() = "You owe $recipientName $formattedAmount for $groupName."

    val fullRecipient: String
        get() = "$recipientName $recipientHandle"
}

val PaymentShareContextSaver: Saver<PaymentShareContext, Any> = mapSaver(
    save = {
        mapOf(
            "groupName" to it.groupName,
            "membersCount" to it.membersCount,
            "expensesCount" to it.expensesCount,
            "recipientName" to it.recipientName,
            "recipientHandle" to it.recipientHandle,
            "amount" to it.amount,
            "note" to it.note
        )
    },
    restore = { map ->
        PaymentShareContext(
            groupName = map["groupName"] as String,
            membersCount = map["membersCount"] as Int,
            expensesCount = map["expensesCount"] as Int,
            recipientName = map["recipientName"] as String,
            recipientHandle = map["recipientHandle"] as String,
            amount = (map["amount"] as Number).toDouble(),
            note = map["note"] as String
        )
    }
)

data class PaymentReceipt(
    val referenceId: String = "REF-AP-2026-0424",
    val formattedDate: String = "24 Apr 2026 · 09:41 AM",
    val fromUser: String = "Alex",
    val toUser: String = "Maya @maya",
    val groupName: String = "Apartment",
    val status: String = "Paid",
    val amount: Double = 14.50,
    val cardMethod: String = "Visa •••• 4242",
    val remainingDebt: Double = 0.00
) {
    val formattedAmount: String
        get() = String.format("$%.2f", amount)

    val formattedRemainingDebt: String
        get() = String.format("$%.2f", remainingDebt)
}

val defaultSavedCards = listOf(
    PaymentCard(
        id = "card_1",
        cardholderName = "Alex Taylor",
        cardNumberMasked = "•••• •••• •••• 4242",
        last4 = "4242",
        expiryDate = "12/28",
        brand = CardBrand.VISA,
        isDefault = true,
        isBackup = false
    ),
    PaymentCard(
        id = "card_2",
        cardholderName = "Alex Taylor",
        cardNumberMasked = "•••• •••• •••• 1881",
        last4 = "1881",
        expiryDate = "12/28",
        brand = CardBrand.MASTERCARD,
        isDefault = false,
        isBackup = true
    )
)
