package com.example.zhouzhihui.audioshift

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.zhouzhihui.audioshift.ui.BaseActivity
import com.example.zhouzhihui.audioshift.ui.cooldialog.CoolDialog
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        when (id) {
            R.id.menu_about -> {
                CoolDialog.getCoolDialogInstance(this)?.show()
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
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }
}
