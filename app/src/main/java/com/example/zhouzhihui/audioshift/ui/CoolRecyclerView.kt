package com.example.zhouzhihui.audioshift.ui

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import com.example.zhouzhihui.audioshift.R
import com.example.zhouzhihui.audioshift.util.ScreenUtil

/**
 * Created by 周智慧 on 07/02/2018.
 */
val LAYOUT_MANAGER_LINEAR = 0
val LAYOUT_MANAGER_GRID = 1
class CoolRecyclerView : RecyclerView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle) { init(context, attributeSet, defStyle) }
    
    private fun init(context: Context, attributeSet: AttributeSet?, defStyle: Int) {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.CoolRecyclerView)
        val layoutManagerType = ta.getInteger(R.styleable.CoolRecyclerView_layout_manager_type, LAYOUT_MANAGER_LINEAR)
        val spanCount = ta.getInteger(R.styleable.CoolRecyclerView_grid_span_count, 3)
        val dividerHeight = ta.getDimensionPixelOffset(R.styleable.CoolRecyclerView_dividerHeight, ScreenUtil.dp2px(context, 1.5f))
        ta.recycle()
        itemAnimator = DefaultItemAnimator()
        itemAnimator.changeDuration = 0//解决闪屏问题：http://www.jianshu.com/p/654dac931667
        (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        val pool = recycledViewPool
        if (pool != null) {
            for (i in 0..39) {
                pool.setMaxRecycledViews(i, 10)
            }
        }
        //下面设置layoutmanager
        if (layoutManagerType == LAYOUT_MANAGER_LINEAR) {
            val layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
            setLayoutManager(layoutManager)
            val decorationThin = object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                    super.getItemOffsets(outRect, view, parent, state)
                    if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
                        outRect.set(0, 0, 0, dividerHeight)
                    } else {
                        outRect.set(0, 0, dividerHeight, 0)
                    }
                }
            }
            addItemDecoration(decorationThin)
        } else if (layoutManagerType == LAYOUT_MANAGER_GRID) {
            layoutManager = GridLayoutManager(getContext(), spanCount, GridLayoutManager.VERTICAL, false)
            val decorationThin = object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.set(dividerHeight, dividerHeight, dividerHeight, dividerHeight)
                }
            }
            addItemDecoration(decorationThin)
        }
//        setHasFixedSize(true)
        LayoutAnimationController(AnimationUtils.loadAnimation(context, R.anim.item_animation_fall_down)).apply {
            delay = 0.15f
            order = LayoutAnimationController.ORDER_NORMAL
            this@CoolRecyclerView.layoutAnimation = this
        }
    }

    fun notifyItemInserted(pos: Int, scrollToBottom: Boolean) {
        adapter?.notifyItemInserted(pos)
        if (scrollToBottom && (adapter?.itemCount ?: 0) > 0) {
            scrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun getOrientation(parent: RecyclerView): Int = (parent.layoutManager as? LinearLayoutManager)?.orientation ?: LinearLayoutManager.VERTICAL
}