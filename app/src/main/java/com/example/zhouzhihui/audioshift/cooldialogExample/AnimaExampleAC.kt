package com.example.zhouzhihui.audioshift.cooldialogExample

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.zhouzhihui.audioshift.R
import com.example.zhouzhihui.audioshift.ui.BaseActivity
import com.example.zhouzhihui.audioshift.util.ScreenUtil
import com.zzh.cooldialog.CoolDialog
import com.zzh.cooldialog.CoolStyle

/**
 * Created by 周智慧 on 08/02/2018.
 */
fun startAnimaExampleAC(context: Context) = Intent(context, AnimaExampleAC::class.java).apply { context.startActivity(this) }
class AnimaExampleAC : BaseActivity() {
    lateinit var mDialog: CoolDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cooldialog_anim)
        mDialog = CoolDialog(this).apply {
            val myMessage = Html.fromHtml("app制作人：黑山<br>邮箱: <a href=\"mailto:1059084407@qq.com\">1059084407@qq.com</a><br>简书: <a href=\"https://www.jianshu.com/u/0b651536da90\">宛丘之上兮</a>")
            var extraStr = TextView(this@AnimaExampleAC)
            var padding = ScreenUtil.dp2px(this@AnimaExampleAC, 20f)
            extraStr.setText(R.string.about_app_public_wx)
            extraStr.setTextColor(Color.WHITE)
            extraStr.setPadding(0, padding, 0, padding / 3)
            var extraImg = ImageView(this@AnimaExampleAC)
            extraImg.setImageResource(R.mipmap.wx_public)
            var lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.gravity = Gravity.CENTER
            withIcon(R.drawable.icon)
                    ?.withTitle(resources.getString(R.string.about_app))
                    ?.withMsg(myMessage)
                    ?.withContentCustom(extraStr)
                    ?.withContentCustom(extraImg)
                    ?.withPositiveBtn(resources.getString(android.R.string.yes))
                    ?.withDuration(300)
                    ?.withCoolStyle(CoolStyle(mRootView))
        }
    }

    fun COOL_STYLE_SLIDE_BOTTOM(view: View) = mDialog.apply { withCoolStyle(CoolStyle(mRootView, com.zzh.cooldialog.COOL_STYLE_SLIDE_BOTTOM)).show() }
    fun COOL_STYLE_SLIDE_TOP(view: View) = mDialog.apply { withCoolStyle(CoolStyle(mRootView, com.zzh.cooldialog.COOL_STYLE_SLIDE_TOP)).show() }
    fun COOL_STYLE_SLIDE_LEFT(view: View) = mDialog.apply { withCoolStyle(CoolStyle(mRootView, com.zzh.cooldialog.COOL_STYLE_SLIDE_LEFT)).show() }
    fun COOL_STYLE_SLIDE_RIGHT(view: View) = mDialog.apply { withCoolStyle(CoolStyle(mRootView, com.zzh.cooldialog.COOL_STYLE_SLIDE_RIGHT)).show() }
    fun COOL_STYLE_ROTATE(view: View) = mDialog.apply { withCoolStyle(CoolStyle(mRootView, com.zzh.cooldialog.COOL_STYLE_ROTATE)).show() }
    fun COOL_STYLE_FADE_IN(view: View) = mDialog.apply { withCoolStyle(CoolStyle(mRootView, com.zzh.cooldialog.COOL_STYLE_FADE_IN)).show() }
    fun COOL_STYLE_FALL_DOWN(view: View) = mDialog.apply { withCoolStyle(CoolStyle(mRootView, com.zzh.cooldialog.COOL_STYLE_FALL_DOWN)).show() }
    fun COOL_STYLE_FALL_DOWN_OFFSET(view: View) = mDialog.apply { withCoolStyle(CoolStyle(mRootView, com.zzh.cooldialog.COOL_STYLE_FALL_DOWN_OFFSET)).show() }
    fun COOL_STYLE_SHAKE(view: View) = mDialog.apply { withCoolStyle(CoolStyle(mRootView, com.zzh.cooldialog.COOL_STYLE_SHAKE)).show() }
    fun COOL_STYLE_3D_ROTATE_LEFT(view: View) = mDialog.apply { withCoolStyle(CoolStyle(mRootView, com.zzh.cooldialog.COOL_STYLE_3D_ROTATE_LEFT)).show() }
    fun COOL_STYLE_3D_ROTATE_BOTTOM(view: View) = mDialog.apply { withCoolStyle(CoolStyle(mRootView, com.zzh.cooldialog.COOL_STYLE_3D_ROTATE_BOTTOM)).show() }
    fun COOL_STYLE_3D_FLIP_H(view: View) = mDialog.apply { withCoolStyle(CoolStyle(mRootView, com.zzh.cooldialog.COOL_STYLE_3D_FLIP_H)).show() }
    fun COOL_STYLE_3D_FLIP_V(view: View) = mDialog.apply { withCoolStyle(CoolStyle(mRootView, com.zzh.cooldialog.COOL_STYLE_3D_FLIP_V)).show() }
    fun COOL_STYLE_3D_SLIT_H(view: View) = mDialog.apply { withCoolStyle(CoolStyle(mRootView, com.zzh.cooldialog.COOL_STYLE_3D_SLIT_H)).show() }
    fun COOL_STYLE_3D_SLIT_V(view: View) = mDialog.apply { withCoolStyle(CoolStyle(mRootView, com.zzh.cooldialog.COOL_STYLE_3D_SLIT_V)).show() }
}