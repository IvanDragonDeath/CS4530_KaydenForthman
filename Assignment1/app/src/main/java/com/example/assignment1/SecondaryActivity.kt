package com.example.assignment1

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.example.assignment1.databinding.ActivityMainBinding
import com.example.assignment1.databinding.ActivitySecondaryBinding

class SecondaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySecondaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // The text stored inside the button
        val passedButtonText = intent.getStringExtra("button_text")

        // setting the text on the second activity to passed in button text
        binding.DisplayText.setText(passedButtonText)


        // returning back to the starting activity
        binding.returnButton.setOnClickListener {
            val intent = Intent(this, ActivityMainBinding::class.java)
            startActivity(intent)
        }
    }
}
