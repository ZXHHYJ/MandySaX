package studio.mandysa.music.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import studio.mandysa.music.databinding.ViewTitleBinding;

public final class TitleView extends FrameLayout {

    private final ViewTitleBinding mBinding;

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mBinding = ViewTitleBinding.inflate(LayoutInflater.from(context), this, true);
    }

    public void setTitle(String title) {
        mBinding.titleName.setText(title);
    }

    public void setAvatarUrl(String url) {
        //mBinding.avatar.setImageURI(url);
    }

}
