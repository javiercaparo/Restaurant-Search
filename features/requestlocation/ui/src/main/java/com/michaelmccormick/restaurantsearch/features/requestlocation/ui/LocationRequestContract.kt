package com.michaelmccormick.restaurantsearch.features.requestlocation.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION
import android.location.Location
import androidx.activity.result.contract.ActivityResultContract

/**
 * An [ActivityResultContract] to request the users current location.
 * Returns a [Location] if successful, null if the user did not grant location permission.
 */
class LocationRequestContract : ActivityResultContract<Unit, Location?>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, LocationRequestActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_NO_ANIMATION)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Location? {
        return intent.takeIf { resultCode == Activity.RESULT_OK }?.getParcelableExtra(LocationRequestActivity.LOCATION_EXTRA)
    }
}
