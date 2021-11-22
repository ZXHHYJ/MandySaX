package mandysax.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

public class DialogFragment extends Fragment implements DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {

    private DialogInterface.OnCancelListener mCancelListener;

    private DialogInterface.OnDismissListener mDismissListener;

    private Dialog mDialog;

    @StyleRes
    public int getTheme() {
        return 0;
    }

    @Override
    protected void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mDialog = onCreateDialog(bundle);
        mDialog.setOnCancelListener(this);
        mDialog.setOnDismissListener(this);
        mDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDialog.setOnDismissListener(null);
        dismissDialog();
    }

    @Override
    protected void onDestroyView() {
        super.onDestroyView();
        dismissDialog();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getContext(), getTheme());
    }

    @Nullable
    public Dialog getDialog() {
        return mDialog;
    }

    public void show(@NonNull FragmentManager manager) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(0, this);
        ft.commit();
    }

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
        onDismiss(null);
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
        if (mCancelListener != null)
            mCancelListener.onCancel(dialog);
    }

    @Override
    public final void onDismiss(DialogInterface dialog) {
        if (mDismissListener != null && dialog != null)
            mDismissListener.onDismiss(dialog);
        getFragmentPlusManager().beginTransaction().remove(this).commit();
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
        mCancelListener = listener;

    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mDismissListener = listener;
    }

}
