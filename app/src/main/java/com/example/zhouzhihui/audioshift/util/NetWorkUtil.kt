package com.example.zhouzhihui.audioshift.util

import android.content.Context
import android.net.ConnectivityManager
import layaair.game.config.config

/**
 * Created by 周智慧 on 17/04/2018.
 */
fun isOpenNetwork(context: Context): Boolean {
    if (!config.GetInstance().m_bCheckNetwork)
        return true
    val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connManager.activeNetworkInfo != null && connManager.activeNetworkInfo.isAvailable && connManager.activeNetworkInfo.isConnected
}