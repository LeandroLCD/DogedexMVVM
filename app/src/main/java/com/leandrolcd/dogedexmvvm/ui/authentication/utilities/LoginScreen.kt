package com.leandrolcd.dogedexmvvm.ui.authentication

import android.app.Activity
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.SyncProblem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.leandrolcd.dogedexmvvm.R
import com.leandrolcd.dogedexmvvm.main.MainActivity
import com.leandrolcd.dogedexmvvm.ui.authentication.utilities.*
import com.leandrolcd.dogedexmvvm.ui.ui.theme.primaryColor
import kotlin.math.floor


@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun LoginScreen(
    viewModel: LoginComposeViewModel,
    navigationController: NavHostController,
    onLoginWithGoogleClicked: () -> Unit
) {

    val activity = LocalContext.current as Activity
    when(val status =viewModel.uiStatus.value){
        is UiStatus.Error -> {
            ErrorLoginScreen(status.message){viewModel.onTryAgain()}
        }
        is UiStatus.Loaded -> {
            LoginContent(viewModel, navigationController, onLoginWithGoogleClicked)
        }
        is UiStatus.Loading -> {
            LoadingScreen()
        }
        is UiStatus.Success -> {
            activity.startActivity(Intent(activity, MainActivity::class.java))
            activity.finish()
        }
    }


}

@Composable
fun LoadingScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Log.d("LoadingScreen", "LoadingScreen: $SDK_INT")
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                if (SDK_INT >= 28) {
                    Log.d("LoadingScreen", "LoadingScreen2: $SDK_INT")
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.ic_loading, imageLoader),
                contentDescription = null,

                modifier = Modifier.scale(2f)
                    .background(Color.Red).padding(bottom = 16.dp)
            )
            LinearProgressIndicator()
            if(SDK_INT<28){
                LinearProgressIndicator()
            }
        }

    }
}

@Composable
fun LoginContent(
    viewModel: LoginComposeViewModel,
    navigationController: NavHostController,
    onLoginWithGoogleClicked: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(primaryColor)
    ) {
        myCardLogin(viewModel, navigationController, onLoginWithGoogleClicked)
    }
}

@Composable
fun myCardLogin(
    viewModel: LoginComposeViewModel,
    navigationController: NavHostController,
    onLoginWithGoogleClicked: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = floor(configuration.screenWidthDp * 0.85).toInt()
    val screenHeightDp = floor(configuration.screenHeightDp * 0.96).toInt()
    Card(
        shape = RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp),

        modifier = Modifier
            .width(screenWidthDp.dp)
            .height(screenHeightDp.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_huellas),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = floor(configuration.screenHeightDp * 0.3).toInt().dp),
            alpha = 0.2f,
        )
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            myHeader()
            BodyLogin(viewModel)
            FooterLogin(viewModel, navigationController, onLoginWithGoogleClicked)
        }

    }
}

@Composable
fun FooterLogin(
    viewModel: LoginComposeViewModel,
    navigationController: NavHostController,
    onLoginWithGoogleClicked: () -> Unit
) {
    Box(contentAlignment = BottomCenter) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            myButton(label = "Sign In", viewModel.isButtonEnabled.value) {
                viewModel.onLoginClicked()
            }
            LoginWithGoogle(Modifier) {
                onLoginWithGoogleClicked()
            }
            SignUp(Modifier.weight(1f)) {
                navigationController.navigate(Routes.ScreenSignUp.route)
            }
        }
    }

}

@Composable
fun BodyLogin(viewModel: LoginComposeViewModel) {
    val email = viewModel.email.value
    val password = viewModel.password.value

    Column(
        Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        TextFields(
            label = "Email",
            text = email,
            icons = { MyIcon(Icons.Default.Email) },
            onValueChange = {
                viewModel.onLoginChange(it, password)
            }
        )
        PasswordFields(
            label = "Password",
            text = password,
            icons = { MyIcon(Icons.Outlined.Key) },
            onValueChange = {
                viewModel.onLoginChange(email, it)
            },
            visualTransformation = PasswordVisualTransformation('*')
        )
        ForgotPassword(modifier = Modifier.align(Alignment.End))
    }
}

@Composable
fun ErrorLoginScreen(message: String, onTryAgain: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.ic_dog),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        )
        Text(text = message, softWrap = true)
        FloatingActionButton(onClick = { onTryAgain() }) {
            Icon(imageVector = Icons.Outlined.SyncProblem, contentDescription = "Try Again")
        }
    }
}


//region Body


//region Controls Body


@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(
        text = "Forgot Password?",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF4Ea8E9),
        modifier = modifier
    )
}


@Composable
fun LoginWithGoogle(modifier: Modifier, onClickAction: () -> Unit) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(top = 32.dp)
    ) {
        Row {

            myDivider(
                Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .align(CenterVertically))
            Text("Or")
            myDivider(
                Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .align(CenterVertically))

        }
        Image(painter = painterResource(id = R.drawable.ic_google),
            contentDescription = "LoginWithGoogle",
            modifier = modifier
                .width(40.dp)
                .height(40.dp)
                .clickable {
                    onClickAction()
                }
        )
    }


}

@Composable
fun myDivider(modifier: Modifier = Modifier) {
    Divider(
        color = Color.Gray,
        thickness = 1.dp,
        modifier = modifier
            .padding(vertical = 4.dp)
    )
}


//endregion

//endregion

//region Footer


@Composable
fun SignUp(
    modifier: Modifier = Modifier,
    onSignUpClicked: () -> Unit
) {
    Row(
        modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Bottom
    ) {
        Text("Don't have an account?", fontSize = 12.sp, color = Color.Gray)
        Text(
            "Sign Up",
            Modifier
                .padding(start = 8.dp)
                .clickable { onSignUpClicked() },
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4Ea8E9)
        )

    }
}
//endregion
