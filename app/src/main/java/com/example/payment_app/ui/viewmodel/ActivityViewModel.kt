package com.example.payment_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.payment_app.data.repository.FakeOverviewRepository
import com.example.payment_app.data.repository.OverviewRepository
import com.example.payment_app.data.model.ActivityEntry
import com.example.payment_app.data.model.ActivityType
import com.example.payment_app.data.model.GroupSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class ActivityTypeFilter(val label: String) {
    ALL("All"),
    EXPENSES("Expenses"),
    SETTLEMENTS("Settlements"),
}

data class ActivityUiState(
    val typeFilter: ActivityTypeFilter = ActivityTypeFilter.ALL,
    val groupFilterId: String? = null,
    val groups: List<GroupSummary> = emptyList(),
    val entries: List<ActivityEntry> = emptyList(),
    val netBalanceCents: Long = 0,
    val isShowingCachedData: Boolean = false,
) {
    val selectedGroupName: String
        get() = groups.firstOrNull { it.id == groupFilterId }?.name ?: "All groups"
}

class ActivityViewModel(
    private val repository: OverviewRepository = FakeOverviewRepository(),
) : ViewModel() {

    private val allEntries: List<ActivityEntry> = repository.activity()

    private val _uiState = MutableStateFlow(
        repository.groups().let { groups ->
            ActivityUiState(
                groups = groups,
                entries = allEntries,
                netBalanceCents = groups.sumOf { it.balanceCents },
                isShowingCachedData = repository.isShowingCachedData(),
            )
        }
    )
    val uiState: StateFlow<ActivityUiState> = _uiState.asStateFlow()

    fun onTypeFilterChange(filter: ActivityTypeFilter) {
        _uiState.update { it.copy(typeFilter = filter, entries = filterEntries(allEntries, filter, it.groupFilterId)) }
    }

    fun onGroupFilterChange(groupId: String?) {
        _uiState.update { it.copy(groupFilterId = groupId, entries = filterEntries(allEntries, it.typeFilter, groupId)) }
    }

    companion object {
        fun filterEntries(
            entries: List<ActivityEntry>,
            typeFilter: ActivityTypeFilter,
            groupId: String?,
        ): List<ActivityEntry> = entries.filter { entry ->
            val typeMatches = when (typeFilter) {
                ActivityTypeFilter.ALL -> true
                ActivityTypeFilter.EXPENSES -> entry.type == ActivityType.EXPENSE
                ActivityTypeFilter.SETTLEMENTS -> entry.type == ActivityType.SETTLEMENT
            }
            typeMatches && (groupId == null || entry.groupId == groupId)
        }
    }
}
