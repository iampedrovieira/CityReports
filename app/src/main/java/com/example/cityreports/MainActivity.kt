package com.example.cityreports

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.cityreports.api.EndPoints
import com.example.cityreports.api.OutPutLogin
import com.example.cityreports.api.ServiceBuilder
import com.example.cityreports.api.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref:SharedPreferences = getSharedPreferences(getString(R.string.sp_login),Context.MODE_PRIVATE)
        val loginValue = sharedPref.getBoolean(getString(R.string.sp_login_value),false)
        if(loginValue){
            val intent = Intent(this,Initial_page::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    fun buttonLogin(view: View){

        //Colocar validação do login aqui
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.verifyLogin("teste","123")


        call.enqueue(object: Callback<OutPutLogin> {
            override fun onResponse(call: Call<OutPutLogin>, response: Response<OutPutLogin>) {

            }
            override fun onFailure(call: Call<OutPutLogin>, t: Throwable) {
                TODO("Not yet implemented")
            }




        })
        val sharedPref:SharedPreferences = getSharedPreferences(getString(R.string.sp_login),Context.MODE_PRIVATE)
        with(sharedPref.edit()){
            putBoolean(getString(R.string.sp_login_value),true)
            commit()
        }
        val intent = Intent(this,Initial_page::class.java)
        //Clear stack
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

    }

    fun buttonNotes(view: View){
        val intent = Intent(this,ListNotes::class.java)
        startActivity(intent)
    }
}