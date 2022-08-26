package com.example.fizz_buzz.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fizz_buzz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }

}
