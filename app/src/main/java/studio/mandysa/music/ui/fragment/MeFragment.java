package studio.mandysa.music.ui.fragment;

import android.os.Bundle;
import android.view.View;

import mandysax.navigation.widget.Navigation;
import studio.mandysa.music.R;
import studio.mandysa.music.ui.base.BaseFragment;
import studio.mandysa.music.ui.viewmodel.LoginViewModel;

public class MeFragment extends BaseFragment {

    @Override
    protected void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginViewModel mViewModel = getViewModel(LoginViewModel.class);
        Navigation.startFragment(this, R.id.nav_me_view, mViewModel.getCookie() == null ? new LoginFragment() : new UserFragment());
    }

    @Override
    protected int onCreateView() {
        return R.layout.me;
    }

}
