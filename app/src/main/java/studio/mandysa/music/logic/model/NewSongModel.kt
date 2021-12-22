package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Path
import mandysax.anna2.annotation.Value
import mandysax.media.model.DefaultArtist
import mandysax.media.model.DefaultMusic
import studio.mandysa.music.logic.network.Url

/**
 * @author liuxiaoliu66
 */
class NewSongModel : DefaultMusic<DefaultArtist>() {
    @Value("id")
    private val id = ""

    @Value("name")
    private val name = ""

    @Value("picUrl")
    private val picUrl = ""

    @Value("name")
    @Path("song/artists")
    private val artistsName = ""

    override fun getArtist(): List<DefaultArtist> {
        return listOf(DefaultArtist(artistsName))
    }

    override fun getId(): String {
        return id
    }

    override fun getCoverUrl(): String {
        return picUrl
    }

    override fun getTitle(): String {
        return name
    }

    override fun getUrl(): String {
        return Url.MUSIC_URL + id
    }
}