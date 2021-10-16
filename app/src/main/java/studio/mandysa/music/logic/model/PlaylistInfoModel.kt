package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value

/**
 * @author liuxiaoliu66
 */
class PlaylistInfoModel {
    @Value("name")
    var name: String? = null

    @Value("description")
    var description: String? = null

    @Value("coverImgUrl")
    var coverImgUrl: String? = null

    @Value("trackIds")
    var songList: List<SongList>? = null

    class SongList {
        @Value("id")
        var id: String? = null

        override fun toString(): String {
            return id!!
        }
    }
}