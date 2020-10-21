package com.FMJJ.MandySa.ui.main;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.FMJJ.MandySa.R;

public class MusicListViewHolder extends RecyclerView.ViewHolder
{

    public TextView Serial_number,Song_name,Singer_name;
    public View onclick;

    public MusicListViewHolder(View view)
    { 
        super(view); 
        Serial_number=view.findViewById(R.id.musicitemNumber);
        Song_name = view.findViewById(R.id.musiclistSongName);
        Singer_name = view.findViewById(R.id.musiclistSinger);
        onclick = view.findViewById(R.id.musiclistLinearLayout1);
    } 
}
