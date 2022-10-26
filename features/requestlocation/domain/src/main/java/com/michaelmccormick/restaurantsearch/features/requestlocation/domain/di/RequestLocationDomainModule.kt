package com.michaelmccormick.restaurantsearch.features.requestlocation.domain.di

import android.content.Context
import android.location.Geocoder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object RequestLocationDomainModule {
    @Provides
    internal fun provideGeocoder(@ApplicationContext appContext: Context): Geocoder {
        return Geocoder(appContext)
    }
}
