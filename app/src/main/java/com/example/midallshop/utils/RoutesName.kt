package com.example.midallshop.utils

sealed class RoutesName(val route : String){

    object MainScreen : RoutesName("MainScreen")
    object IntroScreen : RoutesName("IntroScreen")
    object ProductScreen : RoutesName("ProductScreen")
    object CartScreen : RoutesName("CartScreen")
    object SignInScreen : RoutesName("SignInScreen")
    object SignUpScreen : RoutesName("SignUpScreen")
    object ProfileScreen : RoutesName("ProfileScreen")
    object NoInternetScreen : RoutesName("NoInternetScreen")
    object CategoryScreen : RoutesName("CategoryScreen")

}
