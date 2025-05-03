package com.raihan.basecastfit.presentation.main

import androidx.lifecycle.ViewModel
import com.raihan.basecastfit.data.repository.UserRepository

class MainViewModel (
    private val userRepository: UserRepository,
) : ViewModel() {
    fun isLoggedIn() =
        userRepository
            .isLoggedIn()
}