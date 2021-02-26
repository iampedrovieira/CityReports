package com.example.cityreports

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.material.bottomappbar.BottomAppBar

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val cardHome = findViewById<CardView>(R.id.cardHome)
        cardHome.setOnClickListener{
            Toast.makeText(applicationContext, "aadsat", Toast.LENGTH_LONG).show()
        }

    }
}