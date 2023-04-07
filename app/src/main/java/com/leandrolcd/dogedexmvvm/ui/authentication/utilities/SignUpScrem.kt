package com.leandrolcd.dogedexmvvm.ui.authentication.utilities

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.leandrolcd.dogedexmvvm.R
import com.leandrolcd.dogedexmvvm.ui.authentication.LoadingScreen
import com.leandrolcd.dogedexmvvm.ui.authentication.SignUpViewModel
import com.leandrolcd.dogedexmvvm.ui.ui.theme.primaryColor
import kotlin.math.floor


@Composable
fun SignUpScreen(viewModel: SignUpViewModel, navigationController: NavHostController) {

    when(val status = viewModel.uiStatus.value)
    {
        is UiStatus.Error -> TODO()
        is UiStatus.Loaded -> {
            LoadingScreen()
            //CardSignUp(viewModel, navigationController)
        }
        is UiStatus.Loading -> LoadingScreen()
        is UiStatus.Success -> TODO()
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

    ){
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
                myHeader()
            }

            myBody(viewModel)
            myFooter(viewModel)
        }

    }
}
}

@Composable
fun myFooter(viewModel: SignUpViewModel) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        myButton("Sign Up", viewModel.isButtonEnabled.value) { viewModel.onSignUpClicked()}
    }
}


@Composable
fun myBody(viewModel: SignUpViewModel) {
    Column(
        Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
            val email = viewModel.email.value
        val password = viewModel.password.value
        val passwordConfirmation = viewModel.passwordConfirmation.value
        TextFields(
            label = "Email",
            text = email,
            icons = { MyIcon(Icons.Default.Email) },
            onValueChange = {viewModel.onSignUpChanged(it, password,passwordConfirmation)})
        PasswordFields(
            label = "Password",
            text = password,
            icons = { MyIcon(Icons.Outlined.Key) },
            onValueChange = {viewModel.onSignUpChanged(email, it,passwordConfirmation)},
            visualTransformation = PasswordVisualTransformation('*')
        )
        PasswordFields(
            label = "Confirm Password",
            text = passwordConfirmation,
            icons = { MyIcon(Icons.Outlined.Key) },
            onValueChange = {viewModel.onSignUpChanged(email, password, it)},
            visualTransformation = PasswordVisualTransformation('*')
        )
    }
}


@Composable
fun myHeader() {
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
        IconButton(onClick = { activity.finish() }, Modifier
            .align(Alignment.TopEnd)) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Exits App"
            )
        }

    }
}
