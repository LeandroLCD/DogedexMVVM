package com.leandrolcd.dogedexmvvm.dogslist

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.leandrolcd.dogedexmvvm.controls.DogListLoading
import com.leandrolcd.dogedexmvvm.controls.DogListScreen
import com.leandrolcd.dogedexmvvm.dogdetail.DogDetailComposeActivity
import com.leandrolcd.dogedexmvvm.dogslist.ui.theme.DogedexMVVMTheme
import com.leandrolcd.dogedexmvvm.ui.authentication.utilities.UiStatus
import com.leandrolcd.dogedexmvvm.ui.model.Dog

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
class DogListComposeActivity : ComponentActivity() {
    val viewModel: DogListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogedexMVVMTheme {
                // A surface container using the 'background' color from the theme
                when (viewModel.statusCompose.value) {
                    is UiStatus.Error -> {

                    }
                    is UiStatus.Loading -> {
                        DogListLoading()
                    }
                    is UiStatus.Success -> {
                        DogListScreen(dogList = viewModel.dogComposeList.value,
                            onClipBack = { onClipBack() },
                            onItemSelected = { openDogDetail(it) })
                    }
                    else -> {}
                }


            }
        }
    }

    private fun onClipBack() {
        finish()
    }

    private fun openDogDetail(dog: Dog) {
        val intent = Intent(this, DogDetailComposeActivity::class.java) // crea el intent
        intent.putExtra(DogDetailComposeActivity.DOG_KEY, dog) // agrega el obj dog con key
        startActivity(intent)
    }
}

@Composable
fun Greeting2(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    DogedexMVVMTheme {
        Greeting2("Android")
    }
}