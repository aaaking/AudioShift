package com.example.zhouzhihui.audioshift.record

import android.media.AudioRecord
import android.util.Log
import com.example.zhouzhihui.audioshift.TAG

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class AudioRecorderTask(val audioRecord: AudioRecord, val outputFile: File) : Runnable {

    val outputStream: OutputStream?
        get() {
            val outputStream: OutputStream
            try {
                outputStream = FileOutputStream(outputFile, true)
            } catch (e: FileNotFoundException) {
                Log.e(TAG, "Error opening audio file for writing")
                return null
            }
            return outputStream
        }

    fun stop() {
        try {
            outputStream?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error closing audio file")
        }
    }

    override fun run() {
        val outputStream = outputStream ?: return
        val buffer = ByteArray(BUFFER_SIZE)
        while (true) {
            var read = audioRecord.read(buffer, 0, BUFFER_SIZE)
            while (read > 0) {
                try {
                    outputStream.write(buffer, 0, read)
                } catch (e: IOException) {
                    Log.e(TAG, "Error writing audio file")
                }
                read = audioRecord.read(buffer, 0, BUFFER_SIZE)
                //Timber.d("Read");
            }
        }

    }

    companion object {
        val BUFFER_SIZE = 1024
    }
}
