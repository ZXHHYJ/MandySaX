package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Path
import mandysax.anna2.annotation.Value
import mandysax.media.model.DefaultArtist
import mandysax.media.model.DefaultMusic

class RecommendedSong : DefaultMusic<DefaultArtist>() {
    @Value("name")
    private val name = ""

    @Value("id")
    private val id = ""

    @Value("name")
    @Path("ar")
    private val artistsName = ""

    @Value("picUrl")
    @Path("al")
    private val picUrl = ""

    override fun getTitle(): String {
        return name
    }

    override fun getCoverUrl(): String {
        return picUrl
    }

    override fun getArtist(): List<DefaultArtist> {
        return listOf(DefaultArtist(artistsName))
    }

    override fun getId(): String {
        return id
    }
}