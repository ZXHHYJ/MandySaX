package com.FMJJ.MandySa.Fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.FMJJ.MandySa.Adapder.music_listAdaper;
import com.FMJJ.MandySa.Data.music_bean;
import com.FMJJ.MandySa.R;
import com.FMJJ.MandySa.ViewModel.SearchViewModel;
import java.util.List;
import mandysax.Lifecycle.LifecycleFragment;
import mandysax.Lifecycle.LiveData.Observer;
import mandysax.Lifecycle.ViewModel.ViewModelProviders;

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
						new Thread(new Runnable(){
								@Override
								public void run()
								{
									viewModel.song_bottom();
								}
							}).start();
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
                    new Thread(new Runnable(){
							@Override
							public void run()
							{
								viewModel.song_search(null);
							}
						}).start();
                }
            }
        );
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		viewModel.song_search.observeForever(new Observer<List<music_bean>>() {

				@Override
				public void onChanged(List<music_bean> p1)
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
		viewModel.song_bottom.observeForever(new Observer<List<music_bean>>() {

				@Override
				public void onChanged(List<music_bean> p1)
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
		new Thread(new Runnable(){
				@Override
				public void run()
				{
					viewModel.song_search(search_edit.getText().toString());
				}
			}).start();
	}

}
