package com.raihan.basecastfit.di

import com.google.firebase.auth.FirebaseAuth
import com.raihan.basecastfit.data.datasource.auth.AuthDataSource
import com.raihan.basecastfit.data.datasource.auth.FirebaseAuthDataSource
import com.raihan.basecastfit.data.repository.UserRepository
import com.raihan.basecastfit.data.repository.UserRepositoryImpl
import com.raihan.basecastfit.data.source.firebase.FirebaseService
import com.raihan.basecastfit.data.source.firebase.FirebaseServiceImpl
import com.raihan.basecastfit.presentation.splashscreen.SplashScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {
    private val networkModule =
        module{
        }

    private val firebaseModule =
        module{
            single<FirebaseAuth> { FirebaseAuth.getInstance() }
            single<FirebaseService> { FirebaseServiceImpl(get()) }
        }

    private val localModule =
        module{

        }

    private val dataSource =
        module{
            single<AuthDataSource> { FirebaseAuthDataSource(get()) }
        }

    private val repository =
        module{
            single<UserRepository> { UserRepositoryImpl(get()) }
        }

    private val viewModel =
        module{
            viewModelOf(::SplashScreenViewModel)
        }

    val modules =
        listOf<Module>(
            networkModule,
            localModule,
            dataSource,
            repository,
            viewModel,
            firebaseModule,
        )
}