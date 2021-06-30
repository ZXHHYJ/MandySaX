package studio.mandysa.music.ui.base;

import android.os.Bundle;

import com.yanzhenjie.sofia.Sofia;

import mandysax.fragment.FragmentActivity;
import mandysax.lifecycle.ViewModel;
import mandysax.lifecycle.ViewModelProviders;
import studio.mandysa.music.ui.viewmodel.ShareViewModel;

public abstract class BaseActivity extends FragmentActivity {
    private ShareViewModel mViewModel;

    public final <T extends ViewModel> T getViewModel(Class<T> viewmodel) {
        return ViewModelProviders.of(this).get(viewmodel);
    }

    public final ShareViewModel getShareViewModel() {
        return mViewModel == null ?
                mViewModel = ViewModelProviders.of(this, new ViewModelProviders.Factory() {
                    @Override
                    public <T extends ViewModel> T create(Class<T> modelClass) {
                        return (T) new ShareViewModel(BaseActivity.this);
                    }
                }).get(ShareViewModel.class) : mViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Sofia.with(this).invasionStatusBar().invasionNavigationBar();
    }

}
