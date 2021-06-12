package com.example.angkoot.api

import com.example.angkoot.data.remote.response.PredictionResultsResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AngkootApiEndpoint {
    @Multipart
    @POST("predict")
    suspend fun predictCost(
        @Part file: MultipartBody.Part
    ): Response<PredictionResultsResponse>

    companion object {
        const val BASE_URL = "http://34.101.176.23/"
        const val FILE_KEY = "files"
    }
}