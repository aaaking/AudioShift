package com.example.zhouzhihui.audioshift.ui

import android.graphics.drawable.Animatable
import android.view.View
import android.widget.ImageView

/**
 * Created by 周智慧 on 05/02/2018.
 */
fun startVoiceStateAnimation(imageView: ImageView?) = (imageView?.drawable as? Animatable)?.apply { if (isRunning) stop() else start() }