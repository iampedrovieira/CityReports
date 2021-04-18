package com.example.cityreports

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.cityreports.api.EndPoints
import com.example.cityreports.api.OutPutOccurrence
import com.example.cityreports.api.ServiceBuilder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class OccurrenceOpen : AppCompatActivity(),AdapterView.OnItemSelectedListener{

    private lateinit var description:EditText
    private lateinit var textLocalization:TextView
    private lateinit var textPhoto:TextView
    private lateinit var imageView:ImageView
    private var lat:Double = 0.0
    private var lng:Double = 0.0
    private var typeid:Int =1
    private var new:Boolean=true
    private lateinit var date_:String
    private lateinit var img:String
    lateinit var spinner:Spinner
    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var  imageBitmap: Bitmap
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_occurrence_open)
        setSupportActionBar(findViewById(R.id.toolbarOccurrence))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.ocurrences)
        description= findViewById(R.id.textAreaOccurrence)
        textLocalization= findViewById(R.id.textViewLocalization)
        imageView = findViewById(R.id.imageViewOccurrence)

        //get data from past Activity
        val data:Bundle?=intent.extras
        if(data!=null){
            new = data.getBoolean("new")
            if(!new){
                //Caso seja para editar
            }else{
                updateLocalization()
            }

        }

        if(lat==0.0 || lng == 0.0){

        }else{
            val address = getAdress()
            textLocalization.text = address
        }
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

    fun onClickPhoto(view:View){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)

        }
    }

    fun onClickLocalization(view:View){
        updateLocalization()
    }
    private fun updateLocalization(){
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
                        Log.v("bbbbbbbb","DD")
                        val address = getAdress()
                        textLocalization.text= address
                    }else{
                        Toast.makeText(applicationContext, R.string.enable_gps , Toast.LENGTH_LONG).show()
                    }
                }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun onClickSave(view: View){

        // Create markers on db

        if(description.text.toString()==""){

            if(new){
                return
            }else{
                //Delete
            }
        }

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val sharedPref:SharedPreferences = getSharedPreferences(getString(R.string.sp_login),Context.MODE_PRIVATE)
        val userid = sharedPref.getInt(getString(R.string.sp_userid_value),0)
        var bao = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG,100,bao)
        val stringImg:String = Base64.getEncoder().encodeToString(bao.toByteArray())

        if(new){
            val call = request.createOccurrence(stringImg,description.text.toString(),userid,typeid,lat,lng)
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
        }else{
            //Edit
        }

    }

    private fun getAdress():String{
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(lat,lng,1)
        if(list.size>0){
            return list[0].getAddressLine(0)
        }else{
            return getString(R.string.try_again)
        }

    }
}