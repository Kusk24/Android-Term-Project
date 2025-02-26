package com.example.androidtermprojectmotopedia.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidtermprojectmotopedia.model.User
import com.example.androidtermprojectmotopedia.model.UserWithId
import com.example.androidtermprojectmotopedia.repository.UserPreferencesRepository
import com.example.androidtermprojectmotopedia.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository = UserRepository(),
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // Expose the logged-in state from DataStore as a StateFlow<Boolean>
    val isLoggedInState: StateFlow<Boolean> =
        userPreferencesRepository.isLoggedIn
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                false
            )

    // Holds the current user (null when no user is logged in)
    private val _currentUser = MutableStateFlow<UserWithId?>(null)
    val currentUser: StateFlow<UserWithId?> = _currentUser.asStateFlow()

    // Holds any error messages (e.g. invalid credentials)
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

//    fun createUserWithId(docId: String, user: User) {
//        viewModelScope.launch {
//            try {
//                userRepository.createUserWithId(docId, user)
//                // Consider the newly created user as logged in.
//                _currentUser.value = user
//            } catch (e: Exception) {
//                // Handle error as needed (e.g., set _errorMessage)
//            }
//        }
//    }
//
//    fun createUserAutoId(user: User) {
//        viewModelScope.launch {
//            try {
//                val docId = userRepository.addUserAutoId(user)
//                // Store or use the docId as needed and mark user as logged in.
//                _currentUser.value = user
//            } catch (e: Exception) {
//                // Handle error
//            }
//        }
//    }


    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                // Make sure loginUser(...) in your repository returns UserWithId?
                val result: UserWithId? = userRepository.loginUser(email, password)
                if (result != null) {
                    _currentUser.value = result
                    // Also set DataStore to keep docId
                    userPreferencesRepository.setCurrentUser(result.docId)
                    userPreferencesRepository.setLoggedIn(true)
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Invalid email or password."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Login failed. Please try again."
            }
        }
    }

//    fun getUserById(docId: String) {
//        viewModelScope.launch {
//            try {
//                val user = userRepository.getUserById(docId)
//                _currentUser.value = user
//            } catch (e: Exception) {
//                // Handle error as needed
//            }
//        }
//    }

    // Call this to log out the user.
    fun logoutUser() {
        viewModelScope.launch {
            // Clear the current user.
            _currentUser.value = null
            // Clear stored user info and set loggedIn flag to false.
            userPreferencesRepository.setCurrentUser("")
            userPreferencesRepository.setLoggedIn(false)
        }
    }

    // Optionally, load the current user from DataStore if available.
    fun loadCurrentUser() {
        viewModelScope.launch {
            userPreferencesRepository.currentUser.collect { docId ->
                if (docId.isNotEmpty()) {
                    // If getUserById returns only a User, wrap it in UserWithId
                    val user = userRepository.getUserById(docId)
                    if (user != null) {
                        _currentUser.value = UserWithId(docId, user)
                    }
                } else {
                    _currentUser.value = null
                }
            }
        }
    }
}

class UserViewModelFactory(
    private val userRepository: UserRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(
                userRepository = userRepository,
                userPreferencesRepository = userPreferencesRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
