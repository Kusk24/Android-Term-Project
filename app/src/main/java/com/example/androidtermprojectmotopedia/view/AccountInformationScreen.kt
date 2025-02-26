package com.example.androidtermprojectmotopedia.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androidtermprojectmotopedia.viewModel.UserViewModel

@Composable
fun AccountInformationScreen(
    viewModel: UserViewModel, // Pass your viewModel instance
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Optionally pre-fill fields with current user info if available
    val currentUser = viewModel.currentUser.collectAsState().value
    LaunchedEffect(currentUser) {
        currentUser?.let { userWithId ->
            name = userWithId.user.name
            email = userWithId.user.email
            password = userWithId.user.password
        }
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val (titleRef, nameFieldRef, emailFieldRef, passwordFieldRef, saveButtonRef) = createRefs()

        Text(
            text = "Account Information",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(parent.top, margin = 24.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.constrainAs(nameFieldRef) {
                top.linkTo(titleRef.bottom, margin = 24.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.constrainAs(emailFieldRef) {
                top.linkTo(nameFieldRef.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.constrainAs(passwordFieldRef) {
                top.linkTo(emailFieldRef.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Button(
            onClick = {
                viewModel.updateUser(name, email, password)
            },
            modifier = Modifier.constrainAs(saveButtonRef) {
                top.linkTo(passwordFieldRef.bottom, margin = 24.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Text("Save")
        }
    }
}
