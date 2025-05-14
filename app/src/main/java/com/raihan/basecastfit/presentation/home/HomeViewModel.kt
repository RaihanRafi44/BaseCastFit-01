package com.raihan.basecastfit.presentation.home

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.raihan.basecastfit.data.model.Location
import com.raihan.basecastfit.data.model.Weather
import com.raihan.basecastfit.data.repository.LocationRepository
import com.raihan.basecastfit.data.repository.UserRepository
import com.raihan.basecastfit.data.repository.WeatherRepository
import com.raihan.basecastfit.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _currentLocation = MutableLiveData<CurrentLocationUiState>()
    val currentLocation: LiveData<CurrentLocationUiState> get() = _currentLocation

    private val _weather = MutableLiveData<WeatherUiState>()
    val weather: LiveData<WeatherUiState> get() = _weather

    fun getCurrentUser() = userRepository.getCurrentUser()

    fun fetchLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        geocoder: Geocoder
    ) {
        emitUiState(isLoading = true)
        locationRepository.getCurrentLocation(
            fusedLocationProviderClient = fusedLocationProviderClient,
            onSuccess = { location ->
                viewModelScope.launch(Dispatchers.IO) {
                    val resolved = locationRepository.updateAddress(location, geocoder)
                    locationRepository.saveLocation(resolved)
                    emitUiState(currentLocation = resolved)
                    fetchWeather(resolved.latitude, resolved.longitude)
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

    /*fun fetchWeatherWithLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        geocoder: Geocoder,
        onResult: (String, Double, Double) -> Unit
    ) {
        locationRepository.getCurrentLocation(
            fusedLocationProviderClient = fusedLocationProviderClient,
            onSuccess = { location ->
                viewModelScope.launch(Dispatchers.IO) {
                    val resolved = locationRepository.updateAddress(location, geocoder)
                    locationRepository.saveLocation(resolved)

                    val lat = resolved.latitude
                    val lon = resolved.longitude

                    if (lat != null && lon != null) {
                        val coordinateQuery = "$lat,$lon"
                        onResult(coordinateQuery, lat, lon)
                    }
                    emitUiState(currentLocation = resolved)
                }
            },
            onFailure = {
                emitUiState(error = "Failed to get location")
            }
        )
    }*/

    private fun fetchWeather(lat: Double?, lon: Double?) {
        if (lat == null || lon == null) return
        viewModelScope.launch {
            weatherRepository.getWeathers("$lat,$lon")
                .collect { result ->
                when (result) {
                    is ResultWrapper.Loading -> _weather.postValue(WeatherUiState(isLoading = true))
                    is ResultWrapper.Success -> _weather.postValue(
                        WeatherUiState(data = result.payload)
                    )
                    is ResultWrapper.Error -> _weather.postValue(
                        WeatherUiState(error = result.message ?: "Unknown error")
                    )
                    is ResultWrapper.Empty -> _weather.postValue(
                        WeatherUiState(error = "No data found")
                    )
                    is ResultWrapper.Idle -> {}
                }
            }
        }
    }

    private fun emitUiState(
        isLoading: Boolean = false,
        currentLocation: Location? = null,
        error: String? = null
    ) {
        _currentLocation.postValue(CurrentLocationUiState(isLoading, currentLocation, error))
    }

    data class WeatherUiState(
        val isLoading: Boolean = false,
        val data: Weather? = null,
        val error: String? = null
    )

    data class CurrentLocationUiState(
        val isLoading: Boolean,
        val currentLocation: Location?,
        val error: String?
    )
}