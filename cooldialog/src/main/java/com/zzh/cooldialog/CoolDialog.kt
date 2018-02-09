package com.zzh.cooldialog

import android.animation.Animator
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
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Button
import android.widget.TextView

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
    var mRootView: View? = null
    private var mDuration = -1L
    private var mCoolStyle: CoolStyle? = null
    private var mPadding = 0
    //top
    var mBgTop: Drawable? = GradientDrawable()
    var mBg: Drawable? = GradientDrawable()
    var iv_top: AppCompatImageView? = null
    var topContainer: View? = null
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
        var radius = context.resources.getDimension(R.dimen.btn_common_radius)
        var colorDark = context.resources.getColor(R.color.colorPrimaryDark)
        (mBg as? GradientDrawable)?.cornerRadius = radius
        (mBg as? GradientDrawable)?.setColor(context.resources.getColor(R.color.colorPrimaryLightLight))
        (mBgTop as? GradientDrawable)?.cornerRadii = floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f)
        (mBgTop as? GradientDrawable)?.setColor(colorDark)
        mRootView = View.inflate(context, R.layout.layout_cool_dialog, null)
        mRootView?.apply {
            setBackgroundDrawable(mBg)
            iv_top = findViewById(R.id.iv_top)
            topContainer = findViewById(R.id.top)
            topContainer?.setBackgroundDrawable(mBgTop)
            tv_title = findViewById(R.id.tv_title)
            tv_msg_main = findViewById(R.id.tv_msg_main)
            tv_msg_sub = findViewById(R.id.tv_msg_sub)
            btn_no = findViewById(R.id.btn_no)
            btn_yes = findViewById(R.id.btn_yes)
            setContentView(this)
        }
        btnNegativeBg = context.resources.getDrawable(R.drawable.bg_negative_btn)
        btnPositiveBg = context.resources.getDrawable(R.drawable.bg_positive_btn)
        setOnShowListener { mCoolStyle?.startPlayStyle() }
        setOnDismissListener(CoolDialogDismissListener(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val params = window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        mPadding = context.resources.getDimensionPixelOffset(R.dimen.dialog_margin)
        window?.decorView?.setPadding(mPadding, mPadding, mPadding, mPadding)
    }

    fun withBgAndTopbg(bg: Drawable?, topBg: Drawable?) = apply {
        mBg = bg
        mBgTop = topBg
        mRootView?.setBackgroundDrawable(mBg)
        topContainer?.setBackgroundDrawable(mBgTop)
    }

    fun withBgAndTopbgColor(bgCorlor: Int, topBgCorlor: Int) = apply {
        (mBg as? GradientDrawable)?.setColor(bgCorlor)
        (mBgTop as? GradientDrawable)?.setColor(topBgCorlor)
    }

    fun withDuration(duration: Long): CoolDialog = apply {
        mDuration = duration
        mCoolStyle?.setDuration(mDuration)
    }

    fun withCoolStyle(style: CoolStyle): CoolDialog = apply {
        mCoolStyle = style
        mCoolStyle?.setDuration(mDuration)
    }

    fun withCancelable(cancelable: Boolean): CoolDialog = apply { setCancelable(cancelable) }

    fun withCanceledOnTouchOutside(cancelable: Boolean): CoolDialog = apply { setCanceledOnTouchOutside(cancelable) }

    fun withIcon(imgId: Int): CoolDialog = apply {
        iv_top?.apply {
            setImageResource(imgId)
            visibility = View.VISIBLE
        }
    }

    fun withTitle(str: String, color: Int = Color.WHITE): CoolDialog = apply {
        tv_title?.apply {
            text = str
            setTextColor(color)
        }
        topContainer?.visibility = if (TextUtils.isEmpty(str)) View.GONE else View.VISIBLE
    }

    fun withMsg(str: CharSequence, color: Int = Color.WHITE): CoolDialog = apply {
        tv_msg_main?.apply {
            text = str
            movementMethod = LinkMovementMethod.getInstance()
            setTextColor(color)
            visibility = if (TextUtils.isEmpty(str)) View.GONE else View.VISIBLE
        }
    }

    fun withMsgSub(str: CharSequence, color: Int = Color.WHITE): CoolDialog = apply {
        tv_msg_sub?.apply {
            text = str
            setTextColor(color)
            visibility = if (TextUtils.isEmpty(str)) View.GONE else View.VISIBLE
        }
    }

    fun withContentCustom(childView: View? = null, layoutId: Int = R.layout.layout_dialog_content_custom): CoolDialog = apply {
        initViewStub(layoutId = layoutId)
        content_custom_view?.run {
            (this@run as? ViewGroup)?.takeUnless { childView == null }?.apply {
                addView(childView)
            }
        }
    }
    private fun initViewStub(layoutId: Int) = takeIf { content_custom_view == null }?.apply {
        mRootView?.findViewById<ViewStub>(R.id.content_custom_stub)?.apply {
            layoutResource = layoutId
            content_custom_view = inflate()
        }
    }

    fun withNegativeBtn(str: CharSequence, bg: Drawable? = btnNegativeBg, clickListener: View.OnClickListener = CoolDialogClickListener(this), color: Int = context.resources.getColor(R.color.colorPrimaryDark)): CoolDialog = apply { btn_no?.setStyle(str, clickListener, bg, color) }

    fun withPositiveBtn(str: CharSequence, bg: Drawable? = btnPositiveBg, clickListener: View.OnClickListener = CoolDialogClickListener(this), color: Int = Color.WHITE): CoolDialog = apply { btn_yes?.setStyle(str, clickListener, bg, color) }

    fun Button.setStyle(str: CharSequence, clickListener: View.OnClickListener, bg: Drawable?, color: Int) = apply {
        text = str
        setBackgroundDrawable(bg)
        setTextColor(color)
        visibility = if (TextUtils.isEmpty(str)) View.GONE else View.VISIBLE
        setOnClickListener(clickListener)
    }

    fun withDismissListener(dismissListener: CoolDialogDismissListener = CoolDialogDismissListener(this)): CoolDialog = apply { setOnDismissListener(dismissListener) }

    fun withCustomAnim(items: Collection<Animator>, together: Boolean = true) = apply {
        mCoolStyle = CoolStyle(mRootView, -1).apply {
            setDuration(mDuration)
            mAnimatorSet.apply { if (together) playTogether(items) else playSequentially(items.toList()) }
        }
    }

    open class CoolDialogClickListener(var mDialog: CoolDialog?): View.OnClickListener {
        override fun onClick(v: View?) {
            mDialog?.dismiss()
        }
    }

    open class CoolDialogDismissListener(var mDialog: CoolDialog?): DialogInterface.OnDismissListener {
        override fun onDismiss(dialog: DialogInterface?) {
            mDialog?.mCoolStyle?.stopPlayStyle()
        }
    }

    fun show(gravity: Int = Gravity.CENTER, paddingLeft: Int = mPadding, paddingTop: Int = mPadding, paddingRight: Int = mPadding, paddingBottom: Int = mPadding) = apply {
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
        window?.decorView?.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        window?.setGravity(gravity)
    }
}