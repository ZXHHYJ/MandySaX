package mandysax.lifecycle;

import android.app.Application;

import androidx.annotation.NonNull;

public final class AndroidViewModel extends ViewModel {

    private final Application mApplication;

    public AndroidViewModel(Application application) {
        mApplication = application;
    }

    public Application getApplication() {
        return mApplication;
    }

    /**
     * AndroidViewModel default create factory
     */
    public final static class AndroidViewModelFactory implements ViewModelProviders.Factory {
        private final Application mApplication;

        public AndroidViewModelFactory(Application application) {
            mApplication = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new AndroidViewModel(mApplication);
        }
    }
}
