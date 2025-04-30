package com.raihan.basecastfit.presentation.splashscreen

import androidx.lifecycle.ViewModel
import com.raihan.basecastfit.data.repository.UserRepository

class SplashScreenViewModel (private val repository: UserRepository) : ViewModel() {
    fun isUserLoggedIn() = repository.isLoggedIn()
}