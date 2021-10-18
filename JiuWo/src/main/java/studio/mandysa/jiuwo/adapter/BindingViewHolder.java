package studio.mandysa.jiuwo.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class BindingViewHolder extends RecyclerView.ViewHolder {

    public final int itemViewType;

    public BindingViewHolder(ViewGroup parent, int viewType) {
        super(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        itemViewType = viewType;
    }

}
