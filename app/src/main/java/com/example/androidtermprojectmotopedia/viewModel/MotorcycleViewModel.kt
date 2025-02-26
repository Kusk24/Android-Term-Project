package com.example.androidtermprojectmotopedia.viewModel

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

    private val _motorcycles = MutableStateFlow<List<Motorcycle>>(emptyList())
    val motorcycles: StateFlow<List<Motorcycle>> = _motorcycles.asStateFlow()

    init {
        loadMotorcycles()
    }

    private fun loadMotorcycles() {
        viewModelScope.launch {
            val data = repository.getAllMotorcyclesOnce()
            _motorcycles.value = data
        }
    }

    fun addMotorcycle(motorcycle: Motorcycle) {
        viewModelScope.launch {
            repository.addMotorcycle(motorcycle)
            // Optionally reload or rely on real-time updates
            loadMotorcycles()
        }
    }

    fun requestDeleteMotorcycle(docId: String) {
        viewModelScope.launch {
            repository.requestDeleteMotorcycle(docId)
            // Optionally reload or rely on real-time updates
            loadMotorcycles()
        }
    }

    /**
     * If you want to do a normal "updateMotorcycle" for other fields:
     */
    fun updateMotorcycle(docId: String, newData: Map<String, Any?>) {
        viewModelScope.launch {
            repository.updateMotorcycle(docId, newData)
            // optionally reload
            loadMotorcycles()
        }
    }

    /**
     * If you want a direct delete method (admin-only, etc.):
     */
    fun deleteMotorcycle(docId: String) {
        viewModelScope.launch {
            repository.deleteMotorcycle(docId)
            // optionally reload
            loadMotorcycles()
        }
    }
}
