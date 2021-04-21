package com.example.cityreports.api

data class Occurrence(
    val id:String,
    val description:String,
    val lat:String,
    val lng:String,
    val img:String,
    val occurenceType_id:String,
    val users_id:String,
    val date_:String
)