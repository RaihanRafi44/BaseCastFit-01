package com.raihan.basecastfit.presentation.home

import androidx.lifecycle.ViewModel
import com.raihan.basecastfit.data.repository.UserRepository

class HomeViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    fun getCurrentUser() = userRepository.getCurrentUser()
}