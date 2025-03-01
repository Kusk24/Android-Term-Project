package com.example.androidtermprojectmotopedia.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtermprojectmotopedia.viewModel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountInformationScreen(
    viewModel: UserViewModel,
    onBackClick: () -> Unit,  // pass a callback that navigates back (e.g. navController.popBackStack())
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Observe currentUser from the ViewModel
    val currentUser = viewModel.currentUser.collectAsState().value
    LaunchedEffect(currentUser) {
        currentUser?.let { userWithId ->
            name = userWithId.user.name
            email = userWithId.user.email
            password = userWithId.user.password
        }
    }

    // A top-level Scaffold for a top app bar + content
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Account Information") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // Main content area
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Update your account details",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            // Row of buttons (Save + Cancel)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onBackClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Cancel")
                }

                OutlinedButton(
                    onClick = {
                        viewModel.updateUser(name, email, password)
                        showSuccessDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green,
                        contentColor = Color.White
                    )
                ) {
                    Text("Save")
                }
            }

            // Success dialog if user info is updated
            if (showSuccessDialog) {
                SuccessDialog(
                    title = "Update Successful",
                    text = "Your account information has been updated!",
                    onDismiss = { showSuccessDialog = false }
                )
            }
        }
    }
}
