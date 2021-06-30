package studio.mandysa.music.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import studio.mandysa.music.R;

public class TextViewUtils {

    public static void markByColor(TextView tv, String keyword) {
        SpannableString s = new SpannableString(tv.getText());
        Matcher m = Pattern.compile(keyword).matcher(s);
        while (m.find()) {
            s.setSpan(new ForegroundColorSpan(tv.getContext().getColor(R.color.searchUiColor)), m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setText(s);
    }
}
