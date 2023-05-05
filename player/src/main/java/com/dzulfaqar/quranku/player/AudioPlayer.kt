package com.dzulfaqar.quranku.player

class AudioPlayer : AudioMediaManager.AudioMediaPlayerListener {

    private var audioMediaManager: AudioMediaManager? = null
    private var listener: AudioPlayerListener? = null
    private var audioState: AudioState? = null
    private var listAudio: List<String>? = null
    private var audio: String? = null
    private var totalAudio: Int = 0
    private var playedCount: Int = 0

    private fun setupPlayer(listener: AudioPlayerListener) {
        audioMediaManager = AudioMediaManager.instance()
        audioMediaManager?.listener = this
        audioState = AudioState.NORMAL
        this.listener = listener
    }

    fun play(listAudio: List<String>) {
        totalAudio = listAudio.size
        this.listAudio = listAudio
        audio = listAudio.first()
        playedCount = 0

        playAudio()
    }

    fun pause() {
        audioState = AudioState.PAUSE
        if (audioMediaManager?.mediaPlayer?.isPlaying == true) {
            audioMediaManager?.mediaPlayer?.pause()
        }
    }

    fun resume() {
        playAudio()
    }

    private fun playAudio() {
        if (audioState == AudioState.NORMAL || audioState == AudioState.ERROR) {
            audioState = AudioState.PREPARING

            audioMediaManager?.prepareToPlay(audio)
        } else if (audioState == AudioState.PAUSE) {
            audioState = AudioState.PLAYING
            audioMediaManager?.mediaPlayer?.start()
        }
    }

    override fun onPrepared() {
        if (audioState != AudioState.PREPARING) return

        audioState = AudioState.PLAYING
        audioMediaManager?.mediaPlayer?.start()
    }

    override fun onCompletion() {
        if (playedCount < totalAudio - 1) {
            playedCount++

            listAudio?.let { listAudio ->
                audio = listAudio[playedCount]

                audioState = AudioState.NORMAL
                playAudio()
            }
        } else {
            audioState = AudioState.NORMAL
            playedCount = 0
            listener?.onFinish()
        }
    }

    override fun onBufferingUpdate(percent: Int) {}

    override fun onSeekComplete() {}

    override fun onError(what: Int, extra: Int) {
        if (what != -38) {
            audioState = AudioState.ERROR
            audioMediaManager?.mediaPlayer?.release()
        }
    }

    fun reset() {
        try {
            if (audioMediaManager?.mediaPlayer?.isPlaying == true) {
                audioMediaManager?.mediaPlayer?.stop()
                audioState = AudioState.NORMAL
            }
        } catch (e: IllegalStateException) {
            audioState = AudioState.NORMAL
        }
    }

    fun release() {
        listener = null
        audioMediaManager?.listener = null

        audioState = AudioState.NORMAL
        audioMediaManager?.mediaPlayer?.release()
    }

    interface AudioPlayerListener {
        fun onFinish()
    }

    companion object {
        @Volatile
        private var instance: AudioPlayer? = null

        fun getInstance(listener: AudioPlayerListener): AudioPlayer {
            if (instance == null) {
                synchronized(AudioPlayer::class.java) {
                    instance = AudioPlayer()
                }
            }
            instance?.setupPlayer(listener)
            return instance as AudioPlayer
        }
    }
}
