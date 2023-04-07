package com.leandrolcd.dogedexmvvm.ui.authentication.utilities

sealed class Routes(val route:String) {
    object ScreenLogin:Routes("LoginScreen")
    object ScreenSignUp:Routes("SignUpScreen")
}
