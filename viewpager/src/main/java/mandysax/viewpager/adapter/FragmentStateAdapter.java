package mandysax.viewpager.adapter;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

import mandysax.fragment.Fragment;
import mandysax.fragment.FragmentManager;
import mandysax.viewpager.widget.ViewPager;

public class FragmentStateAdapter extends RecyclerView.Adapter<FragmentStateAdapter.FragmentViewHolder> implements Parcelable {

    @RecyclerView.Orientation
    private int mOrientation;

    private ArrayList<Integer> mFragmentTags;

    private ArrayList<Fragment> mFragments;

    public FragmentStateAdapter() {

    }

    protected FragmentStateAdapter(@NonNull Parcel in) {
        mOrientation = in.readInt();
        mFragmentTags = (ArrayList<Integer>) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<FragmentStateAdapter> CREATOR = new Creator<FragmentStateAdapter>() {
        @NonNull
        @Contract("_ -> new")
        @Override
        public FragmentStateAdapter createFromParcel(Parcel in) {
            return new FragmentStateAdapter(in);
        }

        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public FragmentStateAdapter[] newArray(int size) {
            return new FragmentStateAdapter[size];
        }
    };

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        super.clone();
        return new FragmentStateAdapter() {

            {
                mFragmentTags = (ArrayList<Integer>) mFragmentTags.clone();
            }

            @Override
            public int getItemCount() {
                return mFragmentTags.size();
            }

        };
    }

    @NonNull
    @Override
    public FragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mFragmentTags == null && mFragments == null) {
            mFragmentTags = new ArrayList<>();
            mFragments = new ArrayList<>();
            for (int i = 0; i < getItemCount(); i++) {
                Fragment fragment = createFragment(i);
                mFragments.add(fragment);
                mFragmentTags.add(fragment.hashCode());
            }
        }
        Fragment fragment = mFragments.get(viewType);
        if (fragment == null) {
            fragment = getFragmentManager().findFragmentById(mFragmentTags.get(viewType));
        }
        FrameLayout itemView = new FrameLayout(parent.getContext());
        itemView.setLayoutParams(new FrameLayout.LayoutParams(-1, mOrientation == RecyclerView.HORIZONTAL ? -1 : -2));
        itemView.setId(mFragmentTags.get(viewType));
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
        if (!holder.mFragment.isAdded())
            getFragmentManager().beginTransaction().add(holder.itemView.getId(), holder.mFragment).commitNow();
        else getFragmentManager().beginTransaction().show(holder.mFragment).commitNow();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FragmentViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        getFragmentManager().beginTransaction().hide(holder.mFragment).commitNow();
    }

    public FragmentManager getFragmentManager() {
        return null;
    }

    public Fragment createFragment(int position) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(mOrientation);
        dest.writeValue(mFragmentTags);
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
