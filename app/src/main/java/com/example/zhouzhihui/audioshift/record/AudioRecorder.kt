package com.example.zhouzhihui.audioshift.record

import android.media.AudioRecord

import java.io.File

class AudioRecorder(val audioRecord: AudioRecord, val file: File) : Recorder {

    var recorderThread: Thread? = null
    var recorderTask: AudioRecorderTask? = null

    override fun isRecording(): Boolean = recorderThread != null

    override fun hasRecording(): Boolean = fileHasContent(file.parentFile)

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

    private tailrec fun fileHasContent(fileP: File?): Boolean =
            when {
                fileP == null -> false
                fileP.isDirectory -> fileP.listFiles()?.any { it.length() > 0 } ?: false
                else -> fileHasContent(fileP.parentFile)
            }
}
