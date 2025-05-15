package com.raihan.basecastfit.data.repository

import com.raihan.basecastfit.data.datasource.weather.WeatherDataSource
import com.raihan.basecastfit.data.model.CurrentLocationRemote
import com.raihan.basecastfit.data.model.CurrentWeather
import com.raihan.basecastfit.data.mapper.toWeather
import com.raihan.basecastfit.data.model.Weather
import com.raihan.basecastfit.utils.ResultWrapper
import com.raihan.basecastfit.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getWeathers(
        query: String
    ) : Flow<ResultWrapper<Weather>>
}

class WeatherRepositoryImpl(private val dataSource: WeatherDataSource) : WeatherRepository {
    override fun getWeathers(query: String): Flow<ResultWrapper<Weather>> {
        return proceedFlow {
            val response = dataSource.getWeatherData(query)
            response.toWeather()
        }
    }
}

