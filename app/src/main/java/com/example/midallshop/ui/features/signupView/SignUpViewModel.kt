package com.example.midallshop.ui.features.signupView

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.midallshop.model.repository.user.UserRepository
import com.example.midallshop.utils.coroutineExceptionHandler
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository : UserRepository): ViewModel() {

    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val repeatPassword = MutableLiveData<String>()

    fun registerUser(loggingEvent: (String)-> Unit){
        viewModelScope.launch(coroutineExceptionHandler) {
            val result = repository.registerUser(name.value!!, email.value!!, password.value!!)
            loggingEvent(result)
        }


    }
}