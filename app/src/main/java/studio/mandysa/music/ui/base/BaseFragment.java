package studio.mandysa.music.ui.base;

import mandysax.fragment.Fragment;
import mandysax.lifecycle.ViewModel;
import studio.mandysa.music.ui.viewmodel.ShareViewModel;

public class BaseFragment extends Fragment {

    /**
     * @param viewmodel you viewmodel.class
     * @param <T> extends ViewModel
     * @return you viewmodel
     */
    public final <T extends ViewModel> T getViewModel(Class<T> viewmodel) {
        return getActivity().getViewModel(viewmodel);
    }

    /**
     * @return ShareViewModel
     */
    public final ShareViewModel getShareViewModel() {
        return getActivity().getShareViewModel();
    }

    /**
     * @return BaseActivity
     */
    @Override
    public BaseActivity getActivity() {
        return (BaseActivity) super.getActivity();
    }

}
