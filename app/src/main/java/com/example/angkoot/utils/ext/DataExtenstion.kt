package com.example.angkoot.utils.ext

import com.example.angkoot.data.remote.response.DetailPlacesResponse
import com.example.angkoot.data.remote.response.PlacesResponse
import com.example.angkoot.domain.model.Place

fun List<PlacesResponse>.asModel(): List<Place> {
    val places = ArrayList<Place>()

    for (place in this) {
        places.add(
            Place(
                place.id,
                null,
                null,
                null,
                place.description
            )
        )
    }

    return places
}

fun DetailPlacesResponse.asModel(): Place =
    Place(
        this.id,
        this.geometry,
        this.iconUrl,
        this.name,
        null
    )