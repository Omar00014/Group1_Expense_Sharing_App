package com.example.payment_app.data.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

// Unit tests for NFR1. They run on the computer only (no emulator needed).
class CachedOverviewRepositoryTest {

    // A fake network that we can switch on and off from the test
    private class FakeConnectivity(var online: Boolean) : ConnectivityChecker {
        override fun isOnline() = online
    }

    // Load while online, go offline, and check that the same data is still returned
    @Test
    fun offline_returns_everything_that_was_loaded_online() {
        val net = FakeConnectivity(online = true)
        val repo = CachedOverviewRepository(FakeOverviewRepository(), net)

        val loadedOnline = repo.activity() // loaded while online
        net.online = false                 // simulates airplane mode

        assertEquals(loadedOnline, repo.activity())  // data is still there
        assertTrue(repo.isShowingCachedData())       // and the "Cached" badge is on
    }

    // Edge case: if nothing was ever loaded, there is nothing to show offline
    @Test
    fun offline_before_any_load_returns_nothing() {
        val net = FakeConnectivity(online = false)
        val repo = CachedOverviewRepository(FakeOverviewRepository(), net)

        assertTrue(repo.activity().isEmpty())
    }
}