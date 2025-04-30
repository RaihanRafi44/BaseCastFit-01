package com.raihan.basecastfit.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raihan.basecastfit.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class RegisterViewModel (private val repository: UserRepository) : ViewModel() {
    fun doRegister(
        email: String,
        fullName: String,
        password: String,
    ) = repository
        .doRegister(email, fullName, password)
        .asLiveData(Dispatchers.IO)
}