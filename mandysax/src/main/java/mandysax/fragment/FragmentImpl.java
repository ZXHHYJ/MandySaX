package mandysax.fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public interface FragmentImpl
{
    <T extends View> T findViewById(int id);
    
    Context getContext();
	
    String getTag();
    
    FragmentActivity getActivity();
    
    FragmentController2Impl getFragmentPlusManager();
    
    void startActivity(Intent intent);
    
    void startActivity(Intent intent, Bundle options);
    
    boolean isAdded();

    boolean isDetached();
  
    boolean isRemoving();
  
    boolean isInLayout();

    boolean isResumed();

    boolean isVisible();

    boolean isHidden();
    
    View getRoot();
	
    boolean onBackPressed();
}
