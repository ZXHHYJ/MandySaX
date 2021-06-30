package studio.mandysa.music.logic.Model;

import java.util.List;

import mandysax.anna2.annotation.Body;
import mandysax.anna2.annotation.Get;
import mandysax.anna2.annotation.Path;
import mandysax.anna2.annotation.Post;
import mandysax.anna2.annotation.Query;
import mandysax.anna2.observable.Observable;

public interface NeteaseCloudMusicApi {
    @Get("search")
    @Path("result/songs")
    Observable<SearchMusicModel> searchMusic(@Query("keywords") String name, @Query("offset") int index);

    @Get("search")
    @Path("result/artists")
    Observable<SingerModel> searchSinger(@Query("keywords") String name, @Query("offset") int index,@Query("type") int type);

    @Get("song/detail")
    @Path("songs")
    Observable<MusicModel> getMusicInfo(@Query("ids") String id);

    @Get("song/detail")
    @Path("songs")
    Observable<MusicModel> getMusicInfo(@Query("ids") List<String> ids);

    @Get("personalized")
    @Path("result")
    Observable<PlaylistModel> getRecommendedPlaylist();

    @Get("personalized/newsong")
    @Path("result")
    Observable<NewSongModel> getRecommendedSong();

    @Get("personalized")
    @Path("result")
    Observable<PlaylistModel> getRecommendedPlaylist(@Body() String cookie);

    @Get("lyric")
    @Path("lrc")
    Observable<LyricModel> getLyric(@Query("id") String id);

    @Get("playlist/detail")
    @Path("playlist")
    Observable<PlaylistInfoModel> getSongListInfo(@Query("id") String id);

    @Post("login/cellphone")
    Observable<LoginModel> login(@Body("phone") String phone, @Body("password") String password);

    @Get("user/playlist")

    @Path("playlist")
    Observable<UserPlaylistModel> getUserPlaylist(@Query("uid") String uid);

    @Post("user/account")
    @Path("profile")
    Observable<UserModel> getUserInfo(@Body() String cookie, @Body("timestamp") long timestamp);
}
