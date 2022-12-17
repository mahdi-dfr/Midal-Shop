package com.example.midallshop.ui.features.category

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.midallshop.model.data.Product
import com.example.midallshop.model.repository.product.ProductRepository
import com.example.midallshop.utils.coroutineExceptionHandler
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: ProductRepository) : ViewModel() {

    val productByCategory = mutableStateOf<List<Product>>(listOf())

    fun getProductByCategory(category: String) {

        viewModelScope.launch(coroutineExceptionHandler) {
            val result = repository.getByCategoryName(category)
            productByCategory.value = result
        }

    }

}