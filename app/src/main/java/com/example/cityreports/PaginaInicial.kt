package com.example.cityreports

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomappbar.BottomAppBar

class PaginaInicial : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagina_inicial)
        val appbar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        appbar.setNavigationOnClickListener{
            Toast.makeText(applicationContext, "aadsat", Toast.LENGTH_LONG).show()
            val intent = Intent(this,MenuActivity::class.java)
            startActivity(intent)
        }
    }

}