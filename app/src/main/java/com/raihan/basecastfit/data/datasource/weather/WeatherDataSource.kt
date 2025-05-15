package com.raihan.basecastfit.data.datasource.weather

import com.raihan.basecastfit.data.source.network.model.weather.WeatherData
import com.raihan.basecastfit.data.source.network.service.CastFitApiService

interface WeatherDataSource {
    suspend fun getWeatherData(query: String): WeatherData
}

class WeatherDataSourceImpl(private val service: CastFitApiService) : WeatherDataSource {
    override suspend fun getWeatherData(query: String): WeatherData {
        return service.getWeatherData(query = query)

    }
}

