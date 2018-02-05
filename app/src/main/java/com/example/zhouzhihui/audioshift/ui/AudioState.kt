package com.example.zhouzhihui.audioshift.ui

import android.graphics.drawable.Animatable
import android.widget.ImageView
import android.widget.Toast
import com.example.zhouzhihui.audioshift.AudioApp
import com.example.zhouzhihui.audioshift.R
import java.io.File

/**
 * Created by 周智慧 on 05/02/2018.
 */
fun startVoiceStateAnimation(imageView: ImageView?, startAnim: Boolean) = (imageView?.drawable as? Animatable)?.apply { if (startAnim) start() else stop() }

fun startBtnPlayAnimation(imageView: ImageView?, startAnim: Boolean) = imageView?.apply {
    setImageResource(if (startAnim) R.drawable.audio_take_animate2stop else R.drawable.audio_take_animate2start)
    (drawable as? Animatable)?.start()
}

fun saveRecordFile(file: File?) {
    Toast.makeText(AudioApp.sAppContext, "saveRecordFile", Toast.LENGTH_SHORT).show()
}