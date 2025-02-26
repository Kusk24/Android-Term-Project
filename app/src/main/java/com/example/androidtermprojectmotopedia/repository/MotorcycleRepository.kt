package com.example.androidtermprojectmotopedia.repository

import com.example.androidtermprojectmotopedia.model.Motorcycle
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class MotorcycleRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val motorcyclesRef = db.collection("motorcycles")

    /**
     * Fetch all motorcycles once (not real-time).
     */
    suspend fun getAllMotorcyclesOnce(): List<Motorcycle> {
        val snapshot = motorcyclesRef.get().await()
        return snapshot.toMotorcycleList()
    }

    /**
     * Create/add a new motorcycle document. Firestore auto-generates docId.
     */
    suspend fun addMotorcycle(motorcycle: Motorcycle) {
        // Omit docId so we don't store it in Firestore.
        val data = mapOf(
            "brand"         to motorcycle.brand,
            "detail"        to motorcycle.detail,
            "image"         to motorcycle.image,
            "model"         to motorcycle.model,
            "posted_by"     to motorcycle.posted_by,
            "release_date"  to motorcycle.release_date,
            "status"        to motorcycle.status,
            "video"         to motorcycle.video,
            "request_delete" to motorcycle.request_delete  // <--- new field
        )
        motorcyclesRef.add(data).await()
    }

    /**
     * Update any fields in an existing doc by docId.
     */
    suspend fun updateMotorcycle(docId: String, newData: Map<String, Any?>) {
        motorcyclesRef.document(docId).update(newData).await()
    }

    /**
     * Delete a motorcycle doc by docId.
     */
    suspend fun deleteMotorcycle(docId: String) {
        motorcyclesRef.document(docId).delete().await()
    }

    /**
     * Convenience method: mark `request_delete = true`.
     * (Instead of fully deleting, just set the request_delete flag.)
     */
    suspend fun requestDeleteMotorcycle(docId: String) {
        motorcyclesRef.document(docId)
            .update("request_delete", true)
            .await()
    }

    /**
     * If you want real-time updates, you'd add a snapshot listener:
     */
    fun listenToMotorcycles(
        onDataChange: (List<Motorcycle>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        motorcyclesRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                onError(e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                onDataChange(snapshot.toMotorcycleList())
            } else {
                onDataChange(emptyList())
            }
        }
    }
}

/**
 * Convert a QuerySnapshot into a List<Motorcycle>, including `request_delete`.
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
        val requestDelete = doc.getBoolean("request_delete") ?: false // <--

        Motorcycle(
            brand = brand,
            detail = detail,
            image = image,
            model = model,
            posted_by = posted_by,
            release_date = release_date,
            status = status,
            video = video,
            docId = doc.id,  // Firestore doc ID
            request_delete = requestDelete
        )
    }
}
