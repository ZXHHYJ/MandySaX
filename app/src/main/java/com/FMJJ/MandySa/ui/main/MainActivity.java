package com.FMJJ.MandySa.ui.main;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import androidx.annotation.NonNull;
import com.FMJJ.MandySa.logic.MediaBrowserHelper;
import com.FMJJ.MandySa.logic.model.MusicService;
import com.FMJJ.MandySa.ui.BaseActivity;
import com.FMJJ.MandySa.ui.main.HomeFragment;
import com.FMJJ.MandySa.ui.main.LikeFragment;
import com.FMJJ.MandySa.ui.main.MyFragment;
import com.FMJJ.MandySa.ui.main.SearchListFragment;
import com.FMJJ.MandySa.ui.music.MusicActivity;
import java.util.List;
import mandysax.R;
import mandysax.fragmentpage.widget.FragmentPage;
import mandysax.lifecycle.ViewModelProviders;
import mandysax.musiccontroller.widget.MusicController;
import mandysax.navigationbar.widget.BottomNavigationBar;

public class MainActivity extends BaseActivity
{

	private MainViewModel viewModel;

	private FragmentPage fragmentPage;
	
	private MusicController musicController;

	private BottomNavigationBar bottomNavigationBar;

	private MediaBrowserHelper mediaBrowserHelper;

    private boolean mIsPlaying;


    @Override
    public void onStart()
	{
        super.onStart();
        mediaBrowserHelper.onStart();
    }

    @Override
    public void onStop()
	{
        super.onStop();
		//  mSeekBarAudio.disconnectController();
        mediaBrowserHelper.onStop();
    }

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		fragmentPage = findViewById(R.id.mainFragmentPage);
        musicController = findViewById(R.id.mainMusicController);
        bottomNavigationBar = findViewById(R.id.mainBottomNavigationBar);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
		mediaBrowserHelper = new MediaBrowserConnection(this);
        mediaBrowserHelper.registerCallback(new MediaBrowserListener());
		initFragment();
		musicController.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
                    MusicActivity.startActivity(MainActivity.this);
				}

			});
		musicController.getPlaybutton().setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (mIsPlaying)
					{
                        mediaBrowserHelper.getTransportControls().pause();
                    }
					else
					{
                        mediaBrowserHelper.getTransportControls().play();
                    }
				}
			});
	}

	private void initFragment()
	{
		fragmentPage.add(HomeFragment.class, LikeFragment.class, SearchListFragment.class, MyFragment.class);
		fragmentPage.showFragment(viewModel.index);
        bottomNavigationBar.addItemView(getString(R.string.home), R.mipmap.ic_music, R.mipmap.ic_music_black);
        bottomNavigationBar.addItemView(getString(R.string.like), R.mipmap.ic_cards_heart, R.mipmap.ic_cards_heart_black);
        bottomNavigationBar.addItemView(getString(R.string.search), R.mipmap.ic_magnify_outline, R.mipmap.ic_magnify_outline_black);
		bottomNavigationBar.addItemView(getString(R.string.my), R.mipmap.ic_account, R.mipmap.ic_account_black);
        bottomNavigationBar.setSelected(viewModel.index);
		bottomNavigationBar.setOnItemViewSelectedListener(new BottomNavigationBar.OnItemViewSelectedListener() {
				@Override
				public void onItemClcik(View v, int index)
				{
					viewModel.index = index;
					fragmentPage.showFragment(index);
				}
			});
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
					musicController.playMode();
				else
				if (playbackState.getState() == PlaybackStateCompat.STATE_PAUSED)
					musicController.stopMode();
				else
				if (playbackState.getState() == PlaybackStateCompat.STATE_CONNECTING)
					musicController.loadMode();
			}
		}

        @Override
        public void onMetadataChanged(MediaMetadataCompat mediaMetadata)
		{
            if (mediaMetadata == null)
			{
                return;
            }
            musicController.setTitle(
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

