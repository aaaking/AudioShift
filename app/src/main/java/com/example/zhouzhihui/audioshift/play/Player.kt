package com.example.zhouzhihui.audioshift.play

interface Player {
    fun isPlaying(): Boolean

    fun startPlaying()

    fun stopPlaying()

    fun setSpeed(speed: Float)
}
