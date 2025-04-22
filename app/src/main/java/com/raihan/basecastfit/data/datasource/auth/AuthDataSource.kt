package com.raihan.basecastfit.data.datasource.auth

import kotlin.jvm.Throws

interface AuthDataSource {
    @Throws(exceptionClasses = [Exception::class])
    suspend fun doLogin(
        email: String,
        password: String
    ): Boolean

    @Throws(exceptionClasses = [Exception::class])
    suspend fun doRegister(
        email: String,
        fullName: String,
        password: String
    ): Boolean

    suspend fun updateProfile(fullName: String? = null, photoUrl: String? = null): Boolean

    suspend fun updatePassword(newPassword: String): Boolean

    suspend fun updateEmail(newEmail: String): Boolean

    fun requestChangePasswordByEmail(): Boolean

    fun doLogout(): Boolean

    fun isLoggedIn(): Boolean

    //fun getCurrentUser(): User?
}

class FirebaseAuthDataSource {
}