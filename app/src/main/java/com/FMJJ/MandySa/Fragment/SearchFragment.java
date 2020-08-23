package com.FMJJ.MandySa.Fragment;

import android.content.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.*;
import androidx.swiperefreshlayout.widget.*;
import com.FMJJ.MandySa.*;
import com.FMJJ.MandySa.Service.*;
import com.FMJJ.MandySa.ViewHolder.*;
import com.FMJJ.MandySa.ViewModel.*;
import java.util.*;
import java.util.regex.*;
import mandysax.Lifecycle.*;
import mandysax.Lifecycle.LiveData.*;
import mandysax.Lifecycle.Paradrop.*;
import mandysax.Lifecycle.ViewModel.*;
import mandysax.Service.*;

import mandysax.Lifecycle.LiveData.Observer;

public class SearchFragment extends LifecycleFragment
{

	private final SearchViewModel viewModel=ViewModelProviders.of(this).get(SearchViewModel.class);;

	private music_listAdaper music_listAdaper;

	private EditText search_edit;

	private RecyclerView music_rv;

	private ImageView back;

	private SwipeRefreshLayout music_sl;

	@Override
	public int getLayoutId()
	{
		// TODO: Implement this method
		return R.layout.search_fragment;
	}

	@Override
	public void initView(View view)
	{
		// TODO: Implement this method
		back = view.findViewById(R.id.searchImageView1);
		back.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					search();
				}		
			});
		search_edit = view.findViewById(R.id.searchEditText1);
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
		music_rv = view.findViewById(R.id.searchfragmentRecyclerView1);
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
		music_sl = view.findViewById(R.id.searchfragmentSwipeRefreshLayout1);
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
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		viewModel.song_search.observeForever(new Observer<List<MusicItem>>() {

				@Override
				public void onChanged(List<MusicItem> p1)
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
		viewModel.song_bottom.observeForever(new Observer<List<MusicItem>>() {

				@Override
				public void onChanged(List<MusicItem> p1)
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
			setSearchContentColor(list.get(p2).getTitle(), search_edit.getText().toString(), p1.Song_name);
			String singer_name="";
			for(SingerItem i : list.get(p2).getSinger())
			{
				if(!singer_name.equals(""))
				if(list.get(p2).getSinger().indexOf(i)!=0)
					singer_name+="/";
				singer_name += i.getName();
			}
			setSearchContentColor(singer_name, search_edit.getText().toString(), p1.Singer_name);
			p1.onclick.setOnClickListener(new View.OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						((MusicService.MusicBinder)paradrop.getDrop("music_binder")).playMusic(list, p2);
					}
				});
		}

		private final List<MusicItem> list;

		private final Context context;

		@Override
		public int getItemViewType(int position)
		{
			return position;
		} 

		public music_listAdaper(Context p0, List<MusicItem> p1)
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


}

