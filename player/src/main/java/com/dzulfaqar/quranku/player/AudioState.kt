package com.dzulfaqar.quranku.player

sealed class AudioState {
    object PREPARING: AudioState()
    object PAUSE: AudioState()
    object PLAYING: AudioState()
    object NORMAL: AudioState()
    object ERROR: AudioState()
}
