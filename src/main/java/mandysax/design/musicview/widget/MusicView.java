package mandysax.design.musicview.widget;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import mandysax.R;
import mandysax.utils.DensityUtils;

public class MusicView extends LinearLayout
{
	public enum PlayStack
	{
		PLAY,
		STOP,
	}

    private ImageView mCover,mController,mNextTrack;

    private TextView mTitle,mSubTitle;

	private boolean mPlay=false;

    public MusicView(Context context)
    {
        super(context);
        init(null);
    }

    public MusicView(Context context, AttributeSet attrs)
    {
        super(context, attrs); 
        init(attrs);
    }

    private void init(final AttributeSet attrs)
    {
		setOrientation(LinearLayout.HORIZONTAL);
		post(new Runnable(){

				@Override
				public void run()
				{
					mCover = new ImageView(getContext());
					mCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
					mCover.setLayoutParams(new LinearLayout.LayoutParams(getHeight(), getHeight()));
					addView(mCover);
					mTitle = new TextView(getContext());
					mTitle.setTextColor(android.R.color.black);
					mTitle.setTextAppearance(android.R.style.TextAppearance_Large);
					mTitle.setTextSize(16);

					mSubTitle = new TextView(getContext());
					mSubTitle.setTextColor(android.R.color.black);
					mSubTitle.setTextAppearance(android.R.style.TextAppearance_Small);
					mSubTitle.setTextSize(13);

					LinearLayout layout= new LinearLayout(getContext());
					layout.setOrientation(LinearLayout.VERTICAL);
					layout.setGravity(Gravity.CENTER);
					layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1));
					layout.addView(mTitle);
					layout.addView(mSubTitle);
					addView(layout);

					mController = new ImageView(getContext());
					mController.setLayoutParams(new LinearLayout.LayoutParams(getHeight(), getHeight()));
					mController.setImageDrawable(getContext().getDrawable(R.mipmap.ic_play));
					mController.setColorFilter(getContext().getColor(android.R.color.black));
					int dp10=DensityUtils.dp2px(getContext(), 10);
					mController.setPadding(dp10, dp10, dp10, dp10);		
					mController.setOnClickListener(new View.OnClickListener(){

							@Override
							public void onClick(View p1)
							{
								setPlayStack(mPlay ?PlayStack.STOP: PlayStack.PLAY);
								mPlay = !mPlay;
							}
						});
					addView(mController);

					mNextTrack = new ImageView(getContext());
					mNextTrack.setLayoutParams(new LinearLayout.LayoutParams(getHeight(), getHeight()));
					mNextTrack.setImageDrawable(getContext().getDrawable(R.mipmap.ic_skip_next));
					mNextTrack.setColorFilter(getContext().getColor(android.R.color.black));
					mNextTrack.setPadding(dp10, dp10, dp10, dp10);		
					addView(mNextTrack);

					if (attrs != null)
					{
						/*
						 *Setting MusicView stack
						 */
						TypedArray attr=getContext().obtainStyledAttributes(attrs, R.styleable.MusicView);
						mTitle.setText(attr.getString(R.styleable.MusicView_title));
						mSubTitle.setText(attr.getString(R.styleable.MusicView_subTitle));
						mCover.setImageDrawable(getContext().getDrawable(attr.getResourceId(R.styleable.MusicView_coverImage, R.drawable.bg_album_default)));
						/*
						 *Recycle attr
						 */
						attr.recycle();
					}

				}
			});
    }

	public ImageView getPlaybutton()
	{
		return mController;
	}

	public ImageView getNextTrackButton()
	{
		return mNextTrack;
	}

	public MusicView setPlayStack(PlayStack stack)
	{
		switch (stack)
		{
			case PLAY:
				mController.setImageResource(R.mipmap.ic_pause);
				break;
			case STOP:
				mController.setImageResource(R.mipmap.ic_play);
				break;
		}
		return this;
	}

	public MusicView setTitle(String title)
	{
		mTitle.setText(title);
		return this;
	}

	public MusicView setSubTitle(String title)
	{
		mSubTitle.setText(title);
		return this;
	}

	public ImageView getCoverView()
	{
		return mCover;
	}
}
