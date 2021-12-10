package studio.mandysa.music.logic.utils

import mandysax.media.model.DefaultAlbum
import mandysax.media.model.DefaultArtist
import mandysax.media.model.DefaultMusic

@Suppress("UNCHECKED_CAST")
fun List<Any?>?.createAlbum(): DefaultAlbum {
    val album = DefaultAlbum()
    for (data in this as List<DefaultMusic<DefaultArtist>>) {
        album.add(data)
    }
    return album
}
