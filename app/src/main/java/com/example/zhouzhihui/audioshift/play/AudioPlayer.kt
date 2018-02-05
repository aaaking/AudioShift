package com.example.zhouzhihui.audioshift.play

import android.media.AudioTrack
import android.util.Log
import com.example.zhouzhihui.audioshift.TAG
import java.io.File


class AudioPlayer(private val audioTrack: AudioTrack, private val file: File) : Player {

    private var speed = 1f

    private var playerThread: Thread? = null

    private val positionListener = object : AudioTrack.OnPlaybackPositionUpdateListener {
        override fun onMarkerReached(track: AudioTrack) {
            track.flush()
            track.release()
            Log.d(TAG, "Playback Complete")
        }

        override fun onPeriodicNotification(track: AudioTrack) {
            //NO-OP
        }
    }

    override fun isPlaying(): Boolean {
        return audioTrack.playState == AudioTrack.PLAYSTATE_PLAYING
    }

    override fun startPlaying() {
        if (isPlaying()) {
            audioTrack.stop()
        }
        val playbackParams = audioTrack.playbackParams
        playbackParams.pitch = speed
        audioTrack.playbackParams = playbackParams
        audioTrack.setPlaybackPositionUpdateListener(positionListener)
        audioTrack.play()
        val playerTask = AudioPlayerTask(audioTrack, file)
        playerThread = Thread(playerTask)
        playerThread!!.start()
    }

    override fun setSpeed(speed: Float) {
        this.speed = speed
    }

    override fun stopPlaying() {
        audioTrack.flush()
        audioTrack.stop()
        audioTrack.release()
        playerThread = null
    }
}
