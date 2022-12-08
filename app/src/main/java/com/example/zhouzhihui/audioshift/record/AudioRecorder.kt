package com.example.zhouzhihui.audioshift.record

import android.media.AudioRecord

import java.io.File

class AudioRecorder(val audioRecord: AudioRecord, val file: File) : Recorder {
    override fun getRecordFile(): File? = file

    var recorderThread: Thread? = null
    var recorderTask: AudioRecorderTask? = null

    override fun isRecording(): Boolean = recorderThread != null

    override fun hasRecording(): Boolean = fileHasContent(file.parentFile)

    override fun startRecording() {
        recorderTask = AudioRecorderTask(audioRecord, file)
        recorderThread = Thread(recorderTask)
        audioRecord.startRecording()
        recorderThread!!.start()
    }

    fun pause() {

    }

    override fun stopRecording() {
        audioRecord.stop()
        audioRecord.release()
        recorderThread = null
        recorderTask = null
    }

}

tailrec fun fileHasContent(fileP: File?): Boolean =
        when {
            fileP == null -> false
            fileP.isDirectory -> fileP.listFiles()?.any { it.length() > 0 } ?: false
            else -> fileHasContent(fileP.parentFile)
        }
