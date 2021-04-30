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
import androidx.core.text.set
import com.example.cityreports.api.EndPoints
import com.example.cityreports.api.Occurrence
import com.example.cityreports.api.OutPutOccurrence
import com.example.cityreports.api.ServiceBuilder
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import okhttp3.ResponseBody
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
    private var typeid:Int = 1
    private var occurence_id:Int = -1
    private var new:Boolean=true
    lateinit var spinner:Spinner
    val REQUEST_IMAGE_CAPTURE = 1
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var  imageBitmap: Bitmap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var lastLocation: Location
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_occurrence_open)
        setSupportActionBar(findViewById(R.id.toolbarOccurrence))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.ocurrences)
        description= findViewById(R.id.textAreaOccurrence)
        textLocalization= findViewById(R.id.textViewLocalization)
        imageView = findViewById(R.id.imageViewOccurrence)
        // Real time Location update
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
            }
        }
        //Request location
        createLocationRequest()
        //get data from past Activity
        val data:Bundle?=intent.extras
        if(data!=null){
            new = data.getBoolean("new")
            if(!new){
                occurence_id = data.getInt("occurrenceid")
                description.setText(data.getString("description"))
                typeid = data.getInt("typeid")
                lat= data.getDouble("lat")
                lng= data.getDouble("lng")
                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.getImg(occurence_id)

                call.enqueue(object: Callback<ResponseBody>{
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                        imageBitmap = BitmapFactory.decodeStream(response.body()?.byteStream())
                        imageView.setImageBitmap(imageBitmap)
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            }else{
                lat= data.getDouble("lat")
                lng= data.getDouble("lng")
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
        spinner.setSelection(typeid-1)
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }
    private fun createLocationRequest(){
        locationRequest = LocationRequest()
        locationRequest.interval=10000
        locationRequest.priority= LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private fun startLocationUpdates(){
        if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
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

            Toast.makeText(applicationContext, R.string.changes_not_saved , Toast.LENGTH_LONG).show()
            finish()

        }
        if(item.itemId == R.id.Delete){
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.deleteOccurrence(occurence_id)

            call.enqueue(object: Callback<OutPutOccurrence>{
                override fun onResponse(call: Call<OutPutOccurrence>, response: Response<OutPutOccurrence>) {
                    Toast.makeText(applicationContext, R.string.ocurrence_deleted , Toast.LENGTH_LONG).show()
                    finish()
                }
                override fun onFailure(call: Call<OutPutOccurrence>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message , Toast.LENGTH_LONG).show()
                }
            })


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
        lat = lastLocation.latitude
        lng = lastLocation.longitude
        val address = getAdress()
        textLocalization.text= address
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun onClickSave(view: View){

        // Create markers on db
        if(description.text.toString()!=""){

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
                val call = request.editOccurrence(occurence_id,stringImg,description.text.toString(),userid,typeid,lat,lng)
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
        }else{
            Toast.makeText(applicationContext, R.string.empy_fields , Toast.LENGTH_LONG).show()
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