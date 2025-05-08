package com.raihan.basecastfit.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import coil.load
import com.google.android.gms.location.LocationServices
import com.raihan.basecastfit.R
import com.raihan.basecastfit.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModel()

    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private val geocoder by lazy { Geocoder(requireContext()) }

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation()
        } else {
            Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        showUserData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        showUserData()
        observeCurrentLocation()
        homeViewModel.loadSavedLocation()
    }

    private fun showUserData() {
        homeViewModel.getCurrentUser()?.let { user ->
            binding.homeProfile.textUsernameHome.text =
                getString(R.string.text_username_login, user.fullName)
        }
    }

    private fun observeCurrentLocation() {
        homeViewModel.currentLocation.observe(viewLifecycleOwner) { uiState ->
            when {
                uiState.error != null -> {
                    Toast.makeText(requireContext(), uiState.error, Toast.LENGTH_SHORT).show()
                }
                uiState.currentLocation != null -> {
                    val locationName = uiState.currentLocation.location
                    binding.homeWeather.textCurrentLocation.text = locationName
                }
            }
        }

        binding.btnCurrentLocation.setOnClickListener {
            proceedWithCurrentLocation()
        }
    }


    private fun getCurrentLocation() {
        homeViewModel.fetchLocation(fusedLocationProviderClient, geocoder)
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun proceedWithCurrentLocation(){
        if (isLocationPermissionGranted()) {
            getCurrentLocation()
        } else {
            requestLocationPermission()
        }
    }


}