package com.michaelmccormick.restaurantsearch.features.search.domain

import com.michaelmccormick.restaurantsearch.core.models.Restaurant
import com.michaelmccormick.restaurantsearch.data.repositories.RestaurantRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
internal class GetRestaurantsByPostcodeUseCaseTest {
    private val mockRestaurantRepository: RestaurantRepository = mockk()
    private lateinit var getRestaurantsByPostcodeUseCase: GetRestaurantsByPostcodeUseCase

    @BeforeEach
    fun before() {
        getRestaurantsByPostcodeUseCase = GetRestaurantsByPostcodeUseCase(
            restaurantRepository = mockRestaurantRepository,
        )
    }

    @Test
    fun `Should return success when repository successful`() = runTest {
        // Given
        val postcode = "BS1"
        val restaurants = listOf<Restaurant>(mockk(), mockk())
        coEvery { mockRestaurantRepository.getRestaurantsByPostcode(postcode) } returns Result.success(restaurants)

        // When
        val result = getRestaurantsByPostcodeUseCase(postcode)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(restaurants, result.getOrNull())
        coVerify(exactly = 1) { mockRestaurantRepository.getRestaurantsByPostcode(postcode) }
    }

    @Test
    fun `Should return failure when repository fails`() = runTest {
        // Given
        val postcode = "BS1"
        coEvery { mockRestaurantRepository.getRestaurantsByPostcode(postcode) } returns Result.failure(Exception())

        // When
        val result = getRestaurantsByPostcodeUseCase(postcode)

        // Then
        assertTrue(result.isFailure)
        coVerify(exactly = 1) { mockRestaurantRepository.getRestaurantsByPostcode(postcode) }
    }
}
