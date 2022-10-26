package com.michaelmccormick.restaurantsearch

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.michaelmccormick.restaurantsearch.core.models.Restaurant
import com.michaelmccormick.restaurantsearch.features.requestlocation.ui.LocationRequestActivity
import com.michaelmccormick.restaurantsearch.features.search.ui.SearchScreenTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
internal class MainActivityTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var fakeRestaurantRepositoryImpl: FakeRestaurantRepositoryImpl

    @Before
    fun before() {
        hiltRule.inject()
    }

    @Test
    fun shouldShowRestaurantsWhenValidPostcodeEntered() = runTest {
        // Given
        val restaurants = listOf(
            Restaurant(id = 1, name = "Chicken Shop", logoUrl = "", rating = 3.2, cuisineTypes = "Chicken"),
            Restaurant(id = 2, name = "Supermarket", logoUrl = "", rating = 5.0, cuisineTypes = "Groceries \u2022 Alcohol"),
            Restaurant(id = 3, name = "Burger Joint", logoUrl = "", rating = 4.8, cuisineTypes = "Burgers"),
        )
        fakeRestaurantRepositoryImpl.getRestaurantsByPostcodeResult = Result.success(restaurants)

        // When
        composeTestRule.awaitIdle()
        composeTestRule.onNodeWithTag(SearchScreenTags.POSTCODE_TEXT_FIELD)
            .performTextInput("BS1")
        composeTestRule.onNodeWithTag(SearchScreenTags.SEARCH_BUTTON)
            .performClick()

        // Then
        with(composeTestRule.onNodeWithTag(SearchScreenTags.RESTAURANTS_LIST)) {
            assertIsDisplayed()
            with(onChildAt(0)) {
                onChildAt(0).assertTextContains("Chicken Shop")
                onChildAt(1).assertTextContains("Chicken")
                onChildAt(3).assertTextContains("3.2")
            }
            with(onChildAt(1)) {
                onChildAt(0).assertTextContains("Supermarket")
                onChildAt(1).assertTextContains("Groceries \u2022 Alcohol")
                onChildAt(3).assertTextContains("5.0")
            }
            with(onChildAt(2)) {
                onChildAt(0).assertTextContains("Burger Joint")
                onChildAt(1).assertTextContains("Burgers")
                onChildAt(3).assertTextContains("4.8")
            }
        }
    }

    @Test
    fun shouldShowEmptyRestaurantsListWhenUnsupportedPostcodeEntered() = runTest {
        // Given
        fakeRestaurantRepositoryImpl.getRestaurantsByPostcodeResult = Result.success(emptyList())

        // When
        composeTestRule.awaitIdle()
        composeTestRule.onNodeWithTag(SearchScreenTags.POSTCODE_TEXT_FIELD)
            .performTextInput("BS1")
        composeTestRule.onNodeWithTag(SearchScreenTags.SEARCH_BUTTON)
            .performClick()

        // Then
        composeTestRule.onNodeWithText("Unfortunately, there are no restaurants that deliver to your postcode")
            .assertIsDisplayed()
    }

    @Test
    fun shouldLaunchRequestLocationActivityWhenLocateClicked() {
        // Given
        Intents.init()

        // When
        composeTestRule.onNodeWithTag(SearchScreenTags.LOCATE_BUTTON)
            .performClick()

        // Then
        intended(hasComponent(LocationRequestActivity::class.java.name))
        Intents.release()
    }
}
