package com.example.midallshop.model.repository.product

import android.util.Log
import com.example.midallshop.model.data.Ads
import com.example.midallshop.model.data.Comment
import com.example.midallshop.model.data.Product
import com.example.midallshop.model.db.ProductDao
import com.example.midallshop.model.net.ApiService
import com.google.gson.JsonObject

class ProductRepositoryImpl(
    private val apiService: ApiService,
    private val productDao: ProductDao,
) : ProductRepository {


    override suspend fun getAllProducts(isInternetConnected: Boolean): List<Product> {
        if (isInternetConnected) {

            // get data from net
            val result = apiService.getAllProducts()
            if (result.success) {
                productDao.insertOrUpdate(result.products)
                return result.products
            }
        } else {
            // get data from local
            return productDao.getProducts()
        }
        return listOf()
    }

    override suspend fun getAllAds(isInternetConnected: Boolean): List<Ads> {
        if (isInternetConnected) {

            // get ads
            val result = apiService.getAds()
            if (result.success)
                return result.ads
        }
        return listOf()

    }

    override suspend fun getByCategoryName(category: String): List<Product> {
        return productDao.getByCategory(category)
    }

    override suspend fun getProductById(productId: String): Product {
        return productDao.getById(productId)
    }

    override suspend fun getCommentsFromServer(productId: String): List<Comment> {

        val jsonObject = JsonObject().apply {
            addProperty("productId", productId)
        }
        val result = apiService.getComments(jsonObject)
        if (result.success) {
            return result.comments
        }

        return listOf()
    }

    override suspend fun addComment(
        comment: String,
        productId: String,
        onResultMessage: (String) -> Unit,
    ) {
        val jsonObject = JsonObject().apply {
            addProperty("productId", productId)
            addProperty("text", comment)
        }
        val result = apiService.addNewComment(jsonObject)
        if (result.success) {
            onResultMessage(result.message)
        } else
            onResultMessage("Comment Not Saved!")
    }


}