package mandysax.viewpager.widget;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class ViewPager extends RecyclerView {

    private OnPageChangeCallback mPageChangeCallback;

    private Boolean mUserInputEnabled = true;

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

  /*  private View getViewByPosition(View view, float x, float y) {
        if (view == null) {
            return null;
        }
        int[] location = new int[2];
        view.getLocationInWindow(location);

        int left = location[0];
        int top = location[1];
        int right = left + view.getWidth();
        int bottom = top + view.getHeight();

        boolean b = left < x && top < y && right > x && bottom > y;
        //当前ViewGroup就是顶层View
        //没找到匹配的
        if (view instanceof ViewGroup) { //当前是ViewGroup容器
            int childCount = ((ViewGroup) view).getChildCount();
            //深度优先， 从最后一个子节点开始遍历，如果找到则返回。 先递归判断子View
            if (childCount > 0) {
                for (int i = childCount - 1; i >= 0; i--) {
                    View topView = getViewByPosition(((ViewGroup) view).getChildAt(i), x, y);
                    if (topView != null) {
                        return topView;
                    }
                }
            }
            //子View都没找到匹配的， 再判断自己
        }  //当前是View
        if (b) {
            return view;   //当前ViewGroup就是顶层View
        } else {
            return null; //没找到匹配的
        }
    }

    @Nullable
    private RecyclerView findViewRecyclerView(View view) {
        if (!(view instanceof ViewGroup)) {
            return findViewRecyclerView(((View) view.getParent()));
        }
        ViewGroup parent = (ViewGroup) view;
        while (parent != null) {
            if (parent instanceof RecyclerView) {
                return (RecyclerView) parent;
            }
            if (parent.getParent() != null && parent.getParent() instanceof ViewGroup)
                parent = (ViewGroup) parent.getParent();
        }
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent e) {
        View touchView = getViewByPosition(this, e.getX(), e.getY());
        if (touchView != null) {
            RecyclerView recyclerView = findViewRecyclerView(touchView);
            if (recyclerView != null && layoutManager != null) {
                LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    return linearLayoutManager.getOrientation() != LinearLayoutManager.HORIZONTAL;
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    return staggeredGridLayoutManager.getOrientation() != GridLayoutManager.HORIZONTAL;
                }
            }
        }
        return super.onTouchEvent(e);
    }*/

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        setLayoutManager(createLayoutManager());
        if (mOrientation == HORIZONTAL)
            new PagerSnapHelper().attachToRecyclerView(this);
        addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof StaggeredGridLayoutManager && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
                    if (mPageChangeCallback != null)
                        mPageChangeCallback.onPageSelected(manager.findLastVisibleItemPositions(null)[0]);
                } else if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                    if (mPageChangeCallback != null)
                        mPageChangeCallback.onPageSelected(manager.findFirstVisibleItemPosition());
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
