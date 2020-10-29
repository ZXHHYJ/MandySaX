package com.FMJJ.MandySa.ui.music;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import com.FMJJ.MandySa.R;
import com.FMJJ.MandySa.ui.BaseActivity;

public class MusicActivity extends BaseActivity
{

    private float Y;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        final ViewGroup view= (ViewGroup) getWindow().getDecorView().findViewById(Window.ID_ANDROID_CONTENT).getParent();  
        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN: {
                    Y = event.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE: { 
                    if (view.getAnimation() == null)
                        if (event.getY() - Y >= 0)view.setTranslationY(event.getY() - Y); 
                }
                break;
            case MotionEvent.ACTION_UP :{

                    if (event.getY() - Y > view.getBottom() / 4)
                    {
                        //关闭
                        TranslateAnimation out = new TranslateAnimation(0, 0,  view.getY() - view.getTranslationY(), -(view.getTranslationY() - view.getHeight()));
                        out.setDuration((int)(Y / event.getY() * 250));
                        out.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation)
                                {
                                }
                                @Override
                                public void onAnimationRepeat(Animation animation)
                                {
                                }
                                @Override
                                public void onAnimationEnd(Animation animation)
                                {
                                    finish();
                                }
                            });     
                        view.startAnimation(out);
                    }
                    else
                    {
                        //还原
                        TranslateAnimation in = new TranslateAnimation(0, 0, 0, -view.getY());
                        in.setDuration((int)(Y / event.getY() * 250));
                        in.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation)
                                {
                                }
                                @Override
                                public void onAnimationRepeat(Animation animation)
                                {
                                }
                                @Override
                                public void onAnimationEnd(Animation animation)
                                {
                                    view.clearAnimation();
                                    view.setTranslationY(0);
                                }
                            });     
                        view.startAnimation(in);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
   		super.onCreate(savedInstanceState);
		setContentView(R.layout.music);
	}

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.bottom_out);
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(0, 0);
    }

    public static void startActivity(Activity context)
    {
        context.startActivity(new Intent(context, MusicActivity.class));
        context.overridePendingTransition(R.anim.bottom_in, 0);
    }

}
