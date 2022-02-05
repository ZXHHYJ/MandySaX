package mandysax.fragment;

import mandysax.lifecycle.ViewModel;

/**
 * @author liuxiaoliu66
 */
public final class FragmentManagerViewModel extends ViewModel {

    final FragmentController mController = new FragmentController();

    @Override
    protected void onCleared() {
        super.onCleared();
        mController.clear();
    }

}
