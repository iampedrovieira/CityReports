package com.example.cityreports

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
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
        val progress:ProgressBar = findViewById(R.id.progressBar_Login)
        progress.visibility = View.INVISIBLE
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
        val progress:ProgressBar = findViewById(R.id.progressBar_Login)
        progress.visibility = View.VISIBLE
        val call = request.verifyLogin(username.text.toString(),password.text.toString())

        call.enqueue(object: Callback<OutPutLogin> {
            override fun onResponse(call: Call<OutPutLogin>, response: Response<OutPutLogin>) {

                if(response.isSuccessful ){
                    val user:OutPutLogin = response.body()!!
                        if(user.id !="-1"){
                            progress.visibility = View.INVISIBLE
                            val sharedPref:SharedPreferences = getSharedPreferences(getString(R.string.sp_login),Context.MODE_PRIVATE)
                            with(sharedPref.edit()){
                                putBoolean(getString(R.string.sp_login_value),true)
                                putInt(getString(R.string.sp_userid_value),user.id.toInt())
                                commit()
                            }
                            //Toast.makeText(applicationContext, "${R.string.welcome.to} ${user.name} " , Toast.LENGTH_LONG).show()
                            //Clear stack
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }else{
                            progress.visibility = View.INVISIBLE
                            Toast.makeText(applicationContext, R.string.login_fail , Toast.LENGTH_LONG).show()
                        }
                }
            }

            override fun onFailure(call: Call<OutPutLogin>, t: Throwable) {
                Log.v("TESTE",t.message.toString())
            }


        })

    }

    fun buttonNotes(view: View){
        val intent = Intent(this,ListNotes::class.java)
        startActivity(intent)
    }
}