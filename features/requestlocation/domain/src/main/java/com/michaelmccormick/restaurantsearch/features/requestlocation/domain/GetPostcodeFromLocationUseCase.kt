package com.michaelmccormick.restaurantsearch.features.requestlocation.domain

import android.location.Geocoder
import android.location.Location
import javax.inject.Inject
import timber.log.Timber

class GetPostcodeFromLocationUseCase @Inject internal constructor(
    private val geocoder: Geocoder,
) {
    /**
     * Returns the postcode for the passed [location].
     */
    operator fun invoke(location: Location): Result<String> {
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        return if (addresses.isNullOrEmpty()) {
            val e = Exception("No addresses found for location")
            Timber.e(e)
            Result.failure(e)
        } else {
            Result.success(addresses.first().postalCode)
        }
    }
}
