package com.shreya.foodapp.model

import android.widget.TextView

data class Restraunt (
    val id:String,
    val name:String,
    val price:String,
    val rating:String,
    val image:String,
    var fav:Boolean
)