package com.example.androidtermprojectmotopedia.view

import BrandScreen
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidtermprojectmotopedia.viewModel.MotorcycleViewModel
import com.example.androidtermprojectmotopedia.viewModel.UserViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

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
    var labelTitle : String by remember{ mutableStateOf("Home") }

    ModalNavigationDrawer(
        drawerContent = {
            DetailedDrawer(
                navController = navController,
                drawerState = drawerState,
                userViewModel,
                onItemSelected = { newTitle ->
                    labelTitle = newTitle
                }
            )
        },
        drawerState = drawerState,
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                AppToolbar(
                    title = labelTitle,
                    onNavigationClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open()
                            else drawerState.close()
                        }
                    },
                    searchButtonClick = { navController.navigate("Search") },
                    settingButtonClick = { navController.navigate("Settings") },
                )
            }
        ) { innerPadding ->

            val layoutDirection = LocalLayoutDirection.current

            NavHost(
                navController = navController,
                startDestination = "Home",
                // Override the bottom padding here
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = 0.dp, // remove or reduce bottom padding
                    start = innerPadding.calculateStartPadding(layoutDirection),
                    end = innerPadding.calculateEndPadding(layoutDirection)
                )
            ) {
                composable("Home") {
                    ArticleListScreen(
                        modifier = Modifier,
                        onArticleClick = { docId ->
                            navController.navigate("detail/$docId")},
                        motorcycleViewModel = motorcycleViewModel
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
                    ArticleDetailScreen(docId = docId,
                        motorcycleViewModel)

                }
                composable("Upload") {
                    UploadScreen(
                        modifier = Modifier,
                        motorcycleViewModel,
                        userViewModel
                    )
                }
                composable("Notification") {
                    NotificationScreen(modifier = Modifier,)
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