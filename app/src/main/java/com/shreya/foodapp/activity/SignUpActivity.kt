package com.shreya.foodapp.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
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
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text

class SignUpActivity : AppCompatActivity() {
    lateinit var sharedpref:SharedPreferences

    lateinit var name:EditText
    lateinit var email:EditText
    lateinit var phone:EditText
    lateinit var address:EditText
    lateinit var password:EditText
    lateinit var confirmpass:EditText
    lateinit var warning:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        name=findViewById(R.id.etname)
        email=findViewById(R.id.etemail)
        phone=findViewById(R.id.etphone)
        address=findViewById(R.id.etdeliveryaddress)
        password=findViewById(R.id.etpass)
        confirmpass=findViewById(R.id.etconfirmpass)
        warning=findViewById(R.id.txtwarning)
        warning.visibility = View.INVISIBLE

        sharedpref=getSharedPreferences("FoodappPreference", Context.MODE_PRIVATE)

        txtback.setOnClickListener{
            val intent= Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        btnRegister.setOnClickListener{
            if(password.text.toString()==confirmpass.text.toString()) {
                if(password.text.toString().length>=4) {
                    warning.visibility = View.INVISIBLE

                    val queue = Volley.newRequestQueue(this@SignUpActivity)
                    val url = "http://13.235.250.119/v2/register/fetch_result"

                    if (ConnectionManager().checkConnectivity(this@SignUpActivity)) {


                        val jsonParams = JSONObject()
                        jsonParams.put("name", name.text.toString())
                        jsonParams.put("email", email.text.toString())
                        jsonParams.put("mobile_number", phone.text.toString())
                        jsonParams.put("address", address.text.toString())
                        jsonParams.put("password", password.text.toString())

                        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonParams,
                            Response.Listener {
                                val data = it.getJSONObject("data")
                                try {
                                    val success = data.getBoolean("success")
                                    if (success) {
                                        Toast.makeText(
                                            this@SignUpActivity,
                                            "Registered",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        val errorMessage = data.getString("errorMessage")
                                        Toast.makeText(
                                            this@SignUpActivity,
                                            errorMessage,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                } catch (e: JSONException) {
                                    Toast.makeText(
                                        this@SignUpActivity,
                                        "Json Error Occurred!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            }, Response.ErrorListener {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Volley Error Occurred",
                                    Toast.LENGTH_LONG
                                ).show()
                            }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val header = HashMap<String, String>()
                                header["Content-type"] = "application/json"
                                header["token"] = "d4d68fb2f69f29"
                                return header
                            }
                        }
                        queue.add(jsonObjectRequest)

                    } else {
                        Toast.makeText(this@SignUpActivity, "No Internet Found", Toast.LENGTH_LONG)
                            .show()
                    }
                }else{
                    warning.text=getString(R.string.pass_atleast_4)
                    warning.visibility = View.VISIBLE
                }


            }else{
                warning.text=getString(R.string.pass_not_match)
                warning.visibility = View.VISIBLE
            }



        }
    }
}
