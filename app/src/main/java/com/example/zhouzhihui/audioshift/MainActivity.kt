package com.example.zhouzhihui.audioshift

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.zhouzhihui.audioshift.ui.BaseActivity
import com.example.zhouzhihui.audioshift.util.ScreenUtil
import com.zzh.cooldialog.CoolDialog
import com.zzh.cooldialog.CoolStyle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        egwagawrg.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        when (id) {
            R.id.menu_about -> {
//                var author = SpannableString(resources.getString(R.string.about_app_author))
//                Linkify.addLinks(author, Linkify.WEB_URLS)
//
//                var email = SpannableString(resources.getString(R.string.about_app_email))
//                Linkify.addLinks(email, Linkify.WEB_URLS)
//
//                var icons_by = SpannableString(resources.getString(R.string.about_icon_by))
//                Linkify.addLinks(icons_by, Linkify.WEB_URLS)
//
//                var icon_from = SpannableString(resources.getString(R.string.about_icon_from))
//                Linkify.addLinks(icon_from, Linkify.WEB_URLS)
//
//                var msg = SpannableStringBuilder()
//                msg.append(author).appendln().append(email).appendln().append(icons_by).appendln().append(icon_from)
                val myMessage = Html.fromHtml("app制作人：黑山<br>邮箱: <a href=\"mailto:1059084407@qq.com\">1059084407@qq.com</a><br>简书: <a href=\"https://www.jianshu.com/u/0b651536da90\">宛丘之上兮</a>")
//                val myMessage = Html.fromHtml(resources.getString(R.string.about_msg))
                var extraStr = TextView(this)
                var padding = ScreenUtil.dp2px(this, 20f)
                extraStr.setText(R.string.about_app_public_wx)
                extraStr.setTextColor(Color.WHITE)
                extraStr.setPadding(0, padding, 0, padding / 3)
                var extraImg = ImageView(this)
                extraImg.setImageResource(R.mipmap.wx_public)
                var lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                lp.gravity = Gravity.CENTER

                var dialog = CoolDialog(this)
                dialog?.withIcon(R.drawable.icon)
                        ?.withTitle(resources.getString(R.string.about_app))
                        ?.withMsg(myMessage)
                        ?.withContentCustom(null, R.layout.layout_dialog_content_custom)
                        ?.withContentCustom(extraStr)
                        ?.withContentCustom(extraImg)
                        ?.withPositiveBtn(resources.getString(android.R.string.yes))
                        ?.withDuration(500)
                        ?.withCoolStyle(CoolStyle(dialog?.mRootView))
                        ?.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
