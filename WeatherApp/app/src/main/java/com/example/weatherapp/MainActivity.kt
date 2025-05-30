package com.example.weatherapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.coroutines.resume
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppScreen()
        }
    }
}

@Composable
fun WeatherAppScreen() {
    var weatherData by remember { mutableStateOf("No data yet") }
    var cityName by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                coroutineScope.launch {
                    fetchWeatherByLocation(context) { result ->
                        weatherData = result
                    }
                }
            } else {
                weatherData = "Location permission denied."
            }
        }
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFDFFFC).copy(alpha = 0.4f)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // 🌥️ Clouds at the top
            Image(
                painter = painterResource(id = R.drawable.clouds),
                contentDescription = "Clouds",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .align(Alignment.TopCenter)
            )

            // 🌿 Grass at the bottom
            Image(
                painter = painterResource(id = R.drawable.grass),
                contentDescription = "Grass",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .align(Alignment.BottomCenter)
            )

            // Main content in the middle
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Weather App",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = cityName,
                    onValueChange = { cityName = it },
                    label = { Text("Enter City Name") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )

                Text(
                    text = weatherData,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )

                Button(
                    onClick = {
                        if (cityName.text.isNotBlank()) {
                            coroutineScope.launch {
                                fetchWeatherByCity(cityName.text) { result ->
                                    weatherData = result
                                }
                            }
                        } else {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                coroutineScope.launch {
                                    fetchWeatherByLocation(context) { result ->
                                        weatherData = result
                                    }
                                }
                            } else {
                                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Get Weather")
                }

                Button(
                    onClick = {
                        weatherData = "Refreshing..."
                        if (cityName.text.isNotBlank()) {
                            coroutineScope.launch {
                                fetchWeatherByCity(cityName.text) { result ->
                                    weatherData = result
                                }
                            }
                        } else {
                            coroutineScope.launch {
                                fetchWeatherByLocation(context) { result ->
                                    weatherData = result
                                }
                            }
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(text = "Refresh")
                }
            }
        }
    }
}


suspend fun fetchWeatherByCity(
    city: String,
    updateWeatherData: (String) -> Unit
) {
    try {
        val response = RetrofitInstance.api.getWeatherByCity(
            city = city,
            apiKey = "c7001f4eb0ce13829350cba2f17c8285"
        )
        updateWeatherData(
            """
            City: ${city.capitalize()}
            Temp: ${response.main.temp}°C
            Feels like: ${response.main.feels_like}°C
            Humidity: ${response.main.humidity}%
            Description: ${response.weather.firstOrNull()?.description ?: "N/A"}
            """.trimIndent()
        )
    } catch (e: Exception) {
        e.printStackTrace()
        updateWeatherData("Error: ${e.localizedMessage}")
    }
}

suspend fun fetchWeatherByLocation(
    context: Context,
    updateWeatherData: (String) -> Unit
) {
    try {
        val location = fetchCurrentLocation(context)
        if (location != null) {
            val response = RetrofitInstance.api.getWeather(
                lat = location.latitude,
                lon = location.longitude,
                apiKey = "c7001f4eb0ce13829350cba2f17c8285"
            )
            updateWeatherData(
                """
                Temp: ${response.main.temp}°C
                Feels like: ${response.main.feels_like}°C
                Humidity: ${response.main.humidity}%
                Description: ${response.weather.firstOrNull()?.description ?: "N/A"}
                """.trimIndent()
            )
        } else {
            updateWeatherData("Could not get location.")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        updateWeatherData("Error: ${e.localizedMessage}")
    }
}


suspend fun fetchCurrentLocation(context: Context): Location? {
    return suspendCancellableCoroutine { cont ->
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            cont.resume(null)
            return@suspendCancellableCoroutine
        }

        val locationRequest = LocationRequest.create().apply {
            priority = Priority.PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                cont.resume(locationResult.lastLocation)
                fusedLocationClient.removeLocationUpdates(this)
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}
