package com.zzh.ui.cooledittext

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet


/**
 * Created by 周智慧 on 05/02/2018.
 */
class CoolEditText : TextInputLayout {
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
        val res = context!!.resources
//        cursorWidth = ta.getDimensionPixelOffset(R.styleable.CoolEditText_cet_cursor_width, res.getDimensionPixelOffset(R.dimen.cursor_width))
//        cursorColor = ContextCompat.getColor(context, ta.getResourceId(R.styleable.CoolEditText_cet_cursor_color, R.color.colorCoolEditTextNormal))
    }
}