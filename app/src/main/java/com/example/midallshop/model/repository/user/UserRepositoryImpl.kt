package com.example.midallshop.model.repository.user

import android.content.SharedPreferences
import com.example.midallshop.model.net.ApiService
import com.example.midallshop.model.repository.TokenInMemory
import com.example.midallshop.utils.VALUE_SUCCESS
import com.google.gson.JsonObject

class UserRepositoryImpl(
    private val apiService: ApiService,
    private val sharedPref: SharedPreferences,
) : UserRepository {

    override suspend fun loginUser(email: String, password: String): String {

        var jsonObject = JsonObject().apply {
            addProperty("email", email)
            addProperty("password", password)
        }

        var result = apiService.loginUser(jsonObject)
        if (result.success) {
            TokenInMemory.refreshToken(result.token, email)

            saveToken(result.token)
            saveUserName(email)
            getUserLocationAndPostalCode()
            setUserLoginTime()

            return VALUE_SUCCESS
        }
        return result.message
    }

    override suspend fun registerUser(name: String, email: String, password: String): String {

        var jsonObject = JsonObject().apply {
            addProperty("name", name)
            addProperty("email", email)
            addProperty("password", password)
        }

        var result = apiService.signUpUser(jsonObject)
        if (result.success) {
            TokenInMemory.refreshToken(result.token, email)
            saveToken(result.token)
            saveUserName(email)
            return VALUE_SUCCESS
        }
        return result.message

    }


    override fun logOutUser() {
        sharedPref.edit().remove("token").apply()
        sharedPref.edit().remove("userName").apply()
        TokenInMemory.refreshToken(null, null)
    }

    override fun loadToken() {
        TokenInMemory.refreshToken(getToken(), getUserName())
    }

    override fun saveToken(token: String) {
        sharedPref.edit().putString("token", token).apply()
    }


    override fun getToken(): String? {
        return sharedPref.getString("token", null)
    }

    override fun saveUserName(userName: String) {
        sharedPref.edit().putString("userName", userName).apply()
    }

    override fun getUserName(): String? {
        return sharedPref.getString("userName", null)
    }

    override fun setUserLocationAndPostalCode(location: String, postalCode: String) {
        sharedPref.edit().putString("postalCode", postalCode).apply()
        sharedPref.edit().putString("location", location).apply()

    }

    override fun getUserLocationAndPostalCode(): Pair<String?, String?> {
        val location = sharedPref.getString("location", "Click here to add")
        val postalCode = sharedPref.getString("postalCode","Click here to add")
        return Pair(location, postalCode)
    }

    override fun setUserLoginTime() {

        val time = System.currentTimeMillis()
        sharedPref.edit().putString("time", time.toString()).apply()


    }

    override fun getUserLoginTime() : String?{
        return sharedPref.getString("time", "0")
    }

}