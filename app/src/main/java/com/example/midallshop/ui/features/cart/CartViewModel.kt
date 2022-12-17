package com.example.midallshop.ui.features.cart

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.midallshop.model.data.Product
import com.example.midallshop.model.repository.cart.CartRepository
import com.example.midallshop.model.repository.user.UserRepository
import com.example.midallshop.utils.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: CartRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    val userProducts = mutableStateOf<List<Product>>(listOf<Product>())
    val totalPrice = mutableStateOf("0")
    val isChangingNumber = mutableStateOf(Pair("", false))
    val showInfoDialog = mutableStateOf(false)


    fun getUserCartInfo() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val result = repository.getCartProducts()
            if (result.success) {
                userProducts.value = result.productList
                totalPrice.value = result.totalPrice.toString()
            }

        }
    }

    fun addItem(productId: String) {

        viewModelScope.launch(coroutineExceptionHandler) {

            isChangingNumber.value = isChangingNumber.value.copy(productId, true)

            val isSuccess = repository.addToCart(productId)
            if (isSuccess) {
                getUserCartInfo()
            }

            delay(100)
            isChangingNumber.value = isChangingNumber.value.copy(productId, false)

        }

    }

    fun removeItem(productId: String) {

        viewModelScope.launch(coroutineExceptionHandler) {

            isChangingNumber.value = isChangingNumber.value.copy(productId, true)

            val isSuccess = repository.removeFromCart(productId)
            if (isSuccess) {
                getUserCartInfo()
            }

            delay(100)
            isChangingNumber.value = isChangingNumber.value.copy(productId, false)

        }

    }

    fun purchaseAll(
        address: String,
        postalCode: String,
        onPurchaseResult: (String, Boolean) -> Unit,
    ) {
        viewModelScope.launch {
            val result = repository.submitOrder(address, postalCode)
            onPurchaseResult(result.paymentLink, result.success)
        }
    }

    fun getLocationInfo(): Pair<String?, String?> {
        return userRepository.getUserLocationAndPostalCode()
    }

    fun setUserLocation(address: String, postalCode: String) {
        userRepository.setUserLocationAndPostalCode(address, postalCode)
    }

    fun setPaymentStatus(status: Int) {
        repository.setPurchaseStatus(status)
    }


}