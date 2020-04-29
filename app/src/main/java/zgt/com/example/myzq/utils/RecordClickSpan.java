package zgt.com.example.myzq.utils;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class RecordClickSpan extends ClickableSpan {
    @Override
    public void onClick(View widget) {

    }

    @Override
    public void updateDrawState(TextPaint ds) {
//        ds.setColor(App.getApplication().getResources().getColor(R.color.c0066CC));
        ds.setUnderlineText(false);
    }
}
