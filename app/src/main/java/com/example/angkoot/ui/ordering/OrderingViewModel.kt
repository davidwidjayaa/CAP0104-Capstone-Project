package com.example.angkoot.ui.ordering

import androidx.lifecycle.*
import com.example.angkoot.data.AngkootRepository
import com.example.angkoot.domain.model.Place
import com.example.angkoot.vo.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class OrderingViewModel @Inject constructor(
    private val repository: AngkootRepository
) : ViewModel() {
    private val queryForSearchingPlacesPickup = MutableLiveData<String>()
    private val queryForSearchingPlacesDrop = MutableLiveData<String>()

    private val pickupPoint = MutableLiveData<Place?>()
    private val dropPoint = MutableLiveData<Place?>()

    private val _pickupPointDetail = MutableLiveData<Resource<Place>>()
    val pickupPointDetail: LiveData<Resource<Place>> get() = _pickupPointDetail
    private val _dropPointDetail = MutableLiveData<Resource<Place>>()
    val dropPointDetail: LiveData<Resource<Place>> get() = _dropPointDetail

    var areAllInputsValid = MutableLiveData(listOf(false, false))

    fun validatePickupPoint(valid: Boolean) {
        areAllInputsValid.value = areAllInputsValid.value?.mapIndexed { index, validState ->
            if (index == 0) valid
            else validState
        }
    }

    fun validateDropPoint(valid: Boolean) {
        areAllInputsValid.value = areAllInputsValid.value?.mapIndexed { index, validState ->
            if (index == 1) valid
            else validState
        }
    }

    fun setDetailOfPickupPlace(place: Place) =
        viewModelScope.launch(Dispatchers.IO) {
            val results = repository.getDetailPlacesOf(place.id)

            withContext(Dispatchers.Main) {
                _pickupPointDetail.value = results.value
            }
        }

    fun setDetailOfDropPlace(place: Place) =
        viewModelScope.launch(Dispatchers.IO) {
            val results = repository.getDetailPlacesOf(place.id)

            withContext(Dispatchers.Main) {
                _dropPointDetail.value = results.value
            }
        }


    fun setPickupPoint(pickupPoint: Place?) {
        this.pickupPoint.value = pickupPoint
    }

    fun getPickupPoint() = this.pickupPoint

    fun setDropPoint(dropPoint: Place?) {
        this.dropPoint.value = dropPoint
    }

    fun getDropPoint() = this.dropPoint

    private val searchingPlacesPickupResults = object : MutableLiveData<Resource<List<Place>?>>() {
        override fun onActive() {
            super.onActive()
            value?.let { return }

            viewModelScope.launch {
                queryForSearchingPlacesPickup.asFlow()
                    .debounce(250)
                    .distinctUntilChanged()
                    .collect {
                        if (it.isNotEmpty()) {
                            repository.searchPlaces(it).collect { resultValue ->
                                value = resultValue
                            }
                        } else {
                            value = Resource.error(null, "No data")
                        }
                    }
            }
        }
    }

    private val searchingPlacesDropResults = object : MutableLiveData<Resource<List<Place>?>>() {
        override fun onActive() {
            super.onActive()
            value?.let { return }

            viewModelScope.launch {
                queryForSearchingPlacesDrop.asFlow()
                    .debounce(250)
                    .distinctUntilChanged()
                    .collect {
                        if (it.isNotEmpty()) {
                            repository.searchPlaces(it).collect { resultValue ->
                                value = resultValue
                            }
                        } else {
                            value = Resource.error(null, "No data")
                        }
                    }
            }
        }
    }

    fun getSearchingPlacesPickupResults() = searchingPlacesPickupResults
    fun getSearchingPlacesDropResults() = searchingPlacesDropResults

    fun setQueryForSearchingPlacesPickup(newQuery: String) {
        queryForSearchingPlacesPickup.value = newQuery
    }

    fun setQueryForSearchingPlacesDrop(newQuery: String) {
        queryForSearchingPlacesDrop.value = newQuery
    }
}