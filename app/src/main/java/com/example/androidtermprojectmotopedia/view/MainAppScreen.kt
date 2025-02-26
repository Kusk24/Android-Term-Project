package com.example.androidtermprojectmotopedia.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidtermprojectmotopedia.repository.UserPreferencesRepository
import com.example.androidtermprojectmotopedia.repository.UserRepository
import com.example.androidtermprojectmotopedia.viewModel.UserViewModel
import com.example.androidtermprojectmotopedia.viewModel.UserViewModelFactory
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
                drawerState = drawerState
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
                composable("Upload") { UploadScreen(modifier = Modifier) }
                composable("Notification") { NotificationScreen(modifier = Modifier) }
                composable("Saved") { SavedScreen(modifier = Modifier) }
                composable("Profile") { ProfileScreen(modifier = Modifier) }
                composable("Settings") {
                    // If you need a ViewModel:
                    val context = LocalContext.current
                    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
                    val userRepository = remember { UserRepository() }
                    val factory = remember { UserViewModelFactory(userRepository, userPreferencesRepository) }
                    val userViewModel: UserViewModel = viewModel(factory = factory)

                    SettingScreen(
                        navController = navController,
                        userViewModel = userViewModel
                    )
                }
//                composable("Login") { LoginScreen(navController = navController) }
                composable("accountInfo") {
                    AccountInformationScreen()
                }
            }
        }
    }
}
