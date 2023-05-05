package com.dzulfaqar.quranku.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class AyatResponse(
	@field:SerializedName("verses")
	val verses: List<AyatItemResponse>? = null
)

data class AyatItemResponse(
	@field:SerializedName("verse_key")
	val verseKey: String,
	@field:SerializedName("text_uthmani")
	val textUthmani: String,
	@field:SerializedName("id")
	val id: Int
)
