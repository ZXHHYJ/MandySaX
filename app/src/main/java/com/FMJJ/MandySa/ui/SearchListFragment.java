package com.FMJJ.MandySa.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.FMJJ.MandySa.R;
import com.FMJJ.MandySa.model.SearchViewModel;
import com.FMJJ.MandySa.service.contentcatalogs.MusicLibrary;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mandysax.core.annotation.BindLayoutId;
import mandysax.core.annotation.BindView;
import mandysax.lifecycle.FragmentCompat;
import mandysax.lifecycle.Observer;
import mandysax.lifecycle.ViewModelProviders;

@BindLayoutId(R.layout.search_list_fragment)
public class SearchListFragment extends FragmentCompat
{

    private SearchViewModel viewModel;

	private music_listAdaper music_listAdaper;

	@BindView(R.id.searchbarEditText1)
	private EditText search_edit;

	@BindView(R.id.searchbarImageView1)
	private ImageView search;

	@BindView(R.id.searchfragmentRecyclerView1)
	private RecyclerView music_rv;

	@BindView(R.id.searchfragmentSwipeRefreshLayout1)
	private SwipeRefreshLayout music_sl;

	@Override
	public void onStart()
	{
		search_edit.setText(viewModel.getSearchName());
		super.onStart();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
		viewModel.status.observe(this, new Observer<Integer>(){

				@Override
				public void onChanged(Integer p1)
				{
					if (p1 == viewModel.LOAD)
					{
						System.out.println("执行了这里");
						music_sl.setRefreshing(true);
					}
					else if (p1 == viewModel.BUG){
						music_sl.setRefreshing(false);
						Toast.makeText(getContext(), "电波无法到达哟", 10).show();
						}
				}

			});
		search.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					startFragment(SearchFragment.class);
				}	

			});
		search_edit.setFocusable(false);
		search_edit.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					startFragment(SearchFragment.class);
				}
			});
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
		music_rv.setLayoutManager(linearLayoutManager);  
        music_rv.setHasFixedSize(true);
		music_listAdaper = new music_listAdaper(getContext(), viewModel.song_list);
		music_rv.setAdapter(music_listAdaper);
		music_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {

				@Override
				public void onScrolled(RecyclerView recyclerView, int dx, int dy)
				{
					super.onScrolled(recyclerView, dx, dy);
					LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
					int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
					if (lastCompletelyVisibleItemPosition == layoutManager.getItemCount() - 1)
					{
						music_sl.setRefreshing(true);			
						viewModel.song_bottom();
					}
				}
			});
        music_sl.setColorScheme(R.color.theme_color);
        music_sl.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh()
				{
					music_sl.setRefreshing(true);       
					viewModel.song_search(null);
                }
            }
        );
		viewModel.song_search.observeForever(new Observer<List<MediaMetadataCompat>>() {
				@Override
				public void onChanged(List<MediaMetadataCompat> p1)
				{
					if (p1 != null)
					{
						viewModel.song_list.clear();
						viewModel.song_list.addAll(p1);	
						music_listAdaper.notifyDataSetChanged(); 
					}
					music_sl.setRefreshing(false);
				}
			});
		viewModel.song_bottom.observeForever(new Observer<List<MediaMetadataCompat>>() {

				@Override
				public void onChanged(List<MediaMetadataCompat> p1)
				{
					if (p1 != null)
					{
						viewModel.song_list.addAll(p1);	
						music_listAdaper.notifyDataSetChanged();
					}
					music_sl.setRefreshing(false);
				}
			});
	}

	private class music_listAdaper extends RecyclerView.Adapter<musicList_ViewHolder>
	{

		@Override
		public void onBindViewHolder(musicList_ViewHolder p1, final int p2)
		{
			final String title=list.get(p2).getString(MediaMetadataCompat.METADATA_KEY_TITLE);
			final String singer=list.get(p2).getString(MediaMetadataCompat.METADATA_KEY_ARTIST);
			setSearchContentColor(title, viewModel.getSearchName(), p1.Song_name);
			setSearchContentColor(singer, viewModel.getSearchName(), p1.Singer_name);
			p1.onclick.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View p1)
					{
						MusicLibrary.addMusic(list.get(p2));
					}
				});
		}

		private final List<MediaMetadataCompat> list;

		private final Context context;

		@Override
		public int getItemViewType(int position)
		{
			return position;
		} 

		public music_listAdaper(Context p0, List<MediaMetadataCompat> p1)
		{ 
			this.context = p0;
			this.list = p1;
		}

		public musicList_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
		{ 
			View view = LayoutInflater.from(context).inflate(R.layout.music_list, parent, false); 
			return new musicList_ViewHolder(view);
		} 

		public int getItemCount()
		{ 
			return list.size(); 
		} 

		private void setSearchContentColor(String name, String KeyWord, TextView tv)
		{
			SpannableString s = new SpannableString(name);
			Pattern p = Pattern.compile(KeyWord);
			Matcher m = p.matcher(s);
			while (m.find())
			{
				int start = m.start();
				int end = m.end();
				s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.search_color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			tv.setText(s);
		}

	} 

	private class musicList_ViewHolder extends RecyclerView.ViewHolder
	{ 

		public TextView Song_name,Singer_name;
		public View onclick;

		public musicList_ViewHolder(View view)
		{ 
			super(view); 
			Song_name = view.findViewById(R.id.musiclistTextView1);
			Singer_name = view.findViewById(R.id.musiclistTextView2);
			onclick = view.findViewById(R.id.musiclistLinearLayout1);
		} 
	}

}

