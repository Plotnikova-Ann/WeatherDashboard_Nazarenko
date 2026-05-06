package com.plotnikova.weatherdashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.plotnikova.weatherdashboard.ui.theme.WeatherDashboardTheme
import com.plotnikova.weatherdashboard.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherDashboardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherDashboardScreen()
                }
            }
        }
    }
}

@Composable
fun WeatherDashboardScreen(
    viewModel: WeatherViewModel = viewModel()
) {
    val weatherState by viewModel.weatherState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Weather Dashboard",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        WeatherCard(
            emoji = "🌡️",
            title = "Temperature",
            value = weatherState.temperature?.let { "$it°C" } ?: "—",
            isLoading = weatherState.isLoading && weatherState.temperature == null
        )

        Spacer(modifier = Modifier.height(16.dp))

        WeatherCard(
            emoji = "💧",
            title = "Humidity",
            value = weatherState.humidity?.let { "$it%" } ?: "—",
            isLoading = weatherState.isLoading && weatherState.humidity == null
        )

        Spacer(modifier = Modifier.height(16.dp))

        WeatherCard(
            emoji = "🌪️",
            title = "Wind Speed",
            value = weatherState.windSpeed?.let { "$it m/s" } ?: "—",
            isLoading = weatherState.isLoading && weatherState.windSpeed == null
        )

        if (weatherState.weatherIndex != null) {
            Spacer(modifier = Modifier.height(16.dp))
            WeatherCard(
                emoji = "📊",
                title = "Weather Index",
                value = "${weatherState.weatherIndex}",
                isLoading = false
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.loadWeatherData() },
            enabled = !weatherState.isLoading
        ) {
            Text("🔄 Refresh Weather")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { viewModel.toggleErrorSimulation() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("⚠️ Simulate Error")
        }

        if (weatherState.loadingProgress.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = weatherState.loadingProgress,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        if (weatherState.error != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = weatherState.error!!,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun WeatherCard(
    emoji: String,
    title: String,
    value: String,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}