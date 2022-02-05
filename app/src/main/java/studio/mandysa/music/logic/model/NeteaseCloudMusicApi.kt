package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.*
import mandysax.anna2.model.ResponseBody
import mandysax.anna2.observable.Observable

/**
 * @author liuxiaoliu66
 */
interface NeteaseCloudMusicApi {
    //搜索音乐
    @Get("search")
    @Path("result/songs")
    fun searchMusic(
        @Query("keywords") name: String?,
        @Query("offset") index: Int
    ): Observable<List<SearchMusicModel>>

    //搜索歌手
    @Get("search")
    @Path("result/artists")
    fun searchSinger(
        @Query("keywords") name: String?,
        @Query("offset") index: Int,
        @Query("type") type: Int
    ): Observable<List<SingerModel>>

    //获取音乐详细信息
    @Get("song/detail")
    @Path("songs")
    fun getMusicInfo(@Query("ids") ids: List<Any>?): Observable<List<MusicModel>>

    //获取新歌推荐
    @Path("result")
    @Get("personalized/newsong")
    fun getRecommendedNewSong(): Observable<List<NewSongModel>>

    //获取推荐歌曲
    @Get("recommend/songs")
    fun getRecommendedSong(@Query("cookie") cookie: String): Observable<RecommendSongs>

    //获取推荐歌单
    @Get("recommend/resource")
    fun getRecommendPlaylist(@Query("cookie") cookie: String): Observable<RecommendPlaylist>

    //歌单广场
    @Get("personalized")
    fun getPlaylistSquare(): Observable<PlaylistSquareModel>

    //获取歌词
    @Get("lyric")
    @Path("lrc")
    fun getLyric(@Query("id") id: String?): Observable<LyricModel>

    //获取歌单详情
    @Get("playlist/detail")
    @Path("playlist")
    fun getSongListInfo(
        @Query("cookie") cookie: String?,
        @Query("id") id: String
    ): Observable<PlaylistInfoModel>

    //登录
    @Post("login/cellphone")
    fun login(
        @FormData("phone") phone: String?,
        @FormData("password") password: String?,
        @Query("timestamp") timestamp: Long
    ): Observable<LoginModel>

    @Get("user/playlist")
    fun getUserPlaylist(@Query("uid") uid: String?): Observable<UserPlaylistModel>

    //获取账号信息
    @Post("user/account")
    @Path("profile")
    fun getUserInfo(
        @FormData("cookie") cookie: String?,
        @Query("timestamp") timestamp: Long
    ): Observable<UserModel>

    //获取用户详情
    @Post("user/detail")
    fun getUserDetail(
        @Query("uid") uid: String?
    ): Observable<UserDetailModel>

    //获取所有榜单
    @Get("toplist")
    @Path("list")
    fun getToplist(): Observable<List<ToplistModel>>

    @Get("like")
    fun likeMusic(
        @Query("cookie") cookie: String,
        @Query("id") id: String,
        @Query("like") like: Boolean
    ): Observable<ResponseBody>

    @Get("subscribe")
    fun subscribe(@Query("t") t: Int, @Query("id") playlistId: String): Observable<ResponseBody>

    //主页轮播图
    @Get("banner?type=1")
    fun getBannerList(): Observable<BannerModels>

    @Get("personal_fm")
    @Path("data")
    fun getPersonalFm(@Query("cookie") cookie: String): Observable<List<PersonalFmModel>>

}