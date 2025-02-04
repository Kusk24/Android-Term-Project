package com.example.androidtermprojectmotopedia.View

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun LoginPage(modifier: Modifier) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        val (item1, item2, item3, item4) = createRefs()

        val line1 = createGuidelineFromTop(0.6f)

        var email by remember { mutableStateOf("") }

        var password by remember { mutableStateOf("") }


        Text(
            "Welcome To MotoPedia", fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(item4) {
                bottom.linkTo(item1.top, 75.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, fontSize = (30.sp)
        )

        TextField(value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.constrainAs(item1) {
                bottom.linkTo(item2.top, 50.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        TextField(value = password,
            onValueChange = { password = it },
            label = { Text("password") },
            modifier = Modifier.constrainAs(item2) {
                bottom.linkTo(line1, 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Button(onClick = {}, modifier = Modifier
            .constrainAs(item3) {
                top.linkTo(line1, 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .width(100.dp)
            .height(50.dp)) {
            Text("Log In")
        }
    }
}