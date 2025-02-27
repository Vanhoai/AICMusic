package org.hinsun.music.domain.usecases.audios

import org.hinsun.music.domain.repositories.AudioRepository
import javax.inject.Inject

class FindAllAudiosUseCase @Inject constructor(
    private val audioRepository: AudioRepository
) {
    operator fun invoke() {}
}