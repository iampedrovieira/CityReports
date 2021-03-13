package com.example.cityreports

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.material.bottomappbar.BottomAppBar

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val closeMenu = findViewById<ImageView>(R.id.imageBack)
        closeMenu.setOnClickListener{
            finish()
        }
        val logout = findViewById<CardView>(R.id.cardLogout)
        logout.setOnClickListener{
            val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.sp_login), Context.MODE_PRIVATE)
            with(sharedPref.edit()){
                putBoolean(getString(R.string.sp_login_value),false)
                commit()
            }
            val intent = Intent(this,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val notes = findViewById<CardView>(R.id.cardNotes)
        notes.setOnClickListener{
            val intent = Intent(this,ListNotes::class.java)
            startActivity(intent)
        }

    }
}