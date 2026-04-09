package com.rubengarcia.rubensapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DropDuchyColorScheme = lightColorScheme(
    primary = DropDuchyPrimary100,
    secondary = DropDuchyAlly100,
    tertiary = DropDuchyEnemy100,

    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = DropDuchyPrimary100,
    onSurface = DropDuchyPrimary100,

    background = DropDuchyNeutral80,
    surface = DropDuchyNeutral80,
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun RubensAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DropDuchyColorScheme,
        typography = Typography,
        content = content
    )
}