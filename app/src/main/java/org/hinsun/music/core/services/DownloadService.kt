package org.hinsun.music.core.services

import android.content.Context
import android.os.Environment
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.timeout
import io.ktor.client.request.prepareGet
import io.ktor.http.contentLength
import io.ktor.util.cio.writeChannel
import io.ktor.util.collections.ConcurrentMap
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.close
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

sealed class DownloadState {
    data class Ready(val job: Job) : DownloadState()
    data class Downloading(val progress: Float) : DownloadState()
    data class Complete(val filePath: String) : DownloadState()
    data class Error(val message: String) : DownloadState()
}

@Singleton
class DownloadService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val httpClient: HttpClient
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val jobs: ConcurrentMap<String, Job> = ConcurrentMap()

    fun cancelDownload(jobId: String): Boolean {
        jobs[jobId]?.cancel()
        jobs.remove(jobId)

        return true
    }

    fun downloadMusic(jobId: String, url: String, fileName: String): Flow<DownloadState> =
        channelFlow {
            val newJob = scope.launch {
                try {
                    val file = createFile(fileName)
                    httpClient.prepareGet(url) {
                        timeout {
                            requestTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS
                        }
                    }.execute { response ->
                        val contentLength = response.contentLength()?.toFloat() ?: 0f

                        val channel = response.body<ByteReadChannel>()
                        val output = file.writeChannel()

                        var bytesWritten = 0f
                        try {
                            while (!channel.isClosedForRead) {
                                val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
                                bytesWritten += packet.remaining.toFloat()
                                output.writePacket(packet)

                                val progress = if (contentLength > 0) {
                                    (bytesWritten / contentLength) * 100
                                } else 0f

                                send(DownloadState.Downloading(progress))
                            }

                            send(DownloadState.Complete(file.path))
                        } catch (e: Exception) {
                            send(DownloadState.Error(e.message ?: "Download failed"))
                        } finally {
                            output.close()
                            jobs.remove(jobId)
                        }
                    }
                } catch (exception: Exception) {
                    send(DownloadState.Error(exception.message ?: "Download failed"))
                }
            }

            jobs[jobId] = newJob
            send(DownloadState.Ready(newJob))
            newJob.join()
        }

    private fun createFile(fileName: String): File {
        val dict = File(context.filesDir, MUSIC_DIRECTORY)
        if (!dict.exists()) dict.mkdirs()

        val file = File(dict, fileName)
        return file
    }

    companion object {
        private const val MUSIC_DIRECTORY = "musics"
    }
}