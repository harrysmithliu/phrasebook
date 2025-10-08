package com.harry.phrasebook.di

import android.content.Context
import androidx.room.Room
import com.harry.phrasebook.data.db.PhrasebookDatabase
import com.harry.phrasebook.data.db.PhraseRepositoryImpl
import com.harry.phrasebook.domain.repository.PhraseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): PhrasebookDatabase =
        Room.databaseBuilder(ctx, PhrasebookDatabase::class.java, "phrasebook.db")
            .fallbackToDestructiveMigration() // 开发期方便；发布前可去掉
            .build()

    @Provides @Singleton
    fun provideRepo(db: PhrasebookDatabase): PhraseRepository =
        PhraseRepositoryImpl(db)
}
