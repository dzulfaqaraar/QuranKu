package com.dzulfaqar.quranku.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class SurahResponse(
	@field:SerializedName("chapters")
	val chapters: List<SurahItemReponse>? = null
)

data class SurahItemReponse(
	@field:SerializedName("id")
	val id: Int? = null,
	@field:SerializedName("revelation_place")
	val revelationPlace: String? = null,
	@field:SerializedName("revelation_order")
	val revelationOrder: Int? = null,
	@field:SerializedName("name_simple")
	val nameSimple: String? = null,
	@field:SerializedName("name_arabic")
	val nameArabic: String? = null,
	@field:SerializedName("verses_count")
	val versesCount: Int? = null,
	@field:SerializedName("pages")
	val pages: List<Int?>? = null,
	@field:SerializedName("translated_name")
	val translatedName: TranslatedNameResponse? = null
)
