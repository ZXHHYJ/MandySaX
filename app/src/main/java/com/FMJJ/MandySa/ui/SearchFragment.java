package com.FMJJ.MandySa.ui;

import android.content.*;
import android.os.*;
import android.support.v4.media.*;
import android.text.*;
import android.text.style.*;
import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.*;
import androidx.swiperefreshlayout.widget.*;
import com.FMJJ.MandySa.*;
import com.FMJJ.MandySa.model.*;
import com.FMJJ.MandySa.service.contentcatalogs.*;
import java.util.*;
import java.util.regex.*;
import mandysax.core.annotation.*;
import mandysax.lifecycle.*;

import mandysax.lifecycle.Observer;

@BindLayoutId(R.layout.search_fragment)
public class SearchFragment extends FragmentCompat
{

	private final SearchViewModel viewModel=ViewModelProviders.of(this).get(SearchViewModel.class);;

	private music_listAdaper music_listAdaper;

	@BindView(R.id.searchEditText1)
	private EditText search_edit;

	@BindView(R.id.searchfragmentRecyclerView1)
	private RecyclerView music_rv;

	@BindView(R.id.searchImageView1)
	private ImageView back;

	@BindView(R.id.searchfragmentSwipeRefreshLayout1)
	private SwipeRefreshLayout music_sl;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		search_edit.setOnKeyListener(new View.OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event)
				{
					if (keyCode == KeyEvent.KEYCODE_ENTER)
					{	
						search();
					}
					return false;
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

	private void search()
	{
		music_sl.setRefreshing(true);
		viewModel.song_search(search_edit.getText().toString());	
	}

	private class music_listAdaper extends RecyclerView.Adapter<musicList_ViewHolder>
	{

		@Override
		public void onBindViewHolder(musicList_ViewHolder p1, final int p2)
		{
			final String title=list.get(p2).getString(MediaMetadataCompat.METADATA_KEY_TITLE);
			final String singer=list.get(p2).getString(MediaMetadataCompat.METADATA_KEY_ARTIST);
			setSearchContentColor(title, search_edit.getText().toString(), p1.Song_name);
			setSearchContentColor(singer, search_edit.getText().toString(), p1.Singer_name);
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

