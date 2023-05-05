package com.dzulfaqar.quranku.player

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaPlayer.*
import android.text.TextUtils
import timber.log.Timber
import java.io.IOException

class AudioMediaManager : OnPreparedListener, OnCompletionListener, OnBufferingUpdateListener,
    OnSeekCompleteListener, OnErrorListener {

    var mediaPlayer: MediaPlayer
    var listener: AudioMediaPlayerListener? = null

    init {
        mediaPlayer = MediaPlayer()
    }

    fun prepareToPlay(url: String?) {
        if (TextUtils.isEmpty(url)) return
        try {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

            mediaPlayer.release()
            mediaPlayer = MediaPlayer()
            mediaPlayer.setAudioAttributes(audioAttributes)
            mediaPlayer.setDataSource(url)
            mediaPlayer.setOnPreparedListener(this)
            mediaPlayer.setOnCompletionListener(this)
            mediaPlayer.setOnBufferingUpdateListener(this)
            mediaPlayer.setOnSeekCompleteListener(this)
            mediaPlayer.setOnErrorListener(this)
            mediaPlayer.prepareAsync()
        } catch (e: IOException) {
            Timber.e(e.toString())
        }
    }

    override fun onPrepared(mp: MediaPlayer) {
        listener?.onPrepared()
    }

    override fun onCompletion(mp: MediaPlayer) {
        listener?.onCompletion()
    }

    override fun onBufferingUpdate(mp: MediaPlayer, percent: Int) {
        listener?.onBufferingUpdate(percent)
    }

    override fun onSeekComplete(mp: MediaPlayer) {
        listener?.onSeekComplete()
    }

    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        listener?.onError(what, extra)
        return true
    }

    interface AudioMediaPlayerListener {
        fun onPrepared()
        fun onCompletion()
        fun onBufferingUpdate(percent: Int)
        fun onSeekComplete()
        fun onError(what: Int, extra: Int)
    }

    companion object {
        @Volatile
        private var INSTANCE: AudioMediaManager? = null

        fun instance(): AudioMediaManager {
            if (INSTANCE == null) {
                synchronized(AudioMediaManager::class.java) {
                    INSTANCE = AudioMediaManager()
                }
            }
            return INSTANCE as AudioMediaManager
        }
    }
}
