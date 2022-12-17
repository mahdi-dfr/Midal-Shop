package com.example.midallshop.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.midallshop.model.data.Product

@Database(entities = [Product::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
}