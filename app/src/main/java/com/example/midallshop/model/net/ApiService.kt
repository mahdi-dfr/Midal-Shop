package com.example.midallshop.model.net

import com.example.midallshop.model.data.*
import com.example.midallshop.model.repository.TokenInMemory
import com.example.midallshop.utils.BASE_URL
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("signIn")
    suspend fun loginUser(
        @Body jsonObject: JsonObject,
    ): LoginResponse

    @POST("signUp")
    suspend fun signUpUser(
        @Body jsonObject: JsonObject,
    ): LoginResponse

    @GET("refreshToken")
    fun refreshToken(): Call<LoginResponse>

    @GET("getProducts")
    suspend fun getAllProducts(): ProductResponse

    @GET("getSliderPics")
    suspend fun getAds(): AdsResponse

    @POST("getComments")
    suspend fun getComments(
        @Body jsonObject: JsonObject,
    ): CommentResponse

    @POST("addNewComment")
    suspend fun addNewComment(
        @Body jsonObject: JsonObject,
    ): AddCommentResponse

    @POST("addToCart")
    suspend fun addToCart(
        @Body jsonObject: JsonObject,
    ): CartResponse

    @GET("getUserCart")
    suspend fun getUserCart(): UserCartInfo

    @POST("removeFromCart")
    suspend fun removeFromCart(
        @Body jsonObject: JsonObject
    ): CartResponse

    @POST("submitOrder")
    suspend fun submitOrder(
        @Body jsonObject: JsonObject
    ): PurchaseResponse

    @POST("checkout")
    suspend fun checkout(
        @Body jsonObject: JsonObject
    ): Checkout

}

fun createRetrofit(): ApiService {

    val okHttpClient = OkHttpClient.Builder().addInterceptor {
        val oldRequest = it.request()
        val newRequest = oldRequest.newBuilder()
        if (TokenInMemory.token != null) {
            newRequest.addHeader("Authorization", TokenInMemory.token!!)
        }
        newRequest.method(oldRequest.method, oldRequest.body)
        it.proceed(newRequest.build())
    }.build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    return retrofit.create(ApiService::class.java)

}