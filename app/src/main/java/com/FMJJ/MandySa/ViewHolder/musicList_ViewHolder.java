package com.FMJJ.MandySa.ViewHolder;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.FMJJ.MandySa.R;

public class musicList_ViewHolder extends RecyclerView.ViewHolder
{ 

	public TextView Song_name,Singer_name;
	public View onclick;

	public musicList_ViewHolder(View view)
	{ 
		super(view); 
		Song_name = view.findViewById(R.id.musiclistTextView1);
		Singer_name = view.findViewById(R.id.musiclistTextView2);
		onclick = view.findViewById(R.id.musiclistLinearLayout1);
	} 
}
