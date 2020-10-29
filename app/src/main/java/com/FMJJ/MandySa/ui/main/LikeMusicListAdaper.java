package com.FMJJ.MandySa.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.FMJJ.MandySa.R;
import com.FMJJ.MandySa.logic.model.MusicItem;
import com.FMJJ.MandySa.logic.model.contentcatalogs.MusicLibrary;
import com.FMJJ.MandySa.ui.main.MusicListViewHolder;
import java.util.ArrayList;

public class LikeMusicListAdaper extends RecyclerView.Adapter<MusicListViewHolder>
{

    @Override
    public void onBindViewHolder(MusicListViewHolder p1, final int p2)
    {
        p1.serialNumber.setText(p2+1+"");
        p1.songName.setText(list.get(p2).title);
        p1.singerName.setText(list.get(p2).artist);
        p1.onclick.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    MusicLibrary.addMusic(list.get(p2).createMediaMetadataCompat());
                }
                
            
        });
    }

    private final ArrayList<MusicItem> list;

    private final Context context;

    @Override
    public int getItemViewType(int position)
    {
        return position;
    } 

    public LikeMusicListAdaper(Context p0, ArrayList<MusicItem> p1)
    { 
        this.context = p0;
        this.list = p1;
    }

    public MusicListViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    { 
        final View view = LayoutInflater.from(context).inflate(R.layout.music_item, parent, false); 
        return new MusicListViewHolder(view);
    } 

    public int getItemCount()
    { 
        return list.size(); 
    } 

} 
