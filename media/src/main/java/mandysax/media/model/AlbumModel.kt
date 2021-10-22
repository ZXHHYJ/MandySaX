package mandysax.media.model

import java.util.*

/**
 * @author liuxiaoliu66
 */
interface AlbumModel<T : MusicModel<out ArtistModel>> {
    fun add(vararg musicModels: DefaultMusic<DefaultArtist>)
    fun remove(vararg musicModels: T)
    fun size(): Int
    operator fun get(index: Int): T
    val artist: ArrayList<*>?
}