package com.raihan.basecastfit.data.source.network.model.weather

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ForecastRemote(
    @SerializedName("forecastday") val forecastDay: List<ForecastDayRemote>
)
