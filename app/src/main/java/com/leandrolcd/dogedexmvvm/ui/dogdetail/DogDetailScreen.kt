package com.leandrolcd.dogedexmvvm.ui.dogdetail


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.SubcomposeAsyncImage
import com.leandrolcd.dogedexmvvm.ui.model.Dog
import com.leandrolcd.dogedexmvvm.R
import com.leandrolcd.dogedexmvvm.ui.authentication.utilities.GifScreen
import com.leandrolcd.dogedexmvvm.ui.model.DogRecognition
import com.leandrolcd.dogedexmvvm.ui.model.UiStatus

@ExperimentalCoilApi
@Composable
fun DogDetailScreen(navigationController: NavHostController, isRecognition: Boolean,dogId: String, viewModel: DogDetailViewModel = hiltViewModel())
{


val status = viewModel.uiStatus
    when(status.value){
        is UiStatus.Error -> {
            Log.d("TAG", "DogDetailScreen: ${(status.value as UiStatus.Error<Dog>).message}")
        }
        is UiStatus.Loaded -> {
            Log.d("TAG", "DogDetailScreen: Loaded")
            viewModel.getDogById(dogId)
        }
        is UiStatus.Loading -> {
            Log.d("TAG", "DogDetailScreen: Loading")
            GifScreen(R.drawable.ic_loading)
        }
        is UiStatus.Success -> {
            Log.d("TAG", "DogDetailScreen: Success")
            viewModel.dogStatus.value?.let { DogConten(dog = it) }
        }
    }
}

@Composable
fun DogConten(dog:Dog){
    Log.d("TAG", "DogDetailScreen: $dog")
        Box(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            DogInformation(dog)
            SubcomposeAsyncImage(
                model = dog.imageUrl,
                loading = {
                    Box(contentAlignment = Alignment.Center){
                        CircularProgressIndicator(modifier = Modifier
                            .height(45.dp)
                            .width(45.dp))
                    }
                },
                contentDescription = "Dog image",
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .padding(top = 40.dp)
            )


            FloatingActionButton(
                onClick = {
                    //Agrega dog y navega
                },
                modifier = Modifier.align(Alignment.BottomCenter),
                backgroundColor = Color.Blue
            ) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = "")
            }



        }

}

@Composable
fun DogInformation(dog: Dog) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 156.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "# ${dog.index}",
                    fontSize = 24.sp,
                    color = Color.Black,
                    textAlign = TextAlign.End
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = dog.name.toString(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                IconBarra(Modifier.width(200.dp))
                Text(
                    text = "${dog.lifeExpectancy} years",
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.End
                )
                Text(
                    modifier = Modifier.padding(8.dp), text = dog.temperament.toString(),
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.End
                )
                Divider(
                    modifier = Modifier.padding(
                        top = 8.dp,
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 16.dp
                    ),
                    color = Color.Gray,
                    thickness = 1.dp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ColumnDetail(sex = Sex.Female, dog = dog, Modifier.weight(1f))
                    VerticalDivider()
                    Column(
                        Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = dog.raza.toString(),
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Group",
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            modifier = Modifier.padding(8.dp)

                        )
                    }
                    VerticalDivider()
                    ColumnDetail(sex = Sex.Male, dog = dog, Modifier.weight(1f))

                }
            }

        }
    }
}

@Composable
fun VerticalDivider() {
    Divider(
        modifier = Modifier
            .height(72.dp)
            .width(1.dp), Color.Gray
    )
}

@Composable
fun ColumnDetail(sex: Sex, dog: Dog, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = when (sex) {
                Sex.Male -> "Male"
                Sex.Female -> "Female"
            },
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier.padding(8.dp)

        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = when (sex) {
                Sex.Male -> dog.weightMale.toString()
                Sex.Female -> dog.weightFemale.toString()
            },
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Text(
            text = "Weigh",
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier.padding(8.dp)

        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = when (sex) {
                Sex.Male -> dog.heightFemale.toString()
                Sex.Female -> dog.heightMale.toString()
            },
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Text(
            text = "Height",
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier.padding(8.dp)

        )

    }
}

enum class Sex {
    Male, Female
}

@Composable
fun IconBarra(modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(topEnd = 2.dp, bottomEnd = 2.dp)),
            color = Color.Red
        ) {}

        Surface(
            shape = CircleShape,
            color = Color.Red,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp)
                    .padding(4.dp),
                imageVector = Icons.Default.Favorite,
                contentDescription = "Experanza de vida",
                tint = Color.White
            )
        }

    }
}

@Preview
@Composable
fun Preview() {

    // DogListScreen()
    //  IconBarra()
}
