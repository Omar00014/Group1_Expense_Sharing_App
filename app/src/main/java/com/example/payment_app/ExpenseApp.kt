package com.example.payment_app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.payment_app.ui.screens.ActivityScreen
import com.example.payment_app.ui.screens.HomeScreen
import com.example.payment_app.ui.screens.NotificationsScreen
import com.example.payment_app.ui.screens.ProfileScreen
import com.example.payment_app.ui.screens.groups.GroupsState
import com.example.payment_app.ui.screens.profile.Currency
import com.example.payment_app.ui.screens.profile.EditProfileScreen
import com.example.payment_app.ui.screens.profile.NotificationSettings
import com.example.payment_app.ui.screens.profile.NotificationSettingsSaver
import com.example.payment_app.ui.screens.profile.NotificationSettingsScreen
import com.example.payment_app.ui.screens.profile.UserProfile
import com.example.payment_app.ui.screens.profile.UserProfileSaver
import com.example.payment_app.ui.screens.receipt.ReceiptRoutes
import com.example.payment_app.ui.screens.receipt.navigateToReceipt
import com.example.payment_app.ui.screens.receipt.receiptScreen

private object ActivityRoutes {
    const val HISTORY = "activity/history"
    const val NOTIFICATIONS = "activity/notifications"
}

/** Switch bottom-nav tab the same way the navigation bar does. */
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
    val groupsState = remember { GroupsState() }

    var profile by rememberSaveable(stateSaver = UserProfileSaver) {
        mutableStateOf(
            UserProfile(
                fullName = "John Doe",
                username = "@JD01",
                email = "johndoe@example.com",
                currency = Currency.USD
            )
        )
    }
    var notificationSettings by rememberSaveable(stateSaver = NotificationSettingsSaver) {
        mutableStateOf(NotificationSettings())
    }

    val fullScreenRoutes = ProfileRoutes.fullScreenRoutes + GroupsRoutes.fullScreenRoutes + ReceiptRoutes.ROUTE
    val showBottomBar = currentDestination?.route !in fullScreenRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
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
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onAddExpense = { /* TODO: Add expense (13 · Choose group) */ },
                    onPayShare = { /* TODO: Pay my share (27) */ },
                    onSeeAllGroups = { navController.navigateToTab(Screen.Groups.route) },
                    onGroupClick = { groupId -> navController.navigate(GroupsRoutes.overview(groupId)) },
                    onSeeAllActivity = { navController.navigateToTab(Screen.Activity.route) },
                    onProfileClick = { navController.navigateToTab(Screen.Profile.route) },
                )
            }

            groupsGraph(navController, groupsState)

            // Activity tab: nested graph so the tab stays highlighted on Notifications
            navigation(startDestination = ActivityRoutes.HISTORY, route = Screen.Activity.route) {
                composable(ActivityRoutes.HISTORY) {
                    ActivityScreen(
                        onOpenNotifications = { navController.navigate(ActivityRoutes.NOTIFICATIONS) },
                        // Temporary: opens the receipt until Expense details (16) exists
                        onEntryClick = { entryId -> navController.navigateToReceipt(entryId) },
                    )
                }
                composable(ActivityRoutes.NOTIFICATIONS) {
                    NotificationsScreen(
                        onBack = { navController.popBackStack() },
                        onOpenSettings = { navController.navigate(ProfileRoutes.NOTIFICATIONS) },
                        onNotificationClick = { /* TODO: open the expense / payment it is about */ },
                    )
                }
            }

            // Profile tab: nested graph so the tab stays highlighted on sub-screens
            navigation(startDestination = ProfileRoutes.HOME, route = Screen.Profile.route) {
                composable(ProfileRoutes.HOME) {
                    ProfileScreen(
                        profile = profile,
                        onCurrencyChange = { profile = profile.copy(currency = it) },
                        onEditProfile = { navController.navigate(ProfileRoutes.EDIT) },
                        onNotificationSettings = { navController.navigate(ProfileRoutes.NOTIFICATIONS) },
                        onPaymentMethods = { /* TODO */ },
                        onLogout = { /* TODO */ }
                    )
                }
                composable(ProfileRoutes.EDIT) {
                    EditProfileScreen(
                        profile = profile,
                        onSave = {
                            profile = it
                            navController.popBackStack()
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(ProfileRoutes.NOTIFICATIONS) {
                    NotificationSettingsScreen(
                        settings = notificationSettings,
                        onSave = {
                            notificationSettings = it
                            navController.popBackStack()
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            // 24 · Receipt. Other pages open it with navController.navigateToReceipt(expenseId)
            receiptScreen(onBack = { navController.popBackStack() })
        }
    }
}
