package com.michaelmccormick.restaurantsearch.data.mappers

import com.michaelmccormick.restaurantsearch.core.models.Restaurant
import com.michaelmccormick.restaurantsearch.network.entities.CuisineTypeEntity
import com.michaelmccormick.restaurantsearch.network.entities.RestaurantEntity
import javax.inject.Inject

internal class RestaurantEntityMapper @Inject constructor() {
    fun toRestaurant(entity: RestaurantEntity): Restaurant? {
        return Restaurant(
            id = entity.Id ?: return null,
            name = entity.Name ?: return null,
            logoUrl = entity.LogoUrl?.logoUrlToHttps() ?: return null,
            rating = entity.Rating?.StarRating ?: return null,
            cuisineTypes = entity.CuisineTypes?.buildCuisineTypesString() ?: return null,
        )
    }

    private fun String.logoUrlToHttps(): String {
        if (this.startsWith("http") && !this.startsWith("https")) {
            val sb = StringBuilder(this)
            sb.insert(4, 's')
            return sb.toString()
        }
        return this
    }

    private fun List<CuisineTypeEntity>.buildCuisineTypesString(): String {
        val stringBuilder = StringBuilder()
        this.forEachIndexed { index, cuisineTypeEntity ->
            cuisineTypeEntity.Name?.let {
                stringBuilder.append(it)
                if (index < this.size - 1) {
                    stringBuilder.append(" \u2022 ")
                }
            }
        }
        return stringBuilder.toString()
    }
}
