package studio.mandysa.music.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mandysax.lifecycle.livedata.Observer;
import studio.mandysa.music.R;
import studio.mandysa.music.ui.base.BaseFragment;
import studio.mandysa.music.ui.viewmodel.LoginViewModel;

public class LoginFragment extends BaseFragment implements Observer<String> {

    @Override
    public void onChanged(String p1) {
        if (p1 == null)
            return;
        getFragmentPlusManager().remove(this).add(R.id.fl_me_show, new UserFragment()).commit();
    }

    private LoginViewModel mViewModel;

    private EditText mMobilePhone, mPassword;

    @Override
    protected void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = getViewModel(LoginViewModel.class);
        mViewModel.getCookieLiveData().observeForever(this);

        mMobilePhone = findViewById(R.id.edtTxt_login_mobilePhone);
        mPassword = findViewById(R.id.edtTxt_login_password);
        Button mLoginButton = findViewById(R.id.btn_login_loginButton);
        mLoginButton.setOnClickListener(p1 -> mViewModel.login(mMobilePhone.getText().toString(), mPassword.getText().toString()));
    }

    @Override
    protected int onCreateView() {
        return R.layout.login;
    }

}
