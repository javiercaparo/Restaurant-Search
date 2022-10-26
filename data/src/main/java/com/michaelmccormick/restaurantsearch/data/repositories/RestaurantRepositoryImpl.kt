package com.michaelmccormick.restaurantsearch.data.repositories

import com.michaelmccormick.restaurantsearch.core.models.Restaurant
import com.michaelmccormick.restaurantsearch.data.mappers.RestaurantEntityMapper
import com.michaelmccormick.restaurantsearch.network.services.JustEatApiService
import javax.inject.Inject
import timber.log.Timber

internal class RestaurantRepositoryImpl @Inject constructor(
    private val justEatApiService: JustEatApiService,
    private val restaurantEntityMapper: RestaurantEntityMapper,
) : RestaurantRepository {
    override suspend fun getRestaurantsByPostcode(postcode: String): Result<List<Restaurant>> {
        return try {
            val restaurants = justEatApiService.getRestaurantsByPostCode(postcode)
                .Restaurants
                .mapNotNull { restaurantEntityMapper.toRestaurant(it) }
            Result.success(restaurants)
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure(e)
        }
    }
}
