package com.example.androidtermprojectmotopedia.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidtermprojectmotopedia.viewModel.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(userViewModel: UserViewModel, // Add this
                  modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            DetailedDrawer(
                navController = navController,
                drawerState = drawerState,
                userViewModel
            )
        },
        drawerState = drawerState,
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                AppToolbar(
                    title = "Motopedia",
                    onNavigationClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open()
                            else drawerState.close()
                        }
                    },
                    searchButtonClick = { navController.navigate("Search") },
                    settingButtonClick = { navController.navigate("Settings") }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "Home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("Home") { BrandScreen(modifier = Modifier) }
                composable("Search") { SearchScreen(modifier = Modifier, onMotorcycleClicked = {}) }
                composable("Brands") { BrandScreen(modifier = Modifier) }
                composable("Upload") { UploadScreen(modifier = Modifier, userViewModel) }
                composable("Notification") { NotificationScreen(modifier = Modifier) }
                composable("Profile") { ProfileScreen(modifier = Modifier, userViewModel) }
                composable("Settings") {
                    SettingScreen(
                        navController = navController,
                        userViewModel = userViewModel
                    )
                }
//                composable("Login") { LoginScreen(navController = navController) }
                composable("accountInfo") {
                    AccountInformationScreen(
                        userViewModel,
                        onBackClick = { navController.popBackStack() },
                        modifier = Modifier
                    )
                }
            }
        }
    }
}
