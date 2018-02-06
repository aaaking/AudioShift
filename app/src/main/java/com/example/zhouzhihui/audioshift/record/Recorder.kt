package com.example.zhouzhihui.audioshift.record

import java.io.File

public interface Recorder {
    public fun getRecordFile(): File?

    public fun isRecording(): Boolean

    public fun hasRecording(): Boolean

    public fun stopRecording()

    public fun startRecording()
}
