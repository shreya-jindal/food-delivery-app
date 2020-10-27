package com.shreya.foodapp.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.shreya.foodapp.R
import com.shreya.foodapp.activity.RestDetailActivity
import com.shreya.foodapp.datasbase.RestrauntDatabase
import com.shreya.foodapp.datasbase.RestrauntEntity
import com.shreya.foodapp.model.Restraunt
import com.squareup.picasso.Picasso

class HomeAdapter(val context: Context,val itemList: ArrayList<Restraunt>):RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_row_home,parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val rest=itemList[position]

        holder.id=rest.id
        holder.RestName.text=rest.name
        holder.Price.text="Rs.${rest.price}/person"
        holder.Rating.text=rest.rating
        holder.checked=rest.fav
        Picasso.get().load(rest.image).error(R.drawable.restauran).into(holder.imgRest)
        println("checked ${rest.name}=${holder.checked}")
        if(!rest.fav){
            holder.btnfav.setBackgroundResource(R.drawable.ic_fav_button)
        }else {
            holder.btnfav.setBackgroundResource(R.drawable.ic_fav_button2)
        }

        holder.homeRLayout.setOnClickListener {
            val intent= Intent(context,RestDetailActivity::class.java)
            intent.putExtra("id",holder.id)
            intent.putExtra("RestName",holder.RestName.text.toString())
            context.startActivity(intent)
        }

        //checking for favourites
        val RestEntity=RestrauntEntity(
            rest.id.toInt(),
            rest.name,
            rest.rating,
            rest.price,
            rest.image)

        val checkFav=DBAsyncTask(context,RestEntity,1).execute()
        rest.fav=checkFav.get()

        holder.btnfav.setOnClickListener{
            if(rest.fav){
                val async =DBAsyncTask(context,RestEntity,3).execute()
                async.get()
                it.setBackgroundResource(R.drawable.ic_fav_button)
                rest.fav=false
                holder.checked=rest.fav
            }else {
                val async =DBAsyncTask(context,RestEntity,2).execute()
                async.get()
                it.setBackgroundResource(R.drawable.ic_fav_button2)
                rest.fav=true
                holder.checked=rest.fav
            }
        }
        holder.checked=false

    }

    class HomeViewHolder(view: View): RecyclerView.ViewHolder(view) {
        lateinit var id:String
        val imgRest:ImageView=view.findViewById(R.id.imgHomeRest)
        val RestName:TextView=view.findViewById(R.id.txtHomeRestName)
        val Price:TextView=view.findViewById(R.id.txtHomePrice)
        val Rating:TextView=view.findViewById(R.id.txtHomeRating)
        val homeRLayout:RelativeLayout=view.findViewById(R.id.homeRLayout)
        val btnfav:ImageButton=view.findViewById(R.id.btnHomeFav)
        var checked:Boolean=false
    }

    
    class DBAsyncTask(val context:Context, val RestEntity: RestrauntEntity, val mode:Int): AsyncTask<Void, Void, Boolean>()
    {
        val rest_db= Room.databaseBuilder(context, RestrauntDatabase::class.java,"Restraunt-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode)
            {
                1->{
                    //check
                    val restEntity: RestrauntEntity? =rest_db.restDao().getRestbyId(RestEntity.rest_Id.toString())
                    rest_db.close()
                    return restEntity!=null
                }
                2->{
                    //insert
                    rest_db.restDao().insertRest(RestEntity)
                    rest_db.close()
                    return true
                }
                3->{
                    //remove
                    rest_db.restDao().deleteRest(RestEntity)
                    rest_db.close()
                    return true
                }
            }

            return false
        }

    }
}