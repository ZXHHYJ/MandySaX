package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value

/**
 * @author liuxiaoliu66
 */
class PlaylistModel {

    @Value("result")
    val playlist: List<Playlist>? = null

    class Playlist {
        @Value("id")
        var id: String? = null

        @Value("name")
        var name: String? = null

        /*@Value("copywriter")
        public String info;*/
        @Value("picUrl")
        var picUrl: String? = null
    }

}