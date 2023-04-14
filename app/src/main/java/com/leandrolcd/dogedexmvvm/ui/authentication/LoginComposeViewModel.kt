package com.leandrolcd.dogedexmvvm.ui.authentication

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.leandrolcd.dogedexmvvm.api.models.isNull
import com.leandrolcd.dogedexmvvm.domain.AuthLoginUseCase
import com.leandrolcd.dogedexmvvm.domain.AuthLoginWithGoogleUseCase
import com.leandrolcd.dogedexmvvm.ui.model.UiStatus
import com.leandrolcd.dogedexmvvm.ui.model.LoginUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginComposeViewModel @Inject constructor(
    private val authLoginWithGoogleUseCase: AuthLoginWithGoogleUseCase,
    private val authLoginUseCase: AuthLoginUseCase
): ViewModel() {


    //region Properties
    var email = mutableStateOf<String>("")
    private set

    var userCurrent = mutableStateOf<FirebaseUser?>(null)
        private set

    var password = mutableStateOf("")
        private set

    var isButtonEnabled = mutableStateOf(false)
        private set

    var uiStatus = mutableStateOf<UiStatus<Any>>(UiStatus.Loaded())
    private set
    //endregion
    init {
        // Asegurarse de que userCurrent esté inicializado
        chekedUserCurren()
    }

    //region Methods
    fun onLoginChange(_email:String, pwd:String){
         email.value = _email
        password.value = pwd
        isButtonEnabled.value = enabledLogin(email = _email, password = pwd)
    }

    fun onLoginClicked(){
        uiStatus.value = UiStatus.Loading()
        viewModelScope.launch {

            uiStatus.value = authLoginUseCase.invoke(LoginUser(email.value,password.value))

        }
    }
    fun onLoginWithGoogle(idToken: String) {
        uiStatus.value = UiStatus.Loading()
        viewModelScope.launch {
            delay(1000)
            uiStatus.value = authLoginWithGoogleUseCase.invoke(idToken)

        }
    }



    private fun enabledLogin(email:String, password:String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length > 5

    fun onTryAgain() {
        uiStatus.value = UiStatus.Loaded()
    }

    private fun chekedUserCurren() {
        // Asegurarse de que authLoginUseCase esté inicializado y que getUser() devuelva un objeto válido
        authLoginUseCase?.getUser()?.let { user ->
            userCurrent.value = user
            uiStatus.value = UiStatus.Success(true)
        }
    }
    //region



}