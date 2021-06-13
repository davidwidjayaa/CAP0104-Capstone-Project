package com.example.angkoot.data

import com.example.angkoot.data.remote.RemoteDataSource
import okhttp3.MultipartBody
import javax.inject.Inject

class AngkootRepository @Inject constructor(
    private val remote: RemoteDataSource
) {
    suspend fun searchPlaces(query: String) =
        remote.searchPlaces(query)

    suspend fun getDetailPlacesOf(placeId: String) =
        remote.getDetailPlacesOf(placeId)

    suspend fun predictCost(file: MultipartBody.Part) =
        remote.predictCost(file)
}