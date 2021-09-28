package mandysax.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

/**
 * @author liuxiaoliu66
 */
public class FragmentLifecycle {

    protected void onAttach(@NonNull Context context) {
    }

    protected void onCreate(Bundle bundle) {
    }

    protected View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    protected void onViewCreated(View view, Bundle savedInstanceState) {
    }

    protected void onActivityCreated(Bundle bundle) {
    }

    protected void onStart() {
    }

    protected void onRestart() {
    }

    protected void onResume() {
    }

    protected void onPause() {
    }

    protected void onStop() {
    }

    protected void onDestroyView() {
    }

    protected void onDestroy() {
    }

    protected void onDetach() {
    }

    protected void onHiddenChanged(boolean hidden) {
    }

    protected void onMultiWindowModeChanged(boolean z, Configuration configuration) {
    }

    protected void onSaveInstanceState(Bundle bundle) {
    }

    protected void onConfigurationChanged(Configuration configuration) {
    }
}
