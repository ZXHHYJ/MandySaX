package mandysax.media.model

import java.util.*

open class DefaultMusic<E : ArtistModel?> : MusicModel<E> {
    private val mArtistList = ArrayList<E>()
    private var mTitle: String? = null
    private var mCoverUrl: String? = null
    private var mUrl: String? = null
    private var id: String? = null

    fun setId(id: String?): DefaultMusic<E> {
        this.id = id
        return this
    }

    override fun getId() = id

    override fun getTitle(): String {
        return mTitle!!
    }

    fun setTitle(title: String?) {
        mTitle = title
    }

    override fun getCoverUrl(): String {
        return mCoverUrl!!
    }

    fun setCoverUrl(url: String?) {
        mCoverUrl = url
    }

    override fun getUrl(): String {
        return mUrl!!
    }

    fun setUrl(url: String?) {
        mUrl = url
    }

    fun addArtist(artistModel: E) {
        mArtistList.add(artistModel)
    }

    override fun getArtist(): List<E> {
        return mArtistList
    }
}