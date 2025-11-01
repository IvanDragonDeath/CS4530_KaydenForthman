package com.example.assignment4

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignment4.ui.theme.ComposeDEMOTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = (application as Assignment4App).repository
        val factory = FunFactViewModelFactory(repository)

        setContent {
            ComposeDEMOTheme {
                val vm: FunFactViewModel = viewModel(factory = factory)
                FunFactScreen(vm)
            }
        }
    }
}

@Composable
fun FunFactScreen(viewModel: FunFactViewModel) {
    val funFacts by viewModel.funFacts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = { viewModel.fetchFunFact() },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Fetch FunFact")
        }

        Text(
            "FunFact List",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Blue,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(funFacts) { fact ->
                Text(
                    text = fact.text,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
