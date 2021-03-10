package com.example.cityreports

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_open)
        setSupportActionBar(findViewById(R.id.toolbarNote))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.note)


        //Colocar o id e descText
        noteId = intent.getIntExtra("ID",0)
        descText = findViewById(R.id.textAreaNote2)
        descText.setText(intent.getStringExtra("Description"))
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_delete,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            val replyIntent = Intent()
            if(descText.text.toString()==""){
                replyIntent.putExtra("ID",noteId)
                replyIntent.putExtra("DELETE",true)
                setResult(Activity.RESULT_CANCELED,replyIntent)
                Toast.makeText(applicationContext, "Note Deleted", Toast.LENGTH_LONG).show()
            }
            Toast.makeText(applicationContext, "Note", Toast.LENGTH_LONG).show()
            finish()
        //Volta para gravar,

                //Se o text tiver vazio apaga

        }
        if(item.itemId == R.id.Delete){
            val replyIntent = Intent()
            replyIntent.putExtra("ID",noteId)
            replyIntent.putExtra("DELETE",true)
            setResult(Activity.RESULT_CANCELED,replyIntent)
            Toast.makeText(applicationContext, "Note Deleted", Toast.LENGTH_LONG).show()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun saveNote(view: View){
        Toast.makeText(applicationContext, "Save", Toast.LENGTH_LONG).show()
        val replyIntent = Intent()
        replyIntent.putExtra("ID",noteId)
        replyIntent.putExtra("Description",descText.text)
        setResult(Activity.RESULT_OK,replyIntent)
        Toast.makeText(applicationContext, "Saved", Toast.LENGTH_LONG).show()
        finish()

    }
}