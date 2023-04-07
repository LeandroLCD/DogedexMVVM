package com.leandrolcd.dogedexmvvm.dogdetail

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import coil.annotation.ExperimentalCoilApi
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import com.leandrolcd.dogedexmvvm.R
import com.leandrolcd.dogedexmvvm.controls.DogDetailScreen
import com.leandrolcd.dogedexmvvm.dogdetail.ui.theme.DogedexMVVMTheme
import com.leandrolcd.dogedexmvvm.ui.authentication.utilities.UiStatus

@ExperimentalCoilApi
class DogDetailComposeActivity : ComponentActivity() {
    companion object {
        const val DOG_KEY = "dog"
        const val IS_RECOGNITION_KEY = "is_recognition"
    }
    private val viewModel:DogDetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dog = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(DOG_KEY, Dog::class.java)

        } else {
            intent.getParcelableExtra<Dog>(DOG_KEY)
        }

        val isRecognition = intent?.extras?.getBoolean(IS_RECOGNITION_KEY,false) ?: false

        if (dog == null) {
            Toast.makeText(this, getString(R.string.dog_not_found), Toast.LENGTH_LONG).show()
            finish()
            return
        }

        setContent {
            val status = viewModel.composeStatus.value
            if(status is UiStatus.Success){
                finish()
            }else{
                DogedexMVVMTheme {

                    DogDetailScreen(dog =dog,
                        status = status, onErrorDialogDismiss =  { onErrorDialogDismiss() },
                        onClickAction = {
                        onClickAction(dog.id, isRecognition)
                    })
                }
            }

        }
    }

    private fun onErrorDialogDismiss(){
        viewModel.resetUiState()
    }

    private fun onClickAction(id: Long, isRecognition: Boolean) {
        if(isRecognition){
            viewModel.addDogToUser(id)
        }else{
            finish()
        }

    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DogedexMVVMTheme {
        Greeting("Android")
    }
}