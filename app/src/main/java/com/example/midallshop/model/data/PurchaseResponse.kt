package com.example.midallshop.model.data

import com.google.gson.annotations.SerializedName

data class PurchaseResponse(
    @SerializedName("orderId")
    var orderId: Int,
    @SerializedName("paymentLink")
    var paymentLink: String,
    @SerializedName("success")
    var success: Boolean
)