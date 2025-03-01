plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
    id("com.google.devtools.ksp")

    id("com.google.gms.google-services")

    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

    id("kotlin-parcelize")
}

android {
    namespace = "com.example.androidtermprojectmotopedia"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.androidtermprojectmotopedia"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.cardview)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.messaging)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.adaptive.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.constraintlayout.compose)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.androidx.adaptive)
    implementation(libs.androidx.adaptive.layout)
    implementation(libs.androidx.adaptive.navigation)

    implementation(libs.androidx.material.icons.extended)

    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))

    implementation ("com.google.firebase:firebase-messaging")

    implementation("com.google.firebase:firebase-analytics")

    implementation (libs.androidx.runtime.livedata)

    implementation ("com.google.android.gms:play-services-maps:19.1.0")

    implementation("com.google.maps.android:maps-compose:2.11.4")

    implementation ("com.google.firebase:firebase-storage-ktx")

    implementation ("androidx.compose.material3:material3:1.3.1")

    // Compose Material3 components.
    // For swipe-to-dismiss support, Material3 is enough.

    // ConstraintLayout for Compose.
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    // Coil for Compose image loading.

    // Other Compose dependencies if not already added.
    implementation ("androidx.compose.ui:ui:1.7.8")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.7.8")
    implementation ("androidx.activity:activity-compose:1.7.0")

    implementation ("androidx.compose.material3:material3-window-size-class:1.3.1")
    implementation ("com.google.accompanist:accompanist-adaptive:0.34.0")

    implementation("androidx.media3:media3-exoplayer:1.2.1")
    implementation("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation("androidx.media3:media3-exoplayer:1.5.1")
    implementation("androidx.media3:media3-ui:1.5.1")
}