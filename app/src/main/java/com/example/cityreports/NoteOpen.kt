package com.example.cityreports

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_delete,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        if(item.itemId == R.id.Delete){
            Toast.makeText(applicationContext, "delete", Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    fun saveNote(view: View){
        Toast.makeText(applicationContext, "Save", Toast.LENGTH_LONG).show()
    }
}