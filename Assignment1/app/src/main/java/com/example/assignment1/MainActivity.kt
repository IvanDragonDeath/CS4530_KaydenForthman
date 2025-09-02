package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assignment1.databinding.ActivityMainBinding
import kotlin.jvm.java


class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.button1.setOnClickListener{
            print("this button has been pressed")
            val buttonText = binding.button1.text.toString()

            val intent = Intent(this, SecondaryActivity::class.java)
            //val argsBundle = Bundle()
            //argsBundle.putString("button_text", buttonText as String?)
            intent.putExtra("button_text", buttonText)
            startActivity(intent)
        }
        binding.button2.setOnClickListener{
            val buttonText = binding.button2.text.toString()
            val intent = Intent(this, SecondaryActivity::class.java)
/*            val argsBundle = Bundle()
            argsBundle.putString("button_Text", buttonText as String?)*/
            intent.putExtra("button_text", buttonText)
            startActivity(intent)
        }
        binding.button3.setOnClickListener{
            val buttonText = binding.button3.text.toString()

            val intent = Intent(this, SecondaryActivity::class.java)
  /*          val argsBundle = Bundle()
            argsBundle.putString("button_Text", buttonText as String?)*/
            intent.putExtra("button_text", buttonText)
            startActivity(intent)
        }
        binding.button4.setOnClickListener{
            val buttonText = binding.button4.text.toString()

            val intent = Intent(this, SecondaryActivity::class.java)
/*            val argsBundle = Bundle()
            argsBundle.putString("button_Text", buttonText as String?)*/
            intent.putExtra("button_text", buttonText)
            startActivity(intent)
        }
        binding.button5.setOnClickListener{
            val buttonText = binding.button5.text.toString()

            val intent = Intent(this, SecondaryActivity::class.java)
/*            val argsBundle = Bundle()
            argsBundle.putString("button_Text", buttonText as String?)*/
            intent.putExtra("button_text", buttonText)
            startActivity(intent)
        }

    }


}