package org.hinsun.music.presentation.swipe.save

import android.content.Context
import android.media.MediaMetadataRetriever
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.util.collections.ConcurrentMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.hinsun.music.database.MusicDatabase
import org.hinsun.music.database.entities.SongEntity
import org.hinsun.music.services.DownloadService
import org.hinsun.music.services.DownloadState
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SaveViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val downloadService: DownloadService,
    private val database: MusicDatabase
) : ViewModel() {
    private val _downloadState: MutableStateFlow<ConcurrentMap<String, DownloadState>> =
        MutableStateFlow(ConcurrentMap())

    val downloadState: StateFlow<ConcurrentMap<String, DownloadState>> =
        _downloadState.asStateFlow()

    private fun updateStateDownload(jobId: String, state: DownloadState) {
        val map = _downloadState.value
        map[jobId] = state
        _downloadState.update { map }

        if (state is DownloadState.Downloading) {
            _progress.update { state.progress }
        }
    }

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private fun onCompleteJob(jobId: String) {
        val map = _downloadState.value
        map.remove(jobId)
        _downloadState.value = map
    }

    fun startDownload(url: String): String? {
        val newJob = UUID.randomUUID().toString()
        val fileName = url.substringAfterLast("/")

        if (fileName.isEmpty() || !fileName.endsWith(".mp3")) {
            Toast.makeText(context, "Can not detect file name from url", Toast.LENGTH_SHORT).show()
            return null
        }

        viewModelScope.launch(Dispatchers.IO) {
            downloadService.downloadMusic(newJob, url, fileName).collect { response ->
                when (response) {
                    is DownloadState.Ready -> updateStateDownload(newJob, response)
                    is DownloadState.Downloading -> updateStateDownload(newJob, response)
                    is DownloadState.Complete -> {
                        updateStateDownload(newJob, response)

                        val path = response.filePath
                        val metaData = getMp3Metadata(path)

                        val title = metaData["Title"]
                        val duration = metaData["Duration"];

                        if (title == null || duration == null) {
                            Toast.makeText(context, "Can not get metadata", Toast.LENGTH_SHORT)
                                .show()
                            return@collect
                        }

                        val newSong = SongEntity.newSong(
                            title = title,
                            audioUri = response.filePath,
                            thumbnailUri = response.filePath,
                            duration = duration.split(" ")[0].toInt()
                        )

                        database.insertSong(newSong)
                        onCompleteJob(newJob)
                    }

                    else -> {}
                }
            }
        }

        return newJob
    }

    fun cancelDownload(jobId: String) {
        val response = downloadService.cancelDownload(jobId)
        if (response) {
            // handle cancel and update in ui
        }
    }

    private fun getMp3Metadata(filePath: String): Map<String, String> {
        val retriever = MediaMetadataRetriever()
        val metadata = mutableMapOf<String, String>()

        try {
            retriever.setDataSource(filePath)

            metadata["Title"] =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: "Unknown"
            metadata["Artist"] =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: "Unknown"
            metadata["Album"] =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: "Unknown"
            metadata["Duration"] =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.let {
                    (it.toLong() / 1000).toString() + " sec"
                } ?: "Unknown"

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            retriever.release()
        }

        return metadata
    }
}