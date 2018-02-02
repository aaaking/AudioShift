package com.example.zhouzhihui.audioshift.ui.cooldialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v7.widget.AppCompatImageView
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.zhouzhihui.audioshift.R
import com.example.zhouzhihui.audioshift.TAG
import com.example.zhouzhihui.audioshift.util.ScreenUtil

/**
 * Created by 周智慧 on 02/02/2018.
 */
class CoolDialog : Dialog, DialogInterface {
    companion object {//single instance: double check
        private @Volatile var instance: CoolDialog? = null
        fun getCoolDialogInstance(context: Context?): CoolDialog? {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
//                        instance = CoolDialog(context, android.R.style.Theme_Material_Dialog_Alert)
                        instance = CoolDialog(context, R.style.cool_dialog_dim)
                    }
                }
            }
            return instance
        }
    }
    private var mRootView: View? = null
    //top
    var iv_top: AppCompatImageView? = null
    var tv_title: TextView? = null
    //middle
    var tv_msg_main: TextView? = null
    var tv_msg_sub: TextView? = null
    var content_custom_stub: ViewStub? = null
    var content_custom_view: View? = null
    constructor(context: Context?) : super(context, R.style.cool_dialog_dim) {
    }

    constructor(context: Context?, theme: Int) : super(context, theme) {
    }

    init {
        val bg = GradientDrawable()
        bg.cornerRadius = ScreenUtil.dp2px(context, 3f).toFloat()
        bg.setColor(context.resources.getColor(R.color.colorPrimaryLight))
        mRootView = View.inflate(context, R.layout.layout_cool_dialog, null)
        mRootView?.apply {
            background = bg
            iv_top = findViewById(R.id.iv_top)
            tv_title = findViewById(R.id.tv_title)
            tv_msg_main = findViewById(R.id.tv_msg_main)
            tv_msg_sub = findViewById(R.id.tv_msg_sub)
            setContentView(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val params = window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT//ScreenUtil.dp2px(context, 300f)//ViewGroup.LayoutParams.MATCH_PARENT
        val padding = context.resources.getDimensionPixelOffset(R.dimen.dialog_margin)//setPadding(padding, 0, padding, 0)
        window?.decorView?.setPadding(padding, padding, padding, padding)
    }

    fun withIcon(imgId: Int): CoolDialog =
            apply { iv_top?.apply {
                setImageResource(imgId)
                visibility = View.VISIBLE
            } }

    fun withTitle(str: String): CoolDialog =
            withTitle(str, Color.WHITE)
    fun withTitle(str: String, color: Int): CoolDialog =
            apply {
                tv_title?.apply {
                    text = str
                    setTextColor(color)
                    visibility = if (TextUtils.isEmpty(str)) View.GONE else View.VISIBLE
                }
            }

    fun withMsg(str: CharSequence): CoolDialog =
            withMsg(str, Color.WHITE)
    fun withMsg(str: CharSequence, color: Int): CoolDialog =
            apply {
                tv_msg_main?.apply {
                    text = str
                    movementMethod = LinkMovementMethod.getInstance()
                    setTextColor(color)
                    visibility = if (TextUtils.isEmpty(str)) View.GONE else View.VISIBLE
                }
            }

    fun withMsgSub(str: CharSequence): CoolDialog =
            withMsgSub(str, Color.WHITE)
    fun withMsgSub(str: CharSequence, color: Int): CoolDialog =
            apply {
                tv_msg_sub?.apply {
                    text = str
                    setTextColor(color)
                    visibility = if (TextUtils.isEmpty(str)) View.GONE else View.VISIBLE
                }
            }

    fun withContentCustom(layoutId: Int): CoolDialog =
            apply {
                initViewStub(layoutId = layoutId)
                content_custom_view?.run {
                    (this@run as? ViewGroup)?.apply {
                        var wegahwoi = TextView(context)
                        wegahwoi.text = "ereagaergogro"
                        addView(wegahwoi)
                    }
                }
            }
    fun withContentCustom(view: View): CoolDialog =
            apply {
                initViewStub(layoutId = R.layout.layout_dialog_content_custom)
                content_custom_view?.run {
                    (this@run as? ViewGroup)?.apply {
                        addView(view)
                    }
                }
            }
    fun initViewStub(layoutId: Int) {
        if (content_custom_view == null) {
            content_custom_stub = mRootView?.findViewById<ViewStub>(R.id.content_custom_stub)
            content_custom_stub?.layoutResource = layoutId
            content_custom_view = content_custom_stub?.inflate()
        }
    }

    override fun show() {
        //Here's the magic..
        //Set the dialog to not focusable (makes navigation ignore us adding the window)
//        window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        super.show()
        //Set the dialog to immersive
//        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//
//                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//
//                or View.SYSTEM_UI_FLAG_IMMERSIVE)//(context as? Activity)?.window?.decorView?.systemUiVisibility ?: View.VISIBLE
//        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }
}