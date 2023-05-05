package com.dzulfaqar.quranku.core.di

import android.content.Context
import androidx.room.Room
import com.dzulfaqar.quranku.core.data.source.local.room.*
import com.dzulfaqar.quranku.core.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): QuranDatabase {
        val passphrase: ByteArray = SQLiteDatabase.getBytes(Constant.passphrase().toCharArray())
        val factory = SupportFactory(passphrase)
        return Room.databaseBuilder(
            context,
            QuranDatabase::class.java, "Quran.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }

    @Provides
    fun provideSurahDao(database: QuranDatabase): SurahDao = database.surahDao()

    @Provides
    fun provideJuzDao(database: QuranDatabase): JuzDao = database.juzDao()

    @Provides
    fun provideAyatDao(database: QuranDatabase): AyatDao = database.ayatDao()
}
