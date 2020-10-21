package com.FMJJ.MandySa.logic.model;
import java.io.Serializable;
import android.support.v4.media.MediaMetadataCompat;
import com.FMJJ.MandySa.logic.model.contentcatalogs.MusicLibrary;

public final class MusicItem implements Serializable
{
    public String mediaId,//音乐id
    title,//标题
    artist,//艺术家
    album;//专辑

    public MediaMetadataCompat createMediaMetadataCompat()
    {
        return MusicLibrary.createMediaMetadataCompat(mediaId, title, artist, album);
    }
}
