package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment1.databinding.ActivityMainBinding
import kotlin.jvm.java


class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        buttonHandler(binding.button1)
        buttonHandler(binding.button2)
        buttonHandler(binding.button3)
        buttonHandler(binding.button4)
        buttonHandler(binding.button5)

    }
    // Function to handle the pressing of the buttons and swapping to the other activity
    fun buttonHandler(button: Button) {
        button.setOnClickListener{
            val buttonText = button.text.toString()
            val intent = Intent(this, SecondaryActivity::class.java)
            intent.putExtra("button_text", buttonText)
            startActivity(intent)
        }
    }
}