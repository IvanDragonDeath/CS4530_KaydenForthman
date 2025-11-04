package com.example.myapplication

import android.app.Application
import android.content.Context
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

@Suppress("DEPRECATION")
class MarbleViewModel(application: Application) :
    AndroidViewModel(application), SensorEventListener {

    private val sensorManager =
        application.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val gravitySensor: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)

    private val _position = MutableStateFlow(Pair(0f, 0f)) // center of marble
    val position = _position.asStateFlow()

    private var screenWidth = 0f
    private var screenHeight = 0f
    private val marbleRadius = 20f // half of marble size in pixels

    private var velocityX = 0f
    private var velocityY = 0f
    private val friction = 0.95f
    private val scale = 5f
    private var lastTimestamp: Long = 0L

    fun setScreenSize(width: Float, height: Float) {
        screenWidth = width
        screenHeight = height

        // Only initialize position if it hasn't been set yet
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
        if (event == null || event.sensor.type != Sensor.TYPE_GRAVITY) return
        if (screenWidth == 0f || screenHeight == 0f) return

        val timestamp = event.timestamp
        val dt = if (lastTimestamp == 0L) 0f else (timestamp - lastTimestamp) / 1_000_000_000f
        lastTimestamp = timestamp
        if (dt == 0f) return

        // Get current screen rotation
        val wm = getApplication<Application>()
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val rotation = wm.defaultDisplay.rotation

        val gx = event.values[0]
        val gy = event.values[1]

        // Map gravity to screen coordinates
        val tiltX: Float
        val tiltY: Float
        when (rotation) {
            Surface.ROTATION_0 -> {        // Portrait
                tiltX = -gx
                tiltY = gy
            }
            Surface.ROTATION_90 -> {       // Landscape left
                tiltX = gy
                tiltY = gx
            }
            Surface.ROTATION_180 -> {      // Portrait upside down
                tiltX = gx
                tiltY = -gy
            }
            Surface.ROTATION_270 -> {      // Landscape right
                tiltX = -gy
                tiltY = -gx
            }
            else -> {
                tiltX = -gx
                tiltY = gy
            }
        }

        // Update velocity
        velocityX += tiltX * scale * dt
        velocityY += tiltY * scale * dt

        // Apply friction
        velocityX *= friction
        velocityY *= friction

        // Update marble center position
        var newX = _position.value.first + velocityX
        var newY = _position.value.second + velocityY

        // Keep marble fully on screen
        newX = newX.coerceIn(marbleRadius, screenWidth - marbleRadius)
        newY = newY.coerceIn(marbleRadius, screenHeight - marbleRadius)

        viewModelScope.launch {
            _position.emit(Pair(newX, newY))
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}