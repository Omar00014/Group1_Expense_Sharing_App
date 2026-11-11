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

/** Screens inside the Activity tab. Grouped in a nested graph so the Activity tab stays selected. */
private object ActivityRoutes {
    const val HISTORY = "activity/history"
    const val NOTIFICATIONS = "activity/notifications"
}

/** Switch bottom-nav tab the same way the navigation bar does. */
private fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        // Avoid building up a large back stack
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination
        launchSingleTop = true
        // Restore state when reselecting a tab
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
                    // TODO: open the Add expense flow (13 · Choose group) when the Expenses pages exist
                    onAddExpense = {},
                    // TODO: open 27 · Pay my share when the Card payments pages exist
                    onPayShare = {},
                    onSeeAllGroups = { navController.navigateToTab(Screen.Groups.route) },
                    // TODO: open 10 · Group overview for this group when it exists
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
                        // TODO: open 16 · Expense details when it exists
                        onEntryClick = {},
                    )
                }
                composable(ActivityRoutes.NOTIFICATIONS) {
                    NotificationsScreen(
                        onBack = { navController.popBackStack() },
                        // TODO: open 23 · Notification settings when it exists
                        onOpenSettings = { navController.navigateToTab(Screen.Profile.route) },
                        // TODO: open the expense / payment the notification is about
                        onNotificationClick = {},
                    )
                }
            }
            composable(Screen.Profile.route) { ProfileScreen() }
        }
    }
}

