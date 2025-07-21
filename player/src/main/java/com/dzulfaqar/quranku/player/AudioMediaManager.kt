package com.dzulfaqar.quranku.player

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class AudioMediaManager() :
    MediaPlayer.OnPreparedListener, 
    MediaPlayer.OnCompletionListener, 
    MediaPlayer.OnBufferingUpdateListener,
    MediaPlayer.OnSeekCompleteListener, 
    MediaPlayer.OnErrorListener {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var _mediaPlayer: MediaPlayer? = null
    val mediaPlayer: MediaPlayer? get() = _mediaPlayer

    private val _audioState = MutableStateFlow<AudioState>(AudioState.Idle)
    val audioState: StateFlow<AudioState> = _audioState.asStateFlow()
    
    var listener: AudioMediaPlayerListener? = null

    init {
        initializeMediaPlayer()
    }
    
    private fun initializeMediaPlayer() {
        _mediaPlayer = MediaPlayer().apply {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
            setAudioAttributes(audioAttributes)
            setOnPreparedListener(this@AudioMediaManager)
            setOnCompletionListener(this@AudioMediaManager)
            setOnBufferingUpdateListener(this@AudioMediaManager)
            setOnSeekCompleteListener(this@AudioMediaManager)
            setOnErrorListener(this@AudioMediaManager)
        }
    }

    fun prepareToPlay(url: String?) {
        if (url.isNullOrBlank()) return
        
        scope.launch {
            try {
                _audioState.value = AudioState.Preparing
                
                _mediaPlayer?.apply {
                    reset()
                    setDataSource(url)
                    prepareAsync()
                }
            } catch (e: IOException) {
                Log.e("AudioMediaManager", "Error preparing audio: ${e.message}", e)
                _audioState.value = AudioState.Error
                listener?.onError(-1, -1)
            } catch (e: IllegalStateException) {
                Log.e("AudioMediaManager", "IllegalState: ${e.message}", e)
                _audioState.value = AudioState.Error
                listener?.onError(-1, -1)
            }
        }
    }

    override fun onPrepared(mp: MediaPlayer) {
        _audioState.value = AudioState.Playing
        listener?.onPrepared()
    }

    override fun onCompletion(mp: MediaPlayer) {
        _audioState.value = AudioState.Idle
        listener?.onCompletion()
    }

    override fun onBufferingUpdate(mp: MediaPlayer, percent: Int) {
        listener?.onBufferingUpdate(percent)
    }

    override fun onSeekComplete(mp: MediaPlayer) {
        listener?.onSeekComplete()
    }

    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        Log.e("AudioMediaManager", "MediaPlayer error: what=$what, extra=$extra")
        _audioState.value = AudioState.Error
        listener?.onError(what, extra)
        return true
    }
    
    fun pause() {
        _mediaPlayer?.takeIf { it.isPlaying }?.let {
            it.pause()
            _audioState.value = AudioState.Paused
        }
    }
    
    fun resume() {
        _mediaPlayer?.takeIf { !it.isPlaying }?.let {
            it.start()
            _audioState.value = AudioState.Playing
        }
    }
    
    fun stop() {
        _mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            _audioState.value = AudioState.Idle
        }
    }
    
    fun release() {
        _mediaPlayer?.release()
        _mediaPlayer = null
        _audioState.value = AudioState.Idle
        listener = null
    }

    interface AudioMediaPlayerListener {
        fun onPrepared()
        fun onCompletion()
        fun onBufferingUpdate(percent: Int)
        fun onSeekComplete()
        fun onError(what: Int, extra: Int)
    }
}
