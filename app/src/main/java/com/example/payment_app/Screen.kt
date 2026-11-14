package com.example.payment_app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Home : Screen("home", "Home", Icons.Filled.Home)
    data object Groups : Screen("groups", "Groups", Icons.Filled.Group)
    data object Activity : Screen("activity", "Activity", Icons.Filled.History)
    data object Profile : Screen("profile", "Profile", Icons.Filled.Person)

    companion object {
        val bottomNavItems = listOf(Home, Groups, Activity, Profile)
    }
}

object ProfileRoutes {
    const val HOME = "profile_home"
    const val EDIT = "profile_edit"
    const val NOTIFICATIONS = "profile_notifications"

    /** Destinations that hide the bottom navigation bar. */
    val fullScreenRoutes = setOf(EDIT, NOTIFICATIONS)
}

object PaymentRoutes {
    const val PAYMENT_METHODS = "payment_methods"
    const val ADD_EDIT_CARD = "add_edit_card?cardId={cardId}"
    const val PAY_MY_SHARE = "pay_my_share"
    const val CONFIRM_PAYMENT = "confirm_payment"
    const val PAYMENT_RESULT = "payment_result"

    fun addEditCardRoute(cardId: String? = null): String {
        return if (cardId != null) "add_edit_card?cardId=$cardId" else "add_edit_card"
    }

    /** Destinations that hide the bottom navigation bar. */
    val fullScreenRoutes = setOf(
        "add_edit_card",
        "add_edit_card?cardId={cardId}",
        PAY_MY_SHARE,
        CONFIRM_PAYMENT
    )
}