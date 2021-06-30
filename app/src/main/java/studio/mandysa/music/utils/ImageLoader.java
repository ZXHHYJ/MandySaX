package studio.mandysa.music.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageLoader {

    private ImageLoader() {
    }

    String url;

    ImageView imageView;

    public ImageLoader load(String url) {
        this.url = url;
        return this;
    }

    public void into(ImageView imageView) {
        this.imageView = imageView;
        into();
    }

    void into() {
        if (imageView.getTag() != null)
            if (imageView.getTag().equals(url))
                return;
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(url, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                imageView.setTag(url);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                imageView.setTag(null);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                alphaAnimation.setDuration(150);
                imageView.startAnimation(alphaAnimation);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    public static ImageLoader getInstance() {
        return new ImageLoader();
    }

}
