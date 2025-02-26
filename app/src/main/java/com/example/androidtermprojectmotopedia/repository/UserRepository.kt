package com.example.androidtermprojectmotopedia.repository

import com.example.androidtermprojectmotopedia.model.User
import com.example.androidtermprojectmotopedia.model.UserWithId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()
    // or: private val firestore = Firebase.firestore

    private fun userCollection() = firestore.collection("users")

    /**
     * Create a new user with a specified docId (e.g., "user1")
     * or some custom string ID you generate.
     * If you want Firestore to generate an ID, see addUserAutoId().
     */
    suspend fun createUserWithId(docId: String, user: User) {
        userCollection().document(docId).set(user).await()
    }

    /**
     * Create a new user with an auto-generated Firestore document ID.
     * Returns the newly generated doc ID.
     */
    suspend fun addUserAutoId(user: User): String {
        val docRef = userCollection().add(user).await()
        return docRef.id
    }

    /**
     * Retrieve a user by its document ID.
     */
    suspend fun getUserById(docId: String): User? {
        val docSnap = userCollection().document(docId).get().await()
        return docSnap.toObject<User>()
    }

    /**
     * Basic "login" approach: find user by email & password.
     * This is NOT secure for production, but works as a demo.
     */
    suspend fun loginUser(email: String, password: String): UserWithId? {
        val querySnapshot = userCollection()
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .await()

        // If the user exists, return the first match
        if (!querySnapshot.isEmpty) {
            val doc = querySnapshot.documents.first()
            val user = doc.toObject<User>()
            if (user != null) {
                return UserWithId(doc.id, user)
            }
        }
        return null
    }

    /**
     * Update a user’s fields (e.g., name, profile_image) by doc ID.
     */
    suspend fun updateUserFields(docId: String, fields: Map<String, Any>) {
        userCollection().document(docId).update(fields).await()
    }

    /**
     * Delete a user document by ID.
     */
    suspend fun deleteUser(docId: String) {
        userCollection().document(docId).delete().await()
    }
}
