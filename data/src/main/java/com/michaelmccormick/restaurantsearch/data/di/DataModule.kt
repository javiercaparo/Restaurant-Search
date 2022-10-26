package com.michaelmccormick.restaurantsearch.data.di

import com.michaelmccormick.restaurantsearch.data.repositories.RestaurantRepository
import com.michaelmccormick.restaurantsearch.data.repositories.RestaurantRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindRestaurantRepository(
        restaurantRepositoryImpl: RestaurantRepositoryImpl,
    ): RestaurantRepository
}
