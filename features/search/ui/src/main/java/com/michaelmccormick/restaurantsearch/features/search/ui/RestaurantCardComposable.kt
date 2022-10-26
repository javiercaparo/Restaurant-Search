package com.michaelmccormick.restaurantsearch.features.search.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.michaelmccormick.restaurantsearch.core.models.Restaurant
import com.michaelmccormick.restaurantsearch.core.ui.Dimensions
import com.michaelmccormick.restaurantsearch.features.search.ui.RestaurantCardComposableConstants.CARD_ELEVATION
import com.michaelmccormick.restaurantsearch.features.search.ui.RestaurantCardComposableConstants.RESTAURANT_LOGO_HEIGHT

@Composable
internal fun RestaurantCardComposable(restaurant: Restaurant, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CARD_ELEVATION,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RestaurantLogo(logoUrl = restaurant.logoUrl)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Dimensions.SMALL),
            ) {
                RestaurantName(name = restaurant.name)
                RestaurantCuisineTypes(cuisineTypes = restaurant.cuisineTypes)
            }
            RestaurantRating(rating = restaurant.rating)
        }
    }
}

@Composable
private fun RestaurantLogo(logoUrl: String) {
    AsyncImage(
        modifier = Modifier
            .height(RESTAURANT_LOGO_HEIGHT)
            .padding(all = Dimensions.X_SMALL),
        model = ImageRequest.Builder(LocalContext.current)
            .data(logoUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        error = painterResource(R.drawable.ic_error),
    )
}

@Composable
private fun RestaurantName(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.h3,
    )
}

@Composable
private fun RestaurantCuisineTypes(cuisineTypes: String) {
    Text(
        text = cuisineTypes,
        style = MaterialTheme.typography.body1,
    )
}

@Composable
private fun RestaurantRating(rating: Double) {
    Row(
        modifier = Modifier.padding(end = Dimensions.SMALL),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = stringResource(R.string.star_rating_icon_content_description),
        )
        Text(
            text = "$rating",
            style = MaterialTheme.typography.body1,
        )
    }
}

private object RestaurantCardComposableConstants {
    val CARD_ELEVATION = 5.dp
    val RESTAURANT_LOGO_HEIGHT = 80.dp
}
