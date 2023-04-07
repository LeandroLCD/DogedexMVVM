package com.leandrolcd.dogedexmvvm.ui.authentication

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseOptions
import com.leandrolcd.dogedexmvvm.domain.AuthLoginUseCase
import com.leandrolcd.dogedexmvvm.domain.AuthLoginWithGoogleUseCase
import com.leandrolcd.dogedexmvvm.ui.authentication.utilities.UiStatus
import com.leandrolcd.dogedexmvvm.ui.model.LoginUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginComposeViewModel @Inject constructor(
    private val authLoginWithGoogleUseCase: AuthLoginWithGoogleUseCase,
    private val authLoginUseCase: AuthLoginUseCase,
    private val context: Context
): ViewModel() {
    init {
        Log.d("log", "dogedexIni")
        var user = authLoginUseCase.getUser()
        if(user != null){
            Log.d("log", "Usuario id: ${user.uid}, name ${user.displayName}")
            //navega a la app
        }
    }

    //region Fields
    private var googleToken: String? = null
    val googleAppId = FirebaseOptions.fromResource(context)?.apiKey
    //endregion

    //region Properties
    var email = mutableStateOf<String>("")
    private set

    var password = mutableStateOf("")
        private set

    var isButtonEnabled = mutableStateOf(false)
        private set

    var uiStatus = mutableStateOf<UiStatus<Any>>(UiStatus.Loaded())
    private set
    //endregion

    //region Methods
    fun onLoginChange(_email:String, pwd:String){
         email.value = _email
        password.value = pwd
        isButtonEnabled.value = enabledLogin(email = _email, password = pwd)
    }
    fun setGoogleToken(token: String) {
        googleToken = token
    }

    fun onLoginClicked(){
        uiStatus.value = UiStatus.Loading()
        viewModelScope.launch {

            uiStatus.value = authLoginUseCase.invoke(LoginUser(email.value,password.value))

        }
    }
    fun onLoginWithGoogleClicked(idToken: String) {
        viewModelScope.launch {
            uiStatus.value = authLoginWithGoogleUseCase.invoke(idToken)

        }
    }




    private fun enabledLogin(email:String, password:String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length > 5

    fun onTryAgain() {
        uiStatus.value = UiStatus.Loaded()
    }
    //region



}