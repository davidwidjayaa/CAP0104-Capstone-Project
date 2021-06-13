package com.example.angkoot.utils

import android.content.Context
import com.example.angkoot.api.AngkootApiEndpoint
import com.example.angkoot.domain.model.PredictInfo
import com.example.angkoot.utils.ext.int
import com.google.android.gms.maps.model.LatLng
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

object PredictFIleUtils {
    private const val CSV_HEADER = "key,pickup_datetime,pickup_longitude,pickup_latitude," +
            "dropoff_longitude,dropoff_latitude,passenger_count,year,month,day,hour," +
            "weekday,night,latenight"

    fun prepareMultipartBodyFile(
        context: Context,
        pickupLatLng: LatLng,
        dropOffLatLng: LatLng,
        passengerCount: Int
    ): MultipartBody.Part {
        with(DateAndTimeUtils) {
            val predictInfo = PredictInfo(
                "0",
                getCurrentDateTime(),
                pickupLatLng.longitude,
                pickupLatLng.latitude,
                dropOffLatLng.longitude,
                dropOffLatLng.latitude,
                passengerCount,
                getCurrentYear(),
                getCurrentMonth(),
                getCurrentDay(),
                getCurrentHour(),
                isWeekday().int(),
                isNight().int(),
                isLateNight().int()
            )

            val file = createCSVFile(context, predictInfo)

            val requestFile = RequestBody.create(
                MediaType.parse("text/csv"),
                file
            )

            return MultipartBody.Part.createFormData(
                AngkootApiEndpoint.FILE_KEY,
                file.name,
                requestFile
            )
        }
    }

    private fun createCSVFile(context: Context, predictInfo: PredictInfo): File {
        val fileName = Date().time.toString() + "_angkoot_predict_file.csv"

        File(context.applicationContext.filesDir, fileName).printWriter().use {
            with(it) {
                with(predictInfo) {
                    append(CSV_HEADER)
                    append("\n")

                    with(predictInfo) {
                        append(key)
                        append(",")
                        append(pickupDateTime)
                        append(",")
                        append(pickupLongitude.toString())
                        append(",")
                        append(pickupLatitude.toString())
                        append(",")
                        append(dropOffLongitude.toString())
                        append(",")
                        append(dropOffLatitude.toString())
                        append(",")
                        append(year.toString())
                        append(",")
                        append(month.toString())
                        append(",")
                        append(day.toString())
                        append(",")
                        append(hour.toString())
                        append(",")
                        append(weekDay.toString())
                        append(",")
                        append(isNight.toString())
                        append(",")
                        append(isLateNight.toString())
                    }
                }
            }
        }

        return File(context.applicationContext.filesDir.absolutePath + "/" + fileName)
    }
}