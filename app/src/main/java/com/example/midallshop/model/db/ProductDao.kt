package com.example.midallshop.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.midallshop.model.data.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(data: List<Product>)

    @Query("SELECT * FROM product_table")
    suspend fun getProducts(): List<Product>

    @Query("SELECT * FROM product_table WHERE productId = :productId")
    suspend fun getById(productId: String): Product

    @Query("SELECT * FROM product_table WHERE category = :categoryName")
    suspend fun getByCategory(categoryName: String): List<Product>

}