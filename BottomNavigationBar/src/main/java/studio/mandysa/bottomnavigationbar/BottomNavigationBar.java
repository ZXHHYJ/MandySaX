package studio.mandysa.bottomnavigationbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class BottomNavigationBar extends LinearLayout implements OnClickListener {

    private final List<NavigationItem> mItemList = new ArrayList<>();
    private int mPosition;
    private OnItemViewSelectedListener mListener;

    public BottomNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomNavigationBar setTextColor(int checked) {
        return setTextColor(checked, 0);
    }

    public BottomNavigationBar setTextColor(int checked, int unchecked) {
        for (NavigationItem item : mItemList) {
            item.setTextColor(checked, unchecked);
        }
        return this;
    }

    public void setOnItemViewSelectedListener(OnItemViewSelectedListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onClick(View p1) {
        NavigationItem item = (NavigationItem) p1;
        if (mPosition != item.getPosition()) {
            ((NavigationItem) getChildAt(mPosition)).check();
            item.check();
            mPosition = item.getPosition();
        } else return;
        if (mListener != null)
            mListener.onclick(item.getPosition());
    }

    public void setPosition(int position) {
        onClick(getChildAt(position));
        if (mListener != null) mListener.onclick(position);
    }

    public int getPosition() {
        return mPosition;
    }

    public BottomNavigationBar addItem(String text) {
        addItem(0, text);
        return this;
    }

    public BottomNavigationBar addItem(int image) {
        addItem(image, null);
        return this;
    }

    public BottomNavigationBar addItem(int image, int textRes) {
        addItem(image, getResources().getString(textRes));
        return this;
    }

    public void addItem(int image, String text) {
        NavigationItem item = new NavigationItem(mItemList.size(), text, getContext(), image);
        item.setOnClickListener(this);
        mItemList.add(item);
        addView(item);
        if (mItemList.size() == 1) mItemList.get(0).check();
    }

    public interface OnItemViewSelectedListener {
        void onclick(int position);
    }

}
