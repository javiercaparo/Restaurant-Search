package com.michaelmccormick.restaurantsearch.features.search.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.michaelmccormick.restaurantsearch.core.models.Restaurant
import org.junit.Rule
import org.junit.Test

internal class RestaurantCardComposableTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shouldDisplayAllRequiredRestaurantData() {
        // Given
        val restaurant = Restaurant(
            id = 123,
            name = "Chicken Shop",
            logoUrl = "",
            rating = 3.2,
            cuisineTypes = "Chicken \u2022 Turkey \u2022 Burgers",
        )

        // When
        composeTestRule.setContent {
            RestaurantCardComposable(restaurant = restaurant)
        }

        // Then
        composeTestRule.onNodeWithText("Chicken Shop")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Chicken \u2022 Turkey \u2022 Burgers")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("3.2")
            .assertIsDisplayed()
    }

    @Test
    fun shouldDisplayVeryLongRestaurantData() {
        // Given
        val name = "A very very very very very very very very very very very very very long restaurant name"
        val cuisineTypes = "Food \u2022 Food \u2022 Food \u2022 Food \u2022 Food \u2022 Food \u2022 Food \u2022 Food \u2022 Food \u2022 Food \u2022 Food"
        val restaurant = Restaurant(
            id = 123,
            name = name,
            logoUrl = "",
            rating = 3.2,
            cuisineTypes = cuisineTypes,
        )

        // When
        composeTestRule.setContent {
            RestaurantCardComposable(restaurant = restaurant)
        }

        // Then
        composeTestRule.onNodeWithText(name)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(cuisineTypes)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("3.2")
            .assertIsDisplayed()
    }
}
