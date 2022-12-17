package com.example.midallshop.model.repository.cart

import com.example.midallshop.model.data.Checkout
import com.example.midallshop.model.data.PurchaseResponse
import com.example.midallshop.model.data.UserCartInfo

interface CartRepository {

    suspend fun addToCart(productId: String): Boolean
    suspend fun getCartQuantity(): Int
    suspend fun getCartProducts(): UserCartInfo
    suspend fun removeFromCart(productId: String): Boolean
    suspend fun submitOrder(address: String, postalCode: String): PurchaseResponse
    suspend fun checkout(orderId: String): Checkout
    fun setPurchaseStatus(status : Int)
    fun getPurchaseStatus(): Int
    fun setOrderId(orderID : String)
    fun getOrderId(): String
}