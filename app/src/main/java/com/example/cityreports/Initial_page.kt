package com.example.cityreports

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cityreports.api.EndPoints
import com.example.cityreports.api.Occurrence
import com.example.cityreports.api.OutPutLogin
import com.example.cityreports.api.ServiceBuilder
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomappbar.BottomAppBar
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Initial_page : AppCompatActivity(), OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private lateinit var mMap: GoogleMap
    private var mapIsReady=false
    private val LOCATIONPERMISSIONREQUEST = 1
    private var actual_lat=0.0
    private  var actual_lng=0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var lastLocation: Location
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var filtroKmView:TextView
    private lateinit var spinner:Spinner

    private var filterValue = 100
    private lateinit var marker_list:MutableList<Marker>
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_page)
        val progress: ProgressBar = findViewById(R.id.progressBar_map)
        progress.visibility = View.VISIBLE
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        filtroKmView = findViewById(R.id.textViewFilter)
        filtroKmView.text = filterValue.toString()+" km "+getString(R.string.filter_km)
        marker_list = arrayListOf()
        val appbar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        appbar.setNavigationOnClickListener{
            val intent = Intent(this,MenuActivity::class.java)
            startActivity(intent)
        }
        val addButton: View = findViewById(R.id.addButton)
        addButton.setOnClickListener { view ->
            val intent = Intent(this,OccurrenceOpen::class.java)
            intent.putExtra("new",true)
                .putExtra("lat",actual_lat)
                    .putExtra("lng",actual_lng)

            startActivity(intent)
        }

        val seekbar = findViewById<SeekBar>(R.id.seekBar_filter)
        seekbar.setProgress(filterValue,true)
        seekbar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                filterValue=progress
                calcularDistancia()
                if(progress==100){
                    filtroKmView.text = "+100 km "+getString(R.string.filter_km)
                }else{
                    filtroKmView.text = progress.toString()+" km "+getString(R.string.filter_km)
                }

            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped

            }
        })
        // Real time Location update
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                actual_lat = p0.lastLocation.latitude
                actual_lng = p0.lastLocation.longitude
            }
        }

        //Request location
        createLocationRequest()

        spinner= findViewById(R.id.spinner)
        spinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(this,R.array.type_array,android.R.layout.simple_spinner_item).also {
            adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.setSelection(0)
    }
    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
        if(mapIsReady){
            val progress: ProgressBar = findViewById(R.id.progressBar_map)
            progress.visibility = View.VISIBLE
            mMap.clear()
            // Create markers from db
            val request = ServiceBuilder.buildService(EndPoints::class.java)

            val call = request.getAllOccurrences()
            call.enqueue(object: Callback<List<Occurrence>>{
                override fun onResponse(call: Call<List<Occurrence>>, response: Response<List<Occurrence>>) {
                    response.body()?.forEach {
                        val latlng = LatLng(it.lat.toDouble(), it.lng.toDouble())
                        it.id
                        var  new_marker:Marker
                        val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.sp_login),Context.MODE_PRIVATE)
                        val user_id:Int = sharedPref.getInt(getString(R.string.sp_userid_value),0)

                        if(it.occurenceType_id.toInt() == 1){
                            if(it.users_id.toString().toInt() == user_id){
                                new_marker = mMap.addMarker(MarkerOptions().position(latlng).title(it.description + " " + it.date_)
                                        .icon(BitmapDescriptorFactory.defaultMarker(216F)))
                                new_marker.tag = mapOf("occurrenceid" to it.id,
                                        "userid" to it.users_id, "typeid" to it.occurenceType_id,"description" to it.description,
                                        "lat" to it.lat , "lng" to it.lng, "date_" to it.date_)

                                marker_list.add(new_marker)
                            }else{
                                new_marker = mMap.addMarker(MarkerOptions().position(latlng).title(it.description + " " + it.date_)
                                        .icon(BitmapDescriptorFactory.defaultMarker(20F)))
                                new_marker.tag = mapOf("occurrenceid" to it.id,
                                        "userid" to it.users_id, "typeid" to it.occurenceType_id,"description" to it.description,
                                        "lat" to it.lat , "lng" to it.lng,"date_" to it.date_)

                                marker_list.add(new_marker)
                            }

                        }
                        if(it.occurenceType_id.toInt() == 2){
                            if(it.users_id.toString().toInt() == user_id){
                                new_marker = mMap.addMarker(MarkerOptions().position(latlng).title(it.description + " " + it.date_)
                                        .icon(BitmapDescriptorFactory.defaultMarker(216F)))
                                new_marker.tag = mapOf("occurrenceid" to it.id,
                                        "userid" to it.users_id, "typeid" to it.occurenceType_id,"description" to it.description,
                                        "lat" to it.lat , "lng" to it.lng,"date_" to it.date_)

                                marker_list.add(new_marker)
                            }else{
                                new_marker = mMap.addMarker(MarkerOptions().position(latlng).title(it.description + " " + it.date_)
                                        .icon(BitmapDescriptorFactory.defaultMarker(20F)))
                                new_marker.tag = mapOf("occurrenceid" to it.id,
                                        "userid" to it.users_id, "typeid" to it.occurenceType_id,"description" to it.description,
                                        "lat" to it.lat , "lng" to it.lng,"date_" to it.date_)

                                marker_list.add(new_marker)
                            }
                        }


                    }
                    val progress: ProgressBar = findViewById(R.id.progressBar_map)
                    progress.visibility = View.INVISIBLE
                }

                override fun onFailure(call: Call<List<Occurrence>>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    val progress: ProgressBar = findViewById(R.id.progressBar_map)
                    progress.visibility = View.INVISIBLE

                }

            })
        }


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
        mapIsReady=true
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
                    it.id
                    var  new_marker:Marker
                    val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.sp_login),Context.MODE_PRIVATE)
                    val user_id:Int = sharedPref.getInt(getString(R.string.sp_userid_value),0)

                    if(it.occurenceType_id.toInt() == 1){
                        if(it.users_id.toString().toInt() == user_id){
                            new_marker = mMap.addMarker(MarkerOptions().position(latlng).title(it.description + " " + it.date_)
                                    .icon(BitmapDescriptorFactory.defaultMarker(216F)))
                            new_marker.tag = mapOf("occurrenceid" to it.id,
                                    "userid" to it.users_id, "typeid" to it.occurenceType_id,"description" to it.description,
                                    "lat" to it.lat , "lng" to it.lng,"date_" to it.date_)

                            marker_list.add(new_marker)
                        }else{
                            new_marker = mMap.addMarker(MarkerOptions().position(latlng).title(it.description + " " + it.date_)
                                    .icon(BitmapDescriptorFactory.defaultMarker(20F)))
                            new_marker.tag = mapOf("occurrenceid" to it.id,
                                    "userid" to it.users_id, "typeid" to it.occurenceType_id,"description" to it.description,
                                    "lat" to it.lat , "lng" to it.lng,"date_" to it.date_)

                            marker_list.add(new_marker)
                        }

                    }
                    if(it.occurenceType_id.toInt() == 2){
                        if(it.users_id.toString().toInt() == user_id){
                            new_marker = mMap.addMarker(MarkerOptions().position(latlng).title(it.description + " " + it.date_)
                                    .icon(BitmapDescriptorFactory.defaultMarker(216F)))
                            new_marker.tag = mapOf("occurrenceid" to it.id,
                                    "userid" to it.users_id, "typeid" to it.occurenceType_id,"description" to it.description,
                                    "lat" to it.lat , "lng" to it.lng,"date_" to it.date_)

                            marker_list.add(new_marker)
                        }else{
                            new_marker = mMap.addMarker(MarkerOptions().position(latlng).title(it.description + " " + it.date_)
                                    .icon(BitmapDescriptorFactory.defaultMarker(20F)))
                            new_marker.tag = mapOf("occurrenceid" to it.id,
                                    "userid" to it.users_id, "typeid" to it.occurenceType_id,"description" to it.description,
                                    "lat" to it.lat , "lng" to it.lng,"date_" to it.date_)

                            marker_list.add(new_marker)
                        }
                    }


                }
                val progress: ProgressBar = findViewById(R.id.progressBar_map)
                progress.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<List<Occurrence>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                val progress: ProgressBar = findViewById(R.id.progressBar_map)
                progress.visibility = View.INVISIBLE

            }

        })
        mMap.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(this))
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
        if(user_id == m_user_id.toString().toInt()){

            val intent = Intent(this,OccurrenceOpen::class.java)
            intent.putExtra("new",false)
                    .putExtra("userid",data_marker["userid"].toString().toInt())
                    .putExtra("occurrenceid",data_marker["occurrenceid"].toString().toInt())
                    .putExtra("typeid",data_marker["typeid"].toString().toInt())
                    .putExtra("description",data_marker["description"].toString())
                    .putExtra("lat",data_marker["lat"].toString().toDouble())
                    .putExtra("lng",data_marker["lng"].toString().toDouble())
            mMap.clear()
            startActivity(intent)
        }
    }

    //Filtros
    fun calcularDistancia(){
        //Ir a todos os markers
        marker_list.forEach {
            val distance = FloatArray(1)
            Location.distanceBetween(it.position.latitude,it.position.longitude,actual_lat,actual_lng,distance)
            it.isVisible = distance[0]/1000 <= filterValue.toFloat()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        marker_list?.forEach {
            if(it.tag != null){
                val data_marker = it.tag as Map<*,*>

                it.isVisible = data_marker["typeid"].toString().toInt() == position + 1
            }

        }
    }

    //Class "adapter" to info
    class CustomInfoWindowForGoogleMap(context: Context) : GoogleMap.InfoWindowAdapter {

        var mContext = context
        var mWindow = (context as Activity).layoutInflater.inflate(R.layout.map_info, null)

        private fun rendowWindowText(marker: Marker, view: View){
            val desc = view.findViewById<TextView>(R.id.textViewInfoDesc)
            val type = view.findViewById<TextView>(R.id.textViewInfoTipo)
            val data = view.findViewById<TextView>(R.id.textViewInfoDate)
            val img:ImageView = view.findViewById<ImageView>(R.id.imageViewInfo)
            val tag = marker.tag as Map<*,*>
            var occurence_id = tag["occurrenceid"].toString().toInt()

            desc.text= tag["description"].toString()
            if(tag["typeid"].toString().toInt()==1){
                type.text = "Transito"
            }else{
                type.text = "Buraco na via"
            }
            data.text= tag["date_"].toString()

            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.getImg(occurence_id)

            call.enqueue(object: Callback<ResponseBody>{
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                    val imageBitmap = BitmapFactory.decodeStream(response.body()?.byteStream())
                    img.setImageBitmap(imageBitmap)
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

        }

        override fun getInfoContents(marker: Marker): View {
            rendowWindowText(marker, mWindow)
            return mWindow
        }

        override fun getInfoWindow(marker: Marker): View? {
            rendowWindowText(marker, mWindow)
            return mWindow
        }
    }



}

