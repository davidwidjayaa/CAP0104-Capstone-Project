package com.example.angkoot.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    private val _phoneNumberText = MutableLiveData<String>()
    private val _usernameText = MutableLiveData<String>()
    private val _passwordText = MutableLiveData<String>()
    private val _confirmPasswordText = MutableLiveData<String>()

    val areAllInputsValid = MutableLiveData(false)

    val phoneNumberText: LiveData<String> get() = _phoneNumberText
    val usernameText: LiveData<String> get() = _usernameText
    val passwordText: LiveData<String> get() = _passwordText
    val confirmPasswordText: LiveData<String> get() = _confirmPasswordText

    fun setPhoneNumberText(newString: String) {
        _phoneNumberText.value = newString
    }

    fun setUsernameText(newString: String) {
        _usernameText.value = newString
    }

    fun setPasswordText(newString: String) {
        _passwordText.value = newString
    }

    fun setConfirmPasswordText(newString: String) {
        _confirmPasswordText.value = newString
    }
}