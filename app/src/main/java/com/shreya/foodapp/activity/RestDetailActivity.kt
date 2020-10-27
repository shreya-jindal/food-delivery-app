package com.shreya.foodapp.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shreya.foodapp.R
import com.shreya.foodapp.adapter.RestDetailAdapter
import com.shreya.foodapp.datasbase.OrderDatabase
import com.shreya.foodapp.datasbase.OrderEntity
import com.shreya.foodapp.model.Restraunt
import com.shreya.foodapp.model.menuItem
import com.shreya.foodapp.util.ConnectionManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONException

class RestDetailActivity : AppCompatActivity() {

    lateinit var recyclerDetail: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var adapter: RestDetailAdapter
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var btntoCart:Button
    var restid:String?="100"
    var restName:String?="Menu"
    var itemList= arrayListOf<menuItem>()

    var myOrder= listOf<OrderEntity>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rest_detail)

        progressBar = findViewById(R.id.progressBar)
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        recyclerDetail=findViewById(R.id.recyclerDetail)
        layoutManager=LinearLayoutManager(this)
        btntoCart=findViewById(R.id.btnProceedtoCart)


        setUpToolbar()
        if (intent != null) {
            restid = intent.getStringExtra("id")
            restName=intent.getStringExtra("RestName")

            setSupportActionBar(toolbar)
            supportActionBar?.title=restName
        } else {
            Toast.makeText(this, "Some Unexpected Error Occurred!!", Toast.LENGTH_LONG).show()
            finish()
        }
        title=restName

        btntoCart.setOnClickListener{
            val intent=Intent(this,CartActivity::class.java)
            startActivity(intent)
            finish()
        }

        if (restid == "100") {
            Toast.makeText(this, "Some Unexpected Error Occurred!!", Toast.LENGTH_LONG).show()
            finish()
        } else {

            val queue = Volley.newRequestQueue(this)
            val url = " http://13.235.250.119/v2/restaurants/fetch_result/$restid"
            println(url)

            if (ConnectionManager().checkConnectivity(this)) {

                try {
                    progressLayout.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null,
                        Response.Listener {
                            val data1 = it.getJSONObject("data")
                            val success = data1.getBoolean("success")

                            if (success) {

                                val data = data1.getJSONArray("data")
                                for (i in 0 until data.length()) {
                                    val jsonObject = data.getJSONObject(i)
                                    val menuitem = menuItem(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("cost_for_one"),
                                        jsonObject.getString("restaurant_id")
                                    )
                                    itemList.add(menuitem)
                                }
                                adapter = RestDetailAdapter(this,itemList)
                                recyclerDetail.adapter = adapter
                                recyclerDetail.layoutManager = layoutManager
                            } else {
                                Toast.makeText(
                                    this,
                                    "Some Error Occurred!!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }


                        }, Response.ErrorListener {
                            Toast.makeText(this, "Volley Error!!", Toast.LENGTH_LONG)
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
                } catch (e: JSONException) {
                    Toast.makeText(this, "json Error!!", Toast.LENGTH_LONG).show()
                }


            } else {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Not Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this)
                }
                dialog.create()
                dialog.show()
            }

        }

        //Proceed to Cart
        /*myOrder=RetrieveOrder(this).execute().get()
        if(myOrder.isEmpty())
        {
            btntoCart.visibility=View.INVISIBLE
        }
        else
        {
            btntoCart.visibility=View.VISIBLE
        }*/

        btntoCart.setOnClickListener{

            val intent=Intent(this, CartActivity::class.java)
            startActivity(intent)
            finish()
        }



    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Toolbar"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }



    class CancelOrder(val context:Context):AsyncTask<Void,Void,Boolean>()
    {
        override fun doInBackground(vararg params: Void?): Boolean {

            val db= Room.databaseBuilder(context, OrderDatabase::class.java,"Order-db").build()
            db.orderDao().removeAll()
            return true
        }

    }


    override fun onBackPressed()
    {
        CancelOrder(this@RestDetailActivity).execute().get()
        super.onBackPressed()
    }
}