package com.shreya.foodapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {
    fun checkConnectivity(context: Context):Boolean{
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworks:NetworkInfo?=connectivityManager.activeNetworkInfo
        if (activeNetworks?.isConnected!=null){
            return activeNetworks.isConnected
        }else{
            return false
        }
    }

}