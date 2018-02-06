package com.example.zhouzhihui.audioshift.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.example.zhouzhihui.audioshift.R
import com.example.zhouzhihui.audioshift.util.ScreenUtil
import kotlinx.android.synthetic.main.activity_main.*

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

    fun setToolbar(toolbar: Toolbar, resId: Int, drawer_layout: DrawerLayout) {
        val iconWidth = resources.getDimensionPixelOffset(R.dimen.dialog_icon_width)
        val iconLeftMargin = ScreenUtil.dp2px(this, 7f)
        val logo = resources.getDrawable(resId)
        logo.setBounds(0, 0, iconWidth, iconWidth)
        (toolbar as? Toolbar)?.navigationIcon = logo
        setSupportActionBar(toolbar as? Toolbar)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
//        getSupportActionBar()?.setLogo(logo)
        getSupportActionBar()?.setDisplayUseLogoEnabled(true)
        var right = 0
        (toolbar as? Toolbar)?.viewTreeObserver?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener  {
            override fun onPreDraw(): Boolean {
                for (i in 0 until ((toolbar as? Toolbar)?.childCount ?: -1)) {
                    val child = (toolbar as? Toolbar)?.getChildAt(i)
                    if ((child as? ImageView)?.drawable === logo) {
                        (child as ImageView).adjustViewBounds = true
                        val lp = child.layoutParams
                        lp.width = iconWidth
                        lp.height = iconWidth
                        child.x = iconLeftMargin.toFloat()
                        right = child.right
                        Log.i(com.example.zhouzhihui.audioshift.TAG, "imageview right : ${child.right}")
                        child.setOnClickListener {
                            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                                drawer_layout.closeDrawer(GravityCompat.START)
                            } else {
                                drawer_layout.openDrawer(GravityCompat.START)
                            }
                        }
                    } else if (child is TextView) {
                        Log.i(com.example.zhouzhihui.audioshift.TAG, "textview x : ${iconLeftMargin.toFloat() * 2 + iconWidth}")
                        child.x = iconLeftMargin.toFloat() * 3 + iconWidth * 2
                    }
                }
                (toolbar as? Toolbar)?.viewTreeObserver?.removeOnPreDrawListener(this)
                return true
            }
        })
    }
}