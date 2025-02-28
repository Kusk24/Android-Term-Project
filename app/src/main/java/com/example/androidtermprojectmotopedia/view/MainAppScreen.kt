package com.example.androidtermprojectmotopedia.view

import BrandScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidtermprojectmotopedia.viewModel.MotorcycleViewModel
import com.example.androidtermprojectmotopedia.viewModel.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    userViewModel: UserViewModel,
    motorcycleViewModel: MotorcycleViewModel,
    windowSizeClass: WindowWidthSizeClass, // Add this
    modifier: Modifier = Modifier
) {
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
                composable("Home") {
                    ArticleListScreen(
                        modifier = Modifier,
                        onArticleClick = { docId ->
                            navController.navigate("detail/$docId")
                        }
                    )
                }
                composable("Search") {
                    SearchScreen(
                        modifier = Modifier,
                        onMotorcycleClicked = { docId ->
                            navController.navigate("detail/$docId")
                        }
                    )
                }
                composable("Brands") {
                    BrandScreen(
                        windowSizeClass = windowSizeClass,
                        modifier = Modifier
                    )
                }
                composable(route = "detail/{docId}",
                    arguments = listOf(navArgument("docId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val docId = backStackEntry.arguments?.getString("docId") ?: ""
                    ArticleDetailScreen(docId = docId)

                }
                composable("Upload") {
                    UploadScreen(
                        modifier = Modifier,
                        motorcycleViewModel,
                        userViewModel
                    )
                }
                composable("Notification") {
                    NotificationScreen(modifier = Modifier)
                }
                composable("Profile") {
                    ProfileScreen(
                        modifier = Modifier,
                        userViewModel = userViewModel,
                        motorcycleViewModel = motorcycleViewModel
                    )
                }
                composable("Settings") {
                    SettingScreen(
                        navController = navController,
                        userViewModel = userViewModel
                    )
                }
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