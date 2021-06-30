package studio.mandysa.music.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import studio.mandysa.music.R;

public class SingerViewHolder extends RecyclerView.ViewHolder {

    public final ImageView avatar;

    public final TextView singerName;

    public SingerViewHolder(@NonNull View itemView) {
        super(itemView);
        avatar = itemView.findViewById(R.id.iv_singerItem_avatar);
        singerName = itemView.findViewById(R.id.tv_singerItem_singerName);
    }
}
