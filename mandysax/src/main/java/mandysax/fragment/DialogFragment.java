package mandysax.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

public class DialogFragment extends Fragment {

    private Dialog mDialog;

    @StyleRes
    public int getTheme() {
        return 0;
    }

    @Override
    protected void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mDialog = onCreateDialog(bundle);
        mDialog.setOnCancelListener(dialog -> dismiss());
        mDialog.show();
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
        getFragmentPlusManager().beginTransaction().remove(this).commit();
    }

    @Override
    public <T extends View> T findViewById(int i) {
        if (mDialog != null) {
            return mDialog.findViewById(i);
        }
        return null;
    }

}
