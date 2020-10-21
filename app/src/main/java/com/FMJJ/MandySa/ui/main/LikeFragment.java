package com.FMJJ.MandySa.ui.main;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.FMJJ.MandySa.R;
import com.FMJJ.MandySa.logic.dao.LikeMusicDao;
import mandysax.lifecycle.Fragment;

public class LikeFragment extends Fragment
{

	//private Button button;
    private RecyclerView list;

	//private SwipeRefreshLayout list_sl;

    private LikeMusicListAdaper music_listAdaper; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container)
	{
		return inflater.inflate(R.layout.like_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
        list = view.findViewById(R.id.likefragmentRecyclerView);
        //list_sl = view.findViewById(R.id.likefragmentSwipeRefreshLayout1);
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(linearLayoutManager);  
        list.setHasFixedSize(true);
        music_listAdaper = new LikeMusicListAdaper(getContext(), LikeMusicDao.getMusicList());
        list.setAdapter(music_listAdaper);
        /*list_sl.setColorScheme(R.color.theme_color);
        list_sl.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh()
                {
                    list_sl.setRefreshing(false);
                }
            }
        );*/
	}

}
