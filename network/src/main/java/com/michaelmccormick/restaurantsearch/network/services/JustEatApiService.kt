package com.michaelmccormick.restaurantsearch.network.services

import com.michaelmccormick.restaurantsearch.network.entities.RestaurantSearchResponseEntity
import retrofit2.http.GET
import retrofit2.http.Path

interface JustEatApiService {
    @GET("restaurants/bypostcode/{postcode}")
    suspend fun getRestaurantsByPostCode(
        @Path("postcode") postcode: String,
    ): RestaurantSearchResponseEntity
}
