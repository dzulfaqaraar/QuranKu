package com.dzulfaqar.quranku.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class AudioResponse(
    @field:SerializedName("audio_files")
    val audioFiles: List<AudioItemResponse>? = null
)

data class AudioItemResponse(
    @field:SerializedName("verse_key")
    val verseKey: String? = null,
    @field:SerializedName("url")
    val url: String? = null
)
