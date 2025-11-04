package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.activity.viewModels

class MainActivity : ComponentActivity() {

    private val viewModel: MarbleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarbleScreen(viewModel)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.registerSensor()
    }

    override fun onPause() {
        super.onPause()
        viewModel.unregisterSensor()
    }
}

@Composable
fun MarbleScreen(viewModel: MarbleViewModel) {
    val position by viewModel.position.collectAsState()
    val marbleSize = 40.dp

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDEDED))
    ) {
        val density = LocalDensity.current
        val marbleRadiusPx = with(density) { marbleSize.toPx() / 2 }

        val screenWidthPx = with(density) { maxWidth.toPx() }
        val screenHeightPx = with(density) { maxHeight.toPx() }

        LaunchedEffect(screenWidthPx, screenHeightPx) {
            viewModel.setScreenSize(screenWidthPx, screenHeightPx)
        }

        val xOffset = with(density) { position.first.toDp() - marbleRadiusPx.toDp() }
        val yOffset = with(density) { position.second.toDp() - marbleRadiusPx.toDp() }

        Box(
            modifier = Modifier
                .offset(x = xOffset, y = yOffset)
                .size(marbleSize)
                .background(Color.Blue, CircleShape)
        )
    }
}
