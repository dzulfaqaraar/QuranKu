package com.dzulfaqar.quranku.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class JuzModel(
    val number: Int?,
    val firstAyat: Int?,
    val lastAyat: Int?,
    val totalSurat: Int?,
    val totalAyat: Int?,
) : Parcelable
