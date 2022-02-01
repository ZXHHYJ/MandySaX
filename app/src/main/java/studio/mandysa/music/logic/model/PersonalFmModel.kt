package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Path
import mandysax.anna2.annotation.Value
import mandysax.media.model.DefaultArtist
import mandysax.media.model.DefaultMusic
import studio.mandysa.music.logic.network.Url

class PersonalFmModel : DefaultMusic<DefaultArtist?>() {
    @Value("name")
    private var name: String? = null

    @Value("id")
    private var id: String? = null

    @Value("name")
    @Path("artists")
    private var artistsName: String? = null

    override fun getArtist(): List<DefaultArtist> {
        return listOf(DefaultArtist(artistsName!!))
    }

    override fun getCoverUrl(): String {
        return ""
    }

    override fun getId(): String {
        return id!!
    }

    override fun getTitle(): String {
        return name!!
    }

    override fun getUrl(): String {
        return Url.MUSIC_URL + id
    }

    override fun toString(): String {
        return id!!
    }
}