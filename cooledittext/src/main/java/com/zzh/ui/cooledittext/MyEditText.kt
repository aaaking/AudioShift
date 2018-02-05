package com.zzh.ui.cooledittext

import android.content.Context
import android.support.design.widget.TextInputEditText
import android.util.AttributeSet

/**
 * Created by 周智慧 on 05/02/2018.
 */
class MyEditText : TextInputEditText {
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

    }
}