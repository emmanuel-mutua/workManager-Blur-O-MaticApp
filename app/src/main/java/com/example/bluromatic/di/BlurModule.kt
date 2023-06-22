package com.example.bluromatic.di

import android.content.Context
import com.example.bluromatic.data.BluromaticRepository
import com.example.bluromatic.data.WorkManagerBluromaticRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BlurModule {
    @Provides
    fun provideBluromaticRepository(@ApplicationContext context: Context):BluromaticRepository {
        return WorkManagerBluromaticRepository(context)
    }
}