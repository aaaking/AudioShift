package com.example.zhouzhihui.audioshift.sgs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.example.zhouzhihui.audioshift.R
import com.example.zhouzhihui.audioshift.ui.BaseActivity

/**
 * Created by 周智慧 on 17/04/2018.
 */
fun startSgsAC(context: Context) = Intent(context, SgsAC::class.java).apply { context.startActivity(this) }

class SgsAC : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var txt = TextView(this)
        txt.text = "在线三国杀Demo"
        txt.setTextColor(resources.getColor(R.color.colorPrimaryDark))
        txt.y = 200f
        setContentView(txt)
    }
}