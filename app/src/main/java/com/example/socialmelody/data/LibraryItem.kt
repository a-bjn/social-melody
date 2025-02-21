package com.example.socialmelody.data

sealed class LibraryItem {
    data class LibraryPlaylist(val playlist: Playlist) : LibraryItem()
    data class LibraryAlbum(val album: Album) : LibraryItem()
}
