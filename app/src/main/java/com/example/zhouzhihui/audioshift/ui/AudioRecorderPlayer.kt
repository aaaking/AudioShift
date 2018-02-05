package com.example.zhouzhihui.audioshift.ui

import com.example.zhouzhihui.audioshift.play.AudioPlayer
import com.example.zhouzhihui.audioshift.play.Player
import com.example.zhouzhihui.audioshift.record.AudioRecorder
import com.example.zhouzhihui.audioshift.record.Recorder
import java.io.File

class AudioRecorderPlayer(val mediaToolsProvider: MediaToolsProvider, val file: File) : Recorder, Player {

    var audioRecorder: AudioRecorder? = null
    var audioPlayer: AudioPlayer? = null

    var mSpeed = 1f

    override fun isRecording(): Boolean {
        return audioRecorder != null && audioRecorder!!.isRecording()
    }

    override fun hasRecording(): Boolean {
        return file.exists()
    }

    override fun startRecording() {
        val audioRecord = mediaToolsProvider.audioRecord
        audioRecorder = AudioRecorder(audioRecord, file)
        audioRecorder!!.startRecording()
    }

    override fun stopRecording() {
        if (audioRecorder != null) {
            audioRecorder!!.stopRecording()
        }
        audioRecorder = null
    }

    override fun isPlaying(): Boolean {
        return audioPlayer != null && audioPlayer!!.isPlaying()
    }

    override fun startPlaying() {
        val fileSize = file.length()
        if (fileSize <= 0) {
            return
        }
        if (audioPlayer != null && audioPlayer!!.isPlaying()) {
            audioPlayer!!.stopPlaying()
        }
        val audioTrack = mediaToolsProvider.getAudioTrack(fileSize)
        audioPlayer = AudioPlayer(audioTrack, file)
        audioPlayer!!.setSpeed(mSpeed)
        audioPlayer!!.startPlaying()
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
