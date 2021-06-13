package com.example.angkoot.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.angkoot.api.AngkootApiEndpoint
import com.example.angkoot.api.GoogleMapApiEndpoint
import com.example.angkoot.domain.model.Place
import com.example.angkoot.domain.model.Prediction
import com.example.angkoot.utils.ext.asModel
import com.example.angkoot.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val googleMapApi: GoogleMapApiEndpoint,
    private val angkootApi: AngkootApiEndpoint
) {

    suspend fun searchPlaces(query: String) = flow {
        emit(Resource.loading(null))

        try {
            val callResults = googleMapApi.searchPlaces(query)
            val data = callResults.body()

            if (callResults.isSuccessful && data != null) {
                emit(Resource.success(data.results?.asModel()))
            } else {
                emit(Resource.error(null, callResults.message()))
            }
        } catch (exc: Exception) {
            emit(Resource.error(null, exc.message ?: "Error occurred!"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getDetailPlacesOf(placeId: String): LiveData<Resource<Place>> {
        val actualValue = MutableLiveData<Resource<Place>>(Resource.loading(null))

        try {
            val callResults = googleMapApi.getDetailPlaceOf(placeId)
            val data = callResults.body()

            if (callResults.isSuccessful && data != null) {
                actualValue.postValue(Resource.success(data.results.asModel()))
            } else {
                actualValue.postValue(Resource.error(null, callResults.message()))
            }
        } catch (exc: Exception) {
            actualValue.postValue(Resource.error(null, exc.message ?: "Error occurred!"))
        }

        return actualValue
    }

    suspend fun predictCost(file: MultipartBody.Part): LiveData<Resource<Prediction>> {
        val actualValue = MutableLiveData<Resource<Prediction>>(Resource.loading(null))

        try {
            val callResults = angkootApi.predictCost(file)
            val data = callResults.body()

            if (callResults.isSuccessful && data != null) {
                actualValue.postValue(Resource.success(data.results.asModel()))
            } else {
                actualValue.postValue(Resource.error(null, callResults.message()))
            }
        } catch (exc: Exception) {
            actualValue.postValue(Resource.error(null, exc.message ?: "Error occurred!"))
        }

        return actualValue
    }
}