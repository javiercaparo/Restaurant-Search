package com.michaelmccormick.restaurantsearch

import com.michaelmccormick.restaurantsearch.core.models.Restaurant
import com.michaelmccormick.restaurantsearch.data.repositories.RestaurantRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FakeRestaurantRepositoryImpl @Inject constructor() : RestaurantRepository {
    var getRestaurantsByPostcodeResult: Result<List<Restaurant>> = Result.success(emptyList())

    override suspend fun getRestaurantsByPostcode(postcode: String): Result<List<Restaurant>> {
        return getRestaurantsByPostcodeResult
    }
}
