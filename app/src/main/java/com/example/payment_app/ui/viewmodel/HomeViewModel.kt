package com.example.payment_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.payment_app.data.repository.FakeOverviewRepository
import com.example.payment_app.data.repository.OverviewRepository
import com.example.payment_app.data.model.ActivityEntry
import com.example.payment_app.data.model.GroupSummary
import com.example.payment_app.ui.components.greetingForHour
import java.util.Calendar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUiState(
    val greeting: String,
    val userFirstName: String,
    val netBalanceCents: Long,
    val groups: List<GroupSummary>,
    val recentActivity: List<ActivityEntry>,
)


class HomeViewModel(
    repository: OverviewRepository = FakeOverviewRepository(),
    currentHour: () -> Int = { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) },
) : ViewModel() {

    private val _uiState = MutableStateFlow(buildState(repository, currentHour()))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    companion object {
        const val RECENT_ACTIVITY_LIMIT = 3

        fun buildState(repository: OverviewRepository, hour: Int): HomeUiState {
            val groups = repository.groups()
            return HomeUiState(
                greeting = greetingForHour(hour),
                userFirstName = repository.currentUserFirstName(),
                netBalanceCents = groups.sumOf { it.balanceCents },
                groups = groups,
                recentActivity = repository.activity().take(RECENT_ACTIVITY_LIMIT),
            )
        }
    }
}
