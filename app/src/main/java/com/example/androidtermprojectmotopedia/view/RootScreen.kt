package com.example.androidtermprojectmotopedia.view

import android.window.SplashScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.androidtermprojectmotopedia.viewModel.UserViewModel

@Composable
fun RootScreen(
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    val isLoggedIn by userViewModel.isLoggedInState.collectAsState(initial = false)

    if (isLoggedIn) {
        MainAppScreen(userViewModel = userViewModel)
    } else {
        LoginScreen(userViewModel = userViewModel)
    }
}

