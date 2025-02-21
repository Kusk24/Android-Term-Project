package com.example.androidtermprojectmotopedia

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.androidtermprojectmotopedia.model.Brand
import com.example.androidtermprojectmotopedia.ui.theme.AndroidTermProjectMotopediaTheme
import com.example.androidtermprojectmotopedia.view.BrandScreen
import com.example.androidtermprojectmotopedia.view.HomeScreen
import com.example.androidtermprojectmotopedia.view.MainAppScreen
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    // Register the permission launcher for POST_NOTIFICATIONS (API 33+)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            initializeFirebaseMessaging()
        } else {
            Toast.makeText(this, "Notification permission not granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidTermProjectMotopediaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainAppScreen(modifier = Modifier.padding(innerPadding))
//                    BrandScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }

        // Request the notification permission and initialize Firebase Messaging
        askNotificationPermission()
    }

    private fun askNotificationPermission() {
        // Only needed for API level 33 (TIRAMISU) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission already granted; proceed with Firebase messaging initialization.
                initializeFirebaseMessaging()
            } else {
                // Request the permission.
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            // For devices below API 33, no runtime permission is needed.
            initializeFirebaseMessaging()
        }
    }

    private fun initializeFirebaseMessaging() {
        // For example, subscribe to topics and log the registration token.
        subscribeTopics()
        logRegToken()
    }

    private fun subscribeTopics() {
        Firebase.messaging.subscribeToTopic("New Released")
            .addOnCompleteListener { task ->
                val msg = if (task.isSuccessful) "Subscribed to New Released topic" else "Subscription failed"
                Log.d("MainActivity", msg)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
    }

    private fun logRegToken() {
        Firebase.messaging.token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MainActivity", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("MainActivity", "FCM Registration token: $token")
            Toast.makeText(this, "FCM Registration token: $token", Toast.LENGTH_SHORT).show()
        }
    }
}




//package com.example.androidtermprojectmotopedia
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import com.example.androidtermprojectmotopedia.ui.theme.AndroidTermProjectMotopediaTheme
//import com.example.androidtermprojectmotopedia.view.HomeScreen
//
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            AndroidTermProjectMotopediaTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    HomeScreen(modifier = Modifier.padding(innerPadding))
//                }
//            }
//        }
//    }
//}
