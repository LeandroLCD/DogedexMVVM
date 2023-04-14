package com.leandrolcd.dogedexmvvm.ui.doglist

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraIndoor
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.leandrolcd.dogedexmvvm.R
import com.leandrolcd.dogedexmvvm.opacity
import com.leandrolcd.dogedexmvvm.ui.authentication.utilities.LoadingScreen
import com.leandrolcd.dogedexmvvm.ui.camera.CameraScream
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import com.leandrolcd.dogedexmvvm.ui.model.Routes
import com.leandrolcd.dogedexmvvm.ui.model.UiStatus
import com.leandrolcd.dogedexmvvm.ui.ui.theme.backGroudColor
import com.leandrolcd.dogedexmvvm.ui.ui.theme.primaryColor

@RequiresApi(Build.VERSION_CODES.R)
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@Composable
fun DogListScreen(
    navHostController: NavHostController,
    viewModel: DogListViewModel = hiltViewModel()
) {
    val status = viewModel.uiStatus.value
    when (status) {
        is UiStatus.Error -> TODO()
        is UiStatus.Loaded -> TODO()
        is UiStatus.Loading -> {
            LoadingScreen()
        }
        is UiStatus.Success -> {

            DogListContent(navHostController,status.data ) {
                navHostController.navigate(
                    Routes.ScreenDogDetail.routeName(false, it.id)
                )
            }
        }
    }

}


@RequiresApi(Build.VERSION_CODES.R)
@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalMaterialApi
@Composable
fun DogListContent(navHostController: NavHostController,
    dogList: List<Dog>,
    onItemSelected: (Dog) -> Unit,

    ) {
    var index by rememberSaveable {
        mutableStateOf(0)
    }
    Scaffold(topBar = {
        MyTopBar()
    },
    bottomBar = {
        MyBottomBar(index){
            index = it
        }
    }) {

        if(index == 0){
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize(),
                content = {
                    items(dogList) {
                        ItemDog(it, onItemSelected)
                    }
                })
        }else{
            CameraScream(navigationController = navHostController)
        }

    }
}

@ExperimentalMaterialApi
@Composable
fun ItemDog(dog: Dog, onItemSelected: (Dog) -> Unit) {
    if (dog.inCollection) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .height(100.dp)
                .width(100.dp),
            onClick = {
                onItemSelected(dog)
            },
            shape = RoundedCornerShape(16.dp)
        ) {
            SubcomposeAsyncImage(contentDescription = "Dog Image",
                modifier = Modifier.background(Color.White),
                model = dog.imageUrl,
                loading = {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .height(45.dp)
                                .width(45.dp)
                        )
                    }
                })
        }

    } else {
        Card(
            modifier = Modifier
                .height(100.dp)
                .width(100.dp),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = primaryColor.opacity(0.5f)
        ) {
            Text(text = "#${dog.index}", Modifier.padding(horizontal = 8.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_dog),
                contentDescription = "Dog Image"
            )
        }
    }

}

@Composable
fun MyTopBar() {
    TopAppBar(
        title = { Text(text = stringResource(R.string.title_top_bar)) },
        backgroundColor = primaryColor,
        contentColor = Color.Black,
        elevation = 8.dp

    )
}


@Composable
fun MyBottomBar(index:Int, onClickSelect:(Int)->Unit) {
    BottomAppBar(backgroundColor = backGroudColor,
        contentColor = Color.Black,
        elevation = 8.dp) {
        BottomNavigationItem(selected = index == 0, onClick = { onClickSelect(0) }, icon = {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "Dog"
            )
        }, label = {
            Text(
                text = "Home"
            )
        })
        BottomNavigationItem(selected = index == 1, onClick = { onClickSelect(1) }, icon = {
            Icon(
                imageVector = Icons.Default.CameraIndoor,
                contentDescription = "Camera"
            )
        }, label = {
            Text(
                text = "Camera"
            )
        })
    }
}
