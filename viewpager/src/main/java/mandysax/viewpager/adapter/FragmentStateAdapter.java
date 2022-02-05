package mandysax.viewpager.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import mandysax.fragment.Fragment;
import mandysax.fragment.FragmentView;
import mandysax.viewpager.widget.ViewPager;

public class FragmentStateAdapter extends RecyclerView.Adapter<FragmentStateAdapter.FragmentViewHolder> {

    @RecyclerView.Orientation
    private int mOrientation;

    @NonNull
    @Override
    public FragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Fragment fragment = createFragment(viewType);
        FragmentView itemView = new FragmentView(parent.getContext(), fragment);
        itemView.setLayoutParams(new FrameLayout.LayoutParams(-1, mOrientation == RecyclerView.HORIZONTAL ? -1 : -2));
        itemView.setId(View.generateViewId());
        itemView.setSaveEnabled(false);
        return new FragmentViewHolder(itemView, fragment);
    }

    @Override
    public final int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position) {
    }

    @Override
    public void onViewAttachedToWindow(@NonNull FragmentViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.mFragment.getFragmentManager().beginTransaction().show(holder.mFragment).commitNow();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FragmentViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.mFragment.getFragmentManager().beginTransaction().hide(holder.mFragment).commitNow();
    }

    public Fragment createFragment(int position) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class FragmentViewHolder extends RecyclerView.ViewHolder {

        final Fragment mFragment;

        public FragmentViewHolder(View itemView, @NonNull Fragment fragment) {
            super(itemView);
            mFragment = fragment;
        }

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.setItemViewCacheSize(getItemCount());
        if (recyclerView instanceof ViewPager) {
            ViewPager viewPager = (ViewPager) recyclerView;
            mOrientation = viewPager.getOrientation();
        }
    }

}
