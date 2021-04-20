package com.example.cityreports

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cityreports.api.EndPoints
import com.example.cityreports.api.Occurrence
import com.example.cityreports.api.OutPutLogin
import com.example.cityreports.api.ServiceBuilder
import com.google.android.gms.location.FusedLocationProviderClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomappbar.BottomAppBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Initial_page : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val LOCATIONPERMISSIONREQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_page)
        val progress: ProgressBar = findViewById(R.id.progressBar_map)
        progress.visibility = View.VISIBLE
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val appbar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        appbar.setNavigationOnClickListener{
            val intent = Intent(this,MenuActivity::class.java)
            startActivity(intent)
        }
        val addButton: View = findViewById(R.id.addButton)
        addButton.setOnClickListener { view ->
            val intent = Intent(this,OccurrenceOpen::class.java)
            intent.putExtra("new",true)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val progress: ProgressBar = findViewById(R.id.progressBar_map)
        progress.visibility = View.VISIBLE
        val call = request.getAllOccurrences()
        call.enqueue(object: Callback<List<Occurrence>>{
            override fun onResponse(call: Call<List<Occurrence>>, response: Response<List<Occurrence>>) {
                mMap.clear()
                response.body()?.forEach {
                    val latlng = LatLng(it.lat.toDouble(), it.lng.toDouble())
                    val new_marker:Marker = mMap.addMarker(MarkerOptions().position(latlng).title(it.description + " " + it.date_))
                    new_marker.tag = mapOf("occurrenceid" to it.occurrenceid,
                            "userid" to it.userid, "typeid" to it.typeid,"description" to it.description,
                            "lat" to it.lat , "lng" to it.lng)
                }
                progress.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<List<Occurrence>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLocationAccess()
        mMap.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
            }
            true
        }

        mMap.setOnInfoWindowClickListener { marker ->
            onMarkerClick(marker)
        }
        // Create markers from db
        val request = ServiceBuilder.buildService(EndPoints::class.java)

        val call = request.getAllOccurrences()
        call.enqueue(object: Callback<List<Occurrence>>{
            override fun onResponse(call: Call<List<Occurrence>>, response: Response<List<Occurrence>>) {
                response.body()?.forEach {
                    val latlng = LatLng(it.lat.toDouble(), it.lng.toDouble())
                    it.occurrenceid

                    val new_marker:Marker = mMap.addMarker(MarkerOptions().position(latlng).title(it.description + " " + it.date_))
                    new_marker.tag = mapOf("occurrenceid" to it.occurrenceid,
                            "userid" to it.userid, "typeid" to it.typeid,"description" to it.description,
                            "lat" to it.lat , "lng" to it.lng)
                }
                val progress: ProgressBar = findViewById(R.id.progressBar_map)
                progress.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<List<Occurrence>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATIONPERMISSIONREQUEST) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                mMap.isMyLocationEnabled = true
            }
            else {
                Toast.makeText(this, "User has not granted location access permission", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        }
        else
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATIONPERMISSIONREQUEST)
    }
    private fun onMarkerClick(marker: Marker) {
        val data_marker = marker.tag as Map<*, *>

        val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.sp_login),Context.MODE_PRIVATE)
        val user_id:Int = sharedPref.getInt(getString(R.string.sp_userid_value),0)
        val m_user_id = data_marker["userid"]
        val intent = Intent(this,OccurrenceOpen::class.java)
        intent.putExtra("new",false)
                .putExtra("userid",data_marker["userid"].toString().toInt())
                .putExtra("occurrenceid",data_marker["occurrenceid"].toString().toInt())
                .putExtra("typeid",data_marker["typeid"].toString().toInt())
                .putExtra("description",data_marker["description"].toString())
                .putExtra("lat",data_marker["lat"].toString().toDouble())
                .putExtra("lng",data_marker["lng"].toString().toDouble())
        startActivity(intent)


    }
}

