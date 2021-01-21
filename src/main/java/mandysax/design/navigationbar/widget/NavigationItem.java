package mandysax.design.navigationbar.widget;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import mandysax.utils.DensityUtils;


/**
 * Created by Dikaros on 2016/5/20.
 */

//mandysa修改于7.12日晚5点
//10.23 pm7.29
 //大改于2021.1.7下午三点四十九分

public final class NavigationItem extends LinearLayout
{
    public NavigationItem(Context context, int textCheckedColor, int textUnCheckedColor, int imageCheckedResource, int imageUnCheckedResource, int textSize)
	{
        super(context);
        this.textCheckedColor = textCheckedColor;
        this.textUnCheckedColor = textUnCheckedColor;
        this.imageUnCheckedResource = imageUnCheckedResource;
        this.imageCheckedResource = imageCheckedResource;
		this.textSize = textSize;
		initViews(context);
    }

    public void setChecked(boolean checked)
	{
        this.checked = checked;
    }

    private boolean textVisible = true;
    private boolean imageVisible = true;

    public void setTextVisible(boolean textVisible)
	{
        this.textVisible = textVisible;
        mTextView.setVisibility(textVisible ? VISIBLE : GONE);
        /*if (!textVisible)
		 {
		 Log.e("textView", "disappear");
		 }*/
    }

    public void setImageVisible(boolean imageVisible)
	{
        this.imageVisible = imageVisible;
        if (mImageView != null)
            mImageView.setVisibility(imageVisible ? VISIBLE : GONE);
    }

    public boolean isImageVisible()
	{
        return imageVisible;
    }

    public boolean isTextVisible()
	{
        return textVisible;
    }

    public TextView getmTextView()
	{
        return mTextView;
    }

    private void initViews(Context context)
	{

        setOrientation(VERTICAL);
        //初始化view
        mTextView = new TextView(context);
        if (imageCheckedResource != 0 && imageUnCheckedResource != 0)
            mImageView = new ImageView(context);
        //设置veiw是否可见
        mTextView.setVisibility(textVisible ? VISIBLE : GONE);
        if (mImageView != null)
        {
            mImageView.setVisibility(imageVisible ? VISIBLE : GONE);
            mImageView.setLayoutParams(new LayoutParams(DensityUtils.dip2px(context, imageWidth), DensityUtils.dip2px(context, imageHeight)));
            //设置默认图片
            mImageView.setImageResource(imageUnCheckedResource);
        }
        //设置默认文字
        mTextView.setText("text");
        //设置默认文字大小
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        //设置内边距
        setPadding(0, DensityUtils.dip2px(context, imagePaddingTop), 0, DensityUtils.dip2px(context, textPaddingBottom));
        //设置自身布局
        setGravity(Gravity.CENTER);
        //设置text和image的布局参数
        mTextView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //设置最小宽度
        setMinimumWidth(DensityUtils.dip2px(context, 80));
        //设置本身的布局
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        //设置文字颜色
        mTextView.setTextColor(textUnCheckedColor);
        if (mImageView != null)
            addView(mImageView);
        addView(mTextView);
    }

	private  boolean checked = false;

    public boolean isChecked()
	{
        return checked;
    }

    //文字
	private TextView mTextView;
    //图片
	private ImageView mImageView;
    //文字大小
	private float textSize;

	private final int imageWidth = 24;
	private final int imageHeight=24;
	private final int imagePaddingTop=8;
	private final int textPaddingBottom=10;

    private int textCheckedColor;
    private int textUnCheckedColor;

    private int imageCheckedResource;
    private int imageUnCheckedResource;

    public void setImageCheckedResource(int imageCheckedResource)
	{
        this.imageCheckedResource = imageCheckedResource;
    }

    public void setImageUnCheckedResource(int imageUnCheckedResource)
	{
        this.imageUnCheckedResource = imageUnCheckedResource;
    }

    public void setTextCheckedColor(int textCheckedColor)
	{
        this.textCheckedColor = textCheckedColor;
    }

    public void setTextUnCheckedColor(int textUnCheckedColor)
	{
        this.textUnCheckedColor = textUnCheckedColor;
    }


    public void showView()
	{
        if (checked)
		{
            mTextView.setTextColor(textCheckedColor);
            if (mImageView != null)
                mImageView.setImageResource(imageCheckedResource);
        }
		else
		{
            mTextView.setTextColor(textUnCheckedColor);
            if (mImageView != null)
                mImageView.setImageResource(imageUnCheckedResource);
        }
        mTextView.setVisibility(textVisible ? VISIBLE : GONE);
        if (mImageView != null)
            mImageView.setVisibility(imageVisible ? VISIBLE : GONE);

        /*//重绘
         postInvalidate();
         if(mImageView!=null)
         mImageView.postInvalidate();
         mTextView.postInvalidate();*/

    }

    /**
     * 更改View
     *
     */
    public void changeView()
	{
        checked = !checked;
        showView();
    }

}
