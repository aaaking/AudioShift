package com.example.zhouzhihui.audioshift

import android.app.Application
import android.content.Context
import com.example.zhouzhihui.audioshift.util.ScreenUtil

/**
 * Created by 周智慧 on 02/02/2018.
 */
val TAG: String = AudioApp::class.java.simpleName
class AudioApp : Application() {
    companion object {
        var sAppContext: Context? = null
    }
    override fun onCreate() {
        super.onCreate()
        sAppContext = applicationContext
        ScreenUtil.init(sAppContext)
    }
}