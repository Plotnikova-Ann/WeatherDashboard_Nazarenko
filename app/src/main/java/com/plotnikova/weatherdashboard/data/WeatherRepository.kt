package com.plotnikova.weatherdashboard.data

import kotlinx.coroutines.delay
import kotlin.random.Random

class WeatherRepository {

    private var simulateError = false

    fun toggleErrorSimulation() {
        simulateError = !simulateError
    }

    suspend fun fetchTemperature(): Int {
        delay(2000)
        if (simulateError) {
            throw Exception("Сервер недоступен")
        }
        return Random.nextInt(15, 35)
    }

    suspend fun fetchHumidity(): Int {
        delay(1500)
        return Random.nextInt(40, 80)
    }

    suspend fun fetchWindSpeed(): Int {
        delay(1000)
        return Random.nextInt(0, 20)
    }
}