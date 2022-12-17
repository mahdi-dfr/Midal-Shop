package com.example.midallshop.ui.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.midallshop.R
import com.example.midallshop.ui.theme.Blue
import com.example.midallshop.ui.theme.MidallShopTheme
import com.example.midallshop.utils.RoutesName
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MidallShopTheme {
        IntroScreen()
    }
}

@Composable
fun IntroScreen() {

    val uiController = rememberSystemUiController()
    SideEffect { uiController.setStatusBarColor(Blue) }

    val navigation = getNavController()

    Image(
        painter = painterResource(id = R.drawable.img_intro),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.78f),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = { navigation.navigate(RoutesName.SignUpScreen.route) },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {

            Text(text = "Sign Up")
        }

        Button(
            onClick = { navigation.navigate(RoutesName.SignInScreen.route) },
            modifier = Modifier.fillMaxWidth(0.7f),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
        ) {

            Text(
                text = "Sign In",
                color = Blue
            )
        }

    }


}