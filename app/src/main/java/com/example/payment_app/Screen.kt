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

object GroupsRoutes {
    const val ARG_GROUP_ID = "groupId"

    const val HOME = "groups_home"
    const val CREATE = "group_create"
    const val CHOOSE_MEMBERS = "group_choose_members"
    const val OVERVIEW = "group_overview/{$ARG_GROUP_ID}"
    const val EDIT = "group_edit/{$ARG_GROUP_ID}"
    const val MANAGE = "group_manage/{$ARG_GROUP_ID}"
    const val ADD_MEMBERS = "group_add_members/{$ARG_GROUP_ID}"

    fun overview(id: String) = "group_overview/$id"
    fun edit(id: String) = "group_edit/$id"
    fun manage(id: String) = "group_manage/$id"
    fun addMembers(id: String) = "group_add_members/$id"

    /** Destinations that hide the bottom navigation bar (08, 09, 11 in the lo-fi + add members). */
    val fullScreenRoutes = setOf(CREATE, CHOOSE_MEMBERS, EDIT, ADD_MEMBERS)
}