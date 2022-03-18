package com.example.animation


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateRect
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    var ok by remember {
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = { ok = !ok }) {
                            Text(text = "Click Me to see ${ok}")
                        }
                        AnimatedVisibility(
                            visible = ok,

                            ) {

                            Column(
                                modifier = Modifier
                                    .padding(10.dp)
                                    ,
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
                        Box(modifier=Modifier.size(100.dp).background(color.value))

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