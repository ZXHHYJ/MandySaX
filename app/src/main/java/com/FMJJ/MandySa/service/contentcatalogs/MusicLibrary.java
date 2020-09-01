/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.FMJJ.MandySa.service.contentcatalogs;

import android.content.*;
import android.support.v4.media.*;
import java.util.*;

public class MusicLibrary
{

    private static final TreeMap<String, MediaMetadataCompat> music = new TreeMap<>();

	public static void addMusic(MediaMetadataCompat get)
	{
		// TODO: Implement this method
		music.put(get.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID),get);
	}
	
    public static String getRoot()
	{
        return "root";
    }

    public static List<MediaBrowserCompat.MediaItem> getMediaItems()
	{
        List<MediaBrowserCompat.MediaItem> result = new ArrayList<>();
        for (MediaMetadataCompat metadata : music.values())
		{
            result.add(
				new MediaBrowserCompat.MediaItem(
					metadata.getDescription(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE));
        }
        return result;
    }

    public static MediaMetadataCompat getMetadata(Context context, String mediaId)
	{
        MediaMetadataCompat metadataWithoutBitmap = music.get(mediaId);
        // Since MediaMetadataCompat is immutable, we need to create a copy to set the album art.
        // We don't set it initially on all items so that they don't take unnecessary memory.
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        for (String key :
		new String[]{
			MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
			MediaMetadataCompat.METADATA_KEY_ALBUM,
			MediaMetadataCompat.METADATA_KEY_ARTIST,
			MediaMetadataCompat.METADATA_KEY_GENRE,
			MediaMetadataCompat.METADATA_KEY_TITLE
		})
		{
            builder.putString(key, metadataWithoutBitmap.getString(key));
        }
        builder.putLong(
			MediaMetadataCompat.METADATA_KEY_DURATION,
			metadataWithoutBitmap.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));
        //builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt);
        return builder.build();

		//  .putLong(MediaMetadataCompat.METADATA_KEY_DURATION,
		//TimeUnit.MILLISECONDS.convert(duration, durationUnit))
		//音乐时长
    }

    public static MediaMetadataCompat createMediaMetadataCompat(
		String mediaId,//音乐id
		String title,//标题
		String artist,//艺术家
		String album)//专辑
	{
		return new MediaMetadataCompat.Builder()
			.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaId)
			.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
			.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)        
			.putString(MediaMetadataCompat.METADATA_KEY_TITLE,title)
			.build();
	}
	
}
