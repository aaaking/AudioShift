package com.example.zhouzhihui.audioshift.record

import android.media.AudioRecord

import java.io.File

class AudioRecorder(val audioRecord: AudioRecord, val file: File) : Recorder {

    var recorderThread: Thread? = null
    var recorderTask: AudioRecorderTask? = null

    override fun isRecording(): Boolean {
        return recorderThread != null
    }

    override fun hasRecording(): Boolean {
        return file.exists() && file.length() > 0
    }

    override fun startRecording() {
        recorderTask = AudioRecorderTask(audioRecord, file)
        recorderThread = Thread(recorderTask)
        recorderThread!!.start()
        audioRecord.startRecording()
    }

    override fun stopRecording() {
        audioRecord.stop()
        audioRecord.release()
        recorderThread = null
        recorderTask = null
    }
}
