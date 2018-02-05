package com.example.zhouzhihui.audioshift.util

import android.app.Activity
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.example.zhouzhihui.audioshift.AudioApp

object ScreenUtil {
    var RATIO = 0.75

    var screenWidth: Int = 0
    var screenHeight: Int = 0
    var screenMin: Int = 0// 宽高中，小的一边
    var screenMax: Int = 0// 宽高中，较大的值

    var density: Float = 0.toFloat()
    var scaleDensity: Float = 0.toFloat()
    var xdpi: Float = 0.toFloat()
    var ydpi: Float = 0.toFloat()
    var densityDpi: Int = 0

    var mDialogWidth: Int = 0
    var statusbarheight: Int = 0
    var navbarheight: Int = 0

    val displayHeight: Int
        get() {
            if (screenHeight == 0) {
                GetInfo(AudioApp.sAppContext)
            }
            return screenHeight
        }

    init {
        init(AudioApp.sAppContext)
    }

    fun dip2px(dipValue: Float): Int {
        return (dipValue * density + 0.5f).toInt()
    }

    fun px2dip(pxValue: Float): Int {
        return (pxValue / density + 0.5f).toInt()
    }

    fun getDialogWidth(): Int {
        mDialogWidth = (screenMin * RATIO).toInt()
        return mDialogWidth
    }

    fun init(context: Context?) {
        if (null == context) {
            return
        }
        val dm = context.applicationContext.resources.displayMetrics
        screenWidth = dm.widthPixels
        screenHeight = dm.heightPixels
        screenMin = if (screenWidth > screenHeight) screenHeight else screenWidth
        density = dm.density
        scaleDensity = dm.scaledDensity
        xdpi = dm.xdpi
        ydpi = dm.ydpi
        densityDpi = dm.densityDpi

    }

    fun GetInfo(context: Context?) {
        if (null == context) {
            return
        }
        val dm = context.applicationContext.resources.displayMetrics
        screenWidth = dm.widthPixels
        screenHeight = dm.heightPixels
        screenMin = if (screenWidth > screenHeight) screenHeight else screenWidth
        screenMax = if (screenWidth < screenHeight) screenHeight else screenWidth
        density = dm.density
        scaleDensity = dm.scaledDensity
        xdpi = dm.xdpi
        ydpi = dm.ydpi
        densityDpi = dm.densityDpi
        statusbarheight = getStatusBarHeight(context)
        navbarheight = getNavBarHeight(context)
    }

    fun getStatusBarHeight(context: Context): Int {
        //		Class<?> c = null;
        //		Object obj = null;
        //		Field field = null;
        //		int x = 0, sbar = 0;
        //		try {
        //			c = Class.forName("com.android.internal.R$dimen");
        //			obj = c.newInstance();
        //			field = c.getField("status_bar_height");
        //			x = Integer.parseInt(field.get(obj).toString());
        //			sbar = context.getResources().getDimensionPixelSize(x);
        //		} catch (Exception E) {
        //			E.printStackTrace();
        //		}
        //		return sbar;
        // 获得状态栏高度
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    fun getNavBarHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun screenBrightness(activity: Activity, progress: Float) {
        val lp = activity.window.attributes
        lp.screenBrightness = progress
        activity.window.attributes = lp
    }

    fun getSysScreenBrightness(context: Context): Int {
        var brightness = 0
        try {
            brightness = Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }

        return brightness
    }

    fun inRangeOfView(view: View, ev: MotionEvent): Boolean {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        val x = location[0]
        val y = location[1]
        val eX = ev.rawX.toInt()
        val eY = ev.rawY.toInt()
        return if (eX < x || eX > x + view.width || eY < y || eY > y + view.height) {
            false
        } else true
    }

    fun getScaleHeight(scalewidth: Int, width: Int, height: Int): Int {
        return scalewidth * height / width
    }

    fun changeLight(imageView: ImageView, brightness: Int) {
        val cMatrix = ColorMatrix()
        cMatrix.set(floatArrayOf(1f, 0f, 0f, 0f, brightness.toFloat(), 0f, 1f, 0f, 0f, brightness.toFloat(), // 改变亮度
                0f, 0f, 1f, 0f, brightness.toFloat(), 0f, 0f, 0f, 1f, 0f))
        imageView.colorFilter = ColorMatrixColorFilter(cMatrix)
    }

    fun getScreenWidth(activity: Context): Int {
        return activity.resources.displayMetrics.widthPixels
    }

    fun getScreenHeight(activity: Context): Int {
        return activity.resources.displayMetrics.heightPixels
    }

    /**
     * 全屏
     *
     * @param activity
     */
    fun setFullScreen(activity: Activity) {
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    /**
     * 退出全屏
     *
     * @param activity
     */
    fun quitFullScreen(activity: Activity) {
        val attrs = activity.window.attributes
        attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        activity.window.attributes = attrs
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    fun getDensity(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    fun dp2px(context: Context?, dp: Float): Int {
        if (context == null) {
            return (dp * 2.5 + 0.5).toInt()
        }
        val displayMetrics = context.resources.displayMetrics
        return (dp * displayMetrics.density + 0.5).toInt()
    }
}

fun isCancelled(view: View, event: MotionEvent): Boolean {
    val location = IntArray(2)
    view.getLocationOnScreen(location)
    return event.rawX < location[0] || event.rawX > location[0] + view.width || event.rawY < location[1] - 40 || event.rawY > location[1] + view.height
}