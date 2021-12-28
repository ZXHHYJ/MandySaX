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

fun List<DefaultArtist>.allArtist(): String {
    var string = ""
    for (i in 0 until size) {
        if (i != 0)
            string += "/"
        string += get(i).name
    }
    return string
}
