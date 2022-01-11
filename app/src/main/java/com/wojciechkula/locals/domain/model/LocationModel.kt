package com.wojciechkula.locals.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationModel constructor(
    val geohash: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable {
    constructor() : this("", 0.0, 0.0)
}