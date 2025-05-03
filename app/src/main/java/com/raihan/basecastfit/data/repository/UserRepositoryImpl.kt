package com.raihan.basecastfit.data.repository

import com.raihan.basecastfit.data.datasource.auth.AuthDataSource
import com.raihan.basecastfit.data.model.User
import com.raihan.basecastfit.utils.ResultWrapper
import com.raihan.basecastfit.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val dataSource: AuthDataSource,
) :
    UserRepository {
    override fun doLogin(
        email: String,
        password: String,
    ): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.doLogin(email, password) }
    }

    override fun doRegister(
        email: String,
        fullName: String,
        password: String,
    ): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.doRegister(email, fullName, password) }
    }

    override fun updateProfile(fullName: String?): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.updateProfile(fullName = fullName) }
    }

    override fun updatePassword(newPassword: String): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.updatePassword(newPassword) }
    }

    override fun updateEmail(newEmail: String): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.updateEmail(newEmail) }
    }

    override fun requestChangePasswordByEmail(): Boolean {
        return dataSource.requestChangePasswordByEmail()
    }

    override fun doLogout(): Boolean {
        return dataSource.doLogout()
    }

    override fun isLoggedIn(): Boolean {
        return dataSource.isLoggedIn()
    }

    override fun getCurrentUser(): User? {
        return dataSource.getCurrentUser()
    }

    override fun requestForgotPassword(email: String): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.requestForgotPassword(email) }
    }

}