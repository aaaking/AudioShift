package com.example.zhouzhihui.audioshift.dagger

import android.content.Context
import android.os.Environment
import android.util.Log

import com.example.zhouzhihui.audioshift.AudioApp
import com.example.zhouzhihui.audioshift.TAG
import com.example.zhouzhihui.audioshift.play.Player
import com.example.zhouzhihui.audioshift.record.Recorder
import com.example.zhouzhihui.audioshift.ui.RecorderPlayer
import com.example.zhouzhihui.audioshift.ui.MediaManager

import java.io.File

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class MainModule(val application: AudioApp) {

    @Provides
    @Singleton
    fun providesContext(): Context = application

    @Provides
    @Singleton
    fun providesMediaManager(): MediaManager = MediaManager()

    @Provides
    fun providesDurationInMillis(): Long = DURATION_IN_MILLIS

    @Provides
    fun providesAudioFile(context: Context): File {
        val file = File(providesRootPath(context), AUDIO_DIRECTORY + File.separator + AUDIO_FILENAME)
        var result = file.parentFile.mkdirs()
        Log.i(TAG, "file.mkdirs() result: ${result} ${file.exists()} ${file.name} ${file.path}")
        return file
    }

    @Provides
    fun providesRootPath(context: Context): String =
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {//sd卡能用
                context.getExternalFilesDir(null)!!.path
            } else {//sd卡不能用
                context.filesDir.path
            }

    @Provides
    @Singleton
    fun providesRecorderPlayer(mediaManager: MediaManager, audioFile: File): RecorderPlayer = RecorderPlayer(mediaManager, audioFile)

    @Provides
    fun providesRecorder(recorderPlayer: RecorderPlayer): Recorder = recorderPlayer

    @Provides
    fun providesPlayer(recorderPlayer: RecorderPlayer): Player = recorderPlayer

    companion object {
        val DURATION_IN_MILLIS = 5 * 1000L
        val AUDIO_DIRECTORY = "audio_files"
        val AUDIO_FILENAME = "tempFile"
    }
}
