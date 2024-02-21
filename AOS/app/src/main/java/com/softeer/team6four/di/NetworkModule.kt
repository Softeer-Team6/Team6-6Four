package com.softeer.team6four.di

import com.softeer.team6four.api.ChargerService
import com.softeer.team6four.api.FcmService
import com.softeer.team6four.api.GeoCodeService
import com.softeer.team6four.api.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    fun provideGeoCodeService(): GeoCodeService {
        return GeoCodeService.create()
    }

    @Provides
    fun provideUserService(): UserService {
        return UserService.create()
    }

    @Provides
    fun provideFcmService(): FcmService {
        return FcmService.create()
    }

    @Provides
    fun provideChargerService(): ChargerService {
        return ChargerService.create()
    }
}