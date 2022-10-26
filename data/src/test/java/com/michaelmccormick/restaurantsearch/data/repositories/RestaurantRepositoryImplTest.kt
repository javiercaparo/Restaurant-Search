package com.michaelmccormick.restaurantsearch.data.repositories

import com.michaelmccormick.restaurantsearch.core.models.Restaurant
import com.michaelmccormick.restaurantsearch.data.mappers.RestaurantEntityMapper
import com.michaelmccormick.restaurantsearch.network.entities.RestaurantEntity
import com.michaelmccormick.restaurantsearch.network.entities.RestaurantSearchResponseEntity
import com.michaelmccormick.restaurantsearch.network.services.JustEatApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
internal class RestaurantRepositoryImplTest {
    private val mockJustEatApiService: JustEatApiService = mockk()
    private val mockRestaurantEntityMapper: RestaurantEntityMapper = mockk()
    private lateinit var restaurantRepositoryImpl: RestaurantRepositoryImpl

    @BeforeEach
    fun before() {
        restaurantRepositoryImpl = RestaurantRepositoryImpl(
            justEatApiService = mockJustEatApiService,
            restaurantEntityMapper = mockRestaurantEntityMapper,
        )
    }

    @Nested
    inner class GetRestaurantsByPostcode {
        @Test
        fun `Should return success when service returns restaurants`() = runTest {
            // Given
            val postcode = "BS1"
            val restaurantEntity1: RestaurantEntity = mockk()
            val restaurantEntity2: RestaurantEntity = mockk()
            val restaurantEntity3: RestaurantEntity = mockk()
            coEvery { mockJustEatApiService.getRestaurantsByPostCode(postcode) } returns RestaurantSearchResponseEntity(
                Restaurants = listOf(restaurantEntity1, restaurantEntity2, restaurantEntity3),
            )
            val restaurant1: Restaurant = mockk()
            val restaurant2: Restaurant = mockk()
            val restaurant3: Restaurant = mockk()
            every { mockRestaurantEntityMapper.toRestaurant(restaurantEntity1) } returns restaurant1
            every { mockRestaurantEntityMapper.toRestaurant(restaurantEntity2) } returns restaurant2
            every { mockRestaurantEntityMapper.toRestaurant(restaurantEntity3) } returns restaurant3

            // When
            val result = restaurantRepositoryImpl.getRestaurantsByPostcode(postcode)

            // Then
            assertTrue(result.isSuccess)
            assertEquals(listOf(restaurant1, restaurant2, restaurant3), result.getOrNull())
            coVerify(exactly = 1) { mockJustEatApiService.getRestaurantsByPostCode(postcode) }
        }

        @Test
        fun `Should return success when service returns no valid restaurants`() = runTest {
            // Given
            val postcode = "BS1"
            val restaurantEntity1: RestaurantEntity = mockk()
            coEvery { mockJustEatApiService.getRestaurantsByPostCode(postcode) } returns RestaurantSearchResponseEntity(
                Restaurants = listOf(restaurantEntity1),
            )
            every { mockRestaurantEntityMapper.toRestaurant(restaurantEntity1) } returns null

            // When
            val result = restaurantRepositoryImpl.getRestaurantsByPostcode(postcode)

            // Then
            assertTrue(result.isSuccess)
            assertEquals(emptyList(), result.getOrNull())
            coVerify(exactly = 1) { mockJustEatApiService.getRestaurantsByPostCode(postcode) }
        }

        @Test
        fun `Should return success when service returns empty list`() = runTest {
            // Given
            val postcode = "BS1"
            coEvery { mockJustEatApiService.getRestaurantsByPostCode(postcode) } returns RestaurantSearchResponseEntity(
                Restaurants = emptyList(),
            )

            // When
            val result = restaurantRepositoryImpl.getRestaurantsByPostcode(postcode)

            // Then
            assertTrue(result.isSuccess)
            assertEquals(emptyList(), result.getOrNull())
            coVerify(exactly = 1) { mockJustEatApiService.getRestaurantsByPostCode(postcode) }
        }

        @Test
        fun `Should return failure when service throws`() = runTest {
            // Given
            val postcode = "BS1"
            coEvery { mockJustEatApiService.getRestaurantsByPostCode(postcode) } throws Exception()

            // When
            val result = restaurantRepositoryImpl.getRestaurantsByPostcode(postcode)

            // Then
            assertTrue(result.isFailure)
            coVerify(exactly = 1) { mockJustEatApiService.getRestaurantsByPostCode(postcode) }
        }
    }
}
