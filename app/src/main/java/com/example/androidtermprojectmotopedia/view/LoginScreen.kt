package com.example.androidtermprojectmotopedia.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.androidtermprojectmotopedia.viewModel.UserViewModel
import com.example.androidtermprojectmotopedia.viewModel.UserViewModelFactory

@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    errorMessage: String? = null,  // New parameter for error messages.
    loginButtonClicked: (String, String) -> Unit
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (item1, item2, item3, item4, errorText) = createRefs()
        val line1 = createGuidelineFromTop(0.6f)

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Text(
            text = "Welcome To MotoPedia",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier.constrainAs(item4) {
                bottom.linkTo(item1.top, margin = 75.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.constrainAs(item1) {
                bottom.linkTo(item2.top, margin = 50.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.constrainAs(item2) {
                bottom.linkTo(line1, margin = 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        // Display error message if it's not null.
        if (!errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = androidx.compose.ui.graphics.Color.Red,
                modifier = Modifier.constrainAs(errorText) {
                    top.linkTo(item2.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }

        Button(
            onClick = { loginButtonClicked(email, password) },
            modifier = Modifier
                .constrainAs(item3) {
                    top.linkTo(line1, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .width(100.dp)
                .height(50.dp)
        ) {
            Text("Log In")
        }
    }
}


@Composable
fun LoginScreen(userViewModel: UserViewModel) {
    val currentUser by userViewModel.currentUser.collectAsState(initial = null)
    val errorMessage by userViewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.loadCurrentUser()
    }

    if (currentUser?.docId.isNullOrEmpty()) {
        // Show login UI
        LoginPage(
            loginButtonClicked = { email, password ->
                userViewModel.loginUser(email, password)
            },
            errorMessage = errorMessage
        )
    } else {
        // Show main UI
        MainAppScreen(userViewModel = userViewModel)
    }
}

