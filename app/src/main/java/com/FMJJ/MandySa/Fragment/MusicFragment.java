package com.FMJJ.MandySa.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.FMJJ.MandySa.R;
import mandysax.Lifecycle.LifecycleFragment;

public class MusicFragment extends LifecycleFragment
{

    //private final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

    private TextView title,smalltitle;

    private ImageView album_picture;
    
    @Override
    public int getLayoutId()
    {
        return R.layout.music_fragment;
    }

    @Override
    public void initView(View view)
    {
        
        title = view.findViewById(R.id.musicplaytitle);
        smalltitle = view.findViewById(R.id.musicplaysmalltitle);
        album_picture = view.findViewById(R.id.musicplayalbumpicture);
     
        /*Refresh(viewModel.music_binder.getValue().bean.getValue());
        viewModel.music_binder.getValue().bean.observe(this, new Observer<music_bean>(){
                @Override
                public void onChanged(music_bean p1)
                {
                    Refresh(p1);
                }
			});*/
    }
    
   /* private void Refresh(final music_bean p1)
    {
        final lovelyTask load = lovely.with(this);
        new Thread(new Runnable(){
                @Override
                public void run()
                {
                    try
                    {
                        load.load(p1.getImg()).into(album_picture);
                    }
                    catch (JSONException e)
                    {}
                }
            }).start();
        title.setText(p1.getName());
        smalltitle.setText(p1.getSingerList().get(0).getName());
    }*/
    
}
