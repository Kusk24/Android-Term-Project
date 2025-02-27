package com.example.androidtermprojectmotopedia.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtermprojectmotopedia.viewModel.MotorcycleViewModel
import com.example.androidtermprojectmotopedia.viewModel.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun RootScreen(
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier,
    motorcycleViewModel: MotorcycleViewModel
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
            delay(1000) // 2-second delay
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
            true -> MainAppScreen(
                userViewModel = userViewModel,
                motorcycleViewModel = motorcycleViewModel
            )
            false -> LoginScreen(
                userViewModel = userViewModel,
                motorcycleViewModel = motorcycleViewModel,
            )
            // null shouldn't happen here, but if it does, you could fallback to SplashScreen or a default.
            null -> SplashScreen()
        }
    }
}


@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Using the beige background instead of onBackground
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
                color = MaterialTheme.colorScheme.primary, // Gold accent
                fontSize = 50.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle or tagline
            Text(
                text = "Your Motorcycle Encyclopedia",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary, // Using teal as secondary accent
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Optional: Add a small accent element
            Box(
                modifier = Modifier
                    .size(80.dp, 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}