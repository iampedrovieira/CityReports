package com.example.cityreports

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast

class NoteOpen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_open)
        setSupportActionBar(findViewById(R.id.toolbarNote))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.note)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun deleteNote(view: View){
        Toast.makeText(applicationContext, "delete", Toast.LENGTH_LONG).show()
    }

    fun saveNote(view: View){
        Toast.makeText(applicationContext, "Save", Toast.LENGTH_LONG).show()
    }
}