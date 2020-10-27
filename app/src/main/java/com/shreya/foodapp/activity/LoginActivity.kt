package com.shreya.foodapp.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shreya.foodapp.R
import com.shreya.foodapp.util.ConnectionManager
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class LoginActivity : AppCompatActivity() {
    lateinit var etphoneno:EditText
    lateinit var etpassword:EditText
    lateinit var txtwarning:TextView
    lateinit var sharedpref:SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etphoneno = findViewById(R.id.etphoneno)
        etpassword = findViewById(R.id.etpassword)
        txtwarning = findViewById(R.id.txtwarning)

        sharedpref = getSharedPreferences("FoodappPreference", Context.MODE_PRIVATE)

        var islogin = sharedpref.getBoolean("isloggedin", false)


        intent = Intent(
            this@LoginActivity,
            MainActivity::class.java
        )

        if (islogin) {
            startActivity(intent)
            finish()
        } else {

            val queue = Volley.newRequestQueue(this@LoginActivity)
            val url = "http://13.235.250.119/v2/login/fetch_result/"
            txtwarning.setVisibility(View.INVISIBLE)

            btnlogin.setOnClickListener {
                txtwarning.setVisibility(View.INVISIBLE)
                if (ConnectionManager().checkConnectivity(this@LoginActivity)) {

                    val jsonParams = JSONObject()
                    jsonParams.put("mobile_number", etphoneno.text.toString())
                    jsonParams.put("password", etpassword.text.toString())
                    if (etpassword.text.toString().length >= 4) {

                        val jsonObjectRequest =
                            object : JsonObjectRequest(Method.POST, url, jsonParams,
                                Response.Listener {
                                    val data = it.getJSONObject("data")
                                    try {
                                        val success = data.getBoolean("success")
                                        if (success) {

                                            val user = data.getJSONObject("data")
                                            val userId = user.getString("user_id")
                                            val name = user.getString("name")
                                            val email = user.getString("email")
                                            val mobileNumber = user.getString("mobile_number")
                                            val address = user.getString("address")

                                            sharedpref.edit().putBoolean("isloggedin", true).apply()
                                            sharedpref.edit().putString("name", name).apply()
                                            sharedpref.edit().putString("user_id", userId).apply()
                                            sharedpref.edit().putString("email", email).apply()
                                            sharedpref.edit()
                                                .putString("mobile_number", mobileNumber)
                                                .apply()
                                            sharedpref.edit().putString("address", address).apply()

                                            startActivity(intent)
                                            finish()

                                        } else {
                                            val errorMessage = data.getString("errorMessage")
                                            Toast.makeText(
                                                this@LoginActivity,
                                                errorMessage,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }

                                    } catch (e: JSONException) {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "Json Error Occurred!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                }, Response.ErrorListener {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Volley Error Occurred",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }) {
                                override fun getHeaders(): MutableMap<String, String> {
                                    val header = HashMap<String, String>()
                                    header["Content-Type"] = "application/json"
                                    header["token"] = "d4d68fb2f69f29"
                                    return header
                                }
                            }
                        queue.add(jsonObjectRequest)
                    } else {
                        txtwarning.setVisibility(View.VISIBLE)
                    }

                } else {
                    Toast.makeText(this@LoginActivity, "No Internet Found", Toast.LENGTH_LONG)
                        .show()
                }

            }


            txtsignup.setOnClickListener {
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)

            }

            txtforgotpass.setOnClickListener {
                val intent2 = Intent(this@LoginActivity, ForgotPassActivity::class.java)
                startActivity(intent2)

            }


        }




    }
}
