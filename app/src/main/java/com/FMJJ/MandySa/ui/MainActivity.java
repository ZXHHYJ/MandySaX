package com.FMJJ.MandySa.ui;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;
import android.view.View;
import androidx.annotation.NonNull;
import com.FMJJ.MandySa.R;
import com.FMJJ.MandySa.client.MediaBrowserHelper;
import com.FMJJ.MandySa.model.MainViewModel;
import com.FMJJ.MandySa.service.MusicService;
import java.util.List;
import mandysax.core.annotation.BindFragment;
import mandysax.core.annotation.BindLayoutId;
import mandysax.core.annotation.BindView;
import mandysax.design.BottomNavigationBar;
import mandysax.design.FragmentPage;
import mandysax.design.MusicView;
import mandysax.lifecycle.ViewModelProviders;

@BindLayoutId(R.layout.main)
public class MainActivity extends BaseActivity
{

	private final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

	@BindFragment(R.id.mainFragmentPage)
	private HomeFragment home_fragment;

	@BindFragment(R.id.mainFragmentPage)
	private LiveFragment recommend_fragment;

	@BindFragment(R.id.mainFragmentPage)
	private SearchFragment search_fragment;

	@BindFragment(R.id.mainFragmentPage)
	private MyFragment my_fragment;

	@BindView
	private FragmentPage mainFragmentPage;

	@BindView
	private MusicView mainMusicView;

	@BindView
	private BottomNavigationBar mainBottomNavigationBar;

	private MediaBrowserHelper mMediaBrowserHelper;

    private boolean mIsPlaying;

    @Override
    public void onStart()
	{
        super.onStart();
        mMediaBrowserHelper.onStart();
    }

    @Override
    public void onStop()
	{
        super.onStop();
		//  mSeekBarAudio.disconnectController();
        mMediaBrowserHelper.onStop();
    }

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mMediaBrowserHelper = new MediaBrowserConnection(this);
        mMediaBrowserHelper.registerCallback(new MediaBrowserListener());
		initFragment();
		mainMusicView.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
				}
			
		});
		mainMusicView.getPlaybutton().setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (mIsPlaying)
					{
                        mMediaBrowserHelper.getTransportControls().pause();
                    }
					else
					{
                        mMediaBrowserHelper.getTransportControls().play();
                    }
				}
			});
	}
	
	private void initFragment()
	{
		mainFragmentPage.add(home_fragment, recommend_fragment, search_fragment, my_fragment);
		mainFragmentPage.showFragment(viewModel.index);
		mainBottomNavigationBar.setTextColorRes(R.color.theme_color);
        mainBottomNavigationBar.addItemView(getString(R.string.home), R.mipmap.ic_music, R.mipmap.ic_music_black);
        mainBottomNavigationBar.addItemView(getString(R.string.live), R.mipmap.ic_cards_heart, R.mipmap.ic_cards_heart_black);
        mainBottomNavigationBar.addItemView(getString(R.string.search), R.mipmap.ic_magnify_outline, R.mipmap.ic_magnify_outline_black);
		mainBottomNavigationBar.addItemView(getString(R.string.my), R.mipmap.ic_account, R.mipmap.ic_account_black);
        mainBottomNavigationBar.setSelected(viewModel.index);
		mainBottomNavigationBar.setOnItemViewSelectedListener(new BottomNavigationBar.OnItemViewSelectedListener() {
				@Override
				public void onItemClcik(View v, int index)
				{
					viewModel.index = index;
					mainFragmentPage.showFragment(index);
				}
			});
	}

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
	{
        if (keyCode == KeyEvent.KEYCODE_BACK)
		{
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

	private class MediaBrowserConnection extends MediaBrowserHelper
	{
        private MediaBrowserConnection(Context context)
		{
            super(context, MusicService.class);
        }

        @Override
        protected void onConnected(@NonNull MediaControllerCompat mediaController)
		{
			//进度条
			// mSeekBarAudio.setMediaController(mediaController);
        }

        @Override
        protected void onChildrenLoaded(@NonNull String parentId,
                                        @NonNull List<MediaBrowserCompat.MediaItem> children)
		{
            super.onChildrenLoaded(parentId, children);
//获取播放列表
            final MediaControllerCompat mediaController = getMediaController();

            // Queue up all media items for this simple sample.
            for (final MediaBrowserCompat.MediaItem mediaItem : children)
			{
                mediaController.addQueueItem(mediaItem.getDescription());
            }
            // Call prepare now so pressing play just works.
            mediaController.getTransportControls().prepare();
        }
    }

	/**
	 *我们感兴趣的{@link MediaControllerCompat.Callback}方法的实现。
	 * <p>
	 *这也是可以覆盖的地方
	 * {@code onQueueChanged（List <MediaSessionCompat.QueueItem> queue）}，以便在有项目时得到通知
	 *从队列中添加或删除。 我们此处不这样做是为了保留用户界面
	 *简单。
	 */

    private class MediaBrowserListener extends MediaControllerCompat.Callback
	{
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat playbackState)
		{
			if (playbackState != null)
			{
				mIsPlaying = playbackState != null && playbackState.getState() == PlaybackStateCompat.STATE_PLAYING;
				if (playbackState.getState() == PlaybackStateCompat.STATE_PLAYING)
					mainMusicView.playmode();
				else
				if (playbackState.getState() == PlaybackStateCompat.STATE_PAUSED)
					mainMusicView.stopmode();
				else
				if (playbackState.getState() == PlaybackStateCompat.STATE_CONNECTING)
					mainMusicView.loadmode();
			}
		}

        @Override
        public void onMetadataChanged(MediaMetadataCompat mediaMetadata)
		{
            if (mediaMetadata == null)
			{
                return;
            }
            mainMusicView.setTitle(
				mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
            /*mArtistTextView.setText(
			 mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
			 mAlbumArt.setImageBitmap(MusicLibrary.getAlbumBitmap(
			 MainActivity.this,
			 mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)));
			 */
		}

        @Override
        public void onSessionDestroyed()
		{
            super.onSessionDestroyed();
        }

        @Override
        public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue)
		{
            super.onQueueChanged(queue);
        }
    }
}

