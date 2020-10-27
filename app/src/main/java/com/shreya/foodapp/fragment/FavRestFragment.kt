package com.shreya.foodapp.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.shreya.foodapp.R
import com.shreya.foodapp.adapter.FavAdapter
import com.shreya.foodapp.datasbase.RestrauntDatabase
import com.shreya.foodapp.datasbase.RestrauntEntity
import com.shreya.foodapp.model.Restraunt


class FavRestFragment : Fragment() {


    lateinit var recyclerFav: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var adapter: FavAdapter
    var dbRestList=listOf<RestrauntEntity>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_fav_rest, container, false)
        recyclerFav=view.findViewById(R.id.recyclerFav)
        layoutManager= LinearLayoutManager(activity)


        dbRestList=RetrieveFav(activity as Context).execute().get()

        if(activity!=null)
        {

            adapter=FavAdapter(activity as Context, dbRestList)
            recyclerFav.adapter=adapter
            recyclerFav.layoutManager=layoutManager
        }

        return view
    }

    class RetrieveFav(val context: Context):AsyncTask<Void,Void,List<RestrauntEntity>>()
    {
        override fun doInBackground(vararg params: Void?):List<RestrauntEntity> {

            val db=Room.databaseBuilder(context,RestrauntDatabase::class.java,"Restraunt-db").build()
            return db.restDao().AllRestraunt()
        }

    }



}