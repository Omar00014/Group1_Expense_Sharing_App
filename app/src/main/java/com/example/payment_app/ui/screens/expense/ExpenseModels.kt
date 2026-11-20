package com.example.payment_app.ui.screens.expense

import androidx.compose.runtime.Immutable
import java.util.Locale

@Immutable
data class ExpenseMember(
    val id: String,
    val fullName: String,
    val username: String,
    val avatarInitials: String = fullName.take(1).uppercase()
)

@Immutable
data class ExpenseGroup(
    val id: String,
    val name: String,
    val memberCount: Int,
    val expenseCount: Int,
    val netBalance: Double = 0.0
) {
    val netBalanceFormatted: String
        get() = when {
            netBalance > 0 -> "+$${String.format(Locale.US, "%.2f", netBalance)} owed to you"
            netBalance < 0 -> "-$${String.format(Locale.US, "%.2f", -netBalance)} you owe"
            else -> "Settled up"
        }

    val isOwed: Boolean get() = netBalance >= 0
}

@Immutable
data class ExpenseSplit(
    val member: ExpenseMember,
    val amount: Double,
    val isSelected: Boolean = true
)

@Immutable
data class ExpenseItem(
    val id: String,
    val title: String,
    val amount: Double,
    val currency: String = "USD",
    val payer: ExpenseMember,
    val group: ExpenseGroup,
    val date: String,
    val splitType: String = "Equal split",
    val splits: List<ExpenseSplit>,
    val receiptName: String? = null
) {
    val amountFormatted: String
        get() = "$${String.format(Locale.US, "%.2f", amount)}"
}

fun calculateNetBalanceForMember(memberId: String, expenses: List<ExpenseItem>): Double {
    var total = 0.0
    for (expense in expenses) {
        if (expense.payer.id == memberId) {
            total += expense.splits.filter { it.isSelected && it.member.id != memberId }.sumOf { it.amount }
        } else {
            val mySplit = expense.splits.firstOrNull { it.isSelected && it.member.id == memberId }
            if (mySplit != null) {
                total -= mySplit.amount
            }
        }
    }
    return total
}

/*fun calculateGroupNetBalanceForMember(memberId: String, groupId: String, expenses: List<ExpenseItem>): Double {
    val groupExpenses = expenses.filter { it.group.id == groupId }
    return calculateNetBalanceForMember(memberId, groupExpenses)
}

fun formatNetBalance(balance: Double): String {
    val absVal = kotlin.math.abs(balance)
    val formattedVal = String.format(Locale.US, "%.2f", absVal)
    return when {
        balance > 0 -> "+$$formattedVal"
        balance < 0 -> "-$$formattedVal"
        else -> "$0.00"
    }
}*/

object ExpenseMockData {
    val alex = ExpenseMember("1", "Alex Taylor", "@alex", "A")
    val jordan = ExpenseMember("2", "Jordan", "@jordan", "J")
    val sam = ExpenseMember("3", "Sam", "@sam", "S")
    val casey = ExpenseMember("4", "Casey", "@casey", "C")

    val allMembers = listOf(alex, jordan, sam, casey)

    val weekendTripGroup = ExpenseGroup(
        id = "g1",
        name = "Weekend Trip",
        memberCount = 4,
        expenseCount = 3,
        netBalance = 66.00
    )

    val apartmentGroup = ExpenseGroup(
        id = "g2",
        name = "Apartment",
        memberCount = 3,
        expenseCount = 7,
        netBalance = -14.50
    )

    val sampleGroups = listOf(weekendTripGroup, apartmentGroup)

    val dinnerSplits = listOf(
        ExpenseSplit(casey, 22.00, true),
        ExpenseSplit(alex, 22.00, true),
        ExpenseSplit(jordan, 22.00, true),
        ExpenseSplit(sam, 22.00, true)
    )

    val groceriesSplits = listOf(
        ExpenseSplit(jordan, 14.50, true),
        ExpenseSplit(alex, 14.50, true),
        ExpenseSplit(casey, 14.50, true)
    )

    val sampleDinnerExpense = ExpenseItem(
        id = "e1",
        title = "Dinner",
        amount = 88.00,
        currency = "USD",
        payer = alex,
        group = weekendTripGroup,
        date = "Today, 12:30 PM",
        splitType = "Equal split",
        splits = dinnerSplits,
        receiptName = "Dinner receipt"
    )

    val sampleGroceriesExpense = ExpenseItem(
        id = "e2",
        title = "Groceries",
        amount = 43.50,
        currency = "USD",
        payer = jordan,
        group = apartmentGroup,
        date = "Yesterday",
        splitType = "Equal split",
        splits = groceriesSplits,
        receiptName = null
    )

    val initialExpenses = listOf(sampleDinnerExpense, sampleGroceriesExpense)
}
