package com.michaelmccormick.restaurantsearch.features.search.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.michaelmccormick.restaurantsearch.core.models.Restaurant
import com.michaelmccormick.restaurantsearch.core.ui.Dimensions
import com.michaelmccormick.restaurantsearch.features.requestlocation.ui.LocationRequestContract
import com.michaelmccormick.restaurantsearch.features.search.ui.SearchScreenTags.LOCATE_BUTTON
import com.michaelmccormick.restaurantsearch.features.search.ui.SearchScreenTags.POSTCODE_TEXT_FIELD
import com.michaelmccormick.restaurantsearch.features.search.ui.SearchScreenTags.RESTAURANTS_LIST
import com.michaelmccormick.restaurantsearch.features.search.ui.SearchScreenTags.SEARCH_BUTTON

@Composable
fun SearchScreen(viewModel: SearchScreenViewModel) {
    val scaffoldState = rememberScaffoldState()
    val viewState by viewModel.state.collectAsState()
    val requestLocationLauncher = rememberLauncherForActivityResult(contract = LocationRequestContract(), onResult = viewModel::onUserLocated)
    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = it),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PostcodeTextField(
                postcode = viewState.postcode,
                enabled = viewState.postcode.isNotBlank(),
                onPostcodeChanged = viewModel::onPostcodeChanged,
                onLocateClicked = { requestLocationLauncher.launch(Unit) },
                onSearchClicked = viewModel::onSearchClicked,
            )
            if (viewState.isLoading) {
                LoadingSpinner()
            } else if (viewState.restaurants.isNotEmpty()) {
                RestaurantsList(restaurants = viewState.restaurants)
            } else if (viewState.isInitial) {
                InitialEmptyRestaurantsList()
            } else {
                EmptyRestaurantsList()
            }
        }
        SnackBar(error = viewState.error, scaffoldState = scaffoldState)
    }
}

@Composable
private fun PostcodeTextField(
    postcode: String,
    enabled: Boolean,
    onPostcodeChanged: (String) -> Unit,
    onLocateClicked: () -> Unit,
    onSearchClicked: () -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.testTag(POSTCODE_TEXT_FIELD),
        value = postcode,
        onValueChange = onPostcodeChanged,
        label = {
            Text(text = stringResource(R.string.postcode_text_field_label))
        },
        trailingIcon = {
            Row {
                IconButton(
                    modifier = Modifier.testTag(LOCATE_BUTTON),
                    onClick = onLocateClicked,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = stringResource(R.string.search_button_content_description),
                        tint = MaterialTheme.colors.primary,
                    )
                }
                IconButton(
                    modifier = Modifier.testTag(SEARCH_BUTTON),
                    onClick = onSearchClicked,
                    enabled = enabled,
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_button_content_description),
                        tint = MaterialTheme.colors.primary.copy(alpha = if (enabled) 1f else ContentAlpha.disabled),
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            autoCorrect = false,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(onSearch = { onSearchClicked() }),
        singleLine = true,
        maxLines = 1,
        shape = MaterialTheme.shapes.large,
    )
}

@Composable
private fun LoadingSpinner() {
    CircularProgressIndicator(
        modifier = Modifier
            .padding(all = Dimensions.NORMAL)
            .testTag(SearchScreenTags.LOADING_SPINNER),
    )
}

@Composable
private fun RestaurantsList(restaurants: List<Restaurant>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(RESTAURANTS_LIST),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        items(
            items = restaurants,
            key = { it.id },
            contentType = { Restaurant::class },
        ) {
            RestaurantCardComposable(
                modifier = Modifier.padding(vertical = Dimensions.SMALL, horizontal = Dimensions.X_SMALL),
                restaurant = it,
            )
        }
    }
}

@Composable
private fun InitialEmptyRestaurantsList() {
    Text(
        modifier = Modifier.padding(horizontal = Dimensions.NORMAL, vertical = Dimensions.LARGE),
        text = stringResource(R.string.initial_empty_restaurants_list),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun EmptyRestaurantsList() {
    Text(
        modifier = Modifier.padding(horizontal = Dimensions.NORMAL, vertical = Dimensions.LARGE),
        text = stringResource(R.string.empty_restaurants_list),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun SnackBar(error: SearchScreenViewModel.SearchError?, scaffoldState: ScaffoldState) {
    val message = stringResource(
        id = when (error) {
            SearchScreenViewModel.SearchError.GET_RESTAURANTS_FAILURE -> R.string.get_restaurants_error
            SearchScreenViewModel.SearchError.LOCATION_PERMISSION_NOT_GRANTED -> R.string.location_permission_not_granted_error
            SearchScreenViewModel.SearchError.GET_LOCATION_ERROR -> R.string.get_location_error
            else -> return
        },
    )
    LaunchedEffect(key1 = error) {
        scaffoldState.snackbarHostState.showSnackbar(message = message)
    }
}

object SearchScreenTags {
    const val LOADING_SPINNER = "LoadingSpinnerTag"
    const val POSTCODE_TEXT_FIELD = "PostcodeTextFieldTag"
    const val LOCATE_BUTTON = "LocateButtonTag"
    const val SEARCH_BUTTON = "SearchButtonTag"
    const val RESTAURANTS_LIST = "RestaurantsListTag"
}
