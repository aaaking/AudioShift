package com.example.zhouzhihui.audioshift.record

interface Recorder {
    fun isRecording(): Boolean

    fun hasRecording(): Boolean

    fun stopRecording()

    fun startRecording()
}
