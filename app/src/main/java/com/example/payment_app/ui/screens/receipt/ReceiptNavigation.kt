package com.example.payment_app.ui.screens.receipt

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.payment_app.data.model.ReceiptExpense

object ReceiptRoutes {
    const val EXPENSE_ID = "expenseId"
    const val ROUTE = "receipt/{$EXPENSE_ID}"

    fun forExpense(expenseId: String): String = "receipt/$expenseId"
}

fun NavController.navigateToReceipt(expenseId: String) {
    navigate(ReceiptRoutes.forExpense(expenseId))
}

fun NavGraphBuilder.receiptScreen(
    onBack: () -> Unit,
    findExpense: (expenseId: String) -> ReceiptExpense? = { null },
) {
    composable(
        route = ReceiptRoutes.ROUTE,
        arguments = listOf(navArgument(ReceiptRoutes.EXPENSE_ID) { type = NavType.StringType }),
    ) { backStackEntry ->
        val expenseId = backStackEntry.arguments?.getString(ReceiptRoutes.EXPENSE_ID).orEmpty()
        ReceiptScreen(
            expenseId = expenseId,
            onBack = onBack,
            fallbackExpense = findExpense(expenseId),
        )
    }
}
