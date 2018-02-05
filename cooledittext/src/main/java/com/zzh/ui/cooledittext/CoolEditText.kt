package com.zzh.ui.cooledittext

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.design.widget.TextInputLayout
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
class CoolEditText : TextInputLayout, View.OnTouchListener, TextWatcher,View.OnFocusChangeListener {
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (editText?.compoundDrawables?.get(2) == null)
            return false
        if (event?.action != MotionEvent.ACTION_UP)
            return false
        if (event?.x > (editText?.width ?: 0) - (editText?.paddingRight ?: 0) - rightImage.intrinsicWidth) {
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
            editText?.setText("")
            removeRightButton()
        } else if (rightType == TYPE_RIGHT_EYE) {
            //输入密码可见
            if (editText?.getTransformationMethod() === android.text.method.PasswordTransformationMethod.getInstance()) {
                editText?.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                setRightImage(eyeCloseRes)
            } else {
                editText?.setTransformationMethod(PasswordTransformationMethod.getInstance())
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
        Log.i(TAG, "init editText ${editText}")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setRightBtnType()
        editText?.setOnTouchListener(this)
//        editText?.setPadding(100, editText?.paddingTop ?: 0, editText?.paddingRight ?: 0, editText?.paddingBottom ?: 0)
        editText?.addTextChangedListener(this)
        editText?.onFocusChangeListener = this
        Log.i(TAG, "onAttachedToWindow editText ${editText}")
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
            if (editText == null || editText?.text.toString() == "" || editText?.isFocused != true)
            //增加是否是当前焦点
                removeRightButton()
            else
                addRightButton()
        } else {
            removeRightButton()
        }
    }

    fun removeRightButton() {
        editText?.setCompoundDrawables(this.icon, editText?.compoundDrawables?.get(1), null, editText?.compoundDrawables?.get(3))
    }

    fun addRightButton() {
        editText?.setCompoundDrawables(this.icon, editText?.compoundDrawables?.get(1), rightImage, editText?.compoundDrawables?.get(3))
    }

    private fun setRightBtnType() {
        setRightImage(if (rightType == TYPE_RIGHT_EYE) eyeOpenRes else deleteImageRes)
    }
}