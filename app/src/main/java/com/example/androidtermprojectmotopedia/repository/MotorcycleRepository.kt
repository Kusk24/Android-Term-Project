package com.example.androidtermprojectmotopedia.repository

import android.net.Uri
import com.example.androidtermprojectmotopedia.model.Motorcycle
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class MotorcycleRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) {
    private val motorcyclesRef = db.collection("motorcycles")

    /**
     * 1) Read all motorcycle docs once (not real-time).
     */
    suspend fun getAllMotorcyclesOnce(): List<Motorcycle> {
        val snapshot = motorcyclesRef.get().await()
        return snapshot.toMotorcycleList()
    }

    /**
     * 2) Create/upload a new motorcycle document with optional image/video uploads.
     *    Firestore auto-generates docId.
     */
    suspend fun uploadMotorcycle(
        brand: String,
        model: String,
        detail: String,
        postedBy: String,
        dateString: String,
        imageUri: Uri?,
        videoUri: Uri?
    ) {
        // 1) Upload image to Storage if imageUri != null
        val imageUrl = imageUri?.let { uploadFileToStorage(it, "motorcycle_images") } ?: ""

        // 2) Upload video to Storage if videoUri != null
        val videoUrl = videoUri?.let { uploadFileToStorage(it, "motorcycle_videos") } ?: ""

        // 3) Create a new Motorcycle object

        val dateFormat = java.text.SimpleDateFormat(
            "MMMM dd, yyyy 'at' hh:mm:ss a 'UTC'Z",
            java.util.Locale.getDefault()
        )
        val nowString = dateFormat.format(java.util.Date())

        val newMotorcycle = Motorcycle(
            brand         = brand,
            model         = model,
            detail        = detail,
            posted_by     = postedBy,
            release_date  = dateString,
            image         = imageUrl,
            video         = videoUrl,
            status        = "pending",  // default
            request_delete = false,     // default
            uploaded_date = nowString     // <--- store the date/time of upload
        )

        // 4) Save to Firestore (omit docId so Firestore can auto-generate it).
        val data = mapOf(
            "brand"          to newMotorcycle.brand,
            "detail"         to newMotorcycle.detail,
            "image"          to newMotorcycle.image,
            "model"          to newMotorcycle.model,
            "posted_by"      to newMotorcycle.posted_by,
            "release_date"   to newMotorcycle.release_date,
            "status"         to newMotorcycle.status,
            "video"          to newMotorcycle.video,
            "request_delete" to newMotorcycle.request_delete,
            "uploaded_date"  to newMotorcycle.uploaded_date  // <--- include it in Firestore
        )
        motorcyclesRef.add(data).await()
    }

    /**
     * 3) Update an existing motorcycle doc by docId.
     *    Provide whichever fields changed in [newData].
     */
    suspend fun updateMotorcycle(docId: String, newData: Map<String, Any?>) {
        motorcyclesRef.document(docId).update(newData).await()
    }

    /**
     * 4) Mark `request_delete = true` for a given docId (soft-delete).
     */
    suspend fun requestDeleteMotorcycle(docId: String) {
        motorcyclesRef.document(docId)
            .update("request_delete", true)
            .await()
    }

    /**
     * Helper to upload a single file (image or video) to Firebase Storage
     * under a given folderName (e.g. "motorcycle_images" or "motorcycle_videos").
     * Returns the download URL as a String.
     */
    private suspend fun uploadFileToStorage(fileUri: Uri, folderName: String): String {
        val storageRef = storage.reference.child("$folderName/${fileUri.lastPathSegment}_${System.currentTimeMillis()}")
        // Upload the file
        storageRef.putFile(fileUri).await()
        // Get the download URL
        val downloadUrl = storageRef.downloadUrl.await()
        return downloadUrl.toString()
    }
}

/**
 * Convert a Firestore QuerySnapshot into a List<Motorcycle>.
 */
private fun QuerySnapshot.toMotorcycleList(): List<Motorcycle> {
    return documents.mapNotNull { doc ->
        val brand         = doc.getString("brand") ?: ""
        val detail        = doc.getString("detail") ?: ""
        val image         = doc.getString("image") ?: ""
        val model         = doc.getString("model") ?: ""
        val posted_by     = doc.getString("posted_by") ?: ""
        val release_date  = doc.getString("release_date") ?: ""
        val status        = doc.getString("status") ?: ""
        val video         = doc.getString("video") ?: ""
        val requestDelete = doc.getBoolean("request_delete") ?: false
        val uploadedDate  = doc.getString("uploaded_date") ?: ""  // <--- read new field

        Motorcycle(
            brand = brand,
            detail = detail,
            image = image,
            model = model,
            posted_by = posted_by,
            release_date = release_date,
            status = status,
            video = video,
            docId = doc.id,   // Firestore doc ID
            request_delete = requestDelete,
            uploaded_date = uploadedDate  // <--- store in the data class
        )
    }
}
