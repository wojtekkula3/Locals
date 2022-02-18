package com.wojciechkula.locals.data.mapper

import com.wojciechkula.locals.data.entity.Location
import com.wojciechkula.locals.domain.model.LocationModel
import javax.inject.Inject

class LocationMapper @Inject constructor() {

    fun mapToEntity(location: LocationModel): Location =
        Location(
            geohash = location.geohash,
            latitude = location.latitude,
            longitude = location.longitude
        )

    fun mapToDomain(location: Location): LocationModel =
        LocationModel(
            geohash = location.geohash,
            latitude = location.latitude,
            longitude = location.longitude
        )
}