package com.raihan.basecastfit.presentation.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.loadCurrentUser()
        profileViewModel.resetEditMode()
        observeEditMode()
        setupInputListeners()
        setupClickListeners()
    }

    private fun setupInputListeners() {
        binding.layoutProfile.etEmail.isFocusable = false
        binding.layoutProfile.etEmail.isClickable = true
        binding.layoutProfile.etEmail.setOnClickListener {
            Toast.makeText(requireContext(), "Email tidak dapat diubah", Toast.LENGTH_SHORT).show()
        }

        binding.layoutProfile.etName.addTextChangedListener {
            isSaveProfileButtonEnabled = true
            updateSaveButtonState()
        }

        profileViewModel.editedFullName.observe(viewLifecycleOwner) {
            binding.layoutProfile.etName.setText(it)
        }

        profileViewModel.getCurrentUser()?.let {
            binding.layoutProfile.etEmail.setText(it.email)
        }
    }

    private fun observeEditMode() {
        profileViewModel.isEditMode.observe(viewLifecycleOwner) { isEdit ->
            binding.layoutProfile.etName.isEnabled = isEdit
            binding.layoutProfile.etEmail.isEnabled = isEdit

            binding.layoutProfile.btnEditProfile.isVisible = !isEdit
            binding.layoutProfile.btnCancelEdit.isVisible = isEdit
            binding.layoutProfile.llChangePassSave.isVisible = isEdit
            binding.layoutProfile.flBtnLogout.isVisible = !isEdit
        }
    }

    private fun setupClickListeners() {
        binding.layoutProfile.btnLogout.setOnClickListener { showLogoutConfirmationDialog() }
        binding.layoutProfile.btnEditProfile.setOnClickListener { enterEditMode() }
        binding.layoutProfile.btnCancelEdit.setOnClickListener { exitEditMode() }
        binding.layoutProfile.btnSave.setOnClickListener { doEditProfile() }
        binding.layoutProfile.btnChangePass.setOnClickListener { requestChangePassword() }
    }

    private fun enterEditMode() {
        profileViewModel.changeEditMode()
        binding.layoutProfile.btnEditProfile.visibility = View.GONE
        binding.layoutProfile.btnCancelEdit.visibility = View.VISIBLE
        binding.layoutProfile.llChangePassSave.visibility = View.VISIBLE
        binding.layoutProfile.flBtnLogout.visibility = View.GONE
    }

    private fun exitEditMode() {
        profileViewModel.changeEditMode(isCancel = true)

        // Kembalikan teks ke nilai sebelumnya
        binding.layoutProfile.etName.setText(profileViewModel.editedFullName.value)

        binding.layoutProfile.btnEditProfile.visibility = View.VISIBLE
        binding.layoutProfile.btnCancelEdit.visibility = View.GONE
        binding.layoutProfile.llChangePassSave.visibility = View.GONE
        binding.layoutProfile.flBtnLogout.visibility = View.VISIBLE

        isSaveProfileButtonEnabled = false
        updateSaveButtonState()
    }

    private fun doEditProfile() {
        if (checkNameValidation()) {
            val fullName = binding.layoutProfile.etName.text.toString().trim()
            proceedEdit(fullName)
        }
    }

    private fun proceedEdit(fullName: String) {
        profileViewModel.updateProfileName(fullName = fullName)
            .observe(viewLifecycleOwner) {
                it.proceedWhen(
                    doOnSuccess = {
                        binding.layoutProfile.pbLoadingSave.isVisible = false
                        binding.layoutProfile.btnSave.isVisible = true
                        isSaveProfileButtonEnabled = false
                        updateSaveButtonState()
                        Toast.makeText(requireContext(), "Pengubahan data profil sukses", Toast.LENGTH_SHORT).show()
                        profileViewModel.loadCurrentUser() // refresh nama terbaru
                    },
                    doOnError = {
                        binding.layoutProfile.pbLoadingSave.isVisible = false
                        binding.layoutProfile.btnSave.isVisible = true
                        Toast.makeText(requireContext(), "Pengubahan data profil gagal", Toast.LENGTH_SHORT).show()
                    },
                    doOnLoading = {
                        binding.layoutProfile.pbLoadingSave.isVisible = true
                        binding.layoutProfile.btnSave.isVisible = false
                    }
                )
            }
    }

    private fun updateSaveButtonState() {
        binding.layoutProfile.btnSave.isEnabled = isSaveProfileButtonEnabled
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

    private fun requestChangePassword() {
        profileViewModel.createChangePwdRequest()
        val dialog = buildChangePasswordDialog(
            "Reset password akan dikirimkan ke email ${profileViewModel.getCurrentUser()?.email}. Harap periksa inbox atau folder spam anda."
        )
        dialog.show()
    }

    private fun buildChangePasswordDialog(message: String): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("Oke", null)
            .create()
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Apakah kamu ingin logout?")
            .setPositiveButton("Ya") { _, _ ->
                profileViewModel.doLogout()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
            .setNegativeButton("Tidak", null)
            .show()
    }
}
