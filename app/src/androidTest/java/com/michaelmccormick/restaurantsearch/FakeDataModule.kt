package com.michaelmccormick.restaurantsearch

import com.michaelmccormick.restaurantsearch.data.di.DataModule
import com.michaelmccormick.restaurantsearch.data.repositories.RestaurantRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [DataModule::class],
)
internal abstract class FakeDataModule {
    @Binds
    abstract fun bindRestaurantRepository(
        fakeRestaurantRepositoryImpl: FakeRestaurantRepositoryImpl,
    ): RestaurantRepository
}
