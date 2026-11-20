package com.example.payment_app

import com.example.payment_app.data.remote.DEFAULT_REQUEST_TIMEOUT_MS
import com.example.payment_app.data.remote.FakeNotificationsRemote
import com.example.payment_app.data.remote.NotificationsRemote
import com.example.payment_app.data.remote.safeRequest
import com.example.payment_app.data.repository.FakeOverviewRepository
import com.example.payment_app.ui.viewmodel.NotificationsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

private class FailingRemote : NotificationsRemote {
    var calls = 0
    override suspend fun markAsRead(notificationIds: List<String>) {
        calls++
        throw IOException("No network connection")
    }
}

private class RecordingRemote : NotificationsRemote {
    val sent = mutableListOf<List<String>>()
    override suspend fun markAsRead(notificationIds: List<String>) {
        sent += notificationIds
    }
}

private class HangingRemote : NotificationsRemote {
    override suspend fun markAsRead(notificationIds: List<String>) {
        awaitCancellation()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class FailedRequestTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private val dispatcher get() = mainDispatcherRule.testDispatcher

    private fun viewModel(remote: NotificationsRemote) =
        NotificationsViewModel(FakeOverviewRepository(), remote, dispatcher)

    @Test
    fun failedRequest_doesNotCrash_andShowsMessage() = runTest(dispatcher) {
        val remote = FailingRemote()
        val vm = viewModel(remote)

        vm.markAllAsRead()
        assertTrue(vm.uiState.value.unread.isEmpty())

        advanceUntilIdle()
        assertEquals(1, remote.calls)
        assertEquals(NotificationsViewModel.REQUEST_FAILED_MESSAGE, vm.uiState.value.errorMessage)
    }

    @Test
    fun afterFailedRequest_appStaysUsable() = runTest(dispatcher) {
        val remote = FailingRemote()
        val vm = viewModel(remote)

        vm.markAsRead("n1")
        advanceUntilIdle()
        vm.onErrorMessageShown()
        assertNull(vm.uiState.value.errorMessage)

        vm.markAsRead("n2")
        assertTrue(vm.uiState.value.unread.isEmpty())
        advanceUntilIdle()
        assertEquals(2, remote.calls)
        assertEquals(NotificationsViewModel.REQUEST_FAILED_MESSAGE, vm.uiState.value.errorMessage)
    }

    @Test
    fun successfulRequest_showsNoMessage() = runTest(dispatcher) {
        val remote = RecordingRemote()
        val vm = viewModel(remote)

        vm.markAllAsRead()
        advanceUntilIdle()

        assertEquals(listOf(listOf("n1", "n2")), remote.sent)
        assertNull(vm.uiState.value.errorMessage)
    }

    @Test
    fun hangingServer_timesOut_andAppDidNotWait() = runTest(dispatcher) {
        val vm = viewModel(HangingRemote())

        vm.markAllAsRead()
        assertTrue(vm.uiState.value.unread.isEmpty())

        advanceTimeBy(DEFAULT_REQUEST_TIMEOUT_MS - 1)
        runCurrent()
        assertNull(vm.uiState.value.errorMessage)

        advanceTimeBy(2)
        runCurrent()
        assertEquals(NotificationsViewModel.REQUEST_FAILED_MESSAGE, vm.uiState.value.errorMessage)
    }

    @Test
    fun safeRequest_turnsErrorsIntoFailures() = runTest(dispatcher) {
        assertEquals(42, safeRequest(dispatcher) { 42 }.getOrNull())
        assertTrue(safeRequest(dispatcher) { throw IOException("offline") }.isFailure)
        assertTrue(safeRequest(dispatcher) { throw IllegalStateException("server error") }.isFailure)
        assertTrue(safeRequest(dispatcher, timeoutMs = 1_000) { awaitCancellation() }.isFailure)
    }

    @Test
    fun fakeRemote_failsOnlyWhenOffline() = runTest(dispatcher) {
        val offline = FakeNotificationsRemote({ false })
        val online = FakeNotificationsRemote({ true })

        assertTrue(safeRequest(dispatcher) { offline.markAsRead(listOf("n1")) }.isFailure)
        assertTrue(safeRequest(dispatcher) { online.markAsRead(listOf("n1")) }.isSuccess)
    }
}
