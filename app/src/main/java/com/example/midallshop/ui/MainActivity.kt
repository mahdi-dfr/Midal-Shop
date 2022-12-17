package com.example.midallshop.ui

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.midallshop.di.myModules
import com.example.midallshop.model.repository.TokenInMemory
import com.example.midallshop.model.repository.user.UserRepository
import com.example.midallshop.ui.features.IntroScreen
import com.example.midallshop.ui.features.cart.CartScreen
import com.example.midallshop.ui.features.category.CategoryScreen
import com.example.midallshop.ui.features.main.MainScreen
import com.example.midallshop.ui.features.product.ProductScreen
import com.example.midallshop.ui.features.profile.ProfileScreen
import com.example.midallshop.ui.features.signInView.SignInScreen
import com.example.midallshop.ui.features.signupView.SignUpScreen
import com.example.midallshop.ui.theme.MidallShopTheme
import com.example.midallshop.utils.KEY_CATEGORY_ARG
import com.example.midallshop.utils.KEY_PRODUCT_ARG
import com.example.midallshop.utils.RoutesName
import dev.burnoo.cokoin.Koin
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.KoinNavHost
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        setContent {

            Koin(appDeclaration = {
                androidContext(this@MainActivity)
                modules(myModules)
            }) {
                MidallShopTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background) {
                        val repository: UserRepository = get()
                        repository.loadToken()
                        MidallShop()

                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MidallShopTheme {
        MidallShop()
    }
}

@Composable
fun MidallShop() {
    val navController = rememberNavController()
    KoinNavHost(navController = navController,
        startDestination = RoutesName.MainScreen.route) {

        composable(RoutesName.MainScreen.route) {

            if (TokenInMemory.token == null)
                IntroScreen()
            else
                MainScreen()
        }

        composable(RoutesName.ProfileScreen.route) {
            ProfileScreen()
        }

        composable(RoutesName.CartScreen.route) {
            CartScreen()
        }

        composable(RoutesName.SignInScreen.route) {
            SignInScreen()
        }

        composable(RoutesName.SignUpScreen.route) {
            SignUpScreen()
        }

        composable(
            route = RoutesName.ProductScreen.route + "/" + "{$KEY_PRODUCT_ARG}",
            arguments = listOf(navArgument(KEY_PRODUCT_ARG) {
                type = NavType.StringType
            })
        ) {

            ProductScreen(it.arguments!!.getString(KEY_PRODUCT_ARG, "null"))

        }


        composable(
            route = RoutesName.CategoryScreen.route + "/" + "{$KEY_CATEGORY_ARG}",
            arguments = listOf(navArgument(KEY_CATEGORY_ARG) {
                type = NavType.StringType
            })
        ) {
            CategoryScreen(it.arguments!!.getString(KEY_CATEGORY_ARG)!!)

        }
    }
}
