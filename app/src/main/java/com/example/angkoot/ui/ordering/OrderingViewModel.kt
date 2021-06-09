package com.example.angkoot.ui.ordering

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.angkoot.data.AngkootRepository
import com.example.angkoot.domain.model.Place
import com.example.angkoot.vo.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class OrderingViewModel @Inject constructor(
    private val repository: AngkootRepository
) : ViewModel() {
    private val queryForSearchingPlacesPickup = MutableLiveData<String>()
    private val queryForSearchingPlacesDrop = MutableLiveData<String>()

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