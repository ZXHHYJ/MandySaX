package mandysax.lifecycle;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface FragmentImpl
{

	//public void setFragmentPage(FragmentPage page);

	public Fragment startFragment(Class fragment);

	public void onFragmentResult(Intent data);

	public LayoutInflater getLayoutInflater();

	public Context getContext();

	public FragmentActivity getActivity();

	public void startActivity(Intent intent);

	public void startActivity(Intent intent, Bundle options);

    public boolean isAdded();

    public boolean isDetached();

    public boolean isRemoving();

    public boolean isInLayout();

    public boolean isResumed();

    public boolean isVisible();

    public boolean isHidden();

    public void onHiddenChanged(boolean hidden);

    public void setRetainInstance(boolean retain);

    public boolean getRetainInstance();

    public void onAttach(Context context);

    public void onCreate(Bundle activitySavedInstanceState);

    public View onCreateView(LayoutInflater inflater, ViewGroup container);
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle activitySavedInstanceState);

    public void onViewCreated(View view, Bundle activitySavedInstanceState);

    public View getView();

    public void onActivityCreated(Bundle activitySavedInstanceState);

    public void onStart();

    public void onResume();

    public void onSaveInstanceState(Bundle outState);

    public void onMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig);

    public void onMultiWindowModeChanged(boolean isInMultiWindowMode);

    public void onConfigurationChanged(Configuration newConfig);

    public void onPause();

    public void onStop();

    public void onDestroyView();

    public void onDestroy();

    public void onDetach();

}
