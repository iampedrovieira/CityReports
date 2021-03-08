package com.example.cityreports

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cityreports.adapters.NoteAdapter
import com.example.cityreports.adapters.OnItemClickListener
import com.example.cityreports.dataclasses.Note

class ListNotes : AppCompatActivity(),OnItemClickListener {

    private lateinit var list:ArrayList<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_notes)

        setSupportActionBar(findViewById(R.id.toolbarListNote))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.notes)

        list = ArrayList<Note>()
        for(i in 0 until 30){
            list.add(Note(i,"E que tudo fixe?"))
        }

        val recycler = findViewById<RecyclerView>(R.id.recycler_Notes)
        recycler.adapter = NoteAdapter(list,this)
        recycler.layoutManager = LinearLayoutManager(this)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
    //This method is the interface method
    override fun onItemClick(position: Int) {
        val intent = Intent(this,NoteOpen::class.java)
        startActivity(intent)

        /*Vai abrir a NoteOpen com os dados da nota
            Ou envio os dados como parametro
            ou envio o id e vai buscar os dados a db local

        * */
    }

    fun buttonCreateNote(view: View){
        val intent = Intent(this,NoteOpen::class.java)
        startActivity(intent)
    }
}