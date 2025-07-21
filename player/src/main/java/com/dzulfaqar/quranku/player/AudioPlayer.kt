package com.dzulfaqar.quranku.player

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AudioPlayer(
    private val audioMediaManager: AudioMediaManager
) : AudioMediaManager.AudioMediaPlayerListener {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    private var listener: AudioPlayerListener? = null
    private var audioState: AudioState = AudioState.Idle
    private var audioList: List<String> = emptyList()
    private var currentAudio: String? = null
    private var totalAudio: Int = 0
    private var playedCount: Int = 0
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    private val _currentProgress = MutableStateFlow(0)
    val currentProgress: StateFlow<Int> = _currentProgress.asStateFlow()

    init {
        audioMediaManager.listener = this
    }
    
    fun setAudioPlayerListener(listener: AudioPlayerListener) {
        this.listener = listener
    }

    fun play(audioUrls: List<String>) {
        if (audioUrls.isEmpty()) return
        
        totalAudio = audioUrls.size
        audioList = audioUrls
        currentAudio = audioUrls.first()
        playedCount = 0
        
        scope.launch {
            playCurrentAudio()
        }
    }
    
    fun play(audioUrls: ArrayList<String>) {
        play(audioUrls.toList())
    }

    fun pause() {
        audioState = AudioState.Paused
        audioMediaManager.pause()
        _isPlaying.value = false
    }

    fun resume() {
        when (audioState) {
            AudioState.Paused -> {
                audioMediaManager.resume()
                audioState = AudioState.Playing
                _isPlaying.value = true
            }
            else -> playCurrentAudio()
        }
    }

    private fun playCurrentAudio() {
        when (audioState) {
            AudioState.Idle, AudioState.Error -> {
                audioState = AudioState.Preparing
                audioMediaManager.prepareToPlay(currentAudio)
            }
            AudioState.Paused -> {
                audioState = AudioState.Playing
                audioMediaManager.resume()
                _isPlaying.value = true
            }
            else -> {
                // Already preparing or playing
            }
        }
    }

    override fun onPrepared() {
        if (audioState != AudioState.Preparing) return

        audioState = AudioState.Playing
        audioMediaManager.mediaPlayer?.start()
        _isPlaying.value = true
    }

    override fun onCompletion() {
        _isPlaying.value = false
        
        if (playedCount < totalAudio - 1) {
            playedCount++
            currentAudio = audioList[playedCount]
            audioState = AudioState.Idle
            playCurrentAudio()
        } else {
            audioState = AudioState.Idle
            playedCount = 0
            listener?.onFinish()
        }
    }

    override fun onBufferingUpdate(percent: Int) {
        _currentProgress.value = percent
    }

    override fun onSeekComplete() {
        // Handle seek completion if needed
    }

    override fun onError(what: Int, extra: Int) {
        if (what != -38) { // Ignore specific non-critical error
            audioState = AudioState.Error
            _isPlaying.value = false
            // Try to play next audio or finish
            onCompletion()
        }
    }

    fun reset() {
        try {
            audioMediaManager.stop()
            audioState = AudioState.Idle
            _isPlaying.value = false
            playedCount = 0
        } catch (e: IllegalStateException) {
            audioState = AudioState.Idle
            _isPlaying.value = false
        }
    }

    fun release() {
        listener = null
        audioMediaManager.release()
        audioState = AudioState.Idle
        _isPlaying.value = false
    }

    interface AudioPlayerListener {
        fun onFinish()
    }
    
    companion object {
        @Volatile
        private var instance: AudioPlayer? = null

        fun getInstance(listener: AudioPlayerListener): AudioPlayer? {
            // This method is kept for backward compatibility but should not be used
            // Use dependency injection instead
            return instance?.apply {
                setAudioPlayerListener(listener)
            }
        }
    }
}
