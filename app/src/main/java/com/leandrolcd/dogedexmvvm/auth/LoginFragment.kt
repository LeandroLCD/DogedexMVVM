package com.leandrolcd.dogedexmvvm.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.leandrolcd.dogedexmvvm.R
import com.leandrolcd.dogedexmvvm.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

 interface LoginFragmentActions{
     fun onRegisterClick()
     fun onLoginFieldsValidated(email: String, password: String)

 }
    private lateinit var loginFragmentActions : LoginFragmentActions
    private lateinit var binding:FragmentLoginBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginFragmentActions = try{
            context as LoginFragmentActions
        }catch (e: java.lang.ClassCastException){
            throw java.lang.ClassCastException("$context must implement LoginFragmentActions")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater)
        binding.loginRegisterButton.setOnClickListener{
            loginFragmentActions.onRegisterClick()
        }
        binding.loginButton.setOnClickListener{
            onLoginFieldsValidated()
        }
        return binding.root
    }

    private fun onLoginFieldsValidated() {
        validateFields()
    }
    private fun validateFields() {
        binding.emailInput.error = ""
        binding.passwordInput.error = ""

        val email = binding.emailEdit.text.toString()
        if(!isValidEmail(email)){
            Log.i("email", email)
            binding.emailInput.error = getString(R.string.email_is_not_valid)
            return
        }
        val password = binding.passwordEdit.text.toString()
        if(password.isEmpty()){
            binding.passwordInput.error = getString(R.string.password_is_not_valid)
            return
        }
        loginFragmentActions.onLoginFieldsValidated(email, password)

    }

    private fun isValidEmail(email:String?):Boolean{
        return !email.isNullOrEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()

    }

}