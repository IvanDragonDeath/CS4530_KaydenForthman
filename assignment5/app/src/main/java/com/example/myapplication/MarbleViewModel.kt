package com.example.myapplication

import android.app.Application
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.Surface
import android.view.WindowManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MarbleViewModel(application: Application) :
    AndroidViewModel(application), SensorEventListener {

    private val sensorManager =
        application.getSystemService(SENSOR_SERVICE) as SensorManager

    // Gravity sensor fallback to accelerometer
    private val gravitySensor: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
            ?: sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val _position = MutableStateFlow(Pair(0f, 0f))
    val position = _position.asStateFlow()

    private var screenWidth = 0f
    private var screenHeight = 0f
    private val marbleRadius = 20f

    private var velocityX = 0f
    private var velocityY = 0f
    private val friction = 0.95f
    private val scale = 20f
    private var lastTimestamp: Long = 0L

    fun setScreenSize(width: Float, height: Float) {
        screenWidth = width
        screenHeight = height
        if (_position.value.first == 0f && _position.value.second == 0f) {
            _position.value = Pair(width / 2f, height / 2f)
        }
    }

    fun registerSensor() {
        gravitySensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun unregisterSensor() {
        sensorManager.unregisterListener(this)
        lastTimestamp = 0L
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        if (screenWidth == 0f || screenHeight == 0f) return

        val timestamp = event.timestamp
        val dt = if (lastTimestamp == 0L) 0f else (timestamp - lastTimestamp) / 1_000_000_000f
        lastTimestamp = timestamp
        if (dt == 0f) return

        val gx = event.values[0]
        val gy = event.values[1]
        val gz = if (event.values.size >= 3) event.values[2] else 0f

        // Normalize gravity vector to include Z axis
        val g = kotlin.math.sqrt(gx*gx + gy*gy + gz*gz)
        if (g == 0f) return
        val normX = gx / g
        val normY = gy / g

        val wm = getApplication<Application>()
            .getSystemService(WindowManager::class.java)
        val rotation = wm.defaultDisplay.rotation

        val tiltX: Float
        val tiltY: Float
        when (rotation) {
            Surface.ROTATION_0 -> {
                tiltX = -normX
                tiltY = normY
            }
            Surface.ROTATION_90 -> {
                tiltX = normY
                tiltY = normX
            }
            Surface.ROTATION_180 -> {
                tiltX = normX
                tiltY = -normY
            }
            Surface.ROTATION_270 -> {
                tiltX = -normY
                tiltY = -normX
            }
            else -> {
                tiltX = -normX
                tiltY = normY
            }
        }

        // Update velocity
        velocityX += tiltX * scale * dt
        velocityY += tiltY * scale * dt

        // Apply friction
        velocityX *= friction
        velocityY *= friction

        var newX = _position.value.first + velocityX
        var newY = _position.value.second + velocityY

        newX = newX.coerceIn(marbleRadius, screenWidth - marbleRadius)
        newY = newY.coerceIn(marbleRadius, screenHeight - marbleRadius)

        viewModelScope.launch {
            _position.emit(Pair(newX, newY))
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}