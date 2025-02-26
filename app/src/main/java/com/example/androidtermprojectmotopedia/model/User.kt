package com.example.androidtermprojectmotopedia.model

data class User(
    val email: String = "",
    val name: String = "",
    val password: String = "",     // Not recommended to store raw
    val profile_image: String = "" // This will be a URL if uploaded to Firebase Storage
)

data class UserWithId(
    val docId: String = "",
    val user: User = User()
)
