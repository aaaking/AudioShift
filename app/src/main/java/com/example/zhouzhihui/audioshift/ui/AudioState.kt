package com.example.zhouzhihui.audioshift.ui

import android.graphics.drawable.Animatable
import android.widget.ImageView
import com.example.zhouzhihui.audioshift.R

/**
 * Created by 周智慧 on 05/02/2018.
 */
fun startVoiceStateAnimation(imageView: ImageView?, startAnim: Boolean) = (imageView?.drawable as? Animatable)?.apply { if (startAnim) start() else stop() }

fun startAnimation(imageView: ImageView, startAnim: Boolean) = imageView.apply {
    setImageResource(if (startAnim) R.drawable.audio_take_animate2stop else R.drawable.audio_take_animate2start)
    (drawable as? Animatable)?.start()
}