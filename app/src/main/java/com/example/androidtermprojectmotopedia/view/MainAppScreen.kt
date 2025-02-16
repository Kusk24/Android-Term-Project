package com.example.androidtermprojectmotopedia.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun MainAppScreen(modifier: Modifier){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Login", modifier = Modifier) {


        composable(route = "Login"){
            LoginPage(modifier = Modifier, LoginButtonClicked = { email ->
                navController.navigate("Account/$email")
            })
        }


        composable(route = "Account/{email}",
            arguments = listOf(navArgument("email")
            {type = NavType.StringType})) {  backstackEntry ->
            val email = backstackEntry.arguments?.getString("email") ?: ""

            HomeScreen(modifier = Modifier)
        }

    }

}
