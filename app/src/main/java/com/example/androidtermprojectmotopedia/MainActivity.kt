package com.example.androidtermprojectmotopedia

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidtermprojectmotopedia.repository.UserPreferencesRepository
import com.example.androidtermprojectmotopedia.repository.UserRepository
import com.example.androidtermprojectmotopedia.ui.theme.AndroidTermProjectMotopediaTheme
import com.example.androidtermprojectmotopedia.view.RootScreen
import com.example.androidtermprojectmotopedia.viewModel.MotorcycleViewModel
import com.example.androidtermprojectmotopedia.viewModel.NotificationViewModel
import com.example.androidtermprojectmotopedia.viewModel.UserViewModel
import com.example.androidtermprojectmotopedia.viewModel.UserViewModelFactory
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale

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

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val context = LocalContext.current
                    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
                    val userRepository = remember { UserRepository() }
                    val factory = remember { UserViewModelFactory(userRepository, userPreferencesRepository) }
                    val userViewModel: UserViewModel = viewModel(factory = factory)
                    val isDarkTheme by userViewModel.darkTheme.observeAsState(initial = false)
                    val motorcycleViewModel: MotorcycleViewModel = remember { MotorcycleViewModel() }


                    AndroidTermProjectMotopediaTheme(
                        darkTheme = isDarkTheme
                    ) {
                        // Your composable content
                        RootScreen(widthSizeClass = widthSizeClass,
                            userViewModel = userViewModel,
                            motorcycleViewModel = motorcycleViewModel,
                            modifier = Modifier.padding(innerPadding))
                    }
            }
        }

        // Request the notification permission and initialize Firebase Messaging
        askNotificationPermission()

        checkInternetConnection()
    }

    private fun askNotificationPermission() {
        // Only needed for API level 33 (TIRAMISU) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                lifecycleScope.launch {
                    val userPreferencesRepository = UserPreferencesRepository(this@MainActivity)
                    userPreferencesRepository.setNotiPermission(true)
                    // Permission already granted; proceed with Firebase messaging initialization.
                    initializeFirebaseMessaging()
                }
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
        Firebase.messaging.subscribeToTopic("NewArticle")
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

    override fun attachBaseContext(newBase: Context) {
        // 1) Read the stored language code from DataStore synchronously
        val languageCode = runBlocking {
            // Use the repository’s method
            val repo = UserPreferencesRepository(newBase)
            repo.getCurrentLanguage()
        }
        android.util.Log.d("MainActivity", "attachBaseContext: languageCode = $languageCode")

        // 2) Create/update a configuration with the chosen locale
        val locale = Locale.forLanguageTag(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(newBase.resources.configuration).apply {
            setLocale(locale)
            setLayoutDirection(locale)
        }

        // 3) Wrap the newBase with this updated configuration
        val localizedContext = newBase.createConfigurationContext(config)

        // 4) Pass it up the chain
        super.attachBaseContext(localizedContext)
    }

    private fun checkInternetConnection() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)

            if (capabilities == null || !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                showNoInternetDialog()
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected) {
                showNoInternetDialog()
            }
        }
    }

    private fun showNoInternetDialog() {
        // Use the traditional AlertDialog from AppCompat
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("No Internet Connection")
            .setMessage("Internet connection is required to use this app. Please connect to the internet and try again.")
            .setCancelable(false)
            .setPositiveButton("Quit") { _, _ ->
                finish() // Close the app
            }

        val alert = builder.create()

        // Show the dialog first
        alert.show()

        val positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(ContextCompat.getColor(this, R.color.black))
    }
}

