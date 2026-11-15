package com.example.payment_app

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.payment_app.ui.screens.GroupsScreen
import com.example.payment_app.ui.screens.groups.*

private val groupIdArg = listOf(navArgument(GroupsRoutes.ARG_GROUP_ID) { type = NavType.StringType })

/** Reads the groupId argument and renders [content] only while that group exists. */
@Composable
private fun WithGroup(
    entry: NavBackStackEntry,
    state: GroupsState,
    content: @Composable (id: String, group: GroupSummary) -> Unit
) {
    val id = entry.arguments?.getString(GroupsRoutes.ARG_GROUP_ID).orEmpty()
    state.group(id)?.let { content(id, it) }
}

/** The whole Groups tab: screens 07-12 (+ "add members" reusing screen 09). */
fun NavGraphBuilder.groupsGraph(navController: NavHostController, state: GroupsState) {
    navigation(startDestination = GroupsRoutes.HOME, route = Screen.Groups.route) {

        // 07 · Groups
        composable(GroupsRoutes.HOME) {
            GroupsScreen(
                groups = state.groups,
                onCreateGroup = {
                    state.startDraft()
                    navController.navigate(GroupsRoutes.CREATE)
                },
                onGroupClick = { navController.navigate(GroupsRoutes.overview(it.id)) }
            )
        }

        // 08 · Create group
        composable(GroupsRoutes.CREATE) {
            CreateGroupScreen(
                initialName = state.draftName,
                initialDescription = state.draftDescription,
                onBack = { navController.popBackStack() },
                onClose = { navController.popBackStack(GroupsRoutes.HOME, inclusive = false) },
                onContinue = { name, description ->
                    state.draftName = name
                    state.draftDescription = description
                    navController.navigate(GroupsRoutes.CHOOSE_MEMBERS)
                }
            )
        }

        // 09 · Choose / add members
        composable(GroupsRoutes.CHOOSE_MEMBERS) {
            ChooseMembersScreen(
                selected = state.draftMembers,
                searchResults = state.searchResults(state.draftMembers),
                onSearch = { state.searchQuery = it },
                onAdd = { state.draftMembers.add(it) },
                onRemove = { m -> state.draftMembers.removeAll { it.username == m.username } },
                onBack = { navController.popBackStack() },
                onFinish = {
                    val id = state.finishDraft()
                    navController.navigate(GroupsRoutes.overview(id)) {
                        popUpTo(GroupsRoutes.HOME) // drop create + choose from the back stack
                    }
                }
            )
        }

        // 10 · Group overview
        composable(GroupsRoutes.OVERVIEW, arguments = groupIdArg) { entry ->
            WithGroup(entry, state) { id, group ->
                GroupOverviewScreen(
                    group = group,
                    members = state.membersOf(id),
                    activity = state.activityOf(id),
                    onBack = { navController.popBackStack() },
                    onEdit = { navController.navigate(GroupsRoutes.edit(id)) },
                    onAddExpense = { /* TODO */ },
                    onViewBalances = { /* TODO */ },
                    onManageMembers = { navController.navigate(GroupsRoutes.manage(id)) },
                    onSeeAllActivity = { /* TODO */ }
                )
            }
        }

        // 11 · Edit group / leave
        composable(GroupsRoutes.EDIT, arguments = groupIdArg) { entry ->
            val context = LocalContext.current
            WithGroup(entry, state) { id, group ->
                EditGroupScreen(
                    group = group,
                    onBack = { navController.popBackStack() },
                    onSave = { name, description ->
                        state.saveGroup(id, name, description)
                        navController.popBackStack()
                    },
                    onManageMembers = { navController.navigate(GroupsRoutes.manage(id)) },
                    onKeepGroup = { navController.popBackStack() },
                    onConfirmLeave = {
                        if (state.canLeave(id)) {
                            navController.popBackStack(GroupsRoutes.HOME, inclusive = false)
                            state.leaveGroup(id) // remove after navigating away
                        } else {
                            Toast.makeText(context, "Settle your balance before leaving", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }

        // 12 · Manage / remove members
        composable(GroupsRoutes.MANAGE, arguments = groupIdArg) { entry ->
            WithGroup(entry, state) { id, group ->
                ManageMembersScreen(
                    group = group,
                    members = state.membersOf(id),
                    onBack = { navController.popBackStack() },
                    onAddMember = { navController.navigate(GroupsRoutes.addMembers(id)) },
                    onSearch = { navController.navigate(GroupsRoutes.addMembers(id)) },
                    onRemoveMember = { state.removeMember(id, it) }
                )
            }
        }

        // Add members to an existing group (reuses screen 09)
        composable(GroupsRoutes.ADD_MEMBERS, arguments = groupIdArg) { entry ->
            WithGroup(entry, state) { id, _ ->
                androidx.compose.runtime.LaunchedEffect(Unit) { state.searchQuery = "" }
                val current = state.membersOf(id)
                ChooseMembersScreen(
                    selected = current,
                    searchResults = state.searchResults(current),
                    onSearch = { state.searchQuery = it },
                    onAdd = { state.addMember(id, it) },
                    onRemove = { state.removeMember(id, it) },
                    onBack = { navController.popBackStack() },
                    onFinish = { navController.popBackStack() }
                )
            }
        }
    }
}