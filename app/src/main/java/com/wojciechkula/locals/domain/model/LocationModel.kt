package com.wojciechkula.locals.domain.model

data class LocationModel constructor(
    val geohash: String,
    val latitude: Double,
    val longitude: Double
)