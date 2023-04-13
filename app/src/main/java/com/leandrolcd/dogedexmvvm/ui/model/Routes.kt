package com.leandrolcd.dogedexmvvm.ui.model

sealed class Routes(val route:String) {
    object ScreenLogin: Routes("LoginScreen")
    object ScreenSignUp: Routes("SignUpScreen")
    object ScreenDogDetail: Routes("DogDetailScreen/{dogId}/{isRecognition}"){
        fun routeName(isRecognition: Boolean, dogId:String) = "DogDetailScreen/$dogId/$isRecognition"
    }
    object ScreamCamera: Routes("CameraScream")
}
