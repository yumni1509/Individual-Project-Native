package com.example.mybukuresipi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val ButtonStart = findViewById<Button>(R.id.btnStart)
        ButtonStart.setOnClickListener(){
            val intent = Intent(this, DisplayRecipe::class.java).apply {
            }
            startActivity(intent)
        }

    }

}