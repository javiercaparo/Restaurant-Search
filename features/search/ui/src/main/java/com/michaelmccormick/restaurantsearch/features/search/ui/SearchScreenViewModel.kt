package com.michaelmccormick.restaurantsearch.features.search.ui

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelmccormick.restaurantsearch.core.models.Restaurant
import com.michaelmccormick.restaurantsearch.features.requestlocation.domain.GetPostcodeFromLocationUseCase
import com.michaelmccormick.restaurantsearch.features.search.domain.GetRestaurantsByPostcodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SearchScreenViewModel @Inject internal constructor(
    private val getRestaurantsByPostcodeUseCase: GetRestaurantsByPostcodeUseCase,
    private val getPostcodeFromLocationUseCase: GetPostcodeFromLocationUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<ViewState> = MutableStateFlow(ViewState())
    val state: StateFlow<ViewState> = _state.asStateFlow()

    fun onPostcodeChanged(postcode: String) {
        viewModelScope.launch {
            _state.emit(state.value.copy(postcode = postcode))
        }
    }

    fun onUserLocated(location: Location?) {
        viewModelScope.launch { _state.emit(state.value.copy(error = null)) }
        if (location == null) {
            viewModelScope.launch { _state.emit(state.value.copy(error = SearchError.LOCATION_PERMISSION_NOT_GRANTED)) }
        } else {
            getPostcodeFromLocationUseCase(location)
                .onSuccess {
                    viewModelScope.launch { _state.emit(state.value.copy(postcode = it)) }
                    onSearchClicked()
                }
                .onFailure {
                    viewModelScope.launch { _state.emit(state.value.copy(error = SearchError.GET_LOCATION_ERROR)) }
                }
        }
    }

    fun onSearchClicked() {
        if (state.value.postcode.isBlank()) return
        viewModelScope.launch {
            _state.emit(state.value.copy(isInitial = false, isLoading = true, restaurants = emptyList(), error = null))
            getRestaurantsByPostcodeUseCase(postcode = state.value.postcode)
                .onSuccess {
                    _state.emit(state.value.copy(isLoading = false, restaurants = it))
                }
                .onFailure {
                    _state.emit(state.value.copy(isLoading = false, error = SearchError.GET_RESTAURANTS_FAILURE))
                }
        }
    }

    data class ViewState(
        val isInitial: Boolean = true,
        val isLoading: Boolean = false,
        val postcode: String = "",
        val restaurants: List<Restaurant> = emptyList(),
        val error: SearchError? = null,
    )

    enum class SearchError { GET_RESTAURANTS_FAILURE, LOCATION_PERMISSION_NOT_GRANTED, GET_LOCATION_ERROR }
}
