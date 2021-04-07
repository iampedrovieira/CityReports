package com.example.cityreports

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*

class OccurrenceOpen : AppCompatActivity(),AdapterView.OnItemSelectedListener{

    private lateinit var description:EditText
    private lateinit var textLocalization:TextView
    private lateinit var textPhoto:TextView
    private var lat:Int = 0
    private var lng:Int = 0
    private var typeid:Int =1
    private lateinit var date_:String
    private lateinit var img:String
    lateinit var spinner:Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_occurrence_open)
        setSupportActionBar(findViewById(R.id.toolbarOccurrence))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.ocurrences)
        description= findViewById(R.id.textAreaOccurrence)
        textPhoto= findViewById(R.id.textPhoto)
        textLocalization= findViewById(R.id.textViewLocalization)


        spinner= findViewById(R.id.spinner_type)

        spinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(this,R.array.type_array,android.R.layout.simple_spinner_item).also {
            adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        spinner.setSelection(typeid)
    }
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        typeid=pos+1

    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_delete,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()

        }
        if(item.itemId == R.id.Delete){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun onClickPhoto(){

    }
    fun onClickSave(){

    }
}