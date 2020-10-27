package com.shreya.foodapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Header
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shreya.foodapp.R
import com.shreya.foodapp.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class ForgotPassActivity : AppCompatActivity() {

    lateinit var etphone:EditText
    lateinit var etemail:EditText
    lateinit var btnNext:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        etphone=findViewById(R.id.etphone)
        etemail=findViewById(R.id.etemail)
        btnNext=findViewById(R.id.btnnext)

        val queue=Volley.newRequestQueue(this@ForgotPassActivity)
        val url="http://13.235.250.119/v2/forgot _password/fetch_result"

        val jsonObject=JSONObject()
        jsonObject.put("mobile_number",etphone.text.toString())
        jsonObject.put("email",etemail.text.toString())


        btnNext.setOnClickListener {

            if (ConnectionManager().checkConnectivity(this@ForgotPassActivity)) {
                val JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonObject,
                    Response.Listener {
                        try {
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if (success) {
                                val firstTry = data.getBoolean("first_try")
                                if (firstTry) {
                                    val intent = Intent(
                                        this@ForgotPassActivity,
                                        ForgotPass2Activity::class.java
                                    )
                                    startActivity(intent)
                                    finish()

                                } else {
                                    Toast.makeText(this, "try after 24 hrs", Toast.LENGTH_LONG)
                                        .show()
                                }
                            } else {
                                val errorMessage=data.getString("errorMessage")
                                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }catch (e:JSONException){
                            Toast.makeText(this,"Json Error Occurred!",Toast.LENGTH_LONG).show()
                        }

                    }, Response.ErrorListener {
                            Toast.makeText(this,"volley Error Occurred!",Toast.LENGTH_LONG).show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val header = HashMap<String, String>()
                        header["Content-Type"] = "application/json"
                        header["token"] = "d4d68fb2f69f29"
                        return header
                    }
                }
                queue.add(JsonObjectRequest)
            }else{
                Toast.makeText(this,"Internet not found",Toast.LENGTH_LONG).show()

            }
        }
    }
}
