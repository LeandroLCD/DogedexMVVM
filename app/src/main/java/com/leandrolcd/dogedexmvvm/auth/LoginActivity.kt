package com.leandrolcd.dogedexmvvm.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.findNavController
import com.leandrolcd.dogedexmvvm.main.MainActivity
import com.leandrolcd.dogedexmvvm.R
import com.leandrolcd.dogedexmvvm.auth.model.User
import com.leandrolcd.dogedexmvvm.databinding.ActivityLoginBinding
import com.leandrolcd.dogedexmvvm.ui.authentication.utilities.UiStatus

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
class LoginActivity : AppCompatActivity(), LoginFragment.LoginFragmentActions,
    SignUpFragment.SignUpFragmentActions {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.uiState.observe(this) { status ->
            when (status) {
                is UiStatus.Error -> {
                    binding.loadingWheel.visibility = View.GONE
                    showErrorDialog(status.message)
                }
                is UiStatus.Loading -> binding.loadingWheel.visibility = View.VISIBLE
                is UiStatus.Success -> binding.loadingWheel.visibility = View.GONE
                else -> {}
            }
        }

        viewModel.user.observe(this){
            user ->
            if(user!= null){
                User.setLoggedInUser(this, user) // guarda las credenciales del login
                startMainActivity()
            }
        }

    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish() //destruye el activitylogin para evitar el retorno
    }

    override fun onRegisterClick() {

        findNavController(R.id.nav_host_fragment)
            .navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
    }

    override fun onLoginFieldsValidated(email: String, password: String) {
        viewModel.login(email, password)
    }

    override fun onSignUpFieldsValidated(
        email: String,
        password: String,
        passwordConfirmation: String
    ) {
        viewModel.signUp(
            email = email,
            password = password,
            passwordConfirmation = passwordConfirmation
        )
    }

    private fun showErrorDialog(message: String){
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("ok"){
                _, _ -> /** Dismiss Dialog **/
            }
            .create()
            .show()
    }
}