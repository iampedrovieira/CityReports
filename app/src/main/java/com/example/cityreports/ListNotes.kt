package com.example.cityreports

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.size
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cityreports.adapters.NoteAdapter
import com.example.cityreports.adapters.OnItemClickListener
import com.example.cityreports.entities.Note
import com.example.cityreports.viewModel.NoteViewModel

class ListNotes : AppCompatActivity(),OnItemClickListener {

    private lateinit var noteViewModel:NoteViewModel
    private val newWordActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_notes)

        setSupportActionBar(findViewById(R.id.toolbarListNote))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.notes)

        val recycler = findViewById<RecyclerView>(R.id.recycler_Notes)
        val adapter = NoteAdapter(this,this)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)

        //View model
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer { notes ->
            notes?.let { adapter.setNotes(it) }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK){
            //vai dar Update

        }
        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_CANCELED){

            if(data?.getIntExtra("ID",-1) != null){

                    val nid = data?.getIntExtra("ID",-1)
                    if (nid != null) {
                        noteViewModel.deleteNote(nid)
                    }
            }
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
    //This method is the interface method
    override fun onItemClick(note: Note) {
        val intent = Intent(this@ListNotes, NoteOpen::class.java)
        intent.putExtra("ID",note.id)
        intent.putExtra("Description",note.description)
        startActivityForResult(intent, newWordActivityRequestCode)


        /*Vai abrir a NoteOpen com os dados da nota
            Ou envio os dados como parametro
            ou envio o id e vai buscar os dados a db local

        * */
    }

    fun buttonCreateNote(view: View){
        val note = Note(description="")
        noteViewModel.insert(note)

        val intent = Intent(this,NoteOpen::class.java)
        startActivity(intent)
    }
}