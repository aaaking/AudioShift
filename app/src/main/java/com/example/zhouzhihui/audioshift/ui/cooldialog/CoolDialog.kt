package com.example.zhouzhihui.audioshift.ui.cooldialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v7.widget.AppCompatImageView
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.example.zhouzhihui.audioshift.R
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
    private var mDuration = -1L
    //top
    var iv_top: AppCompatImageView? = null
    var tv_title: TextView? = null
    //middle
    var tv_msg_main: TextView? = null
    var tv_msg_sub: TextView? = null
    var content_custom_view: View? = null
    //bottom
    var btn_no: Button? = null
    var btn_yes: Button? = null
    var btnNegativeBg: Drawable
    var btnPositiveBg: Drawable
    constructor(context: Context?) : super(context, R.style.cool_dialog_dim) {
    }

    constructor(context: Context?, theme: Int) : super(context, theme) {
    }

    init {
        var radius = ScreenUtil.dp2px(context, 4f).toFloat()
        var colorDark = context.resources.getColor(R.color.colorPrimaryDark)
        val bg = GradientDrawable()
        bg.cornerRadius = radius
        bg.setColor(context.resources.getColor(R.color.colorPrimaryLightLight))
        val bgTop = GradientDrawable()
        bgTop.cornerRadii = floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f)
        bgTop.setColor(colorDark)
        mRootView = View.inflate(context, R.layout.layout_cool_dialog, null)
        mRootView?.apply {
            background = bg
            iv_top = findViewById(R.id.iv_top)
            (findViewById<View>(R.id.top) as? FrameLayout)?.background = bgTop
            tv_title = findViewById(R.id.tv_title)
            tv_msg_main = findViewById(R.id.tv_msg_main)
            tv_msg_sub = findViewById(R.id.tv_msg_sub)
            btn_no = findViewById(R.id.btn_no)
            btn_yes = findViewById(R.id.btn_yes)
            setContentView(this)
        }
        btnNegativeBg = context.resources.getDrawable(R.drawable.bg_negative_btn)
        btnPositiveBg = context.resources.getDrawable(R.drawable.bg_positive_btn)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val params = window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT//ScreenUtil.dp2px(context, 300f)//ViewGroup.LayoutParams.MATCH_PARENT
        val padding = context.resources.getDimensionPixelOffset(R.dimen.dialog_margin)//setPadding(padding, 0, padding, 0)
        window?.decorView?.setPadding(padding, padding, padding, padding)
    }

    fun withDuration(duration: Long): CoolDialog = apply { mDuration = duration }

    fun withCancelable(cancelable: Boolean): CoolDialog =
            apply { setCancelable(cancelable) }

    fun withCanceledOnTouchOutside(cancelable: Boolean): CoolDialog =
            apply { setCanceledOnTouchOutside(cancelable) }

    fun withIcon(imgId: Int): CoolDialog =
            apply { iv_top?.apply {
                setImageResource(imgId)
                visibility = View.VISIBLE
            } }

    fun withTitle(str: String, color: Int = Color.WHITE): CoolDialog =
            apply {
                tv_title?.apply {
                    text = str
                    setTextColor(color)
                    visibility = if (TextUtils.isEmpty(str)) View.GONE else View.VISIBLE
                }
            }

    fun withMsg(str: CharSequence, color: Int = Color.WHITE): CoolDialog =
            apply {
                tv_msg_main?.apply {
                    text = str
                    movementMethod = LinkMovementMethod.getInstance()
                    setTextColor(color)
                    visibility = if (TextUtils.isEmpty(str)) View.GONE else View.VISIBLE
                }
            }

    fun withMsgSub(str: CharSequence, color: Int = Color.WHITE): CoolDialog =
            apply {
                tv_msg_sub?.apply {
                    text = str
                    setTextColor(color)
                    visibility = if (TextUtils.isEmpty(str)) View.GONE else View.VISIBLE
                }
            }

    fun withContentCustom(childView: View? = null, layoutId: Int = R.layout.layout_dialog_content_custom): CoolDialog =
            apply {
                initViewStub(layoutId = layoutId)
                content_custom_view?.run {
                    (this@run as? ViewGroup)?.takeUnless { childView == null }?.apply {
                        addView(childView)
                    }
                }
            }
    private fun initViewStub(layoutId: Int) =
        takeIf { content_custom_view == null }?.apply {
            mRootView?.findViewById<ViewStub>(R.id.content_custom_stub)?.apply {
                layoutResource = layoutId
                content_custom_view = inflate()
            }
        }

    fun withNegativeBtn(str: CharSequence, bg: Drawable = btnNegativeBg, clickListener: View.OnClickListener = View.OnClickListener { dismiss() }, color: Int = context.resources.getColor(R.color.colorPrimaryDark)): CoolDialog =
            apply { btn_no?.setStyle(str, clickListener, bg, color) }

    fun withPositiveBtn(str: CharSequence, bg: Drawable = btnPositiveBg, clickListener: View.OnClickListener = View.OnClickListener { dismiss() }, color: Int = Color.WHITE): CoolDialog =
            apply { btn_yes?.setStyle(str, clickListener, bg, color) }

    fun Button.setStyle(str: CharSequence, clickListener: View.OnClickListener, bg: Drawable, color: Int) =
            apply {
                text = str
                background = bg
                setTextColor(color)
                visibility = if (TextUtils.isEmpty(str)) View.GONE else View.VISIBLE
                setOnClickListener(clickListener)
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