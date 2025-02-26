package com.example.androidtermprojectmotopedia.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtermprojectmotopedia.model.Motorcycle
import com.example.androidtermprojectmotopedia.repository.MotorcycleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MotorcycleViewModel(
    private val repository: MotorcycleRepository = MotorcycleRepository()
) : ViewModel() {

    // Holds the list of motorcycles
    private val _motorcycles = MutableStateFlow<List<Motorcycle>>(emptyList())
    val motorcycles: StateFlow<List<Motorcycle>> = _motorcycles.asStateFlow()

    // Holds any error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        // Optionally load all motorcycles immediately
        loadAllMotorcycles()
    }

    /**
     * 1) Read all motorcycles once from Firestore.
     */
    fun loadAllMotorcycles() {
        viewModelScope.launch {
            try {
                val result = repository.getAllMotorcyclesOnce()
                _motorcycles.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    /**
     * 2) Create a new motorcycle document in Firestore,
     *    uploading image/video to Storage if provided.
     */
    fun uploadMotorcycle(
        brand: String,
        model: String,
        detail: String,
        postedBy: String,
        dateString: String,
        imageUri: Uri?,
        videoUri: Uri?
    ) {
        viewModelScope.launch {
            try {
                repository.uploadMotorcycle(
                    brand = brand,
                    model = model,
                    detail = detail,
                    postedBy = postedBy,
                    dateString = dateString,
                    imageUri = imageUri,
                    videoUri = videoUri
                )
                // After uploading, optionally refresh the list
                loadAllMotorcycles()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    /**
     * 3) Update an existing motorcycle by docId.
     *    Provide whichever fields changed in [newData].
     */
    fun updateMotorcycle(docId: String, newData: Map<String, Any?>) {
        viewModelScope.launch {
            try {
                repository.updateMotorcycle(docId, newData)
                // Optionally refresh
                loadAllMotorcycles()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    /**
     * 4) Mark `request_delete = true` for a given docId.
     */
    fun requestDeleteMotorcycle(docId: String) {
        viewModelScope.launch {
            try {
                repository.requestDeleteMotorcycle(docId)
                // Optionally refresh
                loadAllMotorcycles()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
}
