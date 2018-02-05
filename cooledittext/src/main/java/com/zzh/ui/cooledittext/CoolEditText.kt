package com.zzh.ui.cooledittext

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet

/**
 * Created by 周智慧 on 05/02/2018.
 */
class CoolEditText : TextInputLayout {
    lateinit var mEditText: MyEditText
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
        mEditText = if (childCount > 0 && getChildAt(0) is MyEditText) {
            getChildAt(0) as MyEditText
        } else {
            MyEditText(context, attributeSet, defStyleAttr)
        }
    }
}