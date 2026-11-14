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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.payment_app.ui.screens.ActivityScreen
import com.example.payment_app.ui.screens.HomeScreen
import com.example.payment_app.ui.screens.ProfileScreen
import com.example.payment_app.ui.screens.groups.GroupsState
import com.example.payment_app.ui.screens.profile.Currency
import com.example.payment_app.ui.screens.profile.EditProfileScreen
import com.example.payment_app.ui.screens.profile.NotificationSettings
import com.example.payment_app.ui.screens.profile.NotificationSettingsSaver
import com.example.payment_app.ui.screens.profile.NotificationSettingsScreen
import com.example.payment_app.ui.screens.profile.UserProfile
import com.example.payment_app.ui.screens.profile.UserProfileSaver

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

    val fullScreenRoutes = ProfileRoutes.fullScreenRoutes + GroupsRoutes.fullScreenRoutes
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
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
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
            composable(Screen.Home.route) { HomeScreen() }
            groupsGraph(navController, groupsState)
            composable(Screen.Activity.route) { ActivityScreen() }

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
        }
    }
}