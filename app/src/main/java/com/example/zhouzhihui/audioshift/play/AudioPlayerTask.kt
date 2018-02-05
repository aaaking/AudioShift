package com.example.zhouzhihui.audioshift.play

import android.media.AudioTrack
import android.util.Log
import com.example.zhouzhihui.audioshift.TAG

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

class AudioPlayerTask(val audioTrack: AudioTrack, val inputFile: File) : Runnable {

    val inputStream: InputStream?
        get() {
            val inputStream: InputStream
            try {
                inputStream = FileInputStream(inputFile)
            } catch (e: FileNotFoundException) {
                Log.e(TAG, "Error opening audio file for reading")
                return null
            }

            return inputStream
        }

    override fun run() {
        val inputStream = inputStream ?: return
        val buffer = ByteArray(BUFFER_SIZE)
        var read = -1
        var total = 0
        val size = inputFile.length().toInt()
        while (total < size) {
            try {
                read = inputStream.read(buffer, 0, BUFFER_SIZE)
            } catch (e: IOException) {
                Log.e(TAG, "Error reading audio file")
            }

            audioTrack.write(buffer, 0, read, AudioTrack.WRITE_BLOCKING)
            total += read
            //Timber.d("Write");
        }
        try {
            inputStream.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error closing audio file")
        }

        val totalFrames = audioTrack.bufferSizeInFrames
        audioTrack.notificationMarkerPosition = totalFrames
        Log.e(TAG, "Complete")
    }

    companion object {
        val BUFFER_SIZE = 1024
    }
}
