package com.example.cityreports

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
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

        val intent = Intent(this,Initial_page::class.java)
        var username:EditText = findViewById(R.id.editTextTextEmailAddress3)
        var password:EditText = findViewById(R.id.editTextTextPassword3)
        val call = request.verifyLogin(username.text.toString(),password.text.toString())

        call.enqueue(object: Callback<List<OutPutLogin>> {
            override fun onResponse(call: Call<List<OutPutLogin>>, response: Response<List<OutPutLogin>>) {
                val sharedPref:SharedPreferences = getSharedPreferences(getString(R.string.sp_login),Context.MODE_PRIVATE)
                with(sharedPref.edit()){
                    putBoolean(getString(R.string.sp_login_value),true)
                    commit()
                }
                val name: String? = response.body()?.get(0)?.name
                Toast.makeText(applicationContext, StringBuilder().append(R.string.welcome).append(name) , Toast.LENGTH_LONG).show()
                //Clear stack
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            }

            override fun onFailure(call: Call<List<OutPutLogin>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }


        })

    }

    fun buttonNotes(view: View){
        val intent = Intent(this,ListNotes::class.java)
        startActivity(intent)
    }
}