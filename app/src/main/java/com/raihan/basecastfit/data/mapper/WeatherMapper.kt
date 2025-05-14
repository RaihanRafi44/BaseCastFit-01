package com.raihan.basecastfit.data.mapper

import com.raihan.basecastfit.data.model.CurrentLocationRemote
import com.raihan.basecastfit.data.model.CurrentWeather
import com.raihan.basecastfit.data.model.Location
import com.raihan.basecastfit.data.model.Weather
import com.raihan.basecastfit.data.model.WeatherCondition
import com.raihan.basecastfit.data.source.network.model.weather.CurrentWeatherRemote
import com.raihan.basecastfit.data.source.network.model.weather.LocationRemote
import com.raihan.basecastfit.data.source.network.model.weather.WeatherConditionRemote
import com.raihan.basecastfit.data.source.network.model.weather.WeatherData


fun WeatherData?.toWeather() =
    Weather(
        location = this?.location.toCurrentLocation(),
        current = this?.current.toCurrentWeather()
    )

fun LocationRemote?.toCurrentLocation() =
    CurrentLocationRemote(
        name = this?.name.orEmpty(),
        region = this?.region.orEmpty(),
        country = this?.country.orEmpty(),
        lat = this?.lat ?: 0.0,
        lon = this?.lon ?: 0.0
    )

fun CurrentWeatherRemote?.toCurrentWeather() =
    CurrentWeather(
        temperature = this?.temperature ?: 0f,
        condition = this?.condition.toWeatherCondition(),
        wind = this?.wind ?: 0f,
        precipitation = this?.precipitation ?: 0f
    )

fun WeatherConditionRemote?.toWeatherCondition() =
    WeatherCondition(
        text = this?.text.orEmpty(),
        icon = this?.icon.orEmpty()
    )

/*fun Collection<WeatherData>?.toWeathers() =
    this?.map {
        it.toWeather()
    }*/
