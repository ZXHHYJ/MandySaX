package studio.mandysa.music.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import studio.mandysa.music.R;

public class SongViewHolder extends RecyclerView.ViewHolder {

    public final TextView songName, singerName;

    public final ImageView cover;

    public SongViewHolder(View view) {
        super(view);
        cover = view.findViewById(R.id.iv_song_item_cover);
        songName = view.findViewById(R.id.tv_song_item_song_name);
        singerName = view.findViewById(R.id.tv_song_item_singer_name);
    }
}
