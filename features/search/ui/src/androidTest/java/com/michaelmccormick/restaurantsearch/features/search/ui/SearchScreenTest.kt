@file:Suppress("SpellCheckingInspection")

package com.michaelmccormick.restaurantsearch.features.search.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.michaelmccormick.restaurantsearch.core.models.Restaurant
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

internal class SearchScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    private val mockSearchScreenViewModel: SearchScreenViewModel = mockk(relaxed = true)

    @Test
    fun shouldShowInitialStateWhenInitialTrue() {
        // Given
        mockViewState(state = SearchScreenViewModel.ViewState(isInitial = true))

        // When
        renderComposable()

        // Then
        composeTestRule.onNodeWithText(INITIAL_STATE_BODY_TEXT)
            .assertIsDisplayed()
    }

    @Test
    fun shouldShowLoadingSpinnerWhenLoadingTrue() {
        // Given
        mockViewState(state = SearchScreenViewModel.ViewState(isLoading = true))

        // When
        renderComposable()

        // Then
        composeTestRule.onNodeWithTag(SearchScreenTags.LOADING_SPINNER)
            .assertIsDisplayed()
    }

    @Test
    fun shouldShowPostcodeAndDisableSearchWhenPostcodeUnpopulated() {
        // Given
        mockViewState(state = SearchScreenViewModel.ViewState(postcode = ""))

        // When
        renderComposable()

        // Then
        composeTestRule.onNodeWithTag(SearchScreenTags.POSTCODE_TEXT_FIELD)
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertTextContains(POSTCODE_TEXT_FIELD_LABEL)
        composeTestRule.onNodeWithTag(SearchScreenTags.LOCATE_BUTTON)
            .assertIsDisplayed()
            .assertIsEnabled()
        composeTestRule.onNodeWithTag(SearchScreenTags.SEARCH_BUTTON)
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    fun shouldShowPostcodeAndEnableSearchWhenPostcodePopulated() {
        // Given
        val postcode = "BS1 3JA"
        mockViewState(state = SearchScreenViewModel.ViewState(postcode = postcode))

        // When
        renderComposable()

        // Then
        composeTestRule.onNodeWithTag(SearchScreenTags.POSTCODE_TEXT_FIELD)
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertTextContains(postcode)
        composeTestRule.onNodeWithTag(SearchScreenTags.LOCATE_BUTTON)
            .assertIsDisplayed()
            .assertIsEnabled()
        composeTestRule.onNodeWithTag(SearchScreenTags.SEARCH_BUTTON)
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    @Test
    fun shouldCallViewModelWhenPostcodeChanged() {
        // Given
        val postcode = "BS1 3JA"
        mockViewState(state = SearchScreenViewModel.ViewState())
        renderComposable()

        // When
        composeTestRule.onNodeWithTag(SearchScreenTags.POSTCODE_TEXT_FIELD)
            .performTextInput(postcode)

        // Then
        verify(exactly = 1) { mockSearchScreenViewModel.onPostcodeChanged(postcode) }
    }

    @Test
    fun shouldCallViewModelWhenSearchClicked() {
        // Given
        mockViewState(state = SearchScreenViewModel.ViewState())
        renderComposable()

        // When
        composeTestRule.onNodeWithTag(SearchScreenTags.SEARCH_BUTTON)
            .performClick()

        // Then
        verify(exactly = 1) { mockSearchScreenViewModel.onSearchClicked() }
    }

    @Test
    fun shouldShowRestaurants() {
        // Given
        val restaurants = listOf(
            Restaurant(id = 1, name = "Chicken Shop", logoUrl = "", rating = 1.0, cuisineTypes = ""),
            Restaurant(id = 2, name = "Supermarket", logoUrl = "", rating = 1.0, cuisineTypes = ""),
            Restaurant(id = 3, name = "Burger Joint", logoUrl = "", rating = 1.0, cuisineTypes = ""),
        )
        mockViewState(state = SearchScreenViewModel.ViewState(isInitial = false, restaurants = restaurants))

        // When
        renderComposable()

        // Then
        with(composeTestRule.onNodeWithTag(SearchScreenTags.RESTAURANTS_LIST)) {
            assertIsDisplayed()
            onChildAt(0)
                .onChildAt(0)
                .assertTextContains("Chicken Shop")
                .assertIsDisplayed()
            onChildAt(1)
                .onChildAt(0)
                .assertTextContains("Supermarket")
                .assertIsDisplayed()
            onChildAt(2)
                .onChildAt(0)
                .assertTextContains("Burger Joint")
                .assertIsDisplayed()
        }
    }

    @Test
    fun shouldShowEmptySearchResultsWhenRestaurantsEmpty() {
        // Given
        mockViewState(state = SearchScreenViewModel.ViewState(isInitial = false, restaurants = emptyList()))

        // When
        renderComposable()

        // Then
        composeTestRule.onNodeWithText(EMPTY_RESTAURANTS_BODY_TEXT)
            .assertIsDisplayed()
    }

    @Test
    fun shouldShowErrorSnackbarWhenErrorSet() {
        // Given
        mockViewState(state = SearchScreenViewModel.ViewState(error = SearchScreenViewModel.SearchError.GET_RESTAURANTS_FAILURE))

        // When
        renderComposable()

        // Then
        composeTestRule.onNodeWithText(GET_RESTAURANTS_ERROR_TEXT)
            .assertIsDisplayed()
    }

    private fun mockViewState(state: SearchScreenViewModel.ViewState) {
        every { mockSearchScreenViewModel.state } returns MutableStateFlow(state)
    }

    private fun renderComposable() {
        composeTestRule.setContent {
            SearchScreen(viewModel = mockSearchScreenViewModel)
        }
    }

    private companion object {
        const val POSTCODE_TEXT_FIELD_LABEL = "Enter your postcode"
        const val INITIAL_STATE_BODY_TEXT = "Enter your postcode and search for restaurants that deliver to you!"
        const val EMPTY_RESTAURANTS_BODY_TEXT = "Unfortunately, there are no restaurants that deliver to your postcode"
        const val GET_RESTAURANTS_ERROR_TEXT = "There was an error getting restaurants, please check your connection and try again."
    }
}
