package com.example.midallshop.di

import android.content.Context
import androidx.room.Room
import com.example.midallshop.model.db.AppDatabase
import com.example.midallshop.model.net.createRetrofit
import com.example.midallshop.model.repository.cart.CartRepository
import com.example.midallshop.model.repository.cart.CartRepositoryImpl
import com.example.midallshop.model.repository.product.ProductRepository
import com.example.midallshop.model.repository.product.ProductRepositoryImpl
import com.example.midallshop.model.repository.user.UserRepository
import com.example.midallshop.model.repository.user.UserRepositoryImpl
import com.example.midallshop.ui.features.cart.CartViewModel
import com.example.midallshop.ui.features.category.CategoryViewModel
import com.example.midallshop.ui.features.main.MainViewModel
import com.example.midallshop.ui.features.product.ProductViewModel
import com.example.midallshop.ui.features.profile.ProfileViewModel
import com.example.midallshop.ui.features.signInView.SignInViewModel
import com.example.midallshop.ui.features.signupView.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var myModules = module {


    single { androidContext().getSharedPreferences("data", Context.MODE_PRIVATE) }
    single { createRetrofit() }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app_dataBase.db").build()
    }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    single<CartRepository> { CartRepositoryImpl(get(), get()) }

    single<ProductRepository> { ProductRepositoryImpl(get(), get<AppDatabase>().productDao()) }

    viewModel { SignUpViewModel(get()) }
    viewModel { ProductViewModel(get(), get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { CartViewModel(get(), get()) }
    viewModel { (isNetConnected: Boolean) -> MainViewModel(get(), get(), isNetConnected) }


}