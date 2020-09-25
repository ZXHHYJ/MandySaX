package com.FMJJ.MandySa.ui;
import android.os.Bundle;
import com.FMJJ.MandySa.R;
import mandysax.core.annotation.BindLayoutId;
import mandysax.lifecycle.FragmentCompat;

@BindLayoutId(R.layout.live_fragment)
public class LiveFragment extends FragmentCompat
{

	/*@BindView(R.id.recommendfragmentButton1)
	private Button button;*/
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		/*button.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					new recommendMusic().enqueue(
				}
				
			
		});*/
	}
	
}
