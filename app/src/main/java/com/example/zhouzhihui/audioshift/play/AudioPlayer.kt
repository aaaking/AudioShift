package com.example.zhouzhihui.audioshift.play

import android.media.AudioTrack
import android.util.Log
import com.example.zhouzhihui.audioshift.TAG
import java.io.File


public class AudioPlayer(val audioTrack: AudioTrack, val file: File) : Player {
    override fun getPlaybackrate(): Int = audioTrack.playbackRate

    var mSpeed = 1f

    var playerThread: Thread? = null

    val positionListener = object : AudioTrack.OnPlaybackPositionUpdateListener {
        override fun onMarkerReached(track: AudioTrack) {
            track.flush()
            track.release()
            Log.d(TAG, "Playback Complete")
        }

        override fun onPeriodicNotification(track: AudioTrack) {
        }
    }

    override fun isPlaying(): Boolean {
        return audioTrack.playState == AudioTrack.PLAYSTATE_PLAYING
    }

    override fun startPlaying(fileP: File) {
        if (isPlaying()) {
            audioTrack.stop()
        }
        val playbackParams = audioTrack.playbackParams
        playbackParams.pitch = mSpeed
        audioTrack.playbackParams = playbackParams
        Log.e(TAG, "startPlaying  ${audioTrack.playbackRate}  ${audioTrack.sampleRate}")
        audioTrack.setPlaybackPositionUpdateListener(positionListener)
        audioTrack.play()
        val playerTask = AudioPlayerTask(audioTrack, fileP)
        playerThread = Thread(playerTask)
        playerThread!!.start()
    }

    override fun setSpeed(speed: Float) {
        this.mSpeed = speed
    }

    override fun stopPlaying() {
        audioTrack.flush()
        audioTrack.stop()
        audioTrack.release()
        playerThread = null
    }
}
