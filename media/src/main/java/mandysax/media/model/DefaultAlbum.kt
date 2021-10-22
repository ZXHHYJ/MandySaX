package mandysax.media.model

import java.util.*

/**
 * @author liuxiaoliu66
 */
class DefaultAlbum : AlbumModel<DefaultMusic<DefaultArtist>> {
    override val artist = ArrayList<DefaultArtist>()
    private val mMusicList = ArrayList<DefaultMusic<DefaultArtist>>()

    @SafeVarargs
    override fun add(vararg musicModels: DefaultMusic<DefaultArtist>) {
        mMusicList.addAll(listOf(*musicModels))
    }

    @SafeVarargs
    override fun remove(vararg musicModels: DefaultMusic<DefaultArtist>) {
        mMusicList.removeAll(listOf(*musicModels))
    }

    override fun size(): Int {
        return mMusicList.size
    }

    override fun get(index: Int): DefaultMusic<DefaultArtist> {
        return mMusicList[index]
    }
}