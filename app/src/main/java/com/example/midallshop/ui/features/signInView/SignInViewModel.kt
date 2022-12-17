package com.example.midallshop.ui.features.signInView

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.midallshop.model.repository.user.UserRepository
import com.example.midallshop.utils.coroutineExceptionHandler
import kotlinx.coroutines.launch

class SignInViewModel(private val repository : UserRepository)  :ViewModel() {

    val userName = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun signIn(loggingResult:(String)-> Unit){

        viewModelScope.launch(coroutineExceptionHandler){
            val result = repository.loginUser(userName.value!!, password.value!!)
            loggingResult(result)
        }

    }
}