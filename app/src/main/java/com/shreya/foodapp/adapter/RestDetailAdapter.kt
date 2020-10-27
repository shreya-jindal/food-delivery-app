package com.shreya.foodapp.adapter

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.shreya.foodapp.R
import com.shreya.foodapp.datasbase.OrderDatabase
import com.shreya.foodapp.datasbase.OrderEntity
import com.shreya.foodapp.datasbase.RestrauntDatabase
import com.shreya.foodapp.datasbase.RestrauntEntity
import com.shreya.foodapp.model.menuItem

class RestDetailAdapter(val context: Context,val itemList:ArrayList<menuItem>):RecyclerView.Adapter<RestDetailAdapter.RestDetailViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestDetailViewHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_row_rest_detail,parent,false)
       return RestDetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RestDetailViewHolder, position: Int) {

        val item=itemList[position]
        val pos=position+1
        holder.index.text=pos.toString()
        holder.name.text=item.name
        holder.price.text="Rs. ${item.price}"



        var added:Boolean=false
        holder.added=added
        if(added){
            holder.btnAdd.setBackgroundResource(R.color.yellow)
            holder.btnAdd.text="Remove"
        }else {
            holder.btnAdd.setBackgroundResource(R.color.orange)
            holder.btnAdd.text="Add"
        }


        val orderEntity=OrderEntity(
            item.itemId,
            item.restId,
            item.name,
            item.price
        )


        holder.btnAdd.setOnClickListener{


            added= DBAsyncTask(context, orderEntity, 1).execute().get()

            if(added){
                val async = DBAsyncTask(context, orderEntity, 3).execute()
                async.get()
                holder.btnAdd.setBackgroundResource(R.color.orange)
                holder.btnAdd.text="Add"

            }else {
                val async = DBAsyncTask(context, orderEntity, 2).execute()
                async.get()
                holder.btnAdd.setBackgroundResource(R.color.yellow)
                holder.btnAdd.text="Remove"


            }



        }
    }

    class RestDetailViewHolder(view: View):RecyclerView.ViewHolder(view){

        val index:TextView=view.findViewById(R.id.txtDetailIndex)
        val name:TextView=view.findViewById(R.id.txtDetailItemName)
        val price:TextView=view.findViewById(R.id.txtDetailPrice)
        val btnAdd:Button=view.findViewById(R.id.btnDetailAdd)
        var added:Boolean=false

    }




    class DBAsyncTask(val context:Context, val orderEntity: OrderEntity, val mode:Int): AsyncTask<Void, Void, Boolean>()
    {
        val order_db= Room.databaseBuilder(context, OrderDatabase::class.java,"Order-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode)
            {
                1->{
                    //check
                    val orderEntity: OrderEntity? =order_db.orderDao().getDishbyId(orderEntity.dish_Id.toString())
                    order_db.close()
                    return orderEntity!=null
                }
                2->{
                    //insert
                    order_db.orderDao().insertDish(orderEntity)
                    order_db.close()
                    return true
                }
                3->{
                    //remove
                    order_db.orderDao().deleteDish(orderEntity)
                    order_db.close()
                    return true
                }
            }

            return false
        }

    }
}