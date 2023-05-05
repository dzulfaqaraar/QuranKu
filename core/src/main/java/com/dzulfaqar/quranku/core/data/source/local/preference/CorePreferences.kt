package com.dzulfaqar.quranku.core.data.source.local.preference

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dzulfaqar.quranku.core.di.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CorePreferences constructor(
    appContext: Context
) {
    private val appDataStore = appContext.dataStore

    fun getThemeSetting(): Flow<Boolean> {
        return appDataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        appDataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    fun getLatestRecitation(): Flow<Recitation> {
        return appDataStore.data.map { preferences ->
            Recitation(
                preferences[RECITATION_ID_KEY] ?: 0,
                preferences[RECITATION_NAME_KEY] ?: "",
                preferences[RECITATION_STYLE_KEY] ?: ""
            )
        }
    }

    suspend fun setLatestRecitation(latest: Recitation) {
        appDataStore.edit { preferences ->
            preferences[RECITATION_ID_KEY] = latest.id
            preferences[RECITATION_NAME_KEY] = latest.name
            preferences[RECITATION_STYLE_KEY] = latest.style ?: ""
        }
    }

    companion object {
        private val THEME_KEY = booleanPreferencesKey("theme_setting")
        private val RECITATION_ID_KEY = intPreferencesKey("recitation_id")
        private val RECITATION_NAME_KEY = stringPreferencesKey("recitation_name")
        private val RECITATION_STYLE_KEY = stringPreferencesKey("recitation_style")
    }
}
