package com.example.cityreports

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.cityreports.api.EndPoints
import com.example.cityreports.api.OutPutLogin
import com.example.cityreports.api.OutPutOccurrence
import com.example.cityreports.api.ServiceBuilder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class OccurrenceOpen : AppCompatActivity(),AdapterView.OnItemSelectedListener{

    private lateinit var description:EditText
    private lateinit var textLocalization:TextView
    private lateinit var textPhoto:TextView
    private var lat:Double = 0.0
    private var lng:Double = 0.0
    private var typeid:Int =1
    private lateinit var date_:String
    private lateinit var img:String
    lateinit var spinner:Spinner
    private lateinit var fusedLocationClient: FusedLocationProviderClient


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
        //Tirar photo e gravar a mesma
    }
    fun onClickLocalization(view:View){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    if (location != null) {
                        lat = location.latitude
                        lng = location.longitude
                    }
                }
    }
    fun onClickSave(view: View){

        // Create markers on db
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val sharedPref:SharedPreferences = getSharedPreferences(getString(R.string.sp_login),Context.MODE_PRIVATE)
        val userid = sharedPref.getInt(getString(R.string.sp_userid_value),0)

        val call = request.createOccurrence(description.text.toString(),userid,typeid,lat,lng)
        call.enqueue(object: Callback<OutPutOccurrence> {
            override fun onResponse(call: Call<OutPutOccurrence>, response: Response<OutPutOccurrence>) {
                if (response.body()?.status!!){
                    finish()
                }
            }
            override fun onFailure(call: Call<OutPutOccurrence>, t: Throwable) {
                Toast.makeText(applicationContext, t.message , Toast.LENGTH_LONG).show()
            }
        })
    }
}