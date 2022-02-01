package mandysax.viewpager.widget;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class ViewPager extends RecyclerView {
    private OnPageChangeCallback mPageChangeCallback;

    private Boolean mUserInputEnabled = true;

    private final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, RecyclerView.HORIZONTAL) {
        @Override
        public boolean canScrollHorizontally() {
            return mUserInputEnabled;
        }
    };

    public ViewPager(@NonNull Context context) {
        super(context);
        init();
    }

    public ViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayoutManager(layoutManager);
        new PagerSnapHelper().attachToRecyclerView(this);
        addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (mPageChangeCallback != null && manager != null)
                        mPageChangeCallback.onPageSelected(manager.findLastVisibleItemPositions(null)[0]);
                }
            }

        });
    }

    public void setCurrentItem(int position) {
        if (mUserInputEnabled) {
            smoothScrollToPosition(position);
        } else layoutManager.scrollToPositionWithOffset(position, 0);
    }


    public void setUserInputEnabled(Boolean enabled) {
        mUserInputEnabled = enabled;
    }

    public void registerOnPageChangeCallback(OnPageChangeCallback callback) {
        mPageChangeCallback = callback;
    }

    public interface OnPageChangeCallback {
        void onPageSelected(int position);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }
}
