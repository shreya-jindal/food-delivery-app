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

class FavAdapter(val context: Context,val itemList: List<RestrauntEntity>):RecyclerView.Adapter<FavAdapter.FavViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_row_home,parent,false)
        return FavViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val rest=itemList[position]

        holder.id=rest.rest_Id.toString()
        holder.RestName.text=rest.restName
        holder.Price.text="Rs.${rest.cost_for_one}/person"
        holder.Rating.text=rest.restRating
        
        Picasso.get().load(rest.image).error(R.drawable.restauran).into(holder.imgRest)

        //checking for favourites
        val RestEntity=RestrauntEntity(
            rest.rest_Id,
            rest.restName,
            rest.restRating,
            rest.cost_for_one,
            rest.image)
        
        var checkFav=DBAsyncTask(context,RestEntity,1).execute()
        var fav:Boolean=checkFav.get()
        holder.checked=fav
        if(!fav){
            holder.btnfav.setBackgroundResource(R.drawable.ic_fav_button)
        }else {
            holder.btnfav.setBackgroundResource(R.drawable.ic_fav_button2)
        }

        holder.FavRLayout.setOnClickListener {
            val intent= Intent(context,RestDetailActivity::class.java)
            intent.putExtra("id",holder.id)
            intent.putExtra("RestName",holder.RestName.text.toString())
            context.startActivity(intent)
        }

        holder.btnfav.setOnClickListener{
            checkFav=DBAsyncTask(context,RestEntity,1).execute()
            fav=checkFav.get()
            if(fav){
                val async =DBAsyncTask(context,RestEntity,3).execute()
                async.get()
                it.setBackgroundResource(R.drawable.ic_fav_button)
                holder.checked=fav
            }else {
                val async =DBAsyncTask(context,RestEntity,2).execute()
                async.get()
                it.setBackgroundResource(R.drawable.ic_fav_button2)
                fav=true
                holder.checked=fav
            }
        }
        holder.checked=false

    }

    class FavViewHolder(view: View): RecyclerView.ViewHolder(view) {
        lateinit var id:String
        val imgRest:ImageView=view.findViewById(R.id.imgHomeRest)
        val RestName:TextView=view.findViewById(R.id.txtHomeRestName)
        val Price:TextView=view.findViewById(R.id.txtHomePrice)
        val Rating:TextView=view.findViewById(R.id.txtHomeRating)
        val FavRLayout:RelativeLayout=view.findViewById(R.id.homeRLayout)
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