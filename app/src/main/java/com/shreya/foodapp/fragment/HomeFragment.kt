package com.shreya.foodapp.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shreya.foodapp.R
import com.shreya.foodapp.model.Restraunt
import com.shreya.foodapp.adapter.HomeAdapter
import com.shreya.foodapp.datasbase.RestrauntDatabase
import com.shreya.foodapp.datasbase.RestrauntEntity
import com.shreya.foodapp.util.ConnectionManager
import org.json.JSONException
import java.util.stream.DoubleStream.builder


class HomeFragment : Fragment() {

    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var adapter: HomeAdapter
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar: ProgressBar
    val restList=arrayListOf<Restraunt>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_home, container, false)

        recyclerHome=view.findViewById(R.id.recyclerHome)
        layoutManager= LinearLayoutManager(activity)
        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)
        progressLayout.visibility=View.VISIBLE
        progressBar.visibility=View.VISIBLE

        val queue= Volley.newRequestQueue(activity as Context)
        val url= "http://13.235.250.119/v2/restaurants/fetch_result/"

        if(ConnectionManager().checkConnectivity(activity as Context)) {

            try {
                progressLayout.visibility=View.GONE
                progressBar.visibility=View.GONE
                val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null,
                    Response.Listener {
                        val data1 = it.getJSONObject("data")
                        val success = data1.getBoolean("success")

                        if (success) {

                            val data = data1.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val jsonObject = data.getJSONObject(i)
                                val Restdata = Restraunt(
                                    jsonObject.getString("id"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("cost_for_one"),
                                    jsonObject.getString("rating"),
                                    jsonObject.getString("image_url"),
                                    false
                                )
                                restList.add(Restdata)

                            }
                            adapter = HomeAdapter(activity as Context, restList)
                            recyclerHome.adapter = adapter
                            recyclerHome.layoutManager = layoutManager




                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some Error Occurred!!",
                                Toast.LENGTH_LONG
                            ).show()
                        }


                    }, Response.ErrorListener {
                        Toast.makeText(activity as Context, "Volley Error!!", Toast.LENGTH_LONG)
                            .show()

                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val header = HashMap<String, String>()
                        header["token"] = "d4d68fb2f69f29"
                        header["content-type"] = "application/json"
                        return header
                    }

                }
                queue.add(jsonObjectRequest)
            }catch(e:JSONException){
                Toast.makeText(activity as Context, "json Error!!", Toast.LENGTH_LONG).show()
            }


        }else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text,listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }


        return view
    }



}