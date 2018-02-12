package com.example.zhouzhihui.audioshift.play

import java.io.File

interface Player {
    fun isPlaying(): Boolean
    fun getPlaybackrate(): Int

    fun startPlaying(fileP: File)

    fun stopPlaying()

    fun setSpeed(speed: Float)
}
