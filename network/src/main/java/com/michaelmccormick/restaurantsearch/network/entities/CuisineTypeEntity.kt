package com.michaelmccormick.restaurantsearch.network.entities

import androidx.annotation.RestrictTo
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CuisineTypeEntity @RestrictTo(RestrictTo.Scope.TESTS) constructor(
    val Id: String?,
    val Name: String?,
)
