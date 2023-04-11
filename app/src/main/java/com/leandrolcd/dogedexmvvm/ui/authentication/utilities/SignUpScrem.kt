package com.leandrolcd.dogedexmvvm.ui.authentication.utilities

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Key
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.leandrolcd.dogedexmvvm.R
import com.leandrolcd.dogedexmvvm.ui.authentication.SignUpViewModel
import com.leandrolcd.dogedexmvvm.ui.ui.theme.primaryColor
import kotlinx.coroutines.delay
import kotlin.math.floor


@Composable
fun SignUpScreen(navigationController: NavHostController, viewModel: SignUpViewModel = hiltViewModel()) {

    when (val status = viewModel.uiStatus.value) {
        is UiStatus.Error -> TODO()
        is UiStatus.Loaded -> {
            CardSignUp(viewModel, navigationController)
        }
        is UiStatus.Loading -> GifScreen(R.drawable.ic_loading)
        is UiStatus.Success -> {
            LaunchedEffect(true) {
                delay(1000)
                navigationController.popBackStack()
                navigationController.navigate(Routes.ScreamCamera.route)
            }

        }
    }


}


@Composable
fun CardSignUp(viewModel: SignUpViewModel, navigationController: NavHostController) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = floor(configuration.screenWidthDp * 0.85).toInt()
    val screenHeightDp = floor(configuration.screenHeightDp * 0.96).toInt()
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier
            .fillMaxSize()
            .background(primaryColor)

    ) {
        Card(
            shape = RoundedCornerShape(topStart = 30.dp, bottomStart = 30.dp),

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
                Row {
                    IconButton(onClick = { navigationController.navigate(Routes.ScreenLogin.route) }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBackIos,
                            contentDescription = "Back to Login"
                        )
                    }
                    MyHeader()
                }

                MyBody(viewModel)
                MyFooter(viewModel)
            }

        }
    }
}

@Composable
fun MyFooter(viewModel: SignUpViewModel) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        MyButton("Sign Up", viewModel.isButtonEnabled.value) { viewModel.onSignUpClicked() }
    }
}


@Composable
fun MyBody(viewModel: SignUpViewModel) {
    Column(
        Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val email = viewModel.email.value
        val password = viewModel.password.value
        val passwordConfirmation = viewModel.passwordConfirmation.value
        EmailFields(
            label = "Email",
            text = email,
            icons = { MyIcon(Icons.Default.Email) },
            onValueChange = { viewModel.onSignUpChanged(it, password, passwordConfirmation) })
        PasswordFields(
            label = "Password",
            text = password,
            icons = { MyIcon(Icons.Outlined.Key) },
            onValueChange = { viewModel.onSignUpChanged(email, it, passwordConfirmation) },
            action = ImeAction.Next,
            visualTransformation = PasswordVisualTransformation('*')
        )
        PasswordFields(
            label = "Confirm Password",
            text = passwordConfirmation,
            icons = { MyIcon(Icons.Outlined.Key) },
            onValueChange = { viewModel.onSignUpChanged(email, password, it) },
            visualTransformation = PasswordVisualTransformation('*')
        )
    }
}


@Composable
fun MyHeader() {
    val activity = LocalContext.current as Activity
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Icon",
            modifier = Modifier
                .fillMaxSize()
        )
        IconButton(
            onClick = { activity.finish() }, Modifier
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Exits App"
            )
        }

    }
}
