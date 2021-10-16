package studio.mandysa.music.logic.utils

import mandysax.media.model.DefaultAlbum
import mandysax.media.model.DefaultArtist
import mandysax.media.model.DefaultMusic

object ArrayListUtils {
    fun ArrayList<DefaultMusic<DefaultArtist>>.createAlbum(): DefaultAlbum {
        val album = DefaultAlbum()
        for (data in this) {
            album.add(data)
        }
        return album
    }
}