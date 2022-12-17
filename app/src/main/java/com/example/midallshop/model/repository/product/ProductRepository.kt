package com.example.midallshop.model.repository.product

import com.example.midallshop.model.data.Ads
import com.example.midallshop.model.data.Comment
import com.example.midallshop.model.data.Product
import com.google.gson.JsonObject

interface ProductRepository {

    suspend fun getAllProducts(isInternetConnected: Boolean): List<Product>
    suspend fun getAllAds(isInternetConnected: Boolean): List<Ads>

    suspend fun getByCategoryName(category: String): List<Product>

    suspend fun getProductById(productId: String): Product

    suspend fun getCommentsFromServer(productId: String) : List<Comment>
    suspend fun addComment( comment: String, productId: String , onResultMessage: (String)-> Unit)



}