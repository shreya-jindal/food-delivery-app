package com.shreya.foodapp.datasbase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface OrderDao {
    @Insert
    fun insertDish(OrderEntity: OrderEntity)

    @Delete
    fun deleteDish(OrderEntity: OrderEntity)

    @Query("select * from OrderData")
    fun AllDish():List<OrderEntity>

    @Query("select * from OrderData where dish_Id=:dishid")
    fun getDishbyId(dishid: String):OrderEntity

    @Query("delete from OrderData")
    fun removeAll()
}