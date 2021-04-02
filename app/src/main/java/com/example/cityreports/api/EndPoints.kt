package com.example.cityreports.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface EndPoints {

    @GET("alloccurrences")
    fun getAllOccurrences():Call<List<Occurrence>>

    @FormUrlEncoded
    @POST("login")
    fun verifyLogin(@Field("username")username:String,@Field("pass")password:String): Call<OutPutLogin>
}