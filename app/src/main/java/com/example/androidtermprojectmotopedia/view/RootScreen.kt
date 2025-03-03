package com.example.androidtermprojectmotopedia.view

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtermprojectmotopedia.viewModel.MotorcycleViewModel
import com.example.androidtermprojectmotopedia.viewModel.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun RootScreen(
    widthSizeClass: WindowWidthSizeClass,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier,
    motorcycleViewModel: MotorcycleViewModel
) {
    // 1) Try to load the current user from DataStore/Firestore
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
            delay(1200) // 2-second delay
            showSplash = false
        }
    }

    // 5) Decide what to show
    if (showSplash) {
        // Still loading or forcing splash duration
        SplashScreen()
    } else {
        // isLoggedIn is no longer null and shown splash for at least 2s
        when (isLoggedIn) {
            true -> MainAppScreen(
                userViewModel = userViewModel,
                motorcycleViewModel = motorcycleViewModel,
                windowSizeClass = widthSizeClass,
                modifier = modifier
            )
            false -> LoginScreen(
                userViewModel = userViewModel,
                motorcycleViewModel = motorcycleViewModel,
                windowSizeClass = widthSizeClass
                )
            // null shouldn't happen here, but if it does, fallback to SplashScreen or a default.
            null -> SplashScreen()
        }
    }
}


@Composable
fun SplashScreen() {
    // Animation states
    var startAnimation by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }

    // Animation specs
    val circleScale = animateFloatAsState(
        targetValue = if (startAnimation) 15f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "circleScale"
    )

    val contentAlpha = animateFloatAsState(
        targetValue = if (showContent) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            easing = LinearOutSlowInEasing
        ),
        label = "contentAlpha"
    )

    // Trigger animations
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(400)
        showContent = true
    }

    // Background container
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Expanding circle animation - starts small and grows outward
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(40.dp)
                .scale(circleScale.value)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
        )

        // Content that fades in after the circle expands
        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(contentAlpha.value),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
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