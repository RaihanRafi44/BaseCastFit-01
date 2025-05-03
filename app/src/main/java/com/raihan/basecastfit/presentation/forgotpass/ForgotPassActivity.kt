package com.raihan.basecastfit.presentation.forgotpass

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.raihan.basecastfit.R
import com.raihan.basecastfit.databinding.ActivityForgotPassBinding
import com.raihan.basecastfit.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPassBinding
    private val forgotPassViewModel: ForgotPassViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupForm()

        binding.btnForgotPassword.setOnClickListener {
            val email = binding.layoutFpass.etEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                forgotPassViewModel.requestForgotPassword(email)
            }
        }

        observeForgotPasswordResult()
    }

    private fun setupForm() {
        with(binding.layoutFpass) {
            tilEmail.isVisible = true
        }
    }

    private fun observeForgotPasswordResult() {
        forgotPassViewModel.loginResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.pbLoadingFp.isVisible = false
                    binding.btnForgotPassword.isVisible = true
                    showDialog("Kami telah mengirimkan link reset password ke email Anda")
                },
                doOnError = {
                    binding.pbLoadingFp.isVisible = false
                    binding.btnForgotPassword.isVisible = true
                    Toast.makeText(
                        this,
                        "Login Failed : ${it.exception?.message.orEmpty()}",
                        Toast.LENGTH_SHORT,
                    ).show()
                },
                doOnLoading = {
                    binding.pbLoadingFp.isVisible = true
                    binding.btnForgotPassword.isVisible = false
                },
            )
        }
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Berhasil")
            .setMessage(message)
            .setPositiveButton("Oke") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
