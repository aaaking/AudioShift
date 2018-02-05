package com.example.zhouzhihui.audioshift.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator

fun bigger(`object`: View?, duration: Int) {
    if (`object` == null) {
        return
    }
    val animatorScaleY = ObjectAnimator.ofFloat(`object`, "scaleY", 1f, 1.09f)
    val animatorScaleX = ObjectAnimator.ofFloat(`object`, "scaleX", 1f, 1.09f)
    animatorScaleX.interpolator = LinearInterpolator()
    animatorScaleX.duration = duration.toLong()
    animatorScaleY.interpolator = LinearInterpolator()
    animatorScaleY.duration = duration.toLong()
    val animatorSet = AnimatorSet()
    animatorSet.play(animatorScaleX).with(animatorScaleY)
    animatorSet.start()
}

fun smaller(`object`: View?, duration: Int) {
    if (`object` == null) {
        return
    }
    val animatorScaleY = ObjectAnimator.ofFloat(`object`, "scaleY", 1.09f, 1f)
    val animatorScaleX = ObjectAnimator.ofFloat(`object`, "scaleX", 1.09f, 1f)
    animatorScaleX.interpolator = LinearInterpolator()
    animatorScaleX.duration = duration.toLong()
    animatorScaleY.interpolator = LinearInterpolator()
    animatorScaleY.duration = duration.toLong()
    val animatorSet = AnimatorSet()
    animatorSet.play(animatorScaleX).with(animatorScaleY)
    animatorSet.start()
}
