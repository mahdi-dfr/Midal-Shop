package com.example.midallshop.ui.features.product

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.midallshop.model.data.Comment
import com.example.midallshop.model.data.Product
import com.example.midallshop.model.repository.cart.CartRepository
import com.example.midallshop.model.repository.product.ProductRepository
import com.example.midallshop.utils.EMPTY_PRODUCT
import com.example.midallshop.utils.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository,
    private val cartRepository: CartRepository,
) : ViewModel() {

    val thisProduct = mutableStateOf<Product>(EMPTY_PRODUCT)
    val productComments = mutableStateOf<List<Comment>>(listOf())
    val loadingAnim = mutableStateOf(false)
    val cartSize = mutableStateOf(0)

    fun loadProducts(productId: String, isNetConnected: Boolean) {

        getProductById(productId)
        getComments(productId)
        getCartSize()

    }

    private fun getProductById(productId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            val result = repository.getProductById(productId)
            thisProduct.value = result
        }
    }

    private fun getComments(productId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            val result = repository.getCommentsFromServer(productId)
            productComments.value = result
        }
    }

    fun addComment(
        comment: String,
        productId: String, onResultMessage: (String) -> Unit,
    ) {

        viewModelScope.launch(coroutineExceptionHandler) {
            repository.addComment(comment, productId, onResultMessage)
        }

    }

    fun addToCart(productId: String, onResultMessage: (String)-> Unit){
        viewModelScope.launch(coroutineExceptionHandler) {
            loadingAnim.value = true

            cartRepository.addToCart(productId)
            delay(700)
            loadingAnim.value = false
        }
    }

    fun getCartSize(){
        viewModelScope.launch(coroutineExceptionHandler) {

            val result = cartRepository.getCartQuantity()
            cartSize.value = result
        }
    }

}