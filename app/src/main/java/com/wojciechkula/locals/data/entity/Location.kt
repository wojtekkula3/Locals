package com.wojciechkula.locals.data.entity

data class Location constructor(
    val geohash: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)