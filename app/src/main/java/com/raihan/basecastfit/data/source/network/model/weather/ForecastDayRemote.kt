package com.raihan.basecastfit.data.source.network.model.weather

import androidx.annotation.Keep


@Keep
data class ForecastDayRemote(
    val day: DayRemote,
    val hour: List<ForecastHourRemote>
)
