package com.example.zhouzhihui.audioshift

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.Menu
import android.view.MenuItem
import com.example.zhouzhihui.audioshift.ui.BaseActivity
import com.example.zhouzhihui.audioshift.ui.cooldialog.CoolDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import android.text.Html
import android.text.Spanned




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
                var author = SpannableString(resources.getString(R.string.about_app_author))
                Linkify.addLinks(author, Linkify.WEB_URLS)

                var email = SpannableString(resources.getString(R.string.about_app_email))
                Linkify.addLinks(email, Linkify.WEB_URLS)

                var icons_by = SpannableString(resources.getString(R.string.about_icon_by))
                Linkify.addLinks(icons_by, Linkify.WEB_URLS)

                var icon_from = SpannableString(resources.getString(R.string.about_icon_from))
                Linkify.addLinks(icon_from, Linkify.WEB_URLS)

                var msg = SpannableStringBuilder()
                msg.append(author).appendln().append(email).appendln().append(icons_by).appendln().append(icon_from)
                val myMessage = Html.fromHtml("app制作人：黑山<br>邮箱: <a href=\"mailto:1059084407@qq.com\">1059084407@qq.com</a><br>简书: <a href=\"https://www.jianshu.com/u/0b651536da90\">宛丘之上兮</a>")
//                val myMessage = Html.fromHtml(resources.getString(R.string.about_msg))
                CoolDialog.getCoolDialogInstance(this)
                        ?.withIcon(R.mipmap.ic_launcher_round)
                        ?.withTitle(resources.getString(R.string.about_app))
                        ?.withMsg(myMessage)
                        ?.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAboutDialog() {
        val builder: AlertDialog.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            builder = AlertDialog.Builder(this)
        }
        builder.setTitle(R.string.about_app)
                .setMessage(R.string.about_msg)
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                }
//                .setNegativeButton(android.R.string.no) { dialog, which ->
//                }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }
}
