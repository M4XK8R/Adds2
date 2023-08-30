package com.example.adds2.ui.screens.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _isCountryLd = MutableLiveData<Boolean>()
    val isCountryLd: LiveData<Boolean> get() = _isCountryLd

    private val _countryLd = MutableLiveData<String>()
    val countryLd: LiveData<String> get() = _countryLd

    private val _cityLd = MutableLiveData<String>()
    val cityLd: LiveData<String> get() = _cityLd

    private val _listOfLocationLd = MutableLiveData<List<String>>()
    val listOfLocationLd: LiveData<List<String>> get() = _listOfLocationLd

    fun setValueToIsCountryLd(value: Boolean) {
        _isCountryLd.value = value
    }

    fun setValueToListOFLocationLd(value: List<String>) {
        _listOfLocationLd.value = value
    }

    fun setValueToCountryLd(value: String) {
        _countryLd.value = value
    }

    fun setValueToCityLd(value: String) {
        _cityLd.value = value
    }

}