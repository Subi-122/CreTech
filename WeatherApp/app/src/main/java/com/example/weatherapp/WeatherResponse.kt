package com.example.weatherapp

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val humidity: Int
)

data class Weather(
    val description: String
)
