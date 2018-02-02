package com.example.zhouzhihui.audioshift.ui.cooldialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.example.zhouzhihui.audioshift.R

/**
 * Created by 周智慧 on 02/02/2018.
 */
class CoolDialog : Dialog, DialogInterface {
    companion object {//single instance: double check
        @Volatile var instance: CoolDialog? = null
        fun getCoolDialogInstance(context: Context?): CoolDialog? {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = CoolDialog(context, android.R.style.Theme_Material_Dialog_Alert)
                    }
                }
            }
            return instance
        }
    }
    private var mRootView: View? = null
    constructor(context: Context?) : super(context) {
    }

    constructor(context: Context?, theme: Int) : super(context, theme) {
    }

    init {
        mRootView = View.inflate(context, R.layout.layout_cool_dialog, null)
        setContentView(mRootView)
    }
}