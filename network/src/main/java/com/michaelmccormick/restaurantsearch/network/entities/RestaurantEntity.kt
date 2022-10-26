package com.michaelmccormick.restaurantsearch.network.entities

import androidx.annotation.RestrictTo
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RestaurantEntity @RestrictTo(RestrictTo.Scope.TESTS) constructor(
    val Id: Int?,
    val Name: String?,
    val LogoUrl: String?,
    val Rating: RatingEntity?,
    val CuisineTypes: List<CuisineTypeEntity>?,
)
