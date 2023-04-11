package com.leandrolcd.dogedexmvvm.ui.authentication.utilities

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.leandrolcd.dogedexmvvm.ui.ui.theme.backGroudColor
import com.leandrolcd.dogedexmvvm.ui.ui.theme.backGroupTextField
import com.leandrolcd.dogedexmvvm.ui.ui.theme.primaryColor
import com.leandrolcd.dogedexmvvm.ui.ui.theme.textColor

@Composable
fun MyButton(label: String, isButtonEnabled: Boolean, onClickSignUp: () -> Unit) {

    Button(
        onClick = { onClickSignUp() },
        modifier = Modifier.width(150.dp),
        contentPadding = PaddingValues(4.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = backGroudColor,
            contentColor = textColor,
            disabledContentColor = backGroupTextField
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 8.dp,
            pressedElevation = 2.dp,
            disabledElevation = 4.dp
        ),
        enabled = isButtonEnabled
    ) {
        Text(text = label)
    }
}

@Composable
fun MyIcon(image: ImageVector) {
    Icon(imageVector = image, contentDescription = "Icons")
}

@Composable
fun EmailFields(
    label: String,
    text: String,
    onValueChange: (String) -> Unit,
    icons: @Composable () -> Unit,
) {
    TextField(
        placeholder = { Text(text = label, color = textColor) }, value = text,
        onValueChange = { onValueChange(it) },
        leadingIcon = icons,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = textColor,
            disabledLabelColor = Color.Gray,
            errorBorderColor = Color.Red,
            focusedBorderColor = primaryColor,
            unfocusedBorderColor = Color.Transparent,
            backgroundColor = backGroupTextField.toAlpha(0.6f)
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(bottom = 12.dp)


    )
}

private fun Color.toAlpha(alpha: Float): Color {
    return Color(this.red, this.green, this.blue, alpha)
}

@Composable
fun PasswordFields(
    label: String,
    text: String,
    onValueChange: (String) -> Unit,
    icons: @Composable () -> Unit,
    action:ImeAction = ImeAction.Done,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    var passwordVisibility by remember {
        mutableStateOf(false)
    }


    TextField(
        placeholder = { Text(text = label, color = textColor) },
        value = text,
        onValueChange = { onValueChange(it) },
        visualTransformation = if (passwordVisibility) {
            VisualTransformation.None
        } else {
            visualTransformation
        },

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = action
        ),
        leadingIcon = icons,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = textColor,
            disabledLabelColor = Color.Gray,
            errorBorderColor = Color.Red,
            focusedBorderColor = primaryColor,
            unfocusedBorderColor = Color.Transparent,
            backgroundColor = backGroupTextField.toAlpha(0.6f)
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(bottom = 12.dp),
        trailingIcon = {
            val img = if (passwordVisibility) {
                Icons.Outlined.Visibility
            } else {
                Icons.Outlined.VisibilityOff
            }
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(imageVector = img, contentDescription = "Visibility Password")
            }
        }


    )
}
