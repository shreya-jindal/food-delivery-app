package com.shreya.foodapp.datasbase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RestrauntEntity::class], version = 1)
abstract class RestrauntDatabase:RoomDatabase() {
    abstract fun restDao():RestrauntDao
}