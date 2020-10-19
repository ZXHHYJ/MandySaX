package com.FMJJ.MandySa.ui;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.FMJJ.MandySa.R;
import mandysax.lifecycle.Fragment;

public class LiveFragment extends Fragment
{

	private Button button;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container)
	{
		return inflater.inflate(R.layout.live_fragment,container,false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view,savedInstanceState);
		button=view.findViewById(R.id.recommendfragmentButton1);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		/*button.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					startFragment(SettingFragment.class);
				}	

			});*/
	}

}
