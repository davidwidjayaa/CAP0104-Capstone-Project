package com.example.angkoot.data.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.angkoot.api.ApiEndpoint
import com.example.angkoot.domain.model.Place
import com.example.angkoot.utils.ext.asModel
import com.example.angkoot.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: ApiEndpoint
) {

    suspend fun searchPlaces(query: String) = flow {
        emit(Resource.loading(null))

        try {
            val callResults = api.searchPlaces(query)
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
        Log.d("Hehe", "Hehe")
        val actualValue = MutableLiveData<Resource<Place>>(Resource.loading(null))

        try {
            val callResults = api.getDetailPlaceOf(placeId)
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