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