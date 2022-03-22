package com.example.animation


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animation.ui.theme.AnimationTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val networkstate = ConnectionLiveData(this).observeAsState()
                    var ok by remember {
                        mutableStateOf(false)
                    }
                    var shown by remember {
                        mutableStateOf(false)
                    }
                    val density = LocalDensity.current
                    val color = remember { Animatable(Color.Gray) }
                    LaunchedEffect(ok) {
                        color.animateTo(if (ok) Color.Green else Color.Red)
                    }
                    var currentState by remember { mutableStateOf(BoxState.Collapsed) }
                    val transition = updateTransition(currentState)
                    val rect by transition.animateRect { state ->
                        when (state) {
                            BoxState.Collapsed -> Rect(0f, 0f, 100f, 100f)
                            BoxState.Expanded -> Rect(100f, 100f, 300f, 300f)
                        }
                    }
                    val borderWidth by transition.animateDp { state ->
                        when (state) {
                            BoxState.Collapsed -> 1.dp
                            BoxState.Expanded -> 0.dp
                        }
                    }
                    val shortText = "This is a short text."
                    val longtext = "Lorem Ipsum is simply dummy text of the printing" +
                            " and typesetting industry. Lorem Ipsum has been the" +
                            " industry's standard dummy text ever since the 1500s," +
                            " when an unknown printer took a galley of type and" +
                            " scrambled it to make a type specimen book."

                    val isShortText = remember { mutableStateOf(true) }

                    Column(
                        modifier = Modifier
                            .background(Color(0xFFEDEAE0))
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Button(
                            onClick = {
                                isShortText.value = !isShortText.value
                            },
                        ) {
                            Text(if (isShortText.value) "Long Text" else "Short Text")
                        }

                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFF9966))
                                .padding(16.dp)
                                .fillMaxWidth()
                                .animateContentSize()
                        ) {
                            Text(
                                text = if (isShortText.value) shortText else longtext,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily.Cursive,
                                color = Color(0xFFA52A2A)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        AnimatedVisibility(
                            visible = ok,

                            ) {

                            Column(
                                modifier = Modifier
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "Some Random Fuck")
                            }
                        }
                        Spacer(modifier = Modifier.padding(10.dp))
                        Row(
                            modifier = Modifier
                                .padding(5.dp), verticalAlignment = Alignment.CenterVertically
                        ) {
                            var count by remember { mutableStateOf(0) }
                            Button(onClick = { count++ }) {
                                Text("Add")
                            }

                        }
                        Box(modifier = Modifier
                            .size(100.dp)
                            .background(color.value))
                        Button(onClick = { shown = !shown }) {
                            Text(text = "Click Me to see ${shown}")
                        }
                        AnimatedVisibility(
                            visible = networkstate.value ?: false,
                            enter = fadeIn(
                                // customize with tween AnimationSpec
                                animationSpec = tween(
                                    durationMillis = 1000,
                                    delayMillis = 200,
                                    easing = LinearOutSlowInEasing
                                )
                            ) + slideInVertically(),
                            // you can also add animationSpec in fadeOut if need be.
                            exit = fadeOut() + shrinkHorizontally(),
                        ) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                color = Color.LightGray,
                                elevation = 4.dp
                            ) {
                                Text(
                                    text = "No Internet,Please Check your Network",
                                    modifier = Modifier.padding(10.dp),
                                    textAlign = TextAlign.Center,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                    }

                }
            }
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
    AnimationTheme {
        Greeting("Android")
    }
}