package com.example.payment_app

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.example.payment_app.ui.screens.ActivityScreen
import com.example.payment_app.ui.screens.GroupsScreen
import com.example.payment_app.ui.screens.HomeScreen
import com.example.payment_app.ui.screens.NotificationsScreen
import com.example.payment_app.ui.screens.ProfileScreen
import com.example.payment_app.ui.screens.receipt.navigateToReceipt
import com.example.payment_app.ui.screens.receipt.receiptScreen

private object ActivityRoutes {
    const val HISTORY = "activity/history"
    const val NOTIFICATIONS = "activity/notifications"
}

private fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun ExpenseApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                Screen.bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true,
                        onClick = { navController.navigateToTab(screen.route) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onAddExpense = {},
                    onPayShare = {},
                    onSeeAllGroups = { navController.navigateToTab(Screen.Groups.route) },
                    onGroupClick = { navController.navigateToTab(Screen.Groups.route) },
                    onSeeAllActivity = { navController.navigateToTab(Screen.Activity.route) },
                    onProfileClick = { navController.navigateToTab(Screen.Profile.route) },
                )
            }
            composable(Screen.Groups.route) { GroupsScreen() }
            navigation(startDestination = ActivityRoutes.HISTORY, route = Screen.Activity.route) {
                composable(ActivityRoutes.HISTORY) {
                    ActivityScreen(
                        onOpenNotifications = { navController.navigate(ActivityRoutes.NOTIFICATIONS) },
                        onEntryClick = { entryId -> navController.navigateToReceipt(entryId) },
                    )
                }
                composable(ActivityRoutes.NOTIFICATIONS) {
                    NotificationsScreen(
                        onBack = { navController.popBackStack() },
                        onOpenSettings = { navController.navigateToTab(Screen.Profile.route) },
                        onNotificationClick = {},
                    )
                }
            }
            composable(Screen.Profile.route) { ProfileScreen() }
            receiptScreen(onBack = { navController.popBackStack() })
        }
    }
}

