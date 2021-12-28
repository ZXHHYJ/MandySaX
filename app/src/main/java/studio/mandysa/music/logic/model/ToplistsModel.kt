package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value

class ToplistsModel {
    @Value("list")
    var list: List<ToplistModel>? = null

    class ToplistModel {
        @Value("coverImgUrl")
        var coverImgUrl: String? = null

        @Value("updateFrequency")
        var updateFrequency: String? = null

        @Value("description")
        var description: String? = null

        @Value("name")
        var name: String? = null

        @Value("id")
        var id: String? = null
    }
}