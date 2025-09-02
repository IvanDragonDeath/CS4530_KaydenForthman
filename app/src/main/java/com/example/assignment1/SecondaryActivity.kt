package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assignment1.databinding.ActivityMainBinding
import com.example.assignment1.databinding.ActivitySecondaryBinding

class SecondaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySecondaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val passedButtonText = intent.getStringExtra("button_text")

        binding.DisplayText.setText(passedButtonText)
        binding.returnButton.setOnClickListener {
            val intent = Intent(this, ActivityMainBinding::class.java)
            startActivity(intent)
        }
    }
}
