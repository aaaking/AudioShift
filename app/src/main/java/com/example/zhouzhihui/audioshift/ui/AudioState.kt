package com.example.zhouzhihui.audioshift.ui

import android.graphics.drawable.Animatable
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.zhouzhihui.audioshift.AudioApp
import com.example.zhouzhihui.audioshift.R
import com.example.zhouzhihui.audioshift.TAG
import java.io.File

/**
 * Created by 周智慧 on 05/02/2018.
 */
fun startVoiceStateAnimation(imageView: ImageView?, startAnim: Boolean) = (imageView?.drawable as? Animatable)?.apply { if (startAnim) start() else stop() }

fun startBtnPlayAnimation(imageView: ImageView?, startAnim: Boolean) = imageView?.apply {
    setImageResource(if (startAnim) R.drawable.audio_take_animate2stop else R.drawable.audio_take_animate2start)
    (drawable as? Animatable)?.start()
}

fun saveRecordFile(tempFile: File?, saveFileName: String?) = tempFile?.takeIf { tempFile.length() > 0 && !TextUtils.isEmpty(saveFileName) }?.run {
    tempFile.renameTo(File(tempFile.parentFile.absolutePath + File.separator + saveFileName))
}