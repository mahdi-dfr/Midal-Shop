package com.example.midallshop.model.data

import com.google.gson.annotations.SerializedName

data class UserCartInfo(

    @SerializedName("success")
    val success: Boolean,
    @SerializedName("productList")
    val productList: List<Product>,
    @SerializedName("totalPrice")
    val totalPrice: Long,
)