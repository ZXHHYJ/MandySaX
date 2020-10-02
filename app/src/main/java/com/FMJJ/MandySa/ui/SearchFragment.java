package com.FMJJ.MandySa.ui;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.FMJJ.MandySa.R;
import com.FMJJ.MandySa.model.SearchViewModel;
import mandysax.core.annotation.BindLayoutId;
import mandysax.core.annotation.BindView;
import mandysax.lifecycle.FragmentCompat;
import mandysax.lifecycle.ViewModelProviders;

@BindLayoutId(R.layout.search_fragment)
public class SearchFragment extends FragmentCompat
{
	
	private SearchViewModel viewModel;
	
	@BindView(R.id.searchbarEditText1)
	private EditText search_edit;

	@BindView(R.id.searchbarImageView1)
	private ImageView search;
	
	@Override
	public void onStart()
	{
		search_edit.setText(viewModel.getSearchName());
		super.onStart();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		viewModel=ViewModelProviders.of(this).get(SearchViewModel.class);
		search_edit.setOnKeyListener(new View.OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event)
				{
					if (keyCode == KeyEvent.KEYCODE_ENTER)
					{	
						viewModel.song_search(search_edit.getText().toString());
						getActivity().onBackPressed();
					}
					return false;
				}
            });
		search.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					viewModel.song_search(search_edit.getText().toString());
					getActivity().onBackPressed();
				}
				
		});
	}
	
}
