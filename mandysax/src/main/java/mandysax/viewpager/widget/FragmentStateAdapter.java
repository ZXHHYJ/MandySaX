package mandysax.viewpager.widget;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import mandysax.fragment.Fragment;
import mandysax.fragment.FragmentTransaction;
import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleRegistry;

public abstract class FragmentStateAdapter extends RecyclerView.Adapter<FragmentStateAdapter.FragmentViewHolder> {

    @NonNull
    @Override
    public FragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Fragment fragment = createFragment(viewType);
        return new FragmentViewHolder(parent.getContext(), fragment);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position) {
    }

    @Override
    public void onViewAttachedToWindow(@NonNull FragmentViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (!holder.mFragment.isAdded())
            beginTransaction().add(holder.itemView.getId(), holder.mFragment).commitNow();
        else fragmentByRegistry(holder.mFragment).markState(Lifecycle.Event.ON_START);
    }

    private LifecycleRegistry fragmentByRegistry(@NonNull Fragment fragment) {
        return ((LifecycleRegistry) fragment.getViewLifecycleOwner().getLifecycle());
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FragmentViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        fragmentByRegistry(holder.mFragment).markState(Lifecycle.Event.ON_STOP);
    }

    @NonNull
    public abstract FragmentTransaction beginTransaction();

    public Fragment createFragment(int position) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class FragmentViewHolder extends RecyclerView.ViewHolder {

        public final Fragment mFragment;

        public FragmentViewHolder(Context context, @NonNull Fragment fragment) {
            super(new FrameLayout(context));
            itemView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
            itemView.setId(fragment.hashCode());
            itemView.setSaveEnabled(false);
            mFragment = fragment;
        }

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.setItemViewCacheSize(getItemCount());
    }

}
