package com.dzulfaqar.quranku.core.utils

import com.dzulfaqar.quranku.core.BuildConfig

object Constant {
    init {
        System.loadLibrary("native-lib")
    }

    external fun passphrase(): String

    const val HOSTNAME = BuildConfig.BASE_URL
    const val URL_API = "https://${HOSTNAME}/"
    const val URL_AUDIO = BuildConfig.AUDIO_URL
    const val TOTAL_SURAH = 114
    const val TOTAL_JUZ = 30
    const val TOTAL_AYAT = 6236
}
