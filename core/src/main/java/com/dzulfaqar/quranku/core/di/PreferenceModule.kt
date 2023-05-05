package com.dzulfaqar.quranku.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dzulfaqar.quranku.core.BuildConfig
import com.dzulfaqar.quranku.core.data.source.local.preference.CorePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = BuildConfig.LIBRARY_PACKAGE_NAME)

@Module
@InstallIn(SingletonComponent::class)
class PreferenceModule {

    @Singleton
    @Provides
    fun provideCorePreferences(@ApplicationContext context: Context): CorePreferences =
        CorePreferences(context)
}
