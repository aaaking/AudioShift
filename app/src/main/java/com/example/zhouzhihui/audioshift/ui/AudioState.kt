package com.example.zhouzhihui.audioshift.ui

import android.graphics.drawable.Animatable
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
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

fun getSaveFileName(tempFile: File?): String = tempFile?.parentFile?.listFiles()?.filter { it.absolutePath != tempFile.absolutePath }?.run {
    Log.i(TAG, "$size   ${getMaxNum(this)}")
    "音频文件_${Math.max(size, getMaxNum(this)) + 1}"
} ?: "音频文件_1"

/**
 * 已经录音的文件数量，不包括临时文件
 */
fun getRecordedFileNum(tempFile: File?): String = tempFile?.parentFile?.listFiles()?.filter { it.absolutePath != tempFile.absolutePath }?.run {
    "$size"
} ?: "0"

fun getLatestModifiedFileName(tempFile: File?): String = tempFile?.parentFile?.listFiles()?.filter { it.absolutePath != tempFile.absolutePath }?.run {
    if (size > 0) sortedByDescending { it.lastModified() }[0]?.name else ""
} ?: ""

/**
 * 从一个文件列表中找出后缀是数字的最大的数字，比如文件列表：音频文件_000121、音频文件_289、音频文件、音频文件_111，那么返回结果是289
 */
fun getMaxNum(list: List<File>?): Int = list?.run {
    var maxValue = 0
    list.map { it -> findDigit(it) }.asSequence().filter { it > maxValue }.forEach { maxValue = it }
    return maxValue
} ?: 0

/**
 * 返回文件后缀的数字部分，比如文件：音频文件_0001，那么返回1    音频文件_1000，那么返回1000
 */
fun findDigit(file: File?): Int = file?.name?.run {
    var value = 0
    StringBuilder().let {
        (length - 1 downTo 0)
                .takeWhile { get(it).isDigit() == true }
                .forEach { i -> it.append(get(i)) }
        with(it.toString()) {
            if (isEmpty()) 0 else {
                (length - 1 downTo 0).forEach { value += get(it).toString().toInt() * Math.pow(10.0, it.toDouble()).toInt() }
                value
            }
        }
    }
} ?: 0
