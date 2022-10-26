package com.michaelmccormick.restaurantsearch.features.search.ui

import android.location.Location
import app.cash.turbine.test
import com.michaelmccormick.restaurantsearch.core.models.Restaurant
import com.michaelmccormick.restaurantsearch.core.test.DispatchersExtension
import com.michaelmccormick.restaurantsearch.features.requestlocation.domain.GetPostcodeFromLocationUseCase
import com.michaelmccormick.restaurantsearch.features.search.domain.GetRestaurantsByPostcodeUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class, DispatchersExtension::class)
internal class SearchScreenViewModelTest {
    private val mockGetRestaurantsByPostcodeUseCase: GetRestaurantsByPostcodeUseCase = mockk()
    private val mockGetPostcodeFromLocationUseCase: GetPostcodeFromLocationUseCase = mockk()
    private lateinit var searchScreenViewModel: SearchScreenViewModel

    @BeforeEach
    fun before() {
        searchScreenViewModel = SearchScreenViewModel(
            getRestaurantsByPostcodeUseCase = mockGetRestaurantsByPostcodeUseCase,
            getPostcodeFromLocationUseCase = mockGetPostcodeFromLocationUseCase,
        )
    }

    @Nested
    inner class Init {
        @Test
        fun `Should have expected initial state`() {
            assertEquals(
                SearchScreenViewModel.ViewState(
                    isInitial = true,
                    isLoading = false,
                    postcode = "",
                    restaurants = emptyList(),
                    error = null,
                ),
                searchScreenViewModel.state.value,
            )
        }
    }

    @Nested
    inner class OnPostcodeChanged {
        @Test
        fun `Should update postcode in state when postcode changed`() {
            // Given
            val postcode = "BS1 3JA"

            // When
            searchScreenViewModel.onPostcodeChanged(postcode)

            // Then
            assertEquals(
                SearchScreenViewModel.ViewState(postcode = postcode),
                searchScreenViewModel.state.value,
            )
        }
    }

    @Nested
    inner class OnUserLocated {
        @Test
        fun `Should perform search when postcode retrieved from location successfully`() = runTest {
            // Given
            val location: Location = mockk()
            val postcode = "BS1"
            every { mockGetPostcodeFromLocationUseCase(location) } returns Result.success(postcode)
            val restaurants = listOf<Restaurant>(mockk(), mockk())
            coEvery { mockGetRestaurantsByPostcodeUseCase(postcode) } returns Result.success(restaurants)

            searchScreenViewModel.state.test {
                // When
                searchScreenViewModel.onUserLocated(location)

                // Then
                assertEquals(
                    SearchScreenViewModel.ViewState(),
                    awaitItem(),
                )
                assertEquals(
                    SearchScreenViewModel.ViewState(postcode = postcode),
                    awaitItem(),
                )
                assertEquals(
                    SearchScreenViewModel.ViewState(isInitial = false, isLoading = true, postcode = postcode),
                    awaitItem(),
                )
                assertEquals(
                    SearchScreenViewModel.ViewState(isInitial = false, isLoading = false, postcode = postcode, restaurants = restaurants),
                    awaitItem(),
                )
                coVerifyOrder {
                    mockGetPostcodeFromLocationUseCase(location)
                    mockGetRestaurantsByPostcodeUseCase(postcode)
                }
            }
        }

        @Test
        fun `Should set search error when postcode retrieved from location successfully but search fails`() = runTest {
            // Given
            val location: Location = mockk()
            val postcode = "BS1"
            every { mockGetPostcodeFromLocationUseCase(location) } returns Result.success(postcode)
            coEvery { mockGetRestaurantsByPostcodeUseCase(postcode) } returns Result.failure(Exception())

            searchScreenViewModel.state.test {
                // When
                searchScreenViewModel.onUserLocated(location)

                // Then
                assertEquals(
                    SearchScreenViewModel.ViewState(),
                    awaitItem(),
                )
                assertEquals(
                    SearchScreenViewModel.ViewState(postcode = postcode),
                    awaitItem(),
                )
                assertEquals(
                    SearchScreenViewModel.ViewState(isInitial = false, isLoading = true, postcode = postcode),
                    awaitItem(),
                )
                assertEquals(
                    SearchScreenViewModel.ViewState(
                        isInitial = false,
                        isLoading = false,
                        postcode = postcode,
                        error = SearchScreenViewModel.SearchError.GET_RESTAURANTS_FAILURE,
                    ),
                    awaitItem(),
                )
                coVerifyOrder {
                    mockGetPostcodeFromLocationUseCase(location)
                    mockGetRestaurantsByPostcodeUseCase(postcode)
                }
            }
        }

        @Test
        fun `Should set location error when get postcode from location fails`() = runTest {
            // Given
            val location: Location = mockk()
            every { mockGetPostcodeFromLocationUseCase(location) } returns Result.failure(Exception())

            searchScreenViewModel.state.test {
                // When
                searchScreenViewModel.onUserLocated(location)

                // Then
                assertEquals(
                    SearchScreenViewModel.ViewState(),
                    awaitItem(),
                )
                assertEquals(
                    SearchScreenViewModel.ViewState(error = SearchScreenViewModel.SearchError.GET_LOCATION_ERROR),
                    awaitItem(),
                )
                coVerify(exactly = 1) { mockGetPostcodeFromLocationUseCase(location) }
                coVerify(exactly = 0) { mockGetRestaurantsByPostcodeUseCase(any()) }
            }
        }

        @Test
        fun `Should reset when user located after previous get postcode from location failed`() = runTest {
            // Given
            val location: Location = mockk()
            every { mockGetPostcodeFromLocationUseCase(location) } returns Result.failure(Exception())
            searchScreenViewModel.onUserLocated(location)

            searchScreenViewModel.state.test {
                // When
                searchScreenViewModel.onUserLocated(location)

                // Then
                assertEquals(
                    SearchScreenViewModel.ViewState(error = SearchScreenViewModel.SearchError.GET_LOCATION_ERROR),
                    awaitItem(),
                )
                assertEquals(
                    SearchScreenViewModel.ViewState(error = null),
                    awaitItem(),
                )
                assertEquals(
                    SearchScreenViewModel.ViewState(error = SearchScreenViewModel.SearchError.GET_LOCATION_ERROR),
                    awaitItem(),
                )
                coVerify(exactly = 2) { mockGetPostcodeFromLocationUseCase(location) }
                coVerify(exactly = 0) { mockGetRestaurantsByPostcodeUseCase(any()) }
            }
        }

        @Test
        fun `Should set location permission error when location is null`() = runTest {
            // Given
            val location: Location? = null

            searchScreenViewModel.state.test {
                // When
                searchScreenViewModel.onUserLocated(location)

                // Then
                assertEquals(
                    SearchScreenViewModel.ViewState(),
                    awaitItem(),
                )
                assertEquals(
                    SearchScreenViewModel.ViewState(error = SearchScreenViewModel.SearchError.LOCATION_PERMISSION_NOT_GRANTED),
                    awaitItem(),
                )
                coVerify(exactly = 0) {
                    mockGetPostcodeFromLocationUseCase(any())
                    mockGetRestaurantsByPostcodeUseCase(any())
                }
            }
        }

        @Test
        fun `Should reset location permission error after location was previously null`() = runTest {
            // Given
            val location: Location? = null
            searchScreenViewModel.onUserLocated(location)

            searchScreenViewModel.state.test {
                // When
                searchScreenViewModel.onUserLocated(location)

                // Then
                assertEquals(
                    SearchScreenViewModel.ViewState(error = SearchScreenViewModel.SearchError.LOCATION_PERMISSION_NOT_GRANTED),
                    awaitItem(),
                )
                assertEquals(
                    SearchScreenViewModel.ViewState(error = null),
                    awaitItem(),
                )
                assertEquals(
                    SearchScreenViewModel.ViewState(error = SearchScreenViewModel.SearchError.LOCATION_PERMISSION_NOT_GRANTED),
                    awaitItem(),
                )
                coVerify(exactly = 0) {
                    mockGetPostcodeFromLocationUseCase(any())
                    mockGetRestaurantsByPostcodeUseCase(any())
                }
            }
        }
    }

    @Nested
    inner class OnSearchClicked {
        @Test
        fun `Should perform search when search clicked`() = runTest {
            // Given
            val postcode = "BS1"
            searchScreenViewModel.onPostcodeChanged(postcode)
            val restaurants = listOf<Restaurant>(mockk(), mockk(), mockk())
            coEvery { mockGetRestaurantsByPostcodeUseCase(postcode) } returns Result.success(restaurants)

            searchScreenViewModel.state.test {
                // When
                searchScreenViewModel.onSearchClicked()

                // Then
                assertEquals(
                    SearchScreenViewModel.ViewState(postcode = postcode),
                    awaitItem(),
                )
                assertEquals(
                    SearchScreenViewModel.ViewState(isInitial = false, isLoading = true, postcode = postcode),
                    awaitItem(),
                )
                assertEquals(
                    SearchScreenViewModel.ViewState(isInitial = false, isLoading = false, postcode = postcode, restaurants = restaurants),
                    awaitItem(),
                )
                coVerify(exactly = 1) { mockGetRestaurantsByPostcodeUseCase(postcode) }
            }
        }

        @Test
        fun `Should set error in state when search fails`() = runTest {
            // Given
            val postcode = "BS1"
            searchScreenViewModel.onPostcodeChanged(postcode)
            coEvery { mockGetRestaurantsByPostcodeUseCase(postcode) } returns Result.failure(Exception())

            searchScreenViewModel.state.test {
                // When
                searchScreenViewModel.onSearchClicked()

                // Then
                assertEquals(
                    SearchScreenViewModel.ViewState(postcode = postcode),
                    awaitItem(),
                )
                assertEquals(
                    SearchScreenViewModel.ViewState(isInitial = false, isLoading = true, postcode = postcode),
                    awaitItem(),
                )
                assertEquals(
                    SearchScreenViewModel.ViewState(
                        isInitial = false,
                        isLoading = false,
                        postcode = postcode,
                        error = SearchScreenViewModel.SearchError.GET_RESTAURANTS_FAILURE,
                    ),
                    awaitItem(),
                )
                coVerify(exactly = 1) { mockGetRestaurantsByPostcodeUseCase(postcode) }
            }
        }

        @Test
        fun `Should reset error in state when searching again after failed`() = runTest {
            // Given
            val postcode = "BS1"
            searchScreenViewModel.onPostcodeChanged(postcode)
            coEvery { mockGetRestaurantsByPostcodeUseCase(postcode) } returns Result.failure(Exception())
            searchScreenViewModel.onSearchClicked()

            searchScreenViewModel.state.test {
                // When
                searchScreenViewModel.onSearchClicked()

                // Then
                assertEquals(
                    SearchScreenViewModel.ViewState(isInitial = false, postcode = postcode, error = SearchScreenViewModel.SearchError.GET_RESTAURANTS_FAILURE),
                    awaitItem(),
                )
                assertEquals(
                    SearchScreenViewModel.ViewState(isInitial = false, isLoading = true, postcode = postcode, error = null),
                    awaitItem(),
                )
                assertEquals(
                    SearchScreenViewModel.ViewState(
                        isInitial = false,
                        isLoading = false,
                        postcode = postcode,
                        error = SearchScreenViewModel.SearchError.GET_RESTAURANTS_FAILURE,
                    ),
                    awaitItem(),
                )
                coVerify(exactly = 2) { mockGetRestaurantsByPostcodeUseCase(postcode) }
            }
        }

        @Test
        fun `Should do nothing if postcode empty when search clicked`() {
            // When
            searchScreenViewModel.onSearchClicked()

            // Then
            assertEquals(
                SearchScreenViewModel.ViewState(postcode = "", isInitial = true, isLoading = false),
                searchScreenViewModel.state.value,
            )
            coVerify(exactly = 0) { mockGetRestaurantsByPostcodeUseCase(any()) }
        }
    }
}
