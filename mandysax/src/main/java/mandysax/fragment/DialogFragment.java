package mandysax.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleObserver;

/**
 * @author ZXHHYJ
 */
public class DialogFragment extends Fragment implements DialogInterface.OnDismissListener, DialogInterface.OnCancelListener, LifecycleObserver {

    private DialogInterface.OnCancelListener mCancelListener;

    private DialogInterface.OnDismissListener mDismissListener;

    private Dialog mDialog;

    @StyleRes
    public int getTheme() {
        return 0;
    }

    @Override
    protected void onDestroyView() {
        super.onDestroyView();
        requireActivity().getLifecycle().removeObserver(this);
        dismissDialog();
    }

    @Override
    protected void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        requireActivity().getLifecycle().addObserver(this);
        mDialog = onCreateDialog(bundle);
        mDialog.setOnCancelListener(this);
        mDialog.setOnDismissListener(this);
        mDialog.show();
    }

    /**
     * 构建待显示的Dialog
     *
     * @param savedInstanceState SavedInstanceState
     * @return 你的Dialog
     */
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getContext(), getTheme());
    }

    /**
     * 获取你的Dialog
     *
     * @return 你的Dialog
     */
    @Nullable
    public Dialog getDialog() {
        return mDialog;
    }

    /**
     * 显示DialogFragment
     *
     * @param manager FragmentManage
     */
    public void show(@NonNull FragmentManager manager) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(0, this);
        ft.commit();
    }

    /**
     * 显示DialogFragment
     *
     * @param transaction Fragment事务
     * @return 事务id
     */
    public int show(@NonNull FragmentTransaction transaction) {
        transaction.add(0, this);
        return transaction.commit();
    }

    private void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public void dismiss() {
        dismissDialog();
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        if (mDialog != null) {
            mDialog.setCanceledOnTouchOutside(cancel);
        }
    }

    public void setCancelable(boolean cancel) {
        if (mDialog != null) {
            mDialog.setCancelable(cancel);
        }
    }

    @Override
    public <T extends View> T findViewById(int i) {
        if (mDialog != null) {
            return mDialog.findViewById(i);
        }
        return null;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (mCancelListener != null) {
            mCancelListener.onCancel(dialog);
        }
    }

    @Override
    public final void onDismiss(DialogInterface dialog) {
        if (mDismissListener != null && dialog != null) {
            mDismissListener.onDismiss(dialog);
        }
        getChildFragmentManager().beginTransaction().remove(this).commit();
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
        mCancelListener = listener;

    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mDismissListener = listener;
    }

    @Override
    public boolean isVisible() {
        return mDialog != null && mDialog.isShowing();
    }

    @Override
    public void observer(Lifecycle.Event state) {
        if (Lifecycle.Event.ON_DESTROY == state) {
            if (mDialog != null) {
                mDialog.setOnDismissListener(null);
            }
            dismissDialog();
        }
    }
}
