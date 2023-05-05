package com.dzulfaqar.quranku.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class SurahModel(
    val id: Int?,
    val name: String?,
    val arabic: String?,
    val totalAyat: Int?,
) : Parcelable
