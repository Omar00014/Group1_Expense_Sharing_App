package com.example.payment_app

import com.example.payment_app.data.repository.FakeOverviewRepository
import com.example.payment_app.ui.viewmodel.ActivityTypeFilter
import com.example.payment_app.ui.viewmodel.ActivityViewModel
import com.example.payment_app.ui.viewmodel.HomeViewModel
import com.example.payment_app.data.model.ActivityType
import com.example.payment_app.ui.viewmodel.NotificationsViewModel
import com.example.payment_app.ui.components.countLabel
import com.example.payment_app.ui.components.formatMoney
import com.example.payment_app.ui.components.greetingForHour
import com.example.payment_app.ui.components.netBalanceCents
import com.example.payment_app.ui.components.toCents
import com.example.payment_app.ui.screens.groups.sampleGroups
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class FormattingTest {
    @Test
    fun formatMoney_formatsCentsWithTwoDecimals() {
        assertEquals("$47.50", formatMoney(4_750))
        assertEquals("$0.05", formatMoney(5))
        assertEquals("$0.00", formatMoney(0))
        assertEquals("$1,234.56", formatMoney(123_456))
    }

    @Test
    fun formatMoney_addsSigns() {
        assertEquals("+$62.00", formatMoney(6_200, showPlusSign = true))
        assertEquals("-$14.50", formatMoney(-1_450, showPlusSign = true))
        assertEquals("-$14.50", formatMoney(-1_450))
        assertEquals("$0.00", formatMoney(0, showPlusSign = true))
    }

    @Test
    fun formatMoney_usesGivenCurrencySymbol() {
        assertEquals("€10.00", formatMoney(1_000, currencySymbol = "€"))
    }

    @Test
    fun countLabel_pluralises() {
        assertEquals("1 member", countLabel(1, "member"))
        assertEquals("4 members", countLabel(4, "member"))
        assertEquals("0 expenses", countLabel(0, "expense"))
    }

    @Test
    fun greetingForHour_coversWholeDay() {
        assertEquals("Good morning", greetingForHour(5))
        assertEquals("Good morning", greetingForHour(11))
        assertEquals("Good afternoon", greetingForHour(12))
        assertEquals("Good evening", greetingForHour(18))
        assertEquals("Good evening", greetingForHour(2))
    }
}

class GroupBalanceTest {
    @Test
    fun toCents_roundsToWholeCents() {
        assertEquals(6_200L, 62.0.toCents())
        assertEquals(-1_450L, (-14.5).toCents())
        assertEquals(10L, 0.1.toCents())
    }

    @Test
    fun netBalance_isSumOfGroupBalances() {
        assertEquals(4_750L, netBalanceCents(sampleGroups))
        assertEquals(0L, netBalanceCents(emptyList()))
    }
}

class HomeViewModelTest {
    @Test
    fun greetingAndName() {
        val state = HomeViewModel(FakeOverviewRepository(), currentHour = { 9 }).uiState.value
        assertEquals("Good morning", state.greeting)
        assertEquals("Alex", state.userFirstName)
    }

    @Test
    fun recentActivity_isLimited() {
        val state = HomeViewModel(FakeOverviewRepository(), currentHour = { 9 }).uiState.value
        assertEquals(HomeViewModel.RECENT_ACTIVITY_LIMIT, state.recentActivity.size)
    }
}

class ActivityViewModelTest {
    private val repository = FakeOverviewRepository()

    @Test
    fun startsWithAllEntries() {
        val state = ActivityViewModel(repository).uiState.value
        assertEquals(ActivityTypeFilter.ALL, state.typeFilter)
        assertEquals(repository.activity().size, state.entries.size)
    }

    @Test
    fun typeFilter_showsOnlyMatchingEntries() {
        val viewModel = ActivityViewModel(repository)

        viewModel.onTypeFilterChange(ActivityTypeFilter.SETTLEMENTS)
        val settlements = viewModel.uiState.value.entries
        assertTrue(settlements.isNotEmpty())
        assertTrue(settlements.all { it.type == ActivityType.SETTLEMENT })

        viewModel.onTypeFilterChange(ActivityTypeFilter.EXPENSES)
        assertTrue(viewModel.uiState.value.entries.all { it.type == ActivityType.EXPENSE })
    }

    @Test
    fun groupAndTypeFilters_combine() {
        val viewModel = ActivityViewModel(repository)
        viewModel.onTypeFilterChange(ActivityTypeFilter.EXPENSES)
        viewModel.onGroupFilterChange("2")

        val state = viewModel.uiState.value
        assertTrue(state.entries.isNotEmpty())
        assertTrue(state.entries.all { it.groupId == "2" && it.type == ActivityType.EXPENSE })

        viewModel.onGroupFilterChange(null)
        assertEquals(ActivityTypeFilter.EXPENSES, viewModel.uiState.value.typeFilter)
        assertEquals(
            repository.activity().count { it.type == ActivityType.EXPENSE },
            viewModel.uiState.value.entries.size,
        )
    }
}

class NotificationsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun startsWithUnreadNotifications() {
        val state = NotificationsViewModel(FakeOverviewRepository()).uiState.value
        assertEquals(2, state.unread.size)
        assertEquals(4, state.read.size)
    }

    @Test
    fun markAllAsRead_clearsUnread() {
        val viewModel = NotificationsViewModel(FakeOverviewRepository())
        viewModel.markAllAsRead()
        assertTrue(viewModel.uiState.value.unread.isEmpty())
        assertEquals(6, viewModel.uiState.value.read.size)
    }

    @Test
    fun markAsRead_onlyAffectsThatNotification() {
        val viewModel = NotificationsViewModel(FakeOverviewRepository())
        viewModel.markAsRead("n1")
        val state = viewModel.uiState.value
        assertEquals(listOf("n2"), state.unread.map { it.id })
    }
}
