package com.example.androidtermprojectmotopedia.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
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

/**
 * A single ViewModel that handles both user authentication and user preferences,
 * but uses a nullable Boolean for isLoggedInState to avoid flicker on startup.
 */
class UserViewModel(
    private val userRepository: UserRepository = UserRepository(),
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // Instead of Boolean, we use Boolean? so we can represent "still loading" as null.
    val isLoggedInState: StateFlow<Boolean?> =
        userPreferencesRepository.isLoggedIn
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null // <-- null means we haven't loaded DataStore yet
            )

    // Expose other preferences if needed (dark theme, language, notification permission)
    val darkTheme = userPreferencesRepository.darkTheme.asLiveData()
    val language = userPreferencesRepository.language.asLiveData()
    val notiPermission = userPreferencesRepository.notiPermission.asLiveData()

    // Expose current user info as a StateFlow
    private val _currentUser = MutableStateFlow<UserWithId?>(null)
    val currentUser: StateFlow<UserWithId?> = _currentUser.asStateFlow()

    // Holds any error messages (e.g. invalid credentials)
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    /**
     * Logs in a user by email & password, saves user info in DataStore if successful.
     */
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val result: UserWithId? = userRepository.loginUser(email, password)
                if (result != null) {
                    _currentUser.value = result
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

    /**
     * Logs out the user, clears local states & DataStore flags.
     */
    fun logoutUser() {
        viewModelScope.launch {
            _currentUser.value = null
            userPreferencesRepository.setCurrentUser("")
            userPreferencesRepository.setLoggedIn(false)
        }
    }

    /**
     * Load the current user from DataStore docId (if present),
     * then fetch the user from Firestore.
     */
    fun loadCurrentUser() {
        viewModelScope.launch {
            userPreferencesRepository.currentUser.collect { docId ->
                if (docId.isNotEmpty()) {
                    val user = userRepository.getUserById(docId)
                    if (user != null) {
                        _currentUser.value = UserWithId(docId, user)
                    } else {
                        _currentUser.value = null
                    }
                } else {
                    _currentUser.value = null
                }
            }
        }
    }

    init {
        // Whenever the ViewModel is created, try to load the user from docId
        loadCurrentUser()
    }

    /**
     * Update user fields in Firestore and in local state.
     */
    fun updateUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            _currentUser.value?.let { userWithId ->
                val fields = mapOf(
                    "name" to name,
                    "email" to email,
                    "password" to password
                )
                try {
                    userRepository.updateUserFields(userWithId.docId, fields)
                    _currentUser.value = UserWithId(
                        userWithId.docId,
                        userWithId.user.copy(
                            name = name,
                            email = email,
                            password = password
                        )
                    )
                    _errorMessage.value = null
                } catch (e: Exception) {
                    _errorMessage.value = "Update failed. Please try again."
                }
            }
        }
    }

    // --- Preference Helpers ---

    fun setLoggedIn(isLoggedIn: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setLoggedIn(isLoggedIn)
        }
    }

    fun setDarkTheme(darkTheme: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setDarkTheme(darkTheme)
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            userPreferencesRepository.setLanguage(language)
        }
    }

    fun setNotiPermission(notiPermission: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setNotiPermission(notiPermission)
        }
    }

    fun setCurrentUser(currentUser: String) {
        viewModelScope.launch {
            userPreferencesRepository.setCurrentUser(currentUser)
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
