package com.shreya.foodapp.datasbase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestrauntDao {
    @Insert
    fun insertRest(restrauntEntity: RestrauntEntity)

    @Delete
    fun deleteRest(restrauntEntity: RestrauntEntity)

    @Query("select * from RestrauntData")
    fun AllRestraunt():List<RestrauntEntity>


    @Query("select * from RestrauntData where rest_Id=:restId")
    fun getRestbyId(restId: String):RestrauntEntity

}