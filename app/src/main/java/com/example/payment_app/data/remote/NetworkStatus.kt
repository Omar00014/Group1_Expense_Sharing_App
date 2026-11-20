package com.example.payment_app.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun interface NetworkStatus {
    fun isOnline(): Boolean
}

class AndroidNetworkStatus(context: Context) : NetworkStatus {

    private val connectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun isOnline(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
