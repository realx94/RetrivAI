package com.retrivai.app.domain.usecase.album

import com.retrivai.app.domain.model.Album
import com.retrivai.app.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor(
    private val albumRepository: AlbumRepository
) {
    operator fun invoke(): Flow<List<Album>> = albumRepository.getAlbums()
}
