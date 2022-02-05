package mandysax.viewpager.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.jetbrains.annotations.Contract;

import mandysax.viewpager.adapter.FragmentStateAdapter;

public class ViewPager extends RecyclerView {

    private OnPageChangeCallback mPageChangeCallback;

    private Boolean mUserInputEnabled = true;

    private int mPosition = 0;

    @Orientation
    private int mOrientation = RecyclerView.HORIZONTAL;

    @Nullable
    private LayoutManager createLayoutManager() {
        switch (mOrientation) {
            case RecyclerView.HORIZONTAL: {
                return new StaggeredGridLayoutManager(1, RecyclerView.HORIZONTAL) {
                    @Override
                    public boolean canScrollHorizontally() {
                        return mUserInputEnabled;
                    }
                };
            }
            case RecyclerView.VERTICAL: {
                return new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false) {

                    @Override
                    public boolean canScrollVertically() {
                        return mUserInputEnabled;
                    }
                };
            }
            default:
                return null;
        }
    }

    public ViewPager(@NonNull Context context) {
        super(context);
    }

    public ViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.position = mPosition;
        ss.fragmentStateAdapter = (FragmentStateAdapter) getAdapter();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setAdapter(ss.fragmentStateAdapter);
        setCurrentItem(ss.position);
    }

    static class SavedState extends BaseSavedState {
        int position;
        FragmentStateAdapter fragmentStateAdapter;

        /**
         * Constructor called from {@link CompoundButton#onSaveInstanceState()}
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            position = (int) in.readValue(getClass().getClassLoader());
            fragmentStateAdapter = (FragmentStateAdapter) in.readValue(getClass().getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(position);
            out.writeValue(fragmentStateAdapter);
        }

        @SuppressWarnings("hiding")
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    @NonNull
                    @Contract("_ -> new")
                    @Override
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    @NonNull
                    @Contract(value = "_ -> new", pure = true)
                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        setLayoutManager(createLayoutManager());
        if (mOrientation == HORIZONTAL) {
            SnapHelper snapHelper = new PagerSnapHelper();
            setOnFlingListener(null);
            snapHelper.attachToRecyclerView(this);
        }
        addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof StaggeredGridLayoutManager && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
                    changePosition(manager.findLastVisibleItemPositions(null)[0]);
                } else if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                    changePosition(manager.findFirstVisibleItemPosition());
                }

            }

        });
        super.setAdapter(adapter);
    }

    public void setOrientation(@Orientation int orientation) {
        mOrientation = orientation;
    }

    @Orientation
    public int getOrientation() {
        return mOrientation;
    }

    public void setCurrentItem(int position) {
        mPosition = position;
        if (mUserInputEnabled) {
            smoothScrollToPosition(position);
        } else {
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
                manager.scrollToPositionWithOffset(position, 0);
            } else if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                manager.scrollToPositionWithOffset(position, 0);
            }
        }
    }

    public int getCurrentItem() {
        return mPosition;
    }

    public void setUserInputEnabled(Boolean enabled) {
        mUserInputEnabled = enabled;
    }

    public void registerOnPageChangeCallback(OnPageChangeCallback callback) {
        mPageChangeCallback = callback;
    }

    private void changePosition(int position) {
        mPosition = position;
        if (mPageChangeCallback != null)
            mPageChangeCallback.onPageSelected(position);
    }

    public interface OnPageChangeCallback {
        void onPageSelected(int position);
    }

}
