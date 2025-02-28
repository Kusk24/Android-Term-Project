package com.example.androidtermprojectmotopedia.view

import LanguageSelectionDialog
import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.androidtermprojectmotopedia.R
import com.example.androidtermprojectmotopedia.viewModel.UserViewModel

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    userViewModel: UserViewModel
) {
    val context = LocalContext.current
    // Observe the language from ViewModel (this returns the code: "en", "zh-rCN", or "my")
    val languageCode by userViewModel.language.observeAsState(initial = "en")

    // A mapping from code to display name
    val languageMap = mapOf(
        "en" to "English",
        "zh-CN" to "Chinese",
        "my" to "Myanmar"
    )

    // State to control showing the language dialog
    var showLanguageDialog by remember { mutableStateOf(false) }
    // Notification toggle
    var notificationChecked by remember { mutableStateOf(true) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val (box1, box2, box3, box4, box5, box6) = createRefs()

        // 1) Row for "Account Information"
        Row(
            modifier = Modifier
                .clickable {
                    navController.navigate("accountInfo")
                }
                .constrainAs(box1) {
                    top.linkTo(parent.top, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
                .height(50.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = stringResource(id = R.string.account_info))
        }

        // 2) Row for "Language"
        Row(
            modifier = Modifier
                .clickable {
                    showLanguageDialog = true
                }
                .constrainAs(box2) {
                    top.linkTo(box1.bottom, margin = 50.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
                .height(50.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Language,
                contentDescription = null,
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(12.dp))
            // Display the full language name from our map
            Text(text = stringResource(R.string.language) + ": ${languageMap[languageCode] ?: languageCode}")
        }

        // Show the language dialog if needed
        if (showLanguageDialog) {
            LanguageSelectionDialog(
                // Pass the actual language code
                currentLanguage = languageCode,
                onDismiss = { showLanguageDialog = false },
                onLanguageSelected = { chosenLang ->
                    // This updates preferences via ViewModel
                    userViewModel.setLanguage(chosenLang)
                    showLanguageDialog = false

                    (context as? Activity)?.recreate()
                },
                userViewModel = userViewModel
            )
        }

        // 3) Row for "Notification"
        Row(
            modifier = Modifier
                .constrainAs(box3) {
                    top.linkTo(box2.bottom, margin = 50.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
                .height(50.dp)
                .fillMaxWidth()
                .clickable { /* If you want a detail screen */ }
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = null,
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = stringResource(id = R.string.notification))
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = notificationChecked,
                onCheckedChange = { notificationChecked = it }
            )
        }

        // 4) Row for "Help & Support"
        Row(
            modifier = Modifier
                .clickable { /* ... */ }
                .constrainAs(box4) {
                    top.linkTo(box3.bottom, margin = 50.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
                .height(50.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.HelpOutline,
                contentDescription = null,
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = stringResource(id = R.string.help_and_support))
        }

        // 5) Row for "FAQs"
        Row(
            modifier = Modifier
                .clickable { /* ... */ }
                .constrainAs(box5) {
                    top.linkTo(box4.bottom, margin = 50.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
                .height(50.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.QuestionAnswer,
                contentDescription = null,
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = stringResource(id = R.string.faqs))
        }

        // 6) Log out Button
        Button(
            onClick = {
                userViewModel.logoutUser()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Red
            ),
            border = BorderStroke(1.dp, Color.Red),
            modifier = Modifier.constrainAs(box6) {
                top.linkTo(box5.bottom, margin = 50.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        ) {
            Text(text = stringResource(R.string.sign_out))
        }
    }
}
