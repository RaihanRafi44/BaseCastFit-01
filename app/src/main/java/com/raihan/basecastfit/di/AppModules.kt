package com.raihan.basecastfit.di

import com.google.firebase.auth.FirebaseAuth
import com.raihan.basecastfit.data.datasource.auth.AuthDataSource
import com.raihan.basecastfit.data.datasource.auth.FirebaseAuthDataSource
import com.raihan.basecastfit.data.datasource.location.LocationDataSource
import com.raihan.basecastfit.data.datasource.weather.WeatherDataSource
import com.raihan.basecastfit.data.datasource.weather.WeatherDataSourceImpl
import com.raihan.basecastfit.data.repository.LocationRepository
import com.raihan.basecastfit.data.repository.UserRepository
import com.raihan.basecastfit.data.repository.UserRepositoryImpl
import com.raihan.basecastfit.data.repository.WeatherRepository
import com.raihan.basecastfit.data.repository.WeatherRepositoryImpl
import com.raihan.basecastfit.data.source.firebase.FirebaseService
import com.raihan.basecastfit.data.source.firebase.FirebaseServiceImpl
import com.raihan.basecastfit.data.source.network.service.CastFitApiService
import com.raihan.basecastfit.presentation.forgotpass.ForgotPassViewModel
import com.raihan.basecastfit.presentation.home.HomeViewModel
import com.raihan.basecastfit.presentation.login.LoginViewModel
import com.raihan.basecastfit.presentation.main.MainViewModel
import com.raihan.basecastfit.presentation.profile.ProfileViewModel
import com.raihan.basecastfit.presentation.register.RegisterViewModel
import com.raihan.basecastfit.presentation.splashscreen.SplashScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {
    private val networkModule =
        module{
            single<CastFitApiService> { CastFitApiService.invoke() }
        }

    private val firebaseModule =
        module{
            single<FirebaseAuth> { FirebaseAuth.getInstance() }
            single<FirebaseService> { FirebaseServiceImpl(get()) }
        }

    private val localModule =
        module{
            single { com.google.gson.Gson() }
        }

    private val dataSource =
        module{
            single<AuthDataSource> { FirebaseAuthDataSource(get()) }
            single<LocationDataSource> { LocationDataSource(get(), get())}
            single<WeatherDataSource> { WeatherDataSourceImpl(get()) }
        }

    private val repository =
        module{
            single<UserRepository> { UserRepositoryImpl(get()) }
            single<LocationRepository> { LocationRepository(get()) }
            single<WeatherRepository> { WeatherRepositoryImpl(get()) }
        }

    private val viewModel =
        module{
            viewModelOf(::SplashScreenViewModel)
            viewModelOf(::LoginViewModel)
            viewModelOf(::RegisterViewModel)
            viewModelOf(::MainViewModel)
            viewModelOf(::ProfileViewModel)
            viewModelOf(::HomeViewModel)
            viewModelOf(::ForgotPassViewModel)
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