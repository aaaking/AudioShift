package com.example.zhouzhihui.audioshift.record

import android.media.AudioRecord

import java.io.File

internal class AudioRecorder(private val audioRecord: AudioRecord, private val file: File) : Recorder {

    private var recorderThread: Thread? = null
    private var recorderTask: AudioRecorderTask? = null

    override fun isRecording(): Boolean {
        return recorderThread != null && recorderThread!!.isAlive
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
