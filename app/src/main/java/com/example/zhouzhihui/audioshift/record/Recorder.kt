package com.example.zhouzhihui.audioshift.record

public interface Recorder {
    public fun isRecording(): Boolean

    public fun hasRecording(): Boolean

    public fun stopRecording()

    public fun startRecording()
}
