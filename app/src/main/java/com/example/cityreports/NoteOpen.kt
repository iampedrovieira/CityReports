package com.example.cityreports

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class NoteOpen : AppCompatActivity() {

    private lateinit var descText:TextInputEditText
    private var noteId:Int? = null
    private var newNote:Boolean =false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_open)
        setSupportActionBar(findViewById(R.id.toolbarNote))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.note)
        noteId = intent.getIntExtra("ID",0)
        descText = findViewById(R.id.textAreaNote2)
        descText.setText(intent.getStringExtra("Description"))
        newNote=intent.getBooleanExtra("New",false)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_delete,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            val replyIntent = Intent()
            setResult(Activity.RESULT_OK,replyIntent)
            finish()
        }
        if(item.itemId == R.id.Delete){
            val replyIntent = Intent()
            replyIntent.putExtra("ID",noteId)
            setResult(Activity.RESULT_CANCELED,replyIntent)
            Toast.makeText(applicationContext, R.string.note_deleted, Toast.LENGTH_LONG).show()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun saveNote(view: View){
        Toast.makeText(applicationContext,  R.string.note_saved, Toast.LENGTH_LONG).show()
        val replyIntent = Intent()
        replyIntent.putExtra("ID",noteId)
        replyIntent.putExtra("Description",descText.text.toString())

        if(newNote){

            if(descText.text.toString()==""){
                setResult(Activity.RESULT_OK,replyIntent)
                finish()
            }
            replyIntent.putExtra("New",true)
            replyIntent.putExtra("Description",descText.text.toString())
        }else{
            if(descText.text.toString()==""){
                replyIntent.putExtra("ID",noteId)
                setResult(Activity.RESULT_CANCELED,replyIntent)
                Toast.makeText(applicationContext, R.string.note_deleted, Toast.LENGTH_LONG).show()
                finish()
            }
        }

        setResult(Activity.RESULT_OK,replyIntent)
        finish()

    }
}