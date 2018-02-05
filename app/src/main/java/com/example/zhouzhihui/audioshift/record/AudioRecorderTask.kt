package com.example.zhouzhihui.audioshift.record

import android.media.AudioRecord
import android.util.Log

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

import android.content.ContentValues.TAG

internal class AudioRecorderTask(private val audioRecord: AudioRecord, private val outputFile: File) : Runnable {

    private val outputStream: OutputStream?
        get() {
            val outputStream: OutputStream
            try {
                outputStream = FileOutputStream(outputFile)
            } catch (e: FileNotFoundException) {
                Log.e(TAG, "Error opening audio file for writing")
                return null
            }
            return outputStream
        }

    override fun run() {
        val outputStream = outputStream ?: return
        val buffer = ByteArray(BUFFER_SIZE)
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
        try {
            outputStream.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error closing audio file")
        }
    }

    companion object {
        private val BUFFER_SIZE = 1024
    }
}
