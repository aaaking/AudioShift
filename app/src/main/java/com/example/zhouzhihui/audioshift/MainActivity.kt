package com.example.zhouzhihui.audioshift

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.zhouzhihui.audioshift.ui.BaseActivity
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
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
