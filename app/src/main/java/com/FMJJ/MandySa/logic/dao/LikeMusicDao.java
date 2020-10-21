package com.FMJJ.MandySa.logic.dao;
import android.support.v4.media.MediaMetadataCompat;
import com.FMJJ.MandySa.logic.model.MusicItem;
import java.util.ArrayList;
import simon.tuke.Tuke;

public class LikeMusicDao
{
    private final static String KEY="live_music";

    private static final ArrayList<MusicItem> list=Tuke.get(KEY, new ArrayList<MusicItem>());

    public static void addMudic(MediaMetadataCompat music)
    {
        MusicItem muiscItem=new MusicItem();
        muiscItem.mediaId = music.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
        muiscItem.title = music.getString(MediaMetadataCompat.METADATA_KEY_TITLE);
        muiscItem.album = music.getString(MediaMetadataCompat.METADATA_KEY_ALBUM);
        muiscItem.artist = music.getString(MediaMetadataCompat.METADATA_KEY_ARTIST);
        if (isAdded(music))return;
        list.add(muiscItem);
        upData();
        //return list;
    }
    public static boolean isAdded(MediaMetadataCompat music)
    {
        for (MusicItem item:list)
            if (item.mediaId == music.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID))         
                return true;
        return false;
    }

    public static void removeMusic(int in)
    {
        list.remove(in);
        upData();
        //return list;
    }

    public static ArrayList<MusicItem> getMusicList()
    {
        return list;
    }

    private static void upData()
    {
        Tuke.write(KEY, list);
    }
}
