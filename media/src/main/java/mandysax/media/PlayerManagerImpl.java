package mandysax.media;

import java.util.List;

import mandysax.lifecycle.livedata.LiveData;
import mandysax.media.model.AlbumModel;
import mandysax.media.model.ArtistModel;
import mandysax.media.model.MusicModel;


/**
 * @author liuxiaoliu66
 */
public interface PlayerManagerImpl<T extends AlbumModel<? extends MusicModel<? extends ArtistModel>>> {

    /**
     * 播放
     */
    void play();

    /**
     * 暂停
     */
    void pause();

    /**
     * 下一曲
     */
    void skipToNext();

    /**
     * 上一曲
     */
    void skipToPrevious();

    /**
     * 调整进度
     *
     * @param position 进度
     */
    void seekTo(int position);

    /**
     * 加载指定专辑
     *
     * @param albumModel 专辑
     * @param index      播放的歌曲下标
     */
    void loadAlbum(T albumModel, int index);

    /**
     * 加载指定专辑
     *
     * @param albumModel 专辑
     */
    void loadAlbum(T albumModel);

    /**
     * 销毁
     */
    void stop();

    LiveData<Boolean> pauseLiveData();

    LiveData<? extends MusicModel<? extends ArtistModel>> changeMusicLiveData();

    LiveData<? extends List<? extends MusicModel<? extends ArtistModel>>> changePlayListLiveData();

    LiveData<Integer> playingMusicDurationLiveData();

    LiveData<Integer> playingMusicProgressLiveData();
}
