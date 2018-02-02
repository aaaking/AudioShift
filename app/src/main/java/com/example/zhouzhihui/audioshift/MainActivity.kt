package com.example.zhouzhihui.audioshift

import android.os.Bundle
import com.example.zhouzhihui.audioshift.ui.BaseActivity
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }
}
