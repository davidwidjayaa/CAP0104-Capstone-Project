package com.example.angkoot.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictionResponse(
    @SerializedName("0")
    val cost: Double
)