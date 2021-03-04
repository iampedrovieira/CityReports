package com.example.cityreports

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import com.google.android.material.bottomappbar.BottomAppBar

class CreateNote : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)
        setSupportActionBar(findViewById(R.id.toolbarCreateNote))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.create_note)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun deleteNote(view:View){
        Toast.makeText(applicationContext, "delete", Toast.LENGTH_LONG).show()
    }

    fun saveNote(view:View){
        Toast.makeText(applicationContext, "Save", Toast.LENGTH_LONG).show()
    }
}