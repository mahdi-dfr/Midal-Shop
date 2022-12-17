package com.example.midallshop.model.repository

object TokenInMemory {
    var token: String? = null
        private set

    var userName: String? = null
        private set

    fun refreshToken(token: String?, userName: String?) {

        this.token = token
        this.userName = userName

    }


}