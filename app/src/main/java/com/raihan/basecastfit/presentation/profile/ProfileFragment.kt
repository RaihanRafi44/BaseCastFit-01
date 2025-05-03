package com.raihan.basecastfit.presentation.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.raihan.basecastfit.R
import com.raihan.basecastfit.databinding.FragmentProfileBinding
import com.raihan.basecastfit.presentation.login.LoginActivity
import com.raihan.basecastfit.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModel()
    private var isSaveProfileButtonEnabled: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        showUserData()
        binding.layoutProfile.etEmail.isFocusable = false
        binding.layoutProfile.etEmail.isClickable = true // agar bisa disentuh untuk trigger Toast
        binding.layoutProfile.etEmail.setOnClickListener {
            Toast.makeText(requireContext(), "Email tidak dapat diubah", Toast.LENGTH_SHORT).show()
        }
        setClickListeners()
        observeEditMode()
        binding.layoutProfile.etName.addTextChangedListener {
            isSaveProfileButtonEnabled = true
            updateSaveButtonState()
        }

    }

    private fun showUserData() {
        profileViewModel.getCurrentUser()?.let {
            binding.layoutProfile.etName.setText(it.fullName)
            binding.layoutProfile.etEmail.setText(it.email)
            updateSaveButtonState()
        }
    }

    private fun updateSaveButtonState() {
        binding.layoutProfile.btnSave.isEnabled = isSaveProfileButtonEnabled
    }

    private fun doEditProfile() {
        if (checkNameValidation()) {
            val fullName = binding.layoutProfile.etName.text.toString().trim()
            proceedEdit(fullName)
        }
    }

    private fun observeEditMode() {
        profileViewModel.isEditMode.observe(viewLifecycleOwner) {
            binding.layoutProfile.etName.isEnabled = it
            binding.layoutProfile.etEmail.isEnabled = it
        }
    }

    private fun setClickListeners(){
        binding.layoutProfile.btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        binding.layoutProfile.btnEditProfile.setOnClickListener {
            enterEditMode()
        }

        binding.layoutProfile.btnCancelEdit.setOnClickListener {
            exitEditMode()
        }

        binding.layoutProfile.btnSave.setOnClickListener {
            doEditProfile()
        }

        binding.layoutProfile.btnChangePass.setOnClickListener {
            requestChangePassword()
        }
    }

    private fun enterEditMode() {
        profileViewModel.changeEditMode()

        binding.layoutProfile.btnEditProfile.visibility = View.GONE
        binding.layoutProfile.btnCancelEdit.visibility = View.VISIBLE
        binding.layoutProfile.llChangePassSave.visibility = View.VISIBLE
        binding.layoutProfile.flBtnLogout.visibility = View.GONE
    }

    private fun exitEditMode() {
        profileViewModel.changeEditMode()

        binding.layoutProfile.btnEditProfile.visibility = View.VISIBLE
        binding.layoutProfile.btnCancelEdit.visibility = View.GONE
        binding.layoutProfile.llChangePassSave.visibility = View.GONE
        binding.layoutProfile.flBtnLogout.visibility = View.VISIBLE
    }



    private fun proceedEdit(fullName: String) {
        profileViewModel.updateProfileName(fullName = fullName).observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.layoutProfile.pbLoadingSave.isVisible = false
                    binding.layoutProfile.btnSave.isVisible = true // Make button visible again
                    isSaveProfileButtonEnabled = false // Disable saving again until a new edit happens
                    updateSaveButtonState()
                    Toast.makeText(requireContext(), "Pengubahan data profil sukses", Toast.LENGTH_SHORT).show()
                },
                doOnError = {
                    binding.layoutProfile.pbLoadingSave.isVisible = false
                    binding.layoutProfile.btnSave.isVisible = true
                    Toast.makeText(requireContext(), "Pengubahan data profil gagal", Toast.LENGTH_SHORT).show()
                },
                doOnLoading = {
                    binding.layoutProfile.pbLoadingSave.isVisible = true
                    binding.layoutProfile.btnSave.isVisible = false
                },
            )
        }
    }

    private fun requestChangePassword() {
        profileViewModel.createChangePwdRequest()
        val dialog =
            buildChangePasswordDialog(
                "Reset password akan dikirimkan ke email ${profileViewModel.getCurrentUser()?.email}. Harap periksa inbox atau folder spam anda.",
            )
        dialog.show()
    }

    private fun buildChangePasswordDialog(message: String): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage(message)
        dialogBuilder.setPositiveButton(
            "Oke",
        ) { dialog, id ->
            // Dismiss dialog on button click
        }
        return dialogBuilder.create()
    }

    private fun showLogoutConfirmationDialog() {
        val confirmationDialog = buildConfirmationDialog()
        confirmationDialog.show()
    }

    private fun buildConfirmationDialog(): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Apakah kamu ingin logout?")
        dialogBuilder.setPositiveButton(
            "Ya",
        ) { dialog, id ->
            performLogout()
            navigateToMenu()
        }
        dialogBuilder.setNegativeButton(
            "Tidak",
        ) { dialog, id ->
            // Do nothing, user cancels logout
        }
        return dialogBuilder.create()
    }

    private fun checkNameValidation(): Boolean {
        val fullName = binding.layoutProfile.etName.text.toString().trim()
        return if (fullName.isEmpty()) {
            binding.layoutProfile.tilName.isErrorEnabled = true
            binding.layoutProfile.tilName.error = getString(R.string.text_error_name_cannot_empty)
            false
        } else {
            binding.layoutProfile.tilName.isErrorEnabled = false
            true
        }
    }

    private fun performLogout() {
        profileViewModel.doLogout()
    }

    private fun navigateToMenu() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }

}