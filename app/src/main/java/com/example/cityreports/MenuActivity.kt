package com.example.cityreports

import android.content.Intent
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
            val intent = Intent(this,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val note = findViewById<CardView>(R.id.cardNotes)
        note.setOnClickListener{
            val intent = Intent(this,NoteOpen::class.java)

            startActivity(intent)
        }

    }
}