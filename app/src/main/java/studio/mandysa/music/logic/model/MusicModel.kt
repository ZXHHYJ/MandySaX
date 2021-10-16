package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Path
import mandysax.anna2.annotation.Value
import mandysax.media.model.DefaultArtist
import mandysax.media.model.DefaultMusic
import studio.mandysa.music.logic.network.Url

/**
 * @author liuxiaoliu66
 */
class MusicModel : DefaultMusic<DefaultArtist?>() {
    @Value("id")
    private var id: String? = null

    @Value("name")
    private var name: String? = null

    @Value("picUrl")
    @Path("al")
    private var picUrl: String? = null

    @Value("name")
    @Path("ar")
    private var artistsName: String? = null

    override fun getArtist(): List<DefaultArtist> {
        return listOf(DefaultArtist(artistsName!!))
    }

    override fun getId(): String {
        return id!!
    }

    override fun getCoverUrl(): String {
        return picUrl!!
    }

    override fun getTitle(): String {
        return name!!
    }

    override fun getUrl(): String {
        return Url.MUSIC_URL + id
    }
}