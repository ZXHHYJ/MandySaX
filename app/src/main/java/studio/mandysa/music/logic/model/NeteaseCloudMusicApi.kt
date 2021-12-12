package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.*
import mandysax.anna2.model.ResponseBody
import mandysax.anna2.observable.Observable

/**
 * @author liuxiaoliu66
 */
interface NeteaseCloudMusicApi {
    @Get("search")
    @Path("result/songs")
    fun searchMusic(
        @Query("keywords") name: String?,
        @Query("offset") index: Int
    ): Observable<List<SearchMusicModel>>

    @Get("search")
    @Path("result/artists")
    fun searchSinger(
        @Query("keywords") name: String?,
        @Query("offset") index: Int,
        @Query("type") type: Int
    ): Observable<List<SingerModel>>

    @Get("song/detail")
    @Path("songs")
    fun getMusicInfo(@Query("ids") ids: List<Any>?): Observable<List<MusicModel>>

    /* @get:Path("result")*/
    /*  @get:Get("personalized")
      val recommendedPlaylist: Observable<PlaylistModel>*/

    @get:Path("result")
    @get:Get("personalized/newsong")
    val recommendedSong: Observable<List<NewSongModel>>

    @Get("personalized")
    fun getRecommendedPlaylist(@Query("cookie") cookie: String): Observable<PlaylistModel>

    /* @Get("lyric")
     @Path("lrc")
     fun getLyric(@Query("id") id: String?): Observable<LyricModel>*/

    @Get("playlist/detail")
    @Path("playlist")
    fun getSongListInfo(@Query("id") id: String): Observable<PlaylistInfoModel>

    @Post("login/cellphone")
    fun login(
        @FormData("phone") phone: String?,
        @FormData("password") password: String?,
        @Query("timestamp") timestamp: Long
    ): Observable<LoginModel>

    @Get("user/playlist")
    fun getUserPlaylist(@Query("uid") uid: String?): Observable<UserPlaylistModel>

    @Post("user/account")
    @Path("profile")
    fun getUserInfo(
        @FormData("cookie") cookie: String?,
        @Query("timestamp") timestamp: Long
    ): Observable<UserModel>

    @Get("toplist")
    @Path("list")
    fun getToplist(): Observable<List<ToplistModel>>

    @Get("like")
    fun likeMusic(cookie: String, id: String, like: Boolean): Observable<ResponseBody>
}