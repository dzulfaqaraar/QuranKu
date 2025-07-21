package com.dzulfaqar.quranku.player

sealed class AudioState {
    data object Preparing : AudioState()
    data object Paused : AudioState()
    data object Playing : AudioState()
    data object Idle : AudioState()
    data object Error : AudioState()
}
