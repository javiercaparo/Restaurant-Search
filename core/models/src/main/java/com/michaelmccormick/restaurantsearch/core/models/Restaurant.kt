package com.michaelmccormick.restaurantsearch.core.models

data class Restaurant(
    val id: Int,
    val name: String,
    val logoUrl: String,
    val rating: Double,
    val cuisineTypes: String,
)
