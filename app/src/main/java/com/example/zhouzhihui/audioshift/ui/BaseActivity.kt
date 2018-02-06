package com.example.zhouzhihui.audioshift.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

/**
 * Created by 周智慧 on 02/02/2018.
 */
open class BaseActivity : AppCompatActivity() {
    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            var localLayoutParams: WindowManager.LayoutParams = getWindow().getAttributes()
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    fun showKeyboard(show: Boolean) = (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.apply {
        if (show) {
            if (currentFocus == null) {
                toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            } else {
                showSoftInput(currentFocus, 0)
            }
        } else {
            if (currentFocus != null) {
                hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }
}