package mandysax.design;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import mandysax.utils.*;

/**
 * Created by Dikaros on 2016/5/20.
 */

//mandysa修改于7.12日晚5点

public class NavigationItem extends LinearLayout
{
    public NavigationItem(Context context, int textCheckedColor, int textUnCheckedColor, int imageCheckedResource, int imageUnCheckedResource)
	{
        super(context);
        initViews(context);
        this.textCheckedColor = textCheckedColor;
        this.textUnCheckedColor = textUnCheckedColor;
        this.imageUnCheckedResource = imageUnCheckedResource;
        this.imageCheckedResource = imageCheckedResource;
        addView(mImageView);
        addView(mTextView);
    }

    public void setChecked(boolean checked)
	{
        this.checked = checked;
    }

    boolean textVisible = true;
    boolean imageVisible = true;


    public void setTextSize(int textSize)
	{
        this.textSize = textSize;
    }

    public void setTextVisible(boolean textVisible)
	{
        this.textVisible = textVisible;
        mTextView.setVisibility(textVisible ? VISIBLE : GONE);
        if (!textVisible)
		{
            Log.e("textView", "消失");
        }
    }

    public void setImageVisible(boolean imageVisible)
	{
        this.imageVisible = imageVisible;
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

    public ImageView getmImageView()
	{
        return mImageView;
    }

    private void initViews(final Context context)
	{

        setOrientation(VERTICAL);

        //初始化view
        mTextView = new TextView(context);
        mImageView = new ImageView(context);
        //设置veiw是否可见
        mTextView.setVisibility(textVisible ? VISIBLE : GONE);
        mImageView.setVisibility(imageVisible ? VISIBLE : GONE);
        //设置默认文字
        mTextView.setText("text");
        //设置默认文字大小
        mTextView.setTextSize(textSize);
        //设置内边距
        setPadding(0, DensityUtils.dip2px(context, imagePaddingTop), 0, DensityUtils.dip2px(context, textPaddingBottom));
        //设置自身布局
        setGravity(Gravity.CENTER);
        //设置text和image的布局参数
        LayoutParams textParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutParams imageParams = new LayoutParams(DensityUtils.dip2px(context, imageWidth), DensityUtils.dip2px(context, imageHeight));
        mTextView.setLayoutParams(textParams);
        mImageView.setLayoutParams(imageParams);

        //设置最小宽度
        setMinimumWidth(DensityUtils.dip2px(context, 80));
        //设置本身的布局
        LinearLayout.LayoutParams selfParam = new LayoutParams(DensityUtils.dip2px(context, 0), DensityUtils.dip2px(context, 56), 1f);
        setLayoutParams(selfParam);

        //设置文字颜色
        mTextView.setTextColor(textUnCheckedColor);
        //设置默认图片
        mImageView.setImageResource(imageUnCheckedResource);


    }

    boolean checked = false;

    public boolean isChecked()
	{
        return checked;
    }

    //文字
	private TextView mTextView;
    //图片
	private ImageView mImageView;
    //文字大小
	private int textSize=12;

	private final int imageWidth = 24;
	private final int imageHeight=24;
	private final int imagePaddingTop=8;
	private final int textPaddingBottom=10;

    int textCheckedColor;
    int textUnCheckedColor;

    int imageCheckedResource;
    int imageUnCheckedResource;

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
			//   initCheckValue();
            mTextView.setTextColor(textCheckedColor);
            mImageView.setImageResource(imageCheckedResource);
        }
		else
		{
			// initDefaultValue();
            mTextView.setTextColor(textUnCheckedColor);
            mImageView.setImageResource(imageUnCheckedResource);
        }
        mTextView.setVisibility(textVisible ? VISIBLE : GONE);
        mImageView.setVisibility(imageVisible ? VISIBLE : GONE);
        mTextView.setTextSize(textSize);
		// setPadding(DensityUtil.dip2px(getContext(), 12), DensityUtil.dip2px(getContext(), imagePaddingTop), DensityUtil.dip2px(getContext(), 12), DensityUtil.dip2px(getContext(), textPaddingBottom));


        //重绘
        postInvalidate();
        mImageView.postInvalidate();
        mTextView.postInvalidate();
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
