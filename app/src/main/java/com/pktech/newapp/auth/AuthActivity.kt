package com.pktech.newapp.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.pktech.newapp.R
import com.pktech.newapp.databinding.ActivityAuthBinding
import com.pktech.newapp.ui.dashboard.DashboardActivity

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModels()

    private val googleLauncher =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                viewModel.firebaseAuthWithGoogle(account.idToken!!)
            } catch (_: Exception) {}
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoogle.setOnClickListener { signInWithGoogle() }
        binding.btnEmail.setOnClickListener {
            viewModel.loginWithEmail(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        observeAuth()
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val client = GoogleSignIn.getClient(this, gso)
        googleLauncher.launch(client.signInIntent)
    }

    private fun observeAuth() {
        viewModel.authSuccess.observe(this) {
            if (it) {
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
        }
    }
}