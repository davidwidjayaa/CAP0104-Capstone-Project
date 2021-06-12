package com.example.angkoot.domain.model

data class PredictInfo(
    val key: String,
    val pickupDateTime: String,
    val pickupLatitude: Double,
    val pickupLongitude: Double,
    val dropOffLatitude: Double,
    val dropOffLongitude: Double,
    val passengerCount: Int,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val weekDay: Int,
    val isNight: Int,
    val isLateNight: Int
)
