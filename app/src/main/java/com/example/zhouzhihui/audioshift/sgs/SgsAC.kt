package com.example.zhouzhihui.audioshift.sgs

import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Window
import android.view.WindowManager
import android.webkit.ValueCallback
import android.widget.TextView
import com.example.zhouzhihui.audioshift.R
import com.example.zhouzhihui.audioshift.sgs.layaair.autoupdateversion.AutoUpdateAPK
import com.example.zhouzhihui.audioshift.sgs.layaair.game.IMarket.IPlugin
import com.example.zhouzhihui.audioshift.sgs.layaair.game.IMarket.IPluginRuntimeProxy
import com.example.zhouzhihui.audioshift.sgs.layaair.game.Market.GameEngine
import com.example.zhouzhihui.audioshift.ui.BaseActivity
import com.example.zhouzhihui.audioshift.util.isOpenNetwork
import layaair.game.config.config

/**
 * Created by 周智慧 on 17/04/2018.
 */
/**
 * tips:
 * 1: 修改build.gradle和gradle.properties
 * 2: 修改android.manifest
 * 3: jniLibs.srcDir 'libs'
 */
fun startSgsAC(context: Context) = Intent(context, SgsAC::class.java).apply { context.startActivity(this) }

class SgsAC : BaseActivity() {
    val AR_CHECK_UPDATE = 1
    lateinit private var mPlugin: IPlugin
    lateinit private var mProxy: IPluginRuntimeProxy
    internal var isLoad = false
    internal var isExit = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        /*
         * 如果不想使用更新流程，可以屏蔽checkApkUpdate函数，直接打开initEngine函数
         */
        checkApkUpdate(this)
        //initEngine();
    }

    fun initEngine() {
        mProxy = RuntimeProxy(this)
        mPlugin = GameEngine(this)
        mPlugin.game_plugin_set_runtime_proxy(mProxy)
        mPlugin.game_plugin_set_option("localize", "false")
        mPlugin.game_plugin_set_option("gameUrl", "http://10.30.20.118:8900/bin/index.html")
        mPlugin.game_plugin_init(3)
        val gameView = mPlugin.game_plugin_get_view()
        this.setContentView(gameView)
        isLoad = true
    }

    fun settingNetwork(context: Context, p_nType: Int) {
        val pBuilder = AlertDialog.Builder(context)
        pBuilder.setTitle("连接失败，请检查网络或与开发商联系").setMessage("是否对网络进行设置?")
        // 退出按钮
        pBuilder.setPositiveButton("是") { p_pDialog, arg1 ->
            val intent: Intent
            try {
                val sdkVersion = android.os.Build.VERSION.SDK
                if (Integer.valueOf(sdkVersion) > 10) {
                    intent = Intent(
                            android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                } else {
                    intent = Intent()
                    val comp = ComponentName(
                            "com.android.settings",
                            "com.android.settings.WirelessSettings")
                    intent.component = comp
                    intent.action = "android.intent.action.VIEW"
                }
                (context as Activity).startActivityForResult(intent, p_nType)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        pBuilder.setNegativeButton("否") { dialog, which ->
            dialog.cancel()
            (context as Activity).finish()
        }
        val alertdlg = pBuilder.create()
        alertdlg.setCanceledOnTouchOutside(false)
        alertdlg.show()
    }

    fun checkApkUpdate(context: Context, callback: ValueCallback<Int>) {
        if (isOpenNetwork(context)) {
            // 自动版本更新
            if ("0" == config.GetInstance().getProperty("IsHandleUpdateAPK", "0") == false) {
                Log.e("0", "==============Java流程 checkApkUpdate")
                AutoUpdateAPK(context, ValueCallback<Int> { integer ->
                    Log.e("", ">>>>>>>>>>>>>>>>>>")
                    callback.onReceiveValue(integer)
                })
            } else {
                Log.e("0", "==============Java流程 checkApkUpdate 不许要自己管理update")
                callback.onReceiveValue(1)
            }
        } else {
            settingNetwork(context, AR_CHECK_UPDATE)
        }
    }

    fun checkApkUpdate(context: Context) {
        val inputStream = javaClass.getResourceAsStream("/assets/config.ini")
        config.GetInstance().init(inputStream)
        checkApkUpdate(context, ValueCallback { integer ->
            if (integer!!.toInt() == 1) {
                initEngine()
            } else {
                finish()
            }
        })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        if (requestCode == AR_CHECK_UPDATE) {
            checkApkUpdate(this)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isLoad) mPlugin.game_plugin_onPause()
    }

    //------------------------------------------------------------------------------
    override fun onResume() {
        super.onResume()
        if (isLoad) mPlugin.game_plugin_onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isLoad) mPlugin.game_plugin_onDestory()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return super.onKeyDown(keyCode, event)
    }
}