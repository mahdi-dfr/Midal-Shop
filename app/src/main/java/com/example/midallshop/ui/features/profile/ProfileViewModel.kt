package com.example.midallshop.ui.features.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.midallshop.model.repository.user.UserRepository
import com.example.midallshop.utils.styleTime

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    val username = mutableStateOf("")
    val getDialog = mutableStateOf(false)
    val userLocation = mutableStateOf("")
    val userPostalCode = mutableStateOf("")
    val userLoginTime = mutableStateOf("")

    init {
        getUserName()
        getUserLocationAndPostalCode()
        getLoginTime()
    }

    private fun getUserName() {
        username.value = repository.getUserName()!!
    }

    fun signOut() {
        repository.logOutUser()
    }

    fun setUserLocation(location: String, postalCode: String) {
        repository.setUserLocationAndPostalCode(location, postalCode)
    }

    fun getUserLocationAndPostalCode() {
        val locationAndPostalCode = repository.getUserLocationAndPostalCode()
        userLocation.value = locationAndPostalCode.first!!
        userPostalCode.value = locationAndPostalCode.second!!
    }

    private fun getLoginTime() {
        val time = repository.getUserLoginTime()!!.toLong()
        val timeFormat = styleTime(time)
        userLoginTime.value = timeFormat
    }


}