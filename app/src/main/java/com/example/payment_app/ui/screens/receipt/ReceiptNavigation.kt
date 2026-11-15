package com.example.payment_app.ui.screens.receipt

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

object ReceiptRoutes {
    const val EXPENSE_ID = "expenseId"
    const val ROUTE = "receipt/{$EXPENSE_ID}"

    fun forExpense(expenseId: String): String = "receipt/$expenseId"
}

fun NavController.navigateToReceipt(expenseId: String) {
    navigate(ReceiptRoutes.forExpense(expenseId))
}

fun NavGraphBuilder.receiptScreen(onBack: () -> Unit) {
    composable(
        route = ReceiptRoutes.ROUTE,
        arguments = listOf(navArgument(ReceiptRoutes.EXPENSE_ID) { type = NavType.StringType }),
    ) { backStackEntry ->
        ReceiptScreen(
            expenseId = backStackEntry.arguments?.getString(ReceiptRoutes.EXPENSE_ID).orEmpty(),
            onBack = onBack,
        )
    }
}
