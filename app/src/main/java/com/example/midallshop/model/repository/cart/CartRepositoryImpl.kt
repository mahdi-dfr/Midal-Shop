package com.example.midallshop.model.repository.cart

import android.content.SharedPreferences
import com.example.midallshop.model.data.Checkout
import com.example.midallshop.model.data.PurchaseResponse
import com.example.midallshop.model.data.UserCartInfo
import com.example.midallshop.model.net.ApiService
import com.example.midallshop.utils.NO_PAYMENT
import com.google.gson.JsonObject

class CartRepositoryImpl
    (
    private val apiService: ApiService,
    private val sharedPref: SharedPreferences
) : CartRepository {

    override suspend fun addToCart(productId: String): Boolean {

        val jsonObject = JsonObject().apply {
            addProperty("productId", productId)
        }

        val result = apiService.addToCart(jsonObject)
        return result.success
    }


    override suspend fun getCartQuantity(): Int {

        val result = apiService.getUserCart()
        if (result.success) {

            var quantity = 0
            result.productList.forEach {
                quantity += (it.quantity ?: "0").toInt()
            }
            return quantity
        }

        return 0
    }

    override suspend fun getCartProducts(): UserCartInfo {
        return apiService.getUserCart()

    }

    override suspend fun removeFromCart(productId: String): Boolean {
        val jsonObject = JsonObject().apply {
            addProperty("productId", productId)
        }
        val result = apiService.removeFromCart(jsonObject)
        return result.success
    }

    override suspend fun submitOrder(address: String, postalCode: String): PurchaseResponse {

        val jsonObject = JsonObject().apply {
            addProperty("address", address)
            addProperty("postalCode", postalCode)
        }

        val result =  apiService.submitOrder(jsonObject)
        setOrderId(result.orderId.toString())
        return result
    }

    override suspend fun checkout(orderId: String): Checkout {
        val jsonObject = JsonObject().apply {
            addProperty("orderId", orderId)
        }
        return apiService.checkout(jsonObject)
    }

    override fun setPurchaseStatus(status: Int) {
        sharedPref.edit().putInt("status", status).apply()

    }

    override fun getPurchaseStatus(): Int {
        return sharedPref.getInt("status", NO_PAYMENT)

    }

    override fun setOrderId(orderID: String) {
        sharedPref.edit().putString("orderID", orderID).apply()

    }

    override fun getOrderId(): String {
        return sharedPref.getString("orderID", "0")!!
    }


}