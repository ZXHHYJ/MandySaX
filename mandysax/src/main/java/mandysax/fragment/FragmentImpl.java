package mandysax.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author liuxiaoliu66
 */
interface FragmentImpl {
    <T extends View> T findViewById(int id);

    Context getContext();

    String getTag();

    FragmentActivity getActivity();

    FragmentActivity requireActivity();

    FragmentManager getFragmentPlusManager();

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

}
