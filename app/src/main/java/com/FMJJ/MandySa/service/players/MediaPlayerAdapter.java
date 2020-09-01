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

package com.FMJJ.MandySa.service.players;

import android.content.*;
import android.media.*;
import android.os.*;
import android.support.v4.media.*;
import android.support.v4.media.session.*;
import com.FMJJ.MandySa.data.*;
import com.FMJJ.MandySa.service.*;
import mandysax.utils.*;

/**
 *公开{@link MediaPlayer}的功能并实现{@link PlayerAdapter}
 *，以便{@link MainActivity}可以控制音乐播放。
 */
public final class MediaPlayerAdapter extends PlayerAdapter
{
    private final Context mContext;
    private MediaPlayer mMediaPlayer;
    private PlaybackInfoListener mPlaybackInfoListener;
    private MediaMetadataCompat mCurrentMedia;
	private String mMusicid;
    private int mState;
	private int mSeekWhileNotPlaying = -1;
//解决与MediaPlayer.seekTo（）的行为有关的MediaPlayer错误的方法
	//不不播放时。

	private final MediaPlayer.OnPreparedListener prepared=new MediaPlayer.OnPreparedListener(){

		@Override
		public void onPrepared(MediaPlayer p1)
		{	
			play();
		}
	};

	private final MediaPlayer.OnErrorListener error =new MediaPlayer.OnErrorListener() {
		@Override
		public boolean onError(MediaPlayer mediaPlayer, int i, int i1)
		{
			return false;
		}
	};

	private final MediaPlayer.OnCompletionListener completion=new MediaPlayer.OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mediaPlayer)
		{
			mPlaybackInfoListener.onPlaybackCompleted();
			//将状态设置为“已暂停”，因为它与状态最接近
			//在MediaPlayer中关于可用状态转换的比较
			// 停止”。
			//暂停允许：seekTo（），start（），pause（），stop（）
			//停止允许：stop（）
			setNewState(PlaybackStateCompat.STATE_PAUSED);
		}
	};

	private boolean mCurrentMediaPlayedToCompletion;

    public MediaPlayerAdapter(Context context, PlaybackInfoListener listener)
	{
        super(context);
        mContext = context.getApplicationContext();
        mPlaybackInfoListener = listener;

    }

	private void initializeMediaPlayer()
	{
        if (mMediaPlayer == null)
		{
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setOnPreparedListener(prepared);
			mMediaPlayer.setOnErrorListener(error);
			mMediaPlayer.setOnCompletionListener(completion);
        }
    }

	/**
	 * {@link MediaPlayer}发布后，将无法再次使用，而必须使用另一个
	 *已创建。 在{@link MainActivity}的onStop（）方法中，{@ link MediaPlayer}是
	 *发布。 然后在{@link MainActivity}的onStart（）中新建一个{@link MediaPlayer}
	 *必须创建对象。 这就是为什么此方法是私有的，并由load（int）和
	 *不是构造函数。
	 */

    // 构建 PlaybackControl.
    @Override
    public void playFromMedia(MediaMetadataCompat metadata)
	{
        mCurrentMedia = metadata;
        playFile(metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID));
    }

    @Override
    public MediaMetadataCompat getCurrentMedia()
	{
        return mCurrentMedia;
    }

	private void playFile(String id)
	{
        boolean mediaChanged = (mMusicid == null || !id.equals(mMusicid));
        if (mCurrentMediaPlayedToCompletion)
		{
			//最后一个音频文件已播放完毕，resourceId尚未更改，但是
			//播放器已释放，因此请强制重新加载媒体文件以进行播放。            mediaChanged = true;
            mCurrentMediaPlayedToCompletion = false;
        }
        if (!mediaChanged)
		{
            if (!isPlaying())
			{
                play();
            }
            return;
        }
		else
		{
            release();
        }

        mMusicid = id;
		
        initializeMediaPlayer();
        try
		{
            mMediaPlayer.setDataSource(url.url4 + id);
			mMediaPlayer.prepareAsync();
			setNewState(PlaybackStateCompat.STATE_CONNECTING);
        }
		catch (Exception e)
		{
        }

    }

    @Override
    public void onStop()
	{
//无论MediaPlayer是否已创建/启动，状态都必须
		//进行更新，以便MediaNotificationManager可以删除通知。        setNewState(PlaybackStateCompat.STATE_STOPPED);
        release();
    }

    private void release()
	{
        if (mMediaPlayer != null)
		{
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public boolean isPlaying()
	{
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    @Override
    protected void onPlay()
	{
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying())
		{
            mMediaPlayer.start();
            setNewState(PlaybackStateCompat.STATE_PLAYING);
        }
    }

    @Override
    protected void onPause()
	{
        if (mMediaPlayer != null && mMediaPlayer.isPlaying())
		{
            mMediaPlayer.pause();
            setNewState(PlaybackStateCompat.STATE_PAUSED);
        }
    }

//这是播放器状态机的主要减速器。
    private void setNewState(@PlaybackStateCompat.State int newPlayerState)
	{
        mState = newPlayerState;

		//无论播放是完成还是停止，
		// mCurrentMediaPlayedToCompletion设置为true。
		//当MediaPlayer.getCurrentPosition（）在不播放时发生更改时，请变通解决。
		final long reportPosition;
        if (mSeekWhileNotPlaying >= 0)
		{
            reportPosition = mSeekWhileNotPlaying;

            if (mState == PlaybackStateCompat.STATE_PLAYING)
			{
                mSeekWhileNotPlaying = -1;
            }
        }
		else
		{
            reportPosition = mMediaPlayer == null ? 0 : mMediaPlayer.getCurrentPosition();
        }

        final PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder();
        stateBuilder.setActions(getAvailableActions());
        stateBuilder.setState(mState,
                              reportPosition,
                              1.0f,
                              SystemClock.elapsedRealtime());
        mPlaybackInfoListener.onPlaybackStateChange(stateBuilder.build());
    }

	/**
	 *设置此会话上可用的当前功能。 注意：如果没有能力
	 *在功能的位掩码中列出，则MediaSession将不会处理它。 对于
	 *例如，如果您不希望ACTION_STOP由MediaSession处理，则不要
	 *将其包含在返回的位掩码中。
	 */
    @PlaybackStateCompat.Actions
    private long getAvailableActions()
	{
        long actions = PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
			| PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH
			| PlaybackStateCompat.ACTION_SKIP_TO_NEXT
			| PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;
        switch (mState)
		{
            case PlaybackStateCompat.STATE_STOPPED:
                actions |= PlaybackStateCompat.ACTION_PLAY
					| PlaybackStateCompat.ACTION_PAUSE;
                break;
            case PlaybackStateCompat.STATE_PLAYING:
                actions |= PlaybackStateCompat.ACTION_STOP
					| PlaybackStateCompat.ACTION_PAUSE
					| PlaybackStateCompat.ACTION_SEEK_TO;
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                actions |= PlaybackStateCompat.ACTION_PLAY
					| PlaybackStateCompat.ACTION_STOP;
                break;
            default:
                actions |= PlaybackStateCompat.ACTION_PLAY
					| PlaybackStateCompat.ACTION_PLAY_PAUSE
					| PlaybackStateCompat.ACTION_STOP
					| PlaybackStateCompat.ACTION_PAUSE;
        }
        return actions;
    }

    @Override
    public void seekTo(long position)
	{
        if (mMediaPlayer != null)
		{
            if (!mMediaPlayer.isPlaying())
			{
                mSeekWhileNotPlaying = (int)position;
            }
            mMediaPlayer.seekTo((int)position);

			//设置状态（为当前状态），因为位置已更改且应
			//向客户报告。
            setNewState(mState);
        }
    }

    @Override
    public void setVolume(float volume)
	{
        if (mMediaPlayer != null)
		{
            mMediaPlayer.setVolume(volume, volume);
        }
    }
}
