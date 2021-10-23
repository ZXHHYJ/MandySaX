package studio.mandysa.music.ui.home;

import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.litho.ClickEvent;
import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.Row;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.Param;
import com.facebook.litho.fresco.FrescoImage;
import com.facebook.litho.widget.Card;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaEdge;
import com.facebook.yoga.YogaJustify;

@LayoutSpec
class NewSongSpec {

    @OnCreateLayout
    static Component onCreateLayout(ComponentContext c
    ) {
        return Column.create(c)
                .justifyContent(YogaJustify.CENTER)
                .paddingDip(YogaEdge.HORIZONTAL, 10f)
                .heightDip(75f)
                .clickHandler(NewSong.onFaceClicked(c, "aa"))
                .child(
                        Row.create(c)
                                .alignSelf(YogaAlign.CENTER)
                                .alignSelf(YogaAlign.FLEX_START)
                                .child(
                                        Card.create(c)
                                                .cornerRadiusDip(30f)
                                                .heightDip(60f)
                                                .widthDip(60f)
                                                .elevationDip(0f)
                                                .content(
                                                        FrescoImage.create(c).controller(Fresco.newDraweeControllerBuilder()
                                                                .setUri("https://bkimg.cdn.bcebos.com/pic/b64543a98226cffc1e17a4e200495d90f603738dbc01?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UyNzI=,g_7,xp_5,yp_5/format,f_auto")
                                                                .build()
                                                        )
                                                )
                                )
                                .child(
                                        Column.create(c)
                                                .justifyContent(YogaJustify.CENTER)
                                                .paddingDip(YogaEdge.LEFT, 10f)
                                                .child(
                                                        Text.create(c)
                                                                .text("Hello, World!")
                                                                .textSizeSp(17f)
                                                ).child(
                                                Text.create(c)
                                                        .marginDip(YogaEdge.TOP, 3f)
                                                        .text("Hello, World!")
                                                        .textSizeSp(15f)
                                        )
                                )
                ) //.clippingColorRes(R.color.my_clipping_color)
                .build();
    }

    //点击事件的回调
    @OnEvent(ClickEvent.class)
    public static void onFaceClicked(
            ComponentContext c,
            @Param String face) {    //@Param String face上面传递过来的数据  @Param注解很重要哦
        Log.d("FacePileComponent", "Face clicked: " + face);
    }

}