package com.example.zhouzhihui.audioshift

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.example.zhouzhihui.audioshift.ui.BaseActivity
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
}