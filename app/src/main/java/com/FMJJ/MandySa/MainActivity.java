package com.FMJJ.MandySa;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import com.FMJJ.MandySa.Data.*;
import com.FMJJ.MandySa.Fragment.*;
import com.FMJJ.MandySa.Service.*;
import com.FMJJ.MandySa.ViewModel.*;
import java.util.*;
import mandysax.Annotation.*;
import mandysax.Data.*;
import mandysax.Design.*;
import mandysax.Lifecycle.*;
import mandysax.Lifecycle.Anna.*;
import mandysax.Lifecycle.Anna.AnnaEvent.*;
import mandysax.Lifecycle.LiveData.*;
import mandysax.Lifecycle.ViewModel.*;
import mandysax.Service.*;
import mandysax.Service.StateEvent.*;
import mandysax.Tools.*;
import org.json.*;
import simon.tuke.*;

import mandysax.Lifecycle.LiveData.Observer;

@BindLayoutId(R.layout.main)
public class MainActivity extends LifecycleActivity
{
	private final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

	private HomeFragment home_fragment;

	private RecommendFragment recommend_fragment;

	private SearchFragment search_fragment;

	private MyFragment my_fragment;

	@BindView(R.id.mainMusicView1)
	private MusicView music_view;

	@BindView(R.id.mainFragmentPage1)
	private FragmentPage fragment_pgae;

	@BindView(R.id.mainBottomNavigationBar1)
	private BottomNavigationBar bottomNavigationBar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (Tuke.get("mode", null) == null)startActivity(new Intent(this, StartActivity.class));
		AnnotationTool.init(this);
		viewModel.bindService(this);
		viewModel.music_binder.observe(this, new Observer<MusicService.MusicBinder>(){

				@Override
				public void onChanged(MediaService.MusicBinder p1)
				{
					p1.setOnChange(new onChange(){
							@Override
							public void onEvent(final MediaService.MusicBinder root, int mode)
							{
								switch (mode)
								{
									case MediaService.LOADMUSIC:
										music_view.loadmode();
										music_view.setTitle(root.getTitle());
										Anna.getJsonArray(url.url3 + root.getMusicItem().getId())
											.addString("songs")
											.setOnEvent(new onEvent<JSONArray>(){

												@Override
												public void onError()
												{
													// TODO: Implement this method
												}
												
												@Override
												public void onEnd(JSONArray object)
												{
													try
													{
														root.setAlbum(ImageUtils.getBitMBitmap(get.jsonstring(object.getJSONObject(0).getString("al"), "picUrl")));
													}
													catch (JSONException e)
													{}
												}												
											}).start();
										break;
									case MediaService.PLAY:
										music_view.playmode();
										break;
									case MediaService.PAUSE:
										music_view.stopmode();
										break;
								}
							}			
						});
				}	
			});
		initFragment();
		bottomNavigationBar.setTextColorRes(R.color.theme_color);
        bottomNavigationBar.addItemView("Home", R.mipmap.ic_music, R.mipmap.ic_music_black);
        bottomNavigationBar.addItemView("Recommend", R.mipmap.ic_cards_heart, R.mipmap.ic_cards_heart_black);
        bottomNavigationBar.addItemView("Search", R.mipmap.ic_magnify_outline, R.mipmap.ic_magnify_outline_black);
		bottomNavigationBar.addItemView("My", R.mipmap.ic_account, R.mipmap.ic_account_black);
        bottomNavigationBar.setOnItemViewSelectedListener(new BottomNavigationBar.OnItemViewSelectedListener() {
				@Override
				public void onItemClcik(View v, int index)
				{
					viewModel.index = index;
					fragment_pgae.showFragment(index);
				}
			});
		bottomNavigationBar.setSelected(viewModel.index);
		music_view.getPlaybutton().setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					viewModel.music_binder.getValue().playOrPause();
				}
			});
	}

	private void initFragment()
	{
		FragmentManager fragmentManager = getFragmentManager();
		home_fragment = (HomeFragment) fragmentManager.findFragmentByTag("0");
		recommend_fragment = (RecommendFragment) fragmentManager.findFragmentByTag("1");
		search_fragment = (SearchFragment) fragmentManager.findFragmentByTag("2");
		my_fragment = (MyFragment) fragmentManager.findFragmentByTag("3");
		if (home_fragment == null)home_fragment = new HomeFragment();
		if (recommend_fragment == null)recommend_fragment = new RecommendFragment();
		if (search_fragment == null)search_fragment = new SearchFragment();
		if (my_fragment == null)my_fragment = new MyFragment();
		List<Fragment> list_fragment = new ArrayList<Fragment>();
		list_fragment.add(home_fragment);
		list_fragment.add(recommend_fragment);
		list_fragment.add(search_fragment);
		list_fragment.add(my_fragment);
		fragment_pgae.add(list_fragment);
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

}
