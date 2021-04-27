package com.example.cityreports.api

import android.text.Editable
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @GET("alloccurrences")
    fun getAllOccurrences():Call<List<Occurrence>>
    @GET("img/{id}.png")
    fun getImg(@Path("id") id:Int):Call<ResponseBody>
    @POST("delete-occurrence/{id}")
    fun deleteOccurrence(@Path("id") id:Int):Call<OutPutOccurrence>
    @FormUrlEncoded
    @POST("login")
    fun verifyLogin(@Field("username")username:String,@Field("pass")password:String): Call<OutPutLogin>

    @FormUrlEncoded
    @POST("create-occurrences")
    fun createOccurrence(@Field("image") image: String,@Field("description") description: String, @Field("userid") userid: Int,
                         @Field("typeid") typeid:Int,
                         @Field("lat") lat:Double, @Field("lng") lng:Double): Call<OutPutOccurrence>
    @FormUrlEncoded
    @POST("edit-occurrences")
    fun editOccurrence(@Field("id") id: Int,@Field("image") image: String,@Field("description") description: String, @Field("userid") userid: Int,
                         @Field("typeid") typeid:Int,
                         @Field("lat") lat:Double, @Field("lng") lng:Double): Call<OutPutOccurrence>
}