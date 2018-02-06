package com.zzh.ui.cooledittext

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View


/**
 * Created by 周智慧 on 05/02/2018.
 */
val TYPE_RIGHT_DEL = 0
val TYPE_RIGHT_EYE = 1
val TAG = CoolEditText::class.java.simpleName
class CoolEditText : TextInputEditText, View.OnTouchListener, TextWatcher,View.OnFocusChangeListener {
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (compoundDrawables?.get(2) == null)
            return false
        if (event?.action != MotionEvent.ACTION_UP)
            return false
        if (event?.x > width - paddingRight - rightImage.intrinsicWidth) {
            clickRightBtn()
            return true//点击右边的icon消耗掉此次事件
        }
        return false
    }

    override fun afterTextChanged(s: Editable?) {
        manageImage()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            manageImage()
        } else {
            removeRightButton()
        }
    }

    fun clickRightBtn() {
        if (rightType == TYPE_RIGHT_DEL) {
            //删除
            setText("")
            removeRightButton()
        } else if (rightType == TYPE_RIGHT_EYE) {
            //输入密码可见
            if (transformationMethod === android.text.method.PasswordTransformationMethod.getInstance()) {
                transformationMethod = HideReturnsTransformationMethod.getInstance()
                setRightImage(eyeCloseRes)
            } else {
                transformationMethod = PasswordTransformationMethod.getInstance()
                setRightImage(eyeOpenRes)
            }
            this.postInvalidate()
        }
    }

    private var rightType = TYPE_RIGHT_DEL
    lateinit var rightImage: Drawable
    var deleteImageRes = R.mipmap.icon_edit_delete
    var eyeOpenRes = R.mipmap.icon_password_eye_open
    var eyeCloseRes = R.mipmap.icon_password_eye_close
    var icon: Drawable? = null
    private var isShowRightBtn = true

    constructor(context: Context?) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context?, attributeSet: AttributeSet?) : super(context, attributeSet) {
        init(context, attributeSet, 0)
    }

    constructor(context: Context?, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        init(context, attributeSet, defStyleAttr)
    }

    fun init(context: Context?, attributeSet: AttributeSet?, defStyleAttr: Int) {
        val ta = context!!.obtainStyledAttributes(attributeSet, R.styleable.CoolEditText)
//        val res = context!!.resources
//        cursorWidth = ta.getDimensionPixelOffset(R.styleable.CoolEditText_cet_cursor_width, res.getDimensionPixelOffset(R.dimen.cursor_width))
//        cursorColor = ContextCompat.getColor(context, ta.getResourceId(R.styleable.CoolEditText_cet_cursor_color, R.color.colorCoolEditTextNormal))
        rightType = ta.getInteger(R.styleable.CoolEditText_cet_right_type, TYPE_RIGHT_DEL)
        deleteImageRes = ta.getResourceId(R.styleable.CoolEditText_cet_icon_delete, R.mipmap.icon_edit_delete)
        eyeOpenRes = ta.getResourceId(R.styleable.CoolEditText_cet_icon_eye_open, R.mipmap.icon_password_eye_open)
        eyeCloseRes = ta.getResourceId(R.styleable.CoolEditText_cet_icon_eye_close, R.mipmap.icon_password_eye_close)
        Log.i(TAG, "init ${this} ")
        setRightBtnType()
        setOnTouchListener(this)
        addTextChangedListener(this)
        onFocusChangeListener = this
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.i(TAG, "onAttachedToWindow ${this} ")
    }

    fun setIconResource(id: Int) {
        Log.i(TAG, "setIconResource")
        icon = resources.getDrawable(id)
        icon?.takeIf { icon != null }?.setBounds(0, 0, icon!!.intrinsicWidth, icon!!.intrinsicHeight)
        manageImage()
    }

    fun setRightImage(id: Int) {
        rightImage = resources.getDrawable(id)
        rightImage.setBounds(0, 0, rightImage.intrinsicWidth, rightImage.intrinsicHeight)
        manageImage()
    }

    fun setShowRightBtn(showRightBtn: Boolean) {
        isShowRightBtn = showRightBtn
        manageImage()
    }

    fun manageImage() {
        if (isShowRightBtn) {
            if (text.toString() == "" || !isFocused)
            //增加是否是当前焦点
                removeRightButton()
            else
                addRightButton()
        } else {
            removeRightButton()
        }
    }

    fun removeRightButton() {
        setCompoundDrawables(this.icon, compoundDrawables?.get(1), null, compoundDrawables?.get(3))
    }

    fun addRightButton() {
        setCompoundDrawables(this.icon, compoundDrawables?.get(1), rightImage, compoundDrawables?.get(3))
    }

    private fun setRightBtnType() {
        setRightImage(if (rightType == TYPE_RIGHT_EYE) eyeOpenRes else deleteImageRes)
    }
}