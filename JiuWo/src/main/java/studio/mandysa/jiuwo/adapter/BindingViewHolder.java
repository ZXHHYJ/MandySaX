package studio.mandysa.jiuwo.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BindingViewHolder extends RecyclerView.ViewHolder {

    public final int itemViewType;

    public BindingViewHolder(@NonNull ViewGroup parent, int viewType) {
        super(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        itemViewType = viewType;
    }

}
