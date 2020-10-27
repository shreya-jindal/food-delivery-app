package com.shreya.foodapp.adapter

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.shreya.foodapp.R
import com.shreya.foodapp.datasbase.OrderDatabase
import com.shreya.foodapp.datasbase.OrderEntity
import com.shreya.foodapp.model.menuItem


class CartAdapter(val context: Context,val cartItem:List<OrderEntity>,var sharedPreferences: SharedPreferences):RecyclerView.Adapter<CartAdapter.CartViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_row_cart,parent,false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cartItem.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item=cartItem[position]
        holder.name.text=item.itemName
        holder.price.text="Rs. ${item.price}"

        holder.btnRemove.setOnClickListener{
            RemoveDish(context,item).execute().get()
            sharedPreferences.edit().putInt("total",total()).apply()
        }


    }
    class CartViewHolder(val view: View):RecyclerView.ViewHolder(view){
        val btnRemove: ImageButton =view.findViewById(R.id.btnRemove)
        val name:TextView=view.findViewById(R.id.txtCartName)
        val price:TextView=view.findViewById(R.id.txtCartPrice)

    }





    fun total():Int{
        val order=RetrieveOrder(context).execute().get()
        var total=0
        for(dish in order){
            total+=dish.price.toInt()
        }
        return total
    }

    class RetrieveOrder(val context: Context): AsyncTask<Void, Void, List<OrderEntity>>()
    {
        override fun doInBackground(vararg params: Void?):List<OrderEntity> {

            val db= Room.databaseBuilder(context, OrderDatabase::class.java,"Order-db").build()
            return db.orderDao().AllDish()
        }

    }

    class RemoveDish(val context: Context,val item:OrderEntity):AsyncTask<Void,Void,Boolean>()
    {
        override fun doInBackground(vararg params: Void?): Boolean {

            val db= Room.databaseBuilder(context, OrderDatabase::class.java,"Order-db").build()
            db.orderDao().deleteDish(item)
            return true

        }

    }
}