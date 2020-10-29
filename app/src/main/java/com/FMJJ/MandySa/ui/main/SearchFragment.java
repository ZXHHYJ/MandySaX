package com.FMJJ.MandySa.ui.main;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import com.FMJJ.MandySa.R;
import mandysax.lifecycle.Fragment;

public class SearchFragment extends Fragment
{

	private EditText searchEdit;

	private ImageView searchButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container)
	{
		return inflater.inflate(R.layout.search_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		searchButton = findViewById(R.id.searchbarImageView1);
        searchEdit = findViewById(R.id.searchbarEditText1);
        showKeyboard();
		searchEdit.setOnKeyListener(new View.OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event)
				{
					if (keyCode == KeyEvent.KEYCODE_ENTER)
					{	
						closeKeyboard();
						getIntent().putExtra("search_content", searchEdit.getText());	
						getActivity().onBackPressed();
					}
					return false;
				}
            });
		searchButton.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					closeKeyboard();
					getIntent().putExtra("search_content", searchEdit.getText());	
				}

			});
	}

	private void showKeyboard()
	{
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null)
		{
			searchEdit.requestFocus();
			imm.showSoftInput(searchEdit, 0);
		}
    }

	private void closeKeyboard()
	{
        View view = getActivity().getWindow().peekDecorView();
        if (view != null)
		{
            InputMethodManager inputMethodManager = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
