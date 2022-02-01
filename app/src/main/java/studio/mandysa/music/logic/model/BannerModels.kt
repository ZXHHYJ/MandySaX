package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Value

class BannerModels {

    @Value("banners")
    val list: List<BannerModel>? = null

    class BannerModel {
        @Value("pic")
        val pic = ""

        @Value("targetId")
        val targetId = 0

        @Value("targetType")
        val targetType = 0

        /*@Value("typeTitle")
        val typeTitle = ""*/

        @Value("url")
        val url = ""
    }
}