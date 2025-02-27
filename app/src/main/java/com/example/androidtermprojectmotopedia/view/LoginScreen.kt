package com.example.androidtermprojectmotopedia.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.androidtermprojectmotopedia.model.UserWithId
import com.example.androidtermprojectmotopedia.repository.UserPreferencesRepository
import com.example.androidtermprojectmotopedia.repository.UserRepository
import com.example.androidtermprojectmotopedia.viewModel.MotorcycleViewModel
import com.example.androidtermprojectmotopedia.viewModel.UserViewModel
import com.example.androidtermprojectmotopedia.viewModel.UserViewModelFactory

@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    loginButtonClicked: (String, String) -> Unit
) {
    // States for text fields
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Top-level Box for background or padding
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Main column in the center
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App title or logo
            Text(
                text = "MotoPedia",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Log in to continue",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            // Error message if present
            if (!errorMessage.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login button
            Button(
                onClick = { loginButtonClicked(email, password) },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(48.dp)
            ) {
                Text("Log In")
            }
        }
    }
}



@Composable
fun LoginScreen(userViewModel: UserViewModel,
                motorcycleViewModel: MotorcycleViewModel
) {
    val currentUser by userViewModel.currentUser.collectAsState(initial = null)
    val errorMessage by userViewModel.errorMessage.collectAsState()

    // Ensure we load the current user from DataStore/Firestore
    LaunchedEffect(Unit) {
        userViewModel.loadCurrentUser()
    }

    if (currentUser?.docId.isNullOrEmpty()) {
        // Show login UI
        // Wrap in a Surface with a consistent background color
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginPage(
                loginButtonClicked = { email, password ->
                    userViewModel.loginUser(email, password)
                },
                errorMessage = errorMessage
            )
        }
    } else {
        // Already logged in
        MainAppScreen(userViewModel = userViewModel, motorcycleViewModel = motorcycleViewModel)
    }
}

