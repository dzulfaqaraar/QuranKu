package com.dzulfaqar.quranku.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReciterModel(
    val id: Int,
    val name: String,
    val style: String?
): Parcelable
