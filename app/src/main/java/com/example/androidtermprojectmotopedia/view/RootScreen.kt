package com.example.androidtermprojectmotopedia.view

import android.window.SplashScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.androidtermprojectmotopedia.viewModel.UserViewModel

@Composable
fun RootScreen(
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        userViewModel.loadCurrentUser()
    }
    val isLoggedIn: Boolean? by userViewModel.isLoggedInState.collectAsState(initial = null)

    when (isLoggedIn) {
        null -> {
            // Still loading from DataStore or Firestore
            // Show a splash or loading screen
            SplashScreen()
        }
        true -> {
            // Already logged in
            MainAppScreen(userViewModel = userViewModel)
        }
        false -> {
            // Not logged in
            LoginScreen(userViewModel = userViewModel)
        }
    }
}

@Composable
fun SplashScreen() {
    // Simple placeholder for a loading screen
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

