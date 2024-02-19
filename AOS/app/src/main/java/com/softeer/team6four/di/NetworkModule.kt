package com.softeer.team6four.di

import com.softeer.team6four.api.GeoCodeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    fun provideGeoCodeService() : GeoCodeService {
        return GeoCodeService.create()
    }
}