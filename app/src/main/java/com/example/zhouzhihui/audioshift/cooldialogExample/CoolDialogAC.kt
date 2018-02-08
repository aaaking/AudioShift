package com.example.zhouzhihui.audioshift.cooldialogExample

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Html
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.zhouzhihui.audioshift.R
import com.example.zhouzhihui.audioshift.ui.BaseActivity
import com.example.zhouzhihui.audioshift.ui.CoolRecyclerView
import com.example.zhouzhihui.audioshift.util.ScreenUtil
import com.zzh.cooldialog.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by 周智慧 on 08/02/2018.
 */
class CoolDialogAC : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cooldialog)
        setSupportActionBar(toolbar as? Toolbar)
    }

    fun btn_default(view: View) = CoolDialog(this).apply {
        val myMessage = Html.fromHtml("app制作人：黑山<br>邮箱: <a href=\"mailto:1059084407@qq.com\">1059084407@qq.com</a><br>简书: <a href=\"https://www.jianshu.com/u/0b651536da90\">宛丘之上兮</a>")
        var extraStr = TextView(this@CoolDialogAC)
        var padding = ScreenUtil.dp2px(this@CoolDialogAC, 20f)
        extraStr.setText(R.string.about_app_public_wx)
        extraStr.setTextColor(Color.WHITE)
        extraStr.setPadding(0, padding, 0, padding / 3)
        var extraImg = ImageView(this@CoolDialogAC)
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
        show()
    }

    fun custom_bg(view: View) = CoolDialog(this).apply {
        var radius = context.resources.getDimension(com.zzh.cooldialog.R.dimen.btn_common_radius)
        val bg = GradientDrawable()
        bg.cornerRadii = floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f)
        bg.setColor(Color.YELLOW)
        withMsg("msg1", Color.BLACK)
                .withBgAndTopbg(bg, mBgTop)
                ?.withNegativeBtn(resources.getString(android.R.string.no))
                ?.withPositiveBtn(resources.getString(android.R.string.yes))
                ?.withDuration(300)
                ?.withCoolStyle(CoolStyle(mRootView, COOL_STYLE_ROTATE))
        show()
    }
    fun custom_top(view: View) = CoolDialog(this).apply {
        var radius = context.resources.getDimension(com.zzh.cooldialog.R.dimen.btn_common_radius)
        val topBg = GradientDrawable()
        topBg.cornerRadii = floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f)
        topBg.setColor(Color.BLUE)
        withTitle(resources.getString(R.string.about_app))
                ?.withMsg("msg1")
        withBgAndTopbg(mBg, topBg)
                ?.withNegativeBtn(resources.getString(android.R.string.no))
                ?.withPositiveBtn(resources.getString(android.R.string.yes))
                ?.withDuration(300)
                ?.withCoolStyle(CoolStyle(mRootView, COOL_STYLE_3D_ROTATE_LEFT))
        show()
    }
    fun custom_content(view: View) = CoolDialog(this).apply {
        withMsg("msg1", Color.BLACK)
                .withMsgSub("submsg", Color.BLUE)
                ?.withPositiveBtn(resources.getString(android.R.string.yes))
                ?.withDuration(300)
                ?.withContentCustom(null, R.layout.layout_save_file_et)
                ?.withCoolStyle(CoolStyle(mRootView, COOL_STYLE_FALL_DOWN))
        show()
    }
    fun custom_content_with_layout(view: View) = CoolDialog(this).apply {
        withMsg("msg1", Color.BLACK)
                .withMsgSub("submsg", Color.BLUE)
                ?.withPositiveBtn(resources.getString(android.R.string.yes))
                ?.withDuration(300)
                ?.withContentCustom(CoolRecyclerView(this@CoolDialogAC).apply {
                    adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
                            (holder?.itemView as? TextView)?.text = "${position}"
                        }
                        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
                            return object : RecyclerView.ViewHolder(TextView(context)) {}
                        }
                        override fun getItemCount(): Int {
                            return 160
                        }
                    }
                })
                ?.withCoolStyle(CoolStyle(mRootView, COOL_STYLE_3D_FLIP_H))
        show()
    }

    fun anim_example(view: View) {
        startAnimaExampleAC(this)
    }
}