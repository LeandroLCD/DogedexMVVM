package com.leandrolcd.dogedexmvvm.ui.authentication

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandrolcd.dogedexmvvm.domain.SignUpUseCase
import com.leandrolcd.dogedexmvvm.ui.authentication.utilities.UiStatus
import com.leandrolcd.dogedexmvvm.ui.model.LoginUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {


    //region Properties
    var email = mutableStateOf<String>("")
        private set

    var password = mutableStateOf("")
        private set

    var passwordConfirmation = mutableStateOf("")
        private set

    var isButtonEnabled = mutableStateOf(false)
        private set

    var uiStatus = mutableStateOf<UiStatus<Any>>(UiStatus.Loaded())
        private set

    //endregion
    fun onSignUpChanged(mail: String, pwd: String, pswConf: String) {
        email.value = mail
        password.value = pwd
        passwordConfirmation.value = pswConf
        isButtonEnabled.value =
            enabledButton(email = mail, password = pwd, passwordConfirmation = pswConf)
    }

    private fun enabledButton(email: String, password: String, passwordConfirmation: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() && password == passwordConfirmation

    fun onTryAgain() {
        uiStatus.value = UiStatus.Loaded()
    }

    fun onSignUpClicked() {
        uiStatus.value = UiStatus.Loading()
        viewModelScope.launch {

            uiStatus.value = signUpUseCase.invoke(LoginUser(email.value,password.value))

        }
    }
}