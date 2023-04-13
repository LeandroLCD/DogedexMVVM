package com.leandrolcd.dogedexmvvm.controls

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import com.leandrolcd.dogedexmvvm.R


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalMaterialApi
@Composable
fun DogListScreen(
    dogList: List<Dog>,
    onClipBack: () -> Unit,
    onItemSelected: (Dog) -> Unit,

    ) {
    Scaffold(topBar = { MyTopBar(onClipBack)

    }) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            content = {
                items(dogList) {
                    ItemDog(it, onItemSelected)
                }
            })
    }
}

@Composable
fun DogListLoading(){
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        CircularProgressIndicator()
    }
}

@ExperimentalMaterialApi
@Composable
fun ItemDog(dog: Dog, onItemSelected: (Dog) -> Unit) {
    if (dog.inCollection) {
        Card(modifier = Modifier
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
        Card(modifier = Modifier
            .padding(8.dp)
            .height(100.dp)
            .width(100.dp),
            onClick = {
                onItemSelected(dog)
            },
            shape = RoundedCornerShape(16.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_dog),
                contentDescription ="Dog Image" )
        }
    }

}

@Composable
fun MyTopBar(onClipBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.title_top_bar)) },
        backgroundColor = Color.Blue,
        contentColor = Color.Black,
        elevation = 8.dp,
        navigationIcon = {
            IconButton(onClick = { onClipBack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
            }
        }

    )
}
