package org.hinsun.music.presentation.onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class OnBoardUIState(
    val loading: Boolean = false,
    val isFirstTime: Boolean = true
)

@HiltViewModel
class OnBoardViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(OnBoardUIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            initApp()
        }
    }

    private suspend fun initApp() = withContext(Dispatchers.IO) {
        _uiState.update { it.copy(loading = true) }
        delay(2000)
        _uiState.update { it.copy(loading = false) }
    }
}