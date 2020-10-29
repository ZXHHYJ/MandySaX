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
import android.widget.Button;

public class LikeFragment extends Fragment
{

	private View playAll;

    private RecyclerView list;

    private LikeMusicListAdaper musicListAdaper; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container)
	{
		return inflater.inflate(R.layout.like_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
        playAll = findViewById(R.id.likefragmentinclude1);
        playAll.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                }

            });
        list = findViewById(R.id.likefragmentRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(linearLayoutManager);  
        list.setHasFixedSize(true);
        musicListAdaper = new LikeMusicListAdaper(getContext(), LikeMusicDao.getMusicList());
        list.setAdapter(musicListAdaper);
	} 

}
