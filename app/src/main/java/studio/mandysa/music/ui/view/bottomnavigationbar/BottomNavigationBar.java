package studio.mandysa.music.ui.view.bottomnavigationbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigationBar extends LinearLayout implements OnClickListener {

    public BottomNavigationBar setTextColor(int checked) {
        return setTextColor(checked, 0);
    }

    public BottomNavigationBar setTextColor(int checked, int unchecked) {
        for (NavigationItem item : mItemList) {
            item.setTextColor(checked, unchecked);
        }
        return this;
    }

    public BottomNavigationBar setOnItemViewSelectedListener(OnItemViewSelectedListener mListener) {
        this.mListener = mListener;
        return this;
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

    private int mPosition;

    private OnItemViewSelectedListener mListener;

    private final List<NavigationItem> mItemList = new ArrayList<>();

    public BottomNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomNavigationBar setPosition(int position) {
        onClick(getChildAt(position));
        if (mListener != null) mListener.onclick(position);
        return this;
    }

    public BottomNavigationBar addItem(String text) {
        addItem(0,text);
        return this;
    }

    public BottomNavigationBar addItem(int image) {
        addItem(image,null);
        return this;
    }

    public BottomNavigationBar addItem(int image, int textRes) {
        addItem(image, getResources().getString(textRes));
        return this;
    }

    public BottomNavigationBar addItem(int image, String text) {
        NavigationItem item = new NavigationItem(mItemList.size(), text, getContext(), image);
        item.setOnClickListener(this);
        mItemList.add(item);
        addView(item);
        if (mItemList.size() == 1) mItemList.get(0).check();
        return this;
    }

    public interface OnItemViewSelectedListener {
        void onclick(int position);
    }

}
