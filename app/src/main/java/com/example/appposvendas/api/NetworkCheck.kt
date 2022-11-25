package com.example.appposvendas.api

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities


class NetworkCheck(private val connectivityManager: ConnectivityManager?) {

    fun performActionConnectec(action:()->Unit){
        if(hasInternet())
            action()
    }
    private fun hasInternet():Boolean{
        val network: Network = connectivityManager?.activeNetwork ?: return false
        val capabilities: NetworkCapabilities = connectivityManager.getNetworkCapabilities(network)?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    }
}