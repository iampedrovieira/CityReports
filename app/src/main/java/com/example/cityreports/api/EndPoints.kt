package com.example.cityreports.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.POST

interface EndPoints {
    @POST("/login")
    fun verifyLogin(@Field("username")username:String,@Field("pass")password:String): Call<OutPutLogin>
}