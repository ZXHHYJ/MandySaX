package studio.mandysa.jiuwo.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BindingViewHolder extends RecyclerView.ViewHolder {

    public final int itemViewType;



    public BindingViewHolder(@NonNull ViewCreate viewCreate) {
        super(viewCreate.view);
        itemViewType = viewCreate.itemViewType;
    }

}
