package studio.mandysa.music.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import studio.mandysa.music.R;

public class PlaylistViewHolder extends RecyclerView.ViewHolder {
    public final ImageView cover;

    public final TextView title;

    public PlaylistViewHolder(View view) {
        super(view);
        cover = view.findViewById(R.id.song_list_item_cover_view);
        title = view.findViewById(R.id.song_list_item_title);
    }
}
