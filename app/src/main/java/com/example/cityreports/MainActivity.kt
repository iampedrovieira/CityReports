package com.example.cityreports

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun buttonLogin(view: View){

        //Colocar validação do login aqui
        val intent = Intent(this,Initial_page::class.java)
        //Clear stack
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

    }
    fun buttonCreateNote(view: View){

        val intent = Intent(this,CreateNote::class.java)
        startActivity(intent)

    }
}