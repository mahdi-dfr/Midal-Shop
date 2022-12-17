package com.example.midallshop.ui.features.main

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.midallshop.model.data.Ads
import com.example.midallshop.model.data.Checkout
import com.example.midallshop.model.data.Product
import com.example.midallshop.model.repository.cart.CartRepository
import com.example.midallshop.model.repository.product.ProductRepository
import com.example.midallshop.utils.coroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    isInternetConnected: Boolean
) : ViewModel() {

    val dataProducts = mutableStateOf<List<Product>>(listOf())
    val dataAds = mutableStateOf<List<Ads>>(listOf())
    val showProgressBar = mutableStateOf(false)
    val showPaymentResultDialog = mutableStateOf(false)
    val badgeNumber = mutableStateOf(0)
    val checkoutData = mutableStateOf(Checkout(null, null))


    init {
        refreshAllDataFromNet(isInternetConnected)
    }

    private fun refreshAllDataFromNet(isInternetConnected: Boolean) {

        viewModelScope.launch(coroutineExceptionHandler) {

            if (isInternetConnected)
                showProgressBar.value = true

            delay(1000)

            val newDataProducts = async { productRepository.getAllProducts(isInternetConnected) }

            val newDataAds = async { productRepository.getAllAds(isInternetConnected) }

            updateData(newDataProducts.await(), newDataAds.await())

            showProgressBar.value = false

        }

    }

    fun getCheckoutData() {

        viewModelScope.launch(coroutineExceptionHandler) {

            val result = cartRepository.checkout(cartRepository.getOrderId())
            if (result.success!!) {
                checkoutData.value = result
                showPaymentResultDialog.value = true
            }

        }

    }


    private fun updateData(products: List<Product>, ads: List<Ads>) {
        dataProducts.value = products
        Log.i("TAG", "updateData: "+products)
        dataAds.value = ads
    }

    fun getPaymentStatus(): Int {
        return cartRepository.getPurchaseStatus()
    }

    fun setPaymentStatus(status: Int) {
        cartRepository.setPurchaseStatus(status)
    }

    fun loadBadgeNumber() {

        viewModelScope.launch(coroutineExceptionHandler) {
            badgeNumber.value = cartRepository.getCartQuantity()
        }

    }


}