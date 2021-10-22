package mandysax.media.model

class DefaultArtist(private val mName: String) : ArtistModel {
    override fun getName(): String {
        return mName
    }
}