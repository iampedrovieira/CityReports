package com.example.cityreports.api

data class Occurrence(
    val occurrenceid:String,
    val description:String,
    val lat:String,
    val lng:String,
    val img:String,
    val typeid:String,
    val userid:String,
    val date_:String
)