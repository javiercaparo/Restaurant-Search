package com.michaelmccormick.restaurantsearch.data.mappers

import com.michaelmccormick.restaurantsearch.core.models.Restaurant
import com.michaelmccormick.restaurantsearch.network.entities.CuisineTypeEntity
import com.michaelmccormick.restaurantsearch.network.entities.RatingEntity
import com.michaelmccormick.restaurantsearch.network.entities.RestaurantEntity
import java.util.stream.Stream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource

internal class RestaurantEntityMapperTest {
    private lateinit var restaurantEntityMapper: RestaurantEntityMapper

    @BeforeEach
    fun before() {
        restaurantEntityMapper = RestaurantEntityMapper()
    }

    @ParameterizedTest
    @ArgumentsSource(TestArgumentsProvider::class)
    fun `Should map entity to expected model`(entity: RestaurantEntity, expectedModel: Restaurant?) {
        assertEquals(expectedModel, restaurantEntityMapper.toRestaurant(entity))
    }

    private class TestArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                // Valid entity -> valid model
                Arguments.of(
                    RestaurantEntity(
                        Id = 123,
                        Name = "Chicken Shop",
                        LogoUrl = "https://images.com/img1.jpg",
                        Rating = RatingEntity(StarRating = 4.9),
                        CuisineTypes = listOf(CuisineTypeEntity(Id = "1", Name = "Chicken")),
                    ),
                    Restaurant(
                        id = 123,
                        name = "Chicken Shop",
                        logoUrl = "https://images.com/img1.jpg",
                        rating = 4.9,
                        cuisineTypes = "Chicken",
                    ),
                ),
                // Valid entity with multiple cuisine types -> valid model
                Arguments.of(
                    RestaurantEntity(
                        Id = 123,
                        Name = "Chicken Shop",
                        LogoUrl = "https://images.com/img1.jpg",
                        Rating = RatingEntity(StarRating = 4.9),
                        CuisineTypes = listOf(
                            CuisineTypeEntity(Id = "1", Name = "Chicken"),
                            CuisineTypeEntity(Id = "2", Name = "Turkey"),
                            CuisineTypeEntity(Id = "3", Name = "Burgers"),
                        ),
                    ),
                    Restaurant(
                        id = 123,
                        name = "Chicken Shop",
                        logoUrl = "https://images.com/img1.jpg",
                        rating = 4.9,
                        cuisineTypes = "Chicken \u2022 Turkey \u2022 Burgers",
                    ),
                ),
                // http LogoUrl -> https logoUrl
                Arguments.of(
                    RestaurantEntity(
                        Id = 123,
                        Name = "Chicken Shop",
                        LogoUrl = "http://images.com/img1.jpg",
                        Rating = RatingEntity(StarRating = 4.9),
                        CuisineTypes = listOf(CuisineTypeEntity(Id = "1", Name = "Chicken"), CuisineTypeEntity(Id = "2", Name = "Turkey")),
                    ),
                    Restaurant(
                        id = 123,
                        name = "Chicken Shop",
                        logoUrl = "https://images.com/img1.jpg",
                        rating = 4.9,
                        cuisineTypes = "Chicken \u2022 Turkey",
                    ),
                ),
                // null Id -> null model
                Arguments.of(
                    RestaurantEntity(
                        Id = null,
                        Name = "Chicken Shop",
                        LogoUrl = "https://images.com/img1.jpg",
                        Rating = RatingEntity(StarRating = 4.9),
                        CuisineTypes = listOf(CuisineTypeEntity(Id = "1", Name = "Chicken"), CuisineTypeEntity(Id = "2", Name = "Turkey")),
                    ),
                    null,
                ),
                // null Name -> null model
                Arguments.of(
                    RestaurantEntity(
                        Id = 123,
                        Name = null,
                        LogoUrl = "https://images.com/img1.jpg",
                        Rating = RatingEntity(StarRating = 4.9),
                        CuisineTypes = listOf(CuisineTypeEntity(Id = "1", Name = "Chicken"), CuisineTypeEntity(Id = "2", Name = "Turkey")),
                    ),
                    null,
                ),
                // null LogoUrl -> null model
                Arguments.of(
                    RestaurantEntity(
                        Id = 123,
                        Name = "Chicken Shop",
                        LogoUrl = null,
                        Rating = RatingEntity(StarRating = 4.9),
                        CuisineTypes = listOf(CuisineTypeEntity(Id = "1", Name = "Chicken"), CuisineTypeEntity(Id = "2", Name = "Turkey")),
                    ),
                    null,
                ),
                // null Rating -> null model
                Arguments.of(
                    RestaurantEntity(
                        Id = 123,
                        Name = "Chicken Shop",
                        LogoUrl = "https://images.com/img1.jpg",
                        Rating = null,
                        CuisineTypes = listOf(CuisineTypeEntity(Id = "1", Name = "Chicken"), CuisineTypeEntity(Id = "2", Name = "Turkey")),
                    ),
                    null,
                ),
                // null StarRating -> null model
                Arguments.of(
                    RestaurantEntity(
                        Id = 123,
                        Name = "Chicken Shop",
                        LogoUrl = "https://images.com/img1.jpg",
                        Rating = RatingEntity(StarRating = null),
                        CuisineTypes = listOf(CuisineTypeEntity(Id = "1", Name = "Chicken"), CuisineTypeEntity(Id = "2", Name = "Turkey")),
                    ),
                    null,
                ),
                // null CuisineTypes -> null model
                Arguments.of(
                    RestaurantEntity(
                        Id = 123,
                        Name = "Chicken Shop",
                        LogoUrl = "https://images.com/img1.jpg",
                        Rating = RatingEntity(StarRating = 4.9),
                        CuisineTypes = null,
                    ),
                    null,
                ),
            )
        }
    }
}
