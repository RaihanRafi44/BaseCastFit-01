package com.raihan.basecastfit.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class PhysicalActivity(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: String
) : Parcelable
