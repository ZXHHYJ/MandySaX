package mandysax.design.navigationbar.widget;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;
import mandysax.design.fragmentpage.widget.FragmentPage;

public class BottomNavigationBar extends LinearLayout implements OnClickListener
{

	public void setTextColor(int checked)
	{
		setTextColor(checked, 0);
	}


	public BottomNavigationBar setTextColor(int checked, int unchecked)
	{
		for (NavigationItem item:items)
		{
			item.setTextColor(checked, unchecked);
		}
		return this;
	}
    public BottomNavigationBar setOnItemViewSelectedListener(OnItemViewSelectedListener onItemViewSelectedListener)
    {
        this.onItemViewSelectedListener = onItemViewSelectedListener;
		return this;
    }

	public BottomNavigationBar setOnItemViewSelectedListener(final FragmentPage page)
	{
		onClick(getChildAt(page.getIndex()));
		this.onItemViewSelectedListener = new OnItemViewSelectedListener(){
			@Override
			public void onItemClcik(int index)
			{
				page.showFragment(index);
			}
		};
		return this;
	}

    @Override
    public void onClick(View p1)
    {
        NavigationItem item=(NavigationItem) p1;  	
		if (index != item.getIndex())
		{
			((NavigationItem)getChildAt(index)).check();
			item.check();
			index = item.getIndex();
		}
		else return;
		if (onItemViewSelectedListener != null)
			onItemViewSelectedListener.onItemClcik(item.getIndex());
    }

    private int index;

    private OnItemViewSelectedListener onItemViewSelectedListener;

    private final List<NavigationItem> items=new ArrayList<NavigationItem>();

    public BottomNavigationBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

	public BottomNavigationBar setChecked(int index)
	{
		onClick(getChildAt(index));
		return this;
	}

    public BottomNavigationBar addItem(String text)
	{
        addItem(text, 0, 0);
		return this;
    }

	public BottomNavigationBar addItem(int image, int image2)
	{
		addItem(null, image, image2);
		return this;
	}

	public BottomNavigationBar addItem(int textRes, int image, int image2)
	{
		addItem(getResources().getString(textRes), image, image2);
		return this;
    }

    public BottomNavigationBar addItem(String text, int image, int image2)
	{
        NavigationItem item=new NavigationItem(items.size(), text, getContext(), image, image2);
        item.setOnClickListener(this);
        items.add(item);
        addView(item);
		if (items.size() == 1)items.get(0).check();
		return this;
    }

    public abstract interface OnItemViewSelectedListener
	{
        public void onItemClcik(int index);
    }

}
