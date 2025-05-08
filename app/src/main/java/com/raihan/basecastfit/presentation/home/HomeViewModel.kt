package com.raihan.basecastfit.presentation.home

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.raihan.basecastfit.data.model.Location
import com.raihan.basecastfit.data.repository.LocationRepository
import com.raihan.basecastfit.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _currentLocation = MutableLiveData<CurrentLocationUiState>()
    val currentLocation: LiveData<CurrentLocationUiState> get() = _currentLocation

    fun getCurrentUser() = userRepository.getCurrentUser()

    fun fetchLocation(fusedLocationProviderClient: FusedLocationProviderClient, geocoder: Geocoder) {
        emitUiState(isLoading = true)
        locationRepository.getCurrentLocation(
            fusedLocationProviderClient = fusedLocationProviderClient,
            onSuccess = { location ->
                viewModelScope.launch(Dispatchers.IO) {
                    val resolved = locationRepository.updateAddress(location, geocoder)
                    locationRepository.saveLocation(resolved)
                    emitUiState(currentLocation = resolved)
                }
            },
            onFailure = {
                emitUiState(error = "Failed to get location")
            }
        )
    }

    fun loadSavedLocation() {
        val savedLocation = locationRepository.getSavedLocation()
        emitUiState(currentLocation = savedLocation)
    }


    private fun emitUiState(
        isLoading: Boolean = false,
        currentLocation: Location? = null,
        error: String? = null
    ) {
        _currentLocation.postValue(CurrentLocationUiState(isLoading, currentLocation, error))
    }

    data class CurrentLocationUiState(
        val isLoading: Boolean,
        val currentLocation: Location?,
        val error: String?
    )
}