package com.leandrolcd.dogedexmvvm.controls

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.leandrolcd.dogedexmvvm.Dog
import com.leandrolcd.dogedexmvvm.R

@ExperimentalCoilApi
@Composable
fun DogListScreen() {
    Box(
        modifier = Modifier
            .background(Color.Blue)
            .padding(horizontal = 8.dp, vertical = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        val dog = Dog(
            1,
            1,
            "Perro",
            "Dog",
            "12",
            "15",
            "https://t1.uc.ltmcdn.com/es/posts/1/4/3/como_saber_si_mi_caniche_es_puro_caracteristicas_y_rasgos_49341_0_600.jpg",
            "15",
            "bravo",
            "20",
            "22"
        )
        DogInformation(dog)
        Image(
            painter = rememberImagePainter(dog.imageUrl),
            contentDescription = "Dog image ${dog.name}",
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .padding(top = 40.dp)
        )
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
                    text = "# ${dog.lifeExpectancy}",
                    fontSize = 24.sp,
                    color = Color.Black,
                    textAlign = TextAlign.End
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = dog.name,
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
                    modifier = Modifier.padding(8.dp), text = dog.temperament,
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
                            text = dog.type,
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
                Sex.Male -> dog.weightMale
                Sex.Female -> dog.weightFemale
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
                Sex.Male -> dog.heightFemale
                Sex.Female -> dog.heightMale
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
                ){}

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
                contentDescription = "Experanza de vida"
            )
        }

    }
}

@Preview
@Composable
fun Preview() {
    DogListScreen()
    //  IconBarra()
}
