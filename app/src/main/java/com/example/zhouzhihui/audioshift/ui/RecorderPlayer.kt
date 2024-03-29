package com.example.zhouzhihui.audioshift.ui

import com.example.zhouzhihui.audioshift.play.AudioPlayer
import com.example.zhouzhihui.audioshift.play.Player
import com.example.zhouzhihui.audioshift.record.AudioRecorder
import com.example.zhouzhihui.audioshift.record.Recorder
import com.example.zhouzhihui.audioshift.record.fileHasContent
import java.io.File

class RecorderPlayer(val mediaManager: MediaManager, val file: File) : Recorder, Player {
    override fun getPlaybackrate(): Int = audioPlayer?.getPlaybackrate() ?: 48000

    var audioRecorder: AudioRecorder? = null
    var audioPlayer: AudioPlayer? = null

    var mSpeed = 1f

    override fun getRecordFile(): File? = audioRecorder?.getRecordFile() ?: file

    override fun isRecording(): Boolean = audioRecorder?.isRecording() ?: false

    override fun hasRecording(): Boolean =
        audioRecorder?.hasRecording() ?: fileHasContent(file.parentFile)

    override fun isPausing(): Boolean = audioRecorder?.isPausing() ?: false

    override fun pause() {
        audioRecorder?.pause()
    }

    override fun resume() {
        audioRecorder?.resume()
    }

    override fun startRecording() {
        val audioRecord = mediaManager.audioRecord
        audioRecorder = AudioRecorder(audioRecord, file)
        audioRecorder!!.startRecording()
    }

    override fun stopRecording() {
        audioRecorder?.stopRecording()
        audioRecorder = null
    }

    override fun isPlaying(): Boolean = audioPlayer?.isPlaying() ?: false

    override fun startPlaying(fileP: File) {
        val fileSize = fileP.length()
        if (fileSize <= 0 || fileP.isDirectory) {
            return
        }
        if (audioPlayer != null && audioPlayer!!.isPlaying()) {
            audioPlayer!!.stopPlaying()
        }
        val audioTrack = mediaManager.getAudioTrack(fileSize)
        audioPlayer = AudioPlayer(audioTrack, fileP)
        audioPlayer!!.setSpeed(mSpeed)
        audioPlayer!!.startPlaying(fileP)
    }

    override fun stopPlaying() {
        if (audioPlayer != null && audioPlayer!!.isPlaying()) {
            audioPlayer!!.stopPlaying()
        }
        audioPlayer = null
    }

    override fun setSpeed(speed: Float) {
        this.mSpeed = speed
    }
}
