package studio.mandysa.music.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import mandysax.navigation.widget.Navigation;
import mandysax.navigation.widget.NavigationPage;
import studio.mandysa.music.R;
import studio.mandysa.music.ui.base.BaseFragment;
import studio.mandysa.music.ui.view.bottomnavigationbar.BottomNavigationBar;
import studio.mandysa.music.ui.viewmodel.SearchViewModel;


public class SearchListFragment extends BaseFragment {

    private EditText mEdtTxtSearch;

    @Override
    protected void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SearchViewModel mViewModel = getViewModel(SearchViewModel.class);
        NavigationPage navigationPage = findViewById(R.id.navPage_searchList_view);
        BottomNavigationBar bottomNavigationBar = findViewById(R.id.bnb_searchList_view);
        bottomNavigationBar
                .addItem(getContext().getString(R.string.music))
                .addItem(getContext().getString(R.string.singer));
        bottomNavigationBar.setOnItemViewSelectedListener(navigationPage::showNavigation);
        mEdtTxtSearch = findViewById(R.id.edtTxt_searchList_view);
        mEdtTxtSearch.setOnEditorActionListener((p1, p2, p3) -> {
            if (p2 == EditorInfo.IME_ACTION_SEARCH) {
                closeKeyboard();
                mViewModel.searchFor(mEdtTxtSearch.getText().toString());
            }
            return false;
        });
        getRoot().setOnClickListener(p1 -> closeKeyboard());
    }

    @Override
    protected int onCreateView() {
        return R.layout.search_list;
    }

    private void closeKeyboard() {
        if (mEdtTxtSearch.hasFocus()) {
            mEdtTxtSearch.clearFocus();
            View view = getActivity().getWindow().peekDecorView();
            if (view != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
