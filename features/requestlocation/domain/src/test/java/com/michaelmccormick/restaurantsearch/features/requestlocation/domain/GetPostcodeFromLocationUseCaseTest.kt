package com.michaelmccormick.restaurantsearch.features.requestlocation.domain

import android.location.Address
import android.location.Geocoder
import android.location.Location
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class GetPostcodeFromLocationUseCaseTest {
    private val mockGeocoder: Geocoder = mockk()
    private lateinit var getPostcodeFromLocationUseCase: GetPostcodeFromLocationUseCase

    @BeforeEach
    fun before() {
        getPostcodeFromLocationUseCase = GetPostcodeFromLocationUseCase(
            geocoder = mockGeocoder,
        )
    }

    @Test
    fun `Should return success for valid location`() {
        // Given
        val location: Location = mockk()
        every { location.latitude } returns 2.3
        every { location.longitude } returns 54.23
        val mockAddress1: Address = mockk()
        val mockAddress2: Address = mockk()
        every { mockAddress1.postalCode } returns "BS1 3JA"
        every { mockGeocoder.getFromLocation(2.3, 54.23, 1) } returns listOf(mockAddress1, mockAddress2)

        // When
        val result = getPostcodeFromLocationUseCase(location)

        // Then
        assertTrue(result.isSuccess)
        assertEquals("BS1 3JA", result.getOrNull())
    }

    @Test
    fun `Should return failure for invalid location`() {
        // Given
        val location: Location = mockk()
        every { location.latitude } returns 2.0
        every { location.longitude } returns 1.0
        every { mockGeocoder.getFromLocation(2.0, 1.0, 1) } returns emptyList()

        // When
        val result = getPostcodeFromLocationUseCase(location)

        // Then
        assertTrue(result.isFailure)
    }
}
