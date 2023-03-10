package com.example.midallshop.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = DarkBackgroundMain,
    primaryVariant = DarkPrimary,
    secondary = DarkPrimary

)

private val LightColorPalette = lightColors(
    primary = Blue,
    primaryVariant = Blue,
    secondary = Blue,
    background = BackgroundMain

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun MidallShopTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = LightColorPalette
       /* if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }*/

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )

}