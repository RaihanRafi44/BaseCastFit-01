package com.raihan.basecastfit.data.source.network.model.weather

import androidx.annotation.Keep

@Keep
data class WeatherData(
    val current: CurrentWeatherRemote,
    val forecast: ForecastRemote
)
