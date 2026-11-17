package com.example.payment_app.ui.screens.groups

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Holds all group data + the "create group" draft in one place.
 * Later you can move this into a ViewModel / repository without touching the screens.
 */
class GroupsState {
    val me = Member("Alex Taylor", "@alex", 0.0, "You · @alex", isYou = true, isCreator = true)

    /** Fake user directory for "search by username". */
    private val directory = listOf(
        Member("Jordan", "@jordan", note = "@jordan"),
        Member("Sam", "@sam", note = "@sam"),
        Member("Casey", "@casey", note = "@casey"),
        Member("Riley", "@riley", note = "@riley"),
        Member("Morgan", "@morgan", note = "@morgan")
    )

    val groups = mutableStateListOf<GroupSummary>().apply { addAll(sampleGroups) }

    private val memberMap = mutableStateMapOf(
        "1" to sampleMembers,
        "2" to listOf(
            me.copy(balance = -14.5),
            Member("Jordan", "@jordan", 14.5, "@jordan"),
            Member("Sam", "@sam", 0.0, "@sam")
        )
    )
    private val activityMap = mapOf("1" to sampleActivity)
    private val expenseCounts = mutableMapOf("1" to 3, "2" to 7)

    fun group(id: String?): GroupSummary? = groups.firstOrNull { it.id == id }
    fun membersOf(id: String): List<Member> = memberMap[id].orEmpty()
    fun activityOf(id: String): List<ActivityItem> = activityMap[id].orEmpty()

    // ---------- Create-group draft (screens 08 + 09) ----------
    var draftName by mutableStateOf("")
    var draftDescription by mutableStateOf("")
    var searchQuery by mutableStateOf("")
    val draftMembers = mutableStateListOf<Member>()

    fun startDraft() {
        draftName = ""
        draftDescription = ""
        searchQuery = ""
        draftMembers.clear()
        draftMembers.add(me)
    }

    /** Users matching the search box that are not already in [selected]. */
    fun searchResults(selected: List<Member>): List<Member> {
        val q = searchQuery.trim().removePrefix("@")
        return directory.filter { u ->
            selected.none { it.username == u.username } &&
                    (q.isBlank() || u.username.contains(q, ignoreCase = true) || u.name.contains(q, ignoreCase = true))
        }
    }

    /** Turns the draft into a real group and returns its id. */
    fun finishDraft(): String {
        val id = ((groups.maxOfOrNull { it.id.toIntOrNull() ?: 0 } ?: 0) + 1).toString()
        groups.add(GroupSummary(id, draftName.trim(), "", 0.0, draftDescription.trim()))
        memberMap[id] = draftMembers.toList()
        expenseCounts[id] = 0
        refreshSubtitle(id)
        return id
    }

    // ---------- Edit / manage (screens 10-12) ----------
    fun saveGroup(id: String, name: String, description: String) {
        val i = groups.indexOfFirst { it.id == id }
        if (i >= 0) groups[i] = groups[i].copy(name = name.trim().ifBlank { groups[i].name }, description = description.trim())
    }

    /** You can only leave when your own balance in the group is settled (0). */
    fun canLeave(id: String): Boolean = membersOf(id).firstOrNull { it.isYou }?.balance?.let { it == 0.0 } ?: true

    fun leaveGroup(id: String) {
        groups.removeAll { it.id == id }
        memberMap.remove(id)
    }

    fun addMember(id: String, member: Member) {
        val list = membersOf(id)
        if (list.none { it.username == member.username }) {
            memberMap[id] = list + member
            refreshSubtitle(id)
        }
    }

    /** Only members with a settled balance (and not yourself) can be removed. */
    fun removeMember(id: String, member: Member) {
        if (member.isYou || member.balance != 0.0) return
        memberMap[id] = membersOf(id).filterNot { it.username == member.username }
        refreshSubtitle(id)
    }

    private fun refreshSubtitle(id: String) {
        val i = groups.indexOfFirst { it.id == id }
        if (i >= 0) groups[i] = groups[i].copy(
            subtitle = "${membersOf(id).size} members · ${expenseCounts[id] ?: 0} expenses"
        )
    }
}