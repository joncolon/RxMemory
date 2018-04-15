package com.tronography.rxmemory.utilities

import android.net.ConnectivityManager
import javax.inject.Inject

class NetworkConnectivityHelper @Inject constructor(val connectivityManager: ConnectivityManager) {

    fun isOffline(): Boolean {
        return !isConnected()
    }

    fun isConnected(): Boolean {
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    fun isOnWifi(): Boolean {
        val activeNetwork = connectivityManager.activeNetworkInfo
        return isConnected() && activeNetwork.type == ConnectivityManager.TYPE_WIFI
    }

    fun isOnMobileData(): Boolean {
        val activeNetwork = connectivityManager.activeNetworkInfo
        return isConnected() && activeNetwork.type == ConnectivityManager.TYPE_MOBILE
    }
}