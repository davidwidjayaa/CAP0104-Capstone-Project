package com.example.angkoot.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PredictionResultsResponse(
    @Expose
    @SerializedName("prediction")
    val results: PredictionResponse
)