package com.example.androidtermprojectmotopedia.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtermprojectmotopedia.httpClient
import com.example.androidtermprojectmotopedia.model.Brand
import com.example.androidtermprojectmotopedia.model.BrandApiWrapper
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BrandViewModel : ViewModel() {

    private val _brands = MutableStateFlow(listOf<Brand>())
    val brands : StateFlow<List<Brand>> = _brands


    init{
        loadMotorcycles()
    }

    private fun loadMotorcycles() {
//        val url = "https://mocki.io/v1/a65ce55a-3cbd-4eea-a57d-8ce616331ad1"
        val url = "https://mocki.io/v1/48cf18ec-c42b-4b8b-a685-863c9cce8b93"

        viewModelScope.launch{
            val data = httpClient.get(url).body<BrandApiWrapper>()
            _brands.value = data.brands
        }
    }
}