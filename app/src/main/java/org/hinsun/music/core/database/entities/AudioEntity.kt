package org.hinsun.music.core.database.entities

import kotlinx.serialization.Serializable

@Serializable
data class AudioEntity(
    val id: String,
    val name: String,
    val ytId: String,
    val audio: String,
    val thumbnail: String,
    val createdAt: String,
    val updatedAt: String
)