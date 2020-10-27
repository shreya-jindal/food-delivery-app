package com.shreya.foodapp.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shreya.foodapp.R
import com.shreya.foodapp.adapter.CartAdapter
import com.shreya.foodapp.adapter.FavAdapter
import com.shreya.foodapp.adapter.RestDetailAdapter
import com.shreya.foodapp.datasbase.OrderDatabase
import com.shreya.foodapp.datasbase.OrderEntity
import com.shreya.foodapp.datasbase.RestrauntDatabase
import com.shreya.foodapp.datasbase.RestrauntEntity
import com.shreya.foodapp.fragment.FavRestFragment
import com.shreya.foodapp.model.menuItem
import com.shreya.foodapp.util.ConnectionManager
import org.json.JSONException

class CartActivity : AppCompatActivity() {

    lateinit var recyclerCart: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var adapter: CartAdapter
    lateinit var sharedPref:SharedPreferences
    lateinit var btnOrder:Button
    var myOrder= listOf<OrderEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        btnOrder=findViewById(R.id.btnOrder)
        recyclerCart=findViewById(R.id.recyclerCart)
        layoutManager= LinearLayoutManager(this)
        sharedPref=getSharedPreferences("FoodappPreference", Context.MODE_PRIVATE)

        myOrder= RetrieveOrder(this).execute().get()


        adapter=CartAdapter(this, myOrder,sharedPref)
        recyclerCart.adapter=adapter
        recyclerCart.layoutManager=layoutManager

        var total=sharedPref.getInt("total",0)
        btnOrder.text="Place Order(Total: Rs.$total)"

    }

    class RetrieveOrder(val context: Context): AsyncTask<Void, Void, List<OrderEntity>>()
    {
        override fun doInBackground(vararg params: Void?):List<OrderEntity> {

            val db= Room.databaseBuilder(context, OrderDatabase::class.java,"Order-db").build()
            return db.orderDao().AllDish()
        }

    }

    override fun onBackPressed() {
        val intent=Intent(this,RestDetailActivity::class.java)
        startActivity(intent)
        finish()

    }
}