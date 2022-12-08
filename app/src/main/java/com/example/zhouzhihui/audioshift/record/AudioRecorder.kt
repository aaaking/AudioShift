package com.example.zhouzhihui.audioshift.record

import android.media.AudioRecord

import java.io.File

class AudioRecorder(val audioRecord: AudioRecord, val file: File) : Recorder {
    override fun getRecordFile(): File? = file

    var pause: Boolean = false
    var recorderThread: Thread? = null
    var recorderTask: AudioRecorderTask? = null

    override fun isRecording(): Boolean = recorderThread != null

    override fun hasRecording(): Boolean = fileHasContent(file.parentFile)

    override fun startRecording() {
        file?.delete()
        recorderTask = AudioRecorderTask(audioRecord, file)
        recorderThread = Thread(recorderTask)
        audioRecord.startRecording()
        recorderThread!!.start()
    }

    override fun pause() {
        pause = true
        audioRecord.stop()
    }

    override fun resume() {
        pause = false
        audioRecord.startRecording()
    }

    override fun stopRecording() {
        pause = false
        audioRecord.stop()
        audioRecord.release()
        recorderTask?.stop()
        recorderTask = null
        recorderThread?.interrupt()
        recorderThread = null
    }

    override fun isPausing(): Boolean = pause

}

tailrec fun fileHasContent(fileP: File?): Boolean =
        when {
            fileP == null -> false
            fileP.isDirectory -> fileP.listFiles()?.any { it.length() > 0 } ?: false
            else -> fileHasContent(fileP.parentFile)
        }
