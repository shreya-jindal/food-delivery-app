package com.shreya.foodapp.datasbase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RestrauntData")
data class RestrauntEntity (
    @PrimaryKey val rest_Id:Int,
    @ColumnInfo(name = "rest_name") val restName:String,
    @ColumnInfo(name="rest_rating") val restRating:String,
    @ColumnInfo(name="cost_for_one") val cost_for_one:String,
    @ColumnInfo(name="image") val image:String

)