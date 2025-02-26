package com.example.androidtermprojectmotopedia.model

//data class Motorcycle(
//    val name: String,
//    val engine: String,
//    val image: String
//)
/**
 * A model matching your Firestore fields:
 * brand, detail, image, model, posted_by, release_date, status, video.
 *
 * docId is optional but helps track the Firestore document ID.
 */
data class Motorcycle(
    val brand: String = "",
    val detail: String = "",
    val image: String = "",
    val model: String = "",
    val posted_by: String = "",
    val release_date: String = "",
    val status: String = "",
    val video: String = "",
    val docId: String = "" ,  // Firestore doc ID (not a field in the DB)
    val request_delete : Boolean = false
)
