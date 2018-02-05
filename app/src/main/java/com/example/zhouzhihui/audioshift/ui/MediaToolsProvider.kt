package com.example.zhouzhihui.audioshift.ui

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import android.media.audiofx.PresetReverb

class MediaToolsProvider {
    var presetReverb: PresetReverb? = null

    val audioRecord: AudioRecord
        get() {
            val audioFormat = getAudioFormat(AudioFormat.CHANNEL_IN_MONO)
            val bufferSize = getInputBufferSize(audioFormat)
            return AudioRecord.Builder()
                    .setAudioSource(MediaRecorder.AudioSource.MIC)
                    .setAudioFormat(audioFormat)
                    .setBufferSizeInBytes(bufferSize)
                    .build()
        }

    val reverb: PresetReverb?
        get() {
            if (presetReverb == null) {
                presetReverb = createPresetReverb()
            }
            return presetReverb
        }

    fun getAudioFormat(channelMask: Int): AudioFormat {
        val sampleRate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC)
        return AudioFormat.Builder()
                .setSampleRate(sampleRate)
                .setChannelMask(channelMask)
                .setEncoding(AudioFormat.ENCODING_DEFAULT)
                .build()
    }

    fun getInputBufferSize(audioFormat: AudioFormat): Int {
        var bufferSize = AudioRecord.getMinBufferSize(
                audioFormat.sampleRate,
                audioFormat.channelCount,
                audioFormat.encoding
        )
        if (bufferSize <= 0) {
            bufferSize = CHANNEL_SIZE * audioFormat.channelCount
        }
        return bufferSize
    }

    fun getAudioTrack(bufferSize: Long): AudioTrack {
        val audioFormat = getAudioFormat(AudioFormat.CHANNEL_OUT_MONO)
        val attributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        val track = AudioTrack.Builder()
                .setAudioFormat(audioFormat)
                .setBufferSizeInBytes(bufferSize.toInt())
                .setAudioAttributes(attributes)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()
        val reverb = reverb
        track.attachAuxEffect(reverb?.id ?: 0)
        track.setAuxEffectSendLevel(1.0f)
        return track
    }

    fun createPresetReverb(): PresetReverb {
        val reverb = PresetReverb(1, 0)
        reverb.preset = PresetReverb.PRESET_PLATE
        reverb.enabled = true
        return reverb
    }

    companion object {
        val CHANNEL_SIZE = 1920
    }
}
