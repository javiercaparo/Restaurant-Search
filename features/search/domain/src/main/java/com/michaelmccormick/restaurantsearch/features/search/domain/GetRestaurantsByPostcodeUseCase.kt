package com.michaelmccormick.restaurantsearch.features.search.domain

import com.michaelmccormick.restaurantsearch.core.models.Restaurant
import com.michaelmccormick.restaurantsearch.data.repositories.RestaurantRepository
import javax.inject.Inject

class GetRestaurantsByPostcodeUseCase @Inject internal constructor(
    private val restaurantRepository: RestaurantRepository,
) {
    /**
     * Returns a list of [Restaurant]'s that deliver to the passed [postcode].
     */
    suspend operator fun invoke(postcode: String): Result<List<Restaurant>> {
        return restaurantRepository.getRestaurantsByPostcode(postcode = postcode)
    }
}
