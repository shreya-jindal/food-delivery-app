package com.shreya.foodapp.datasbase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="OrderData")
data class OrderEntity (
    @PrimaryKey val dish_Id:String,
    @ColumnInfo(name="Restraunt_id") val res_id:String,
    @ColumnInfo(name="ItemName") val itemName:String,
    @ColumnInfo(name = "price") var price:String
)