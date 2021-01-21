package mandysax.plus.fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public interface FragmentImpl
{
    public <T extends View> T findViewById(int id)
    
    public Intent getIntent()
    
    public Context getContext()
    
    public FragmentActivity getActivity()
    
    public void startActivity(Intent intent)
    
    public void startActivity(Intent intent, Bundle options)
    
    public boolean isAdded()

    public boolean isDetached()
  
    public boolean isRemoving()
  
    public boolean isInLayout()

    public boolean isResumed()

    public boolean isVisible()

    public boolean isHidden()
    
    public void setRetainInstance(boolean retain)

    public boolean getRetainInstance()
    
    public View getView()
}
