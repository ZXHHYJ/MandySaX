package com.FMJJ.MandySa;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import com.FMJJ.MandySa.Fragment.HomeFragment;
import com.FMJJ.MandySa.Fragment.MyFragment;
import com.FMJJ.MandySa.Fragment.RecommendFragment;
import com.FMJJ.MandySa.Fragment.SearchFragment;
import com.FMJJ.MandySa.R;
import com.FMJJ.MandySa.Service.MusicService;
import com.FMJJ.MandySa.ViewModel.MainViewModel;
import java.util.ArrayList;
import java.util.List;
import mandysax.Design.BottomNavigationBar;
import mandysax.Design.FragmentPage;
import mandysax.Design.MusicView;
import mandysax.Lifecycle.LifecycleActivity;
import mandysax.Lifecycle.LiveData.Observer;
import mandysax.Lifecycle.ViewModel.ViewModelProviders;
import mandysax.Service.MediaService;
import mandysax.Service.StateEvent.onChange;
import simon.tuke.Tuke;

public class MainActivity extends LifecycleActivity
{
	private final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

	private HomeFragment home_fragment;

	private RecommendFragment recommend_fragment;

	private SearchFragment search_fragment;

	private MyFragment my_fragment;

	private MusicView music_view;

	private FragmentPage fragment_pgae;

	private BottomNavigationBar bottomNavigationBar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (Tuke.get("mode", null) == null)startActivity(new Intent(this, StartActivity.class));
		viewModel.bindService(this);
		setContentView(R.layout.main);
		viewModel.music_binder.observe(this, new Observer<MusicService.MusicBinder>(){

				@Override
				public void onChanged(MediaService.MusicBinder p1)
				{
					p1.setOnChange(new onChange(){
							@Override
							public void onEvent(MediaService.MusicBinder root, int mode)
							{
								switch (mode)
								{
									case MediaService.LOADMUSIC:
										music_view.loadmode();
										music_view.setTitle(root.getTitle());
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
		music_view = findViewById(R.id.mainMusicView1);
		fragment_pgae = findViewById(R.id.mainFragmentPage1);
		initFragment();
		bottomNavigationBar = findViewById(R.id.mainBottomNavigationBar1);
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
		music_view.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
				}
			});
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
