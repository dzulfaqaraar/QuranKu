package com.dzulfaqar.quranku.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ReciterResponse(
	@field:SerializedName("recitations")
	val recitations: List<RecitationsItemResponse>? = null
)

data class RecitationsItemResponse(
	@field:SerializedName("reciter_name")
	val reciterName: String? = null,
	@field:SerializedName("translated_name")
	val translatedName: TranslatedNameResponse? = null,
	@field:SerializedName("style")
	val style: String? = null,
	@field:SerializedName("id")
	val id: Int? = null
)
