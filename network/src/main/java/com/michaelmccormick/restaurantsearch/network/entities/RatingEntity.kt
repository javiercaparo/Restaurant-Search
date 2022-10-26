package com.michaelmccormick.restaurantsearch.network.entities

import androidx.annotation.RestrictTo
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RatingEntity @RestrictTo(RestrictTo.Scope.TESTS) constructor(
    val StarRating: Double?,
)
