package mandysax.lifecycle;

import android.app.Application;

public class AndroidViewModel extends ViewModel {

    private final Application mApplication;

    public AndroidViewModel(Application application) {
        mApplication = application;
    }

    public <T extends Application> T getApplication() {
        return (T) mApplication;
    }

    /**
     * AndroidViewModel default create factory
     */
    public final static class AndroidViewModelFactory implements ViewModelProviders.Factory {
        private final Application mApplication;

        public AndroidViewModelFactory(Application application) {
            mApplication = application;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new AndroidViewModel(mApplication);
        }
    }
}
