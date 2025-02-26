package com.example.androidtermprojectmotopedia.view

import android.window.SplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtermprojectmotopedia.viewModel.UserViewModel

@Composable
fun RootScreen(
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    // 1) Ensure we try to load the current user from DataStore/Firestore
    LaunchedEffect(Unit) {
        userViewModel.loadCurrentUser()
    }

    // 2) Collect the login state (null = still loading, true = logged in, false = logged out)
    val isLoggedIn: Boolean? by userViewModel.isLoggedInState.collectAsState(initial = null)

    // 3) A local flag to show/hide the splash screen
    var showSplash by remember { mutableStateOf(true) }

    // 4) Once isLoggedIn is known (not null), wait 2 seconds, then hide splash
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn != null) {
            kotlinx.coroutines.delay(1000) // 2-second delay
            showSplash = false
        }
    }

    // 5) Decide what to show
    if (showSplash) {
        // Still loading or forcing splash duration
        SplashScreen()
    } else {
        // isLoggedIn is no longer null and we've shown splash for at least 2s
        when (isLoggedIn) {
            true -> MainAppScreen(userViewModel = userViewModel)
            false -> LoginScreen(userViewModel = userViewModel)
            // null shouldn't happen here, but if it does, you could fallback to SplashScreen or a default.
            null -> SplashScreen()
        }
    }
}


@Composable
fun SplashScreen() {
    // If you have a background image, place it in drawable and reference here.
    // e.g. painterResource(R.drawable.splash_background)
    Box(
        modifier = Modifier
            .fillMaxSize()
         .background(Color(0xFFF9F4E2)) // Alternatively, a plain color
    ) {
        // Content in the center
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Large Title
            Text(
                text = "MotoPedia",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 50.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle or tagline
            Text(
                text = "Your Motorcycle Encyclopedia",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

        }}
}

