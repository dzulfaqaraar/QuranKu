package com.dzulfaqar.quranku.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class JuzResponse(
	@field:SerializedName("juzs")
	val juzs: List<JuzItemResponse>? = null
)

data class JuzItemResponse(
	@field:SerializedName("id")
	val id: Int? = null,
	@field:SerializedName("juz_number")
	val juzNumber: Int? = null,
	@field:SerializedName("verse_mapping")
	val verseMapping: HashMap<String, String>? = null,
	@field:SerializedName("first_verse_id")
	val firstVerseId: Int? = null,
	@field:SerializedName("last_verse_id")
	val lastVerseId: Int? = null,
	@field:SerializedName("verses_count")
	val versesCount: Int? = null
)
