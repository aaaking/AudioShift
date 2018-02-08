package com.zzh.cooldialog

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

/**
 * Created by 周智慧 on 03/02/2018.
 */
val COOL_STYLE_SLIDE_BOTTOM = 0
val COOL_STYLE_SLIDE_TOP = 1
val COOL_STYLE_SLIDE_LEFT = 2
val COOL_STYLE_SLIDE_RIGHT = 3

val COOL_STYLE_ROTATE = 4

val COOL_STYLE_FADE_IN = 5

val COOL_STYLE_FALL_DOWN = 6
val COOL_STYLE_FALL_DOWN_OFFSET = 7

val COOL_STYLE_SHAKE = 8

val COOL_STYLE_3D_ROTATE_LEFT = 9
val COOL_STYLE_3D_ROTATE_BOTTOM = 10
val COOL_STYLE_3D_FLIP_H = 11
val COOL_STYLE_3D_FLIP_V = 12
val COOL_STYLE_3D_SLIT_H = 13
val COOL_STYLE_3D_SLIT_V = 14
data class CoolStyle(var mTargetView: View?, var mStyle: Int = COOL_STYLE_SLIDE_BOTTOM) {
    private var mDuration = -1L
    private var mAlphaDuration = -1L
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
    
    fun setDuration(duration: Long) {
        mDuration = duration
        mAlphaDuration = duration * 3 / 2
    }

    private fun setAnimation() {
        when (mStyle) {
            COOL_STYLE_SLIDE_BOTTOM -> getSlideBottomAnimators(mTargetView)?.apply { mAnimatorSet.playTogether(this) }
            COOL_STYLE_SLIDE_TOP -> getSlideTopAnimators(mTargetView)?.apply { mAnimatorSet.playTogether(this) }
            COOL_STYLE_SLIDE_LEFT -> getSlideLeftAnimators(mTargetView)?.apply { mAnimatorSet.playTogether(this) }
            COOL_STYLE_SLIDE_RIGHT -> getSlideRightAnimators(mTargetView)?.apply { mAnimatorSet.playTogether(this) }
            COOL_STYLE_ROTATE -> getRotateAnimators(mTargetView)?.apply { mAnimatorSet.playTogether(this) }//
            COOL_STYLE_FADE_IN -> getFadeInAnimators(mTargetView)?.apply { mAnimatorSet.playTogether(this) }
            COOL_STYLE_FALL_DOWN -> getFallDownAnimators(mTargetView)?.apply { mAnimatorSet.playTogether(this) }
            COOL_STYLE_FALL_DOWN_OFFSET -> getFallDownOffsetAnimators(mTargetView)?.apply { mAnimatorSet.playTogether(this) }//
            COOL_STYLE_SHAKE -> getShakeAnimators(mTargetView)?.apply { mAnimatorSet.playTogether(this) }
            COOL_STYLE_3D_ROTATE_LEFT -> get3DRotateLeftAnimators(mTargetView)?.apply { mAnimatorSet.playTogether(this) }//
            COOL_STYLE_3D_ROTATE_BOTTOM -> get3DRotateBottomAnimators(mTargetView)?.apply { mAnimatorSet.playTogether(this) }//
            COOL_STYLE_3D_FLIP_H -> get3DFlitHAnimators(mTargetView)?.apply { mAnimatorSet.playTogether(this) }
            COOL_STYLE_3D_FLIP_V -> get3DFlitVAnimators(mTargetView)?.apply { mAnimatorSet.playTogether(this) }
            COOL_STYLE_3D_SLIT_H -> get3DSlitHAnimators(mTargetView)?.apply { mAnimatorSet.playTogether(this) }//
            COOL_STYLE_3D_SLIT_V -> get3DSlitVAnimators(mTargetView)?.apply { mAnimatorSet.playTogether(this) }//
        }
    }

    private fun getRotateAnimators(view: View?): Collection<Animator>? = takeIf { view != null }?.run {
        arrayListOf(
                ObjectAnimator.ofFloat(view, "rotation", 1080f, 720f, 360f, 0f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(mAlphaDuration),
                ObjectAnimator.ofFloat(view, "scaleX", 0.1f, 0.55f, 1f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "scaleY", 0.1f, 0.55f, 1f).setDuration(mDuration)
        )
    }

    private fun getSlideBottomAnimators(view: View?): Collection<Animator>? = takeIf { view != null }?.run {
        arrayListOf(
                ObjectAnimator.ofFloat(view, "translationY", 300f, 0f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(mAlphaDuration)
        )
    }

    private fun getSlideTopAnimators(view: View?): Collection<Animator>? = takeIf { view != null }?.run {
        arrayListOf(
                ObjectAnimator.ofFloat(view, "translationY", -300f, 0f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(mAlphaDuration)
        )
    }

    private fun getSlideLeftAnimators(view: View?): Collection<Animator>? = takeIf { view != null }?.run {
        arrayListOf(
                ObjectAnimator.ofFloat(view, "translationX", -300f, 0f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(mAlphaDuration)
        )
    }

    private fun getSlideRightAnimators(view: View?): Collection<Animator>? = takeIf { view != null }?.run {
        arrayListOf(
                ObjectAnimator.ofFloat(view, "translationX", 300f, 0f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(mAlphaDuration)
        )
    }

    private fun getFadeInAnimators(view: View?): Collection<Animator>? = takeIf { view != null }?.run {
        arrayListOf(
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(mDuration)
        )
    }

    private fun getFallDownAnimators(view: View?): Collection<Animator>? = takeIf { view != null }?.run {
        arrayListOf(
                ObjectAnimator.ofFloat(view, "scaleX", 2f, 1.5f, 1f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "scaleY", 2f, 1.5f, 1f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(mAlphaDuration)
        )
    }

    private fun getFallDownOffsetAnimators(view: View?): Collection<Animator>? = takeIf { view != null }?.run {
        arrayListOf(
                ObjectAnimator.ofFloat(view, "scaleX", 2f, 1.5f, 1f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "scaleY", 2f, 1.5f, 1f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "rotation", 25f, 0f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "translationX", 80f, 0f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(mAlphaDuration)
        )
    }

    private fun getShakeAnimators(view: View?): Collection<Animator>? = takeIf { view != null }?.run {
        arrayListOf(
                ObjectAnimator.ofFloat(view, "translationX", 0f, .11f, -22f, .25f, 22f, .44f, -26f, .61f, 26f, .68f, -24f, .88f, 1f, 0f).setDuration(mDuration)
        )
    }

    private fun get3DRotateLeftAnimators(view: View?): Collection<Animator>? = takeIf { view != null }?.run {
        arrayListOf(
                ObjectAnimator.ofFloat(view, "rotationY", 90f, 0f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "translationX", -300f, 0f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(mAlphaDuration)
        )
    }

    private fun get3DRotateBottomAnimators(view: View?): Collection<Animator>? = takeIf { view != null }?.run {
        arrayListOf(
                ObjectAnimator.ofFloat(view, "rotationX", 90f, 0f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "translationY", 300f, 0f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(mAlphaDuration)
        )
    }

    private fun get3DFlitHAnimators(view: View?): Collection<Animator>? = takeIf { view != null }?.run {
        arrayListOf(
                ObjectAnimator.ofFloat(view, "rotationY", -90f, 0f).setDuration(mDuration)
        )
    }

    private fun get3DFlitVAnimators(view: View?): Collection<Animator>? = takeIf { view != null }?.run {
        arrayListOf(
                ObjectAnimator.ofFloat(view, "rotationX", -90f, 0f).setDuration(mDuration)
        )
    }

    private fun get3DSlitHAnimators(view: View?): Collection<Animator>? = takeIf { view != null }?.run {
        arrayListOf(
                ObjectAnimator.ofFloat(view, "rotationY", 90f, 80f, 60f, 40f, 0f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 0.4f, 0.6f, 0.8f, 1f).setDuration(mAlphaDuration),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 0.4f, 0.8f, 0.9f, 1f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 0.4f, 0.8f, 0.9f, 1f).setDuration(mDuration)
        )
    }

    private fun get3DSlitVAnimators(view: View?): Collection<Animator>? = takeIf { view != null }?.run {
        arrayListOf(
                ObjectAnimator.ofFloat(view, "rotationX", 90f, 80f, 60f, 40f, 0f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 0.4f, 0.6f, 0.8f, 1f).setDuration(mAlphaDuration),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 0.4f, 0.8f, 0.9f, 1f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 0.4f, 0.8f, 0.9f, 1f).setDuration(mDuration)
        )
    }
}