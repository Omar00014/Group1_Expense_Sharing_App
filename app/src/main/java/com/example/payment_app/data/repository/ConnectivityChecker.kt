package com.example.payment_app.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

// Asks "is the device online?". An interface, so tests can use a fake one.
interface ConnectivityChecker {
    fun isOnline(): Boolean
}

class AndroidConnectivityChecker(private val context: Context) : ConnectivityChecker {
    override fun isOnline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // activeNetwork is null in airplane mode
        val caps = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}