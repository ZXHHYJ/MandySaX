package com.FMJJ.MandySa.Adapder;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.FMJJ.MandySa.Data.music_bean;
import com.FMJJ.MandySa.MainActivity;
import com.FMJJ.MandySa.R;
import com.FMJJ.MandySa.ViewHolder.musicList_ViewHolder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mandysax.Lifecycle.Paradrop.paradrop;
import com.FMJJ.MandySa.Service.MusicService;

public class music_listAdaper extends RecyclerView.Adapter<musicList_ViewHolder>
{
	
	@Override
	public void onBindViewHolder(musicList_ViewHolder p1, final int p2)
	{
		setSearchContentColor(list.get(p2).getTitle(),list.get(p2).getName(),p1.Song_name);
		setSearchContentColor(list.get(p2).getSinger().get(0).getName(),list.get(p2).getName(),p1.Singer_name);
		p1.onclick.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					((MusicService.MusicBinder)paradrop.getDrop("music_binder")).playMusic(list,p2);
                    //MainActivity.music_binder.playMusic(list,p2);
				}
			});
	}

	private final List<music_bean> list;

	private final Context context;
	
	@Override
	public int getItemViewType(int position)
	{
		return position;
	} 

	public music_listAdaper(Context p0, List<music_bean> p1)
	{ 
		this.context = p0;
		this.list = p1;
	} 

	public musicList_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{ 
		View view = LayoutInflater.from(context).inflate(R.layout.music_list, parent, false); 
		return new musicList_ViewHolder(view);
	} 

	public int getItemCount()
	{ 
		return list.size(); 
	} 

	private void setSearchContentColor(String name, String KeyWord, TextView tv)
	{
		SpannableString s = new SpannableString(name);
        Pattern p = Pattern.compile(KeyWord);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.theme_color_)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
		tv.setText(s);
	}

} 

