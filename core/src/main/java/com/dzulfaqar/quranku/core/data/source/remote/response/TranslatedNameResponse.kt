package com.dzulfaqar.quranku.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class TranslatedNameResponse(
    @field:SerializedName("name")
    val name: String? = null,
    @field:SerializedName("language_name")
    val languageName: String? = null
)
