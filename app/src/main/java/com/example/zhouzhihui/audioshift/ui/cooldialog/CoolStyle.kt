package com.example.zhouzhihui.audioshift.ui.cooldialog

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

/**
 * Created by 周智慧 on 03/02/2018.
 */
val COOL_STYLE_ROTATE = 3

data class CoolStyle(var mTargetView: View?, var mStyle: Int = 0) {
    var mDuration = -1L
    private var mAnimatorSet: AnimatorSet = AnimatorSet()

    fun startPlayStyle() = takeIf { mTargetView != null }?.run {
        setAnimation()
        mAnimatorSet.start()
    }

    fun stopPlayStyle() {
        mAnimatorSet.childAnimations.forEach {
            it.takeIf { it.isRunning }?.apply {
                cancel()
                removeAllListeners()
            }
        }
        mTargetView?.clearAnimation()
    }

    private fun setAnimation() {
        when (mStyle) {
            COOL_STYLE_ROTATE -> getRotateAnimators(mTargetView)?.apply { mAnimatorSet.playTogether(this) }
        }
    }

    private fun getRotateAnimators(view: View?): Collection<Animator>? = takeIf { view != null }?.run {
        arrayListOf(
                ObjectAnimator.ofFloat(view, "rotation", 1080f, 720f, 360f, 0f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(mDuration * 3 / 2),
                ObjectAnimator.ofFloat(view, "scaleX", 0.1f, 0.5f, 1f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "scaleY", 0.1f, 0.5f, 1f).setDuration(mDuration))
    }
}