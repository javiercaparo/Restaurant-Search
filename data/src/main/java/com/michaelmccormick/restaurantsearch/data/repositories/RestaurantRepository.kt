package com.michaelmccormick.restaurantsearch.data.repositories

import com.michaelmccormick.restaurantsearch.core.models.Restaurant

interface RestaurantRepository {
    /**
     * Returns a list of [Restaurant]'s that deliver to the passed [postcode].
     */
    suspend fun getRestaurantsByPostcode(postcode: String): Result<List<Restaurant>>
}
