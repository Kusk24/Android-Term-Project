package com.example.androidtermprojectmotopedia.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidtermprojectmotopedia.repository.UserPreferencesRepository
import kotlinx.coroutines.launch

class UserPreferencesViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel(){

    val isLoggedIn = userPreferencesRepository.isLoggedIn.asLiveData()

    val darkTheme = userPreferencesRepository.darkTheme.asLiveData()

    val language = userPreferencesRepository.language.asLiveData()

    val notiPermission = userPreferencesRepository.notiPermission.asLiveData()

    val currentUser = userPreferencesRepository.currentUser.asLiveData()

    fun setLoggedIn(isLoggedIn: Boolean){
        viewModelScope.launch{
            userPreferencesRepository.setLoggedIn(isLoggedIn)
        }
    }

    fun setDarkTheme(darkTheme : Boolean){
        viewModelScope.launch{
            userPreferencesRepository.setDarkTheme(darkTheme)
        }
    }

    fun setLanguage(language : String){
        viewModelScope.launch {
            userPreferencesRepository.setLanguage(language)
        }
    }

    fun setNotiPermission(notiPermission : Boolean){
        viewModelScope.launch {
            userPreferencesRepository.setNotiPermission(notiPermission)
        }
    }

    fun setCurrentUser(currentUser: String){
        viewModelScope.launch{
            userPreferencesRepository.setCurrentUser(currentUser)
        }
    }


}