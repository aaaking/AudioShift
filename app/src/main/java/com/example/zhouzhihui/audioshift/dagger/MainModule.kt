package com.example.zhouzhihui.audioshift.dagger

import android.content.Context
import android.os.Environment

import com.example.zhouzhihui.audioshift.AudioApp
import com.example.zhouzhihui.audioshift.play.Player
import com.example.zhouzhihui.audioshift.record.Recorder
import com.example.zhouzhihui.audioshift.ui.AudioRecorderPlayer
import com.example.zhouzhihui.audioshift.ui.MediaToolsProvider

import java.io.File

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class MainModule(val application: AudioApp) {

    @Provides
    @Singleton
    fun providesContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun providesMediaToolsProvider(): MediaToolsProvider {
        return MediaToolsProvider()
    }

    @Provides
    fun providesDurationInMillis(): Int {
        return DURATION_IN_MILLIS
    }

    @Provides
    fun providesAudioFile(context: Context): File {
        var rootPath = ""
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {//sd卡能用
            rootPath = context.getExternalFilesDir(null).path
        } else {//sd卡不能用
            if (context.filesDir != null) {
                rootPath = context.filesDir.path
            }
        }
        return File(rootPath, AUDIO_FILENAME)
    }

    @Provides
    @Singleton
    fun providesAudioRecorderPlayer(mediaToolsProvider: MediaToolsProvider, audioFile: File): AudioRecorderPlayer {
        return AudioRecorderPlayer(mediaToolsProvider, audioFile)

    }

    @Provides
    fun providesRecorder(audioRecorderPlayer: AudioRecorderPlayer): Recorder {
        return audioRecorderPlayer
    }

    @Provides
    fun providesPlayer(audioRecorderPlayer: AudioRecorderPlayer): Player {
        return audioRecorderPlayer
    }

    companion object {
        val DURATION_IN_MILLIS = 5 * 1000
        val AUDIO_FILENAME = "audio_shift"
    }
}
