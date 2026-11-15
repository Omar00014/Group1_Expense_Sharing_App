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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.payment_app.ui.screens.ActivityScreen
import com.example.payment_app.ui.screens.HomeScreen
import com.example.payment_app.ui.screens.ProfileScreen
import com.example.payment_app.ui.screens.expense.AddExpenseScreen
import com.example.payment_app.ui.screens.expense.EditExpenseScreen
import com.example.payment_app.ui.screens.expense.ExpenseDetailsScreen
import com.example.payment_app.ui.screens.expense.ExpenseGroup
import com.example.payment_app.ui.screens.expense.ExpenseItem
import com.example.payment_app.ui.screens.expense.ExpenseMockData
import com.example.payment_app.ui.screens.expense.SplitExpenseScreen
import com.example.payment_app.ui.screens.expense.calculateNetBalanceForMember
import com.example.payment_app.ui.screens.groups.GroupsState
import com.example.payment_app.ui.screens.payment.AddEditCardScreen
import com.example.payment_app.ui.screens.payment.ConfirmPaymentScreen
import com.example.payment_app.ui.screens.payment.PaymentCard
import com.example.payment_app.ui.screens.payment.PaymentMethodsScreen
import com.example.payment_app.ui.screens.payment.PaymentReceipt
import com.example.payment_app.ui.screens.payment.PaymentResultScreen
import com.example.payment_app.ui.screens.payment.PaymentShareContext
import com.example.payment_app.ui.screens.payment.PayMyShareScreen
import com.example.payment_app.ui.screens.payment.defaultSavedCards
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

    // Expense state
    var groups by remember { mutableStateOf(ExpenseMockData.sampleGroups) }
    var expenses by remember { mutableStateOf(ExpenseMockData.initialExpenses) }
    var selectedGroup by remember { mutableStateOf<ExpenseGroup?>(null) }
    var activeExpenseItem by remember { mutableStateOf<ExpenseItem?>(null) }

    val johnNetBalance = calculateNetBalanceForMember(ExpenseMockData.john.id, expenses)

    // Payment state
    var savedCards by remember { mutableStateOf(defaultSavedCards) }
    var activeShareContext by remember { mutableStateOf(PaymentShareContext()) }
    var selectedPaymentCard by remember {
        mutableStateOf<PaymentCard?>(savedCards.firstOrNull { it.isDefault } ?: savedCards.firstOrNull())
    }
    var activeReceipt by remember {
        mutableStateOf<PaymentReceipt?>(null)
    }

    val currentRoute = currentDestination?.route
    val isFullScreen = (ProfileRoutes.fullScreenRoutes + ExpenseRoutes.fullScreenRoutes + PaymentRoutes.fullScreenRoutes + GroupsRoutes.fullScreenRoutes).any { route ->
        currentRoute == route || (route.contains("?") && currentRoute?.startsWith(route.substringBefore("?")) == true)
    }
    val showBottomBar = !isFullScreen

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
            composable(Screen.Home.route) {
                HomeScreen(
                    currentUserId = ExpenseMockData.john.id,
                    groups = groups,
                    expenses = expenses,
                    onGroupSelectedForAdd = { grp ->
                        selectedGroup = grp
                        navController.navigate(ExpenseRoutes.ADD)
                    },
                    onExpenseSelected = { item ->
                        activeExpenseItem = item
                        navController.navigate(ExpenseRoutes.DETAILS)
                    },
                    onPayMyShare = { navController.navigate(PaymentRoutes.PAY_MY_SHARE) },
                    onPaymentMethods = { navController.navigate(PaymentRoutes.PAYMENT_METHODS) }
                )
            }
            groupsGraph(navController, groupsState)
            composable(Screen.Activity.route) { ActivityScreen() }

            // Expense Flow Screens
            composable(ExpenseRoutes.ADD) {
                AddExpenseScreen(
                    initialGroup = selectedGroup ?: ExpenseMockData.weekendTripGroup,
                    groups = groups,
                    members = ExpenseMockData.allMembers,
                    onContinueToSplit = { item ->
                        activeExpenseItem = item
                        navController.navigate(ExpenseRoutes.SPLIT)
                    },
                    onSaveDraft = { item ->
                        expenses = expenses + item
                        navController.popBackStack(Screen.Home.route, inclusive = false)
                    },
                    onDismiss = { navController.popBackStack() }
                )
            }

            composable(ExpenseRoutes.SPLIT) {
                SplitExpenseScreen(
                    expenseItem = activeExpenseItem ?: ExpenseMockData.sampleDinnerExpense,
                    onConfirmSplit = { item ->
                        expenses = expenses.filter { it.id != item.id } + item
                        activeExpenseItem = item
                        navController.navigate(ExpenseRoutes.DETAILS) {
                            popUpTo(Screen.Home.route)
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(ExpenseRoutes.DETAILS) {
                ExpenseDetailsScreen(
                    expenseItem = activeExpenseItem ?: ExpenseMockData.sampleDinnerExpense,
                    onEditExpense = { navController.navigate(ExpenseRoutes.EDIT) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(ExpenseRoutes.EDIT) {
                EditExpenseScreen(
                    expenseItem = activeExpenseItem ?: ExpenseMockData.sampleDinnerExpense,
                    groups = groups,
                    members = ExpenseMockData.allMembers,
                    onSave = { updated ->
                        expenses = expenses.map { if (it.id == updated.id) updated else it }
                        activeExpenseItem = updated
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() }
                )
            }

            // Payment Routes
            composable(PaymentRoutes.PAYMENT_METHODS) {
                PaymentMethodsScreen(
                    cards = savedCards,
                    onAddCard = { navController.navigate(PaymentRoutes.addEditCardRoute()) },
                    onManageCard = { cardId -> navController.navigate(PaymentRoutes.addEditCardRoute(cardId)) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = PaymentRoutes.ADD_EDIT_CARD,
                arguments = listOf(
                    navArgument("cardId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val cardId = backStackEntry.arguments?.getString("cardId")
                val cardToEdit = savedCards.firstOrNull { it.id == cardId }
                AddEditCardScreen(
                    cardToEdit = cardToEdit,
                    onSaveCard = { updatedCard ->
                        savedCards = if (savedCards.any { it.id == updatedCard.id }) {
                            savedCards.map { card ->
                                if (card.id == updatedCard.id) updatedCard
                                else if (updatedCard.isDefault) card.copy(isDefault = false)
                                else card
                            }
                        } else {
                            val list = if (updatedCard.isDefault) {
                                savedCards.map { it.copy(isDefault = false) }
                            } else {
                                savedCards
                            }
                            list + updatedCard
                        }
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() }
                )
            }

            composable(PaymentRoutes.PAY_MY_SHARE) {
                PayMyShareScreen(
                    context = activeShareContext,
                    savedCards = savedCards,
                    onBack = { navController.popBackStack() },
                    onClose = { navController.popBackStack() },
                    onProceedToConfirm = { card, note ->
                        selectedPaymentCard = card
                        activeShareContext = activeShareContext.copy(note = note)
                        navController.navigate(PaymentRoutes.CONFIRM_PAYMENT)
                    },
                    onAddNewCard = {
                        navController.navigate(PaymentRoutes.addEditCardRoute())
                    }
                )
            }

            composable(PaymentRoutes.CONFIRM_PAYMENT) {
                val card = selectedPaymentCard ?: savedCards.firstOrNull { it.isDefault } ?: defaultSavedCards.first()
                ConfirmPaymentScreen(
                    context = activeShareContext,
                    card = card,
                    senderName = profile.fullName,
                    onConfirm = {
                        activeReceipt = PaymentReceipt(
                            referenceId = "REF-AP-2026-0424",
                            formattedDate = "24 Apr 2026 · 09:41 AM",
                            fromUser = profile.fullName.split(" ").first(),
                            toUser = activeShareContext.fullRecipient,
                            groupName = activeShareContext.groupName,
                            status = "Paid",
                            amount = activeShareContext.amount,
                            cardMethod = card.displayCompact,
                            remainingDebt = 0.00
                        )
                        navController.navigate(PaymentRoutes.PAYMENT_RESULT)
                    },
                    onBack = { navController.popBackStack() },
                    onEditDetails = { navController.popBackStack() }
                )
            }

            composable(PaymentRoutes.PAYMENT_RESULT) {
                PaymentResultScreen(
                    receipt = activeReceipt ?: PaymentReceipt(),
                    onDone = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    onViewDetails = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }

            // Profile tab
            navigation(startDestination = ProfileRoutes.HOME, route = Screen.Profile.route) {
                composable(ProfileRoutes.HOME) {
                    ProfileScreen(
                        profile = profile,
                        netOwedBalance = johnNetBalance,
                        onCurrencyChange = { profile = profile.copy(currency = it) },
                        onEditProfile = { navController.navigate(ProfileRoutes.EDIT) },
                        onNotificationSettings = { navController.navigate(ProfileRoutes.NOTIFICATIONS) },
                        onPaymentMethods = { navController.navigate(PaymentRoutes.PAYMENT_METHODS) },
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
