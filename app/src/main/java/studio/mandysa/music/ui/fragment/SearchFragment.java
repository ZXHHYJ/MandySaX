package studio.mandysa.music.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import mandysax.navigation.widget.Navigation;
import studio.mandysa.music.R;
import studio.mandysa.music.ui.base.BaseFragment;


public class SearchFragment extends BaseFragment {

    private final SearchHistoryFragment mSearchHistoryFragment = new SearchHistoryFragment();

    @Override
    protected void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFragmentPlusManager().add(R.id.lv_search_show, mSearchHistoryFragment).commitNow();
        EditText mEdtTxtSearch = findViewById(R.id.edtTxt_search_view);
        mEdtTxtSearch.setFocusableInTouchMode(false);//不可编辑
        mEdtTxtSearch.setKeyListener(null);//不可粘贴，长按不会弹出粘贴框
        mEdtTxtSearch.setClickable(false);//不可点击，但是这个效果我这边没体现出来，不知道怎没用
        mEdtTxtSearch.setFocusable(false);//不可编辑
        mEdtTxtSearch.setOnClickListener(view1 -> Navigation.startFragment(SearchFragment.this, R.id.nav_search_view, new SearchListFragment()));
    }

    @Override
    protected int onCreateView() {
        return R.layout.search;
    }

}
