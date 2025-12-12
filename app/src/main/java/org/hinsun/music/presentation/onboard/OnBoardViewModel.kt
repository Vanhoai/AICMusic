package org.hinsun.music.presentation.onboard

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.hinsun.music.R
import javax.inject.Inject

data class OnBoardUIState(
    val loading: Boolean = false,
    val isFirstTime: Boolean = true
)

@HiltViewModel
class OnBoardViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnBoardUIState())
    val uiState = _uiState.asStateFlow()

    private var exoPlayer: ExoPlayer? = null

    init {
        viewModelScope.launch {
            initApp()
        }
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer?.release()
    }

    private suspend fun initApp() = withContext(Dispatchers.IO) {
        _uiState.update { it.copy(loading = true) }
        delay(2000)
        _uiState.update { it.copy(loading = false) }
    }
}