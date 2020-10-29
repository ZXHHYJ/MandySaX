package com.FMJJ.MandySa.ui.main;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.FMJJ.MandySa.R;

public class MusicListViewHolder extends RecyclerView.ViewHolder
{

    public TextView serialNumber,songName,singerName;
    public View onclick;

    public MusicListViewHolder(View view)
    { 
        super(view); 
        serialNumber=view.findViewById(R.id.musicitemNumber);
        songName = view.findViewById(R.id.musiclistSongName);
        singerName = view.findViewById(R.id.musiclistSinger);
        onclick = view.findViewById(R.id.musiclistLinearLayout1);
    } 
}
