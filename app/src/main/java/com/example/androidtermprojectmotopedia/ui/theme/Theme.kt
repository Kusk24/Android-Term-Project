package com.example.androidtermprojectmotopedia.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)
//
//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
//)

private val LightColorScheme = lightColorScheme(
    primary = GoldAccent,           // Gold accent
    secondary = TealAccent,         // Teal accent
    tertiary = TertiaryAccent,      // Warm accent

    background = BeigeBackground,
    surface = SoftBeige,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = DarkGray,
    onSurface = DarkGray,

    surfaceVariant = CardLight, // A custom, soft light color for cards, for example
)

private val DarkColorScheme = darkColorScheme(
    primary = GoldAccentDark,        // Gold accent
    secondary = TealAccentDark,      // Teal accent
    tertiary = TertiaryAccentDark,   // Warm accent

    background = DarkBackground,     // Deep black background
    surface = DarkSurface,           // Dark surface for cards/modals
    onPrimary = Color.Black,         // Text on primary elements
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = LightGrayText,    // Light gray text on background
    onSurface = LightGrayText,        // Light gray text on surfaces

    surfaceVariant = CardDark, // A custom, soft light color for cards, for example
)


@Composable
fun AndroidTermProjectMotopediaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = when {

        darkTheme -> DarkColorScheme
        else -> LightColorScheme

    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
