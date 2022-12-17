package com.example.midallshop.model.repository.user

interface UserRepository {

    // online methods
    suspend fun loginUser(email: String, password: String): String
    suspend fun registerUser(name: String, email: String, password: String): String

    // offline methods
    fun logOutUser()
    fun loadToken()

    fun saveToken(token: String)
    fun getToken(): String?

    fun saveUserName(userName: String)
    fun getUserName(): String?

    fun setUserLocationAndPostalCode(location: String, postalCode: String)
    fun getUserLocationAndPostalCode(): Pair<String?, String?>
    fun setUserLoginTime()
    fun getUserLoginTime(): String?



}