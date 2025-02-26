package com.example.androidtermprojectmotopedia.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedDrawer(
    navController: NavController,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf("Home") }
    var theme by remember { mutableStateOf(false) }

    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))
            Text(
                "Motopedia",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )
            HorizontalDivider()

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                AsyncImage(
                    model = "https://i.pinimg.com/736x/53/fe/d1/53fed15d25b9308613788977fca0d509.jpg",
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .border(
                            border = BorderStroke(1.dp, Color.Black),
                            shape = RoundedCornerShape(50.dp)
                        )
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                "Profile Name",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally),
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp
            )

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // "Uses" Section
            Text(
                "Uses",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )

            NavigationDrawerItem(
                label = { Text("Home") },
                selected = selectedItem == "Home",
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                onClick = {
                    selectedItem = "Home"
                    scope.launch { drawerState.close() }
                    // Navigate to "Home"
                    navController.navigate("Home")
                }
            )
            NavigationDrawerItem(
                label = { Text("Search") },
                selected = selectedItem == "Search",
                icon = { Icon(Icons.Default.Search, contentDescription = null) },
                onClick = {
                    selectedItem = "Search"
                    scope.launch { drawerState.close() }
                    navController.navigate("Search")
                }
            )
            NavigationDrawerItem(
                label = { Text("Explore Brands") },
                selected = selectedItem == "Brands",
                icon = { Icon(Icons.Default.Map, contentDescription = null) },
                onClick = {
                    selectedItem = "Brands"
                    scope.launch { drawerState.close() }
                    navController.navigate("Brands")
                }
            )
            NavigationDrawerItem(
                label = { Text("Upload") },
                selected = selectedItem == "Upload",
                icon = { Icon(Icons.Default.Upload, contentDescription = null) },
                onClick = {
                    selectedItem = "Upload"
                    scope.launch { drawerState.close() }
                    navController.navigate("Upload")
                }
            )
            NavigationDrawerItem(
                label = { Text("Notification") },
                selected = selectedItem == "Notification",
                icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                onClick = {
                    selectedItem = "Notification"
                    scope.launch { drawerState.close() }
                    navController.navigate("Notification")
                }
            )
            NavigationDrawerItem(
                label = { Text("Saved") },
                selected = selectedItem == "Saved",
                icon = { Icon(Icons.Default.Bookmarks, contentDescription = null) },
                onClick = {
                    selectedItem = "Saved"
                    scope.launch { drawerState.close() }
                    navController.navigate("Saved")
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // "Management" Section
            Text(
                "Management",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )

            NavigationDrawerItem(
                label = { Text("Profile") },
                selected = selectedItem == "Profile",
                icon = { Icon(Icons.Default.Person, contentDescription = null) },
                onClick = {
                    selectedItem = "Profile"
                    scope.launch { drawerState.close() }
                    navController.navigate("Profile")
                }
            )
            NavigationDrawerItem(
                label = { Text("Settings") },
                selected = selectedItem == "Settings",
                icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                onClick = {
                    selectedItem = "Settings"
                    scope.launch { drawerState.close() }
                    navController.navigate("Settings")
                }
            )
            NavigationDrawerItem(
                label = { Text("Help and feedback") },
                selected = false,
                icon = { Icon(Icons.AutoMirrored.Outlined.Help, contentDescription = null) },
                onClick = {
                    scope.launch { drawerState.close() }
                    // Additional logic...
                }
            )

            // Theme Switch
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(16.dp)
            ) {
                val (item1, item2) = createRefs()
                Text(
                    "Theme",
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start)
                        .constrainAs(item1) {
                            start.linkTo(parent.start)
                        }
                )
                Switch(
                    checked = theme,
                    onCheckedChange = { theme = it },
                    modifier = Modifier
                        .wrapContentHeight(Alignment.Bottom)
                        .constrainAs(item2) {
                            end.linkTo(parent.end)
                        }
                )
            }
            Spacer(Modifier.height(12.dp))

            // Log out (optional)
//            NavigationDrawerItem(
//                label = { Text("Log out") },
//                selected = false,
//                icon = { /* e.g. Icon(Icons.Default.ExitToApp, contentDescription = null) */ },
//                onClick = {
//                    scope.launch { drawerState.close() }
//                    // Example: navigate to Login and clear back stack
//                    navController.navigate("Login") {
//                        popUpTo("Home") { inclusive = true }
//                    }
//                },
//                colors = NavigationDrawerItemDefaults.colors(
//                    selectedContainerColor = Color.Transparent,
//                    unselectedContainerColor = Color.Transparent,
//                    selectedIconColor = Color.Red,
//                    unselectedIconColor = Color.Red,
//                    selectedTextColor = Color.Red,
//                    unselectedTextColor = Color.Red,
//                )
//            )
        }
    }
}
