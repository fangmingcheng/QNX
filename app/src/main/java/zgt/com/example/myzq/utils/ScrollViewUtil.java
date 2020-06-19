package zgt.com.example.myzq.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ScrollViewUtil extends ScrollView {

    private OnScrollistener onScrollistener;
    public OnScrollistener getOnScrollistener() {
        return onScrollistener;
    }
    public void setOnScrollistener(OnScrollistener onScrollistener) {
        this.onScrollistener = onScrollistener;
    }
    public ScrollViewUtil(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollViewUtil(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewUtil(Context context) {
        super(context);
    }

    public interface OnScrollistener{
        void onScrollChanged(ScrollViewUtil scrollView, int x, int y,
                             int oldx, int oldy);
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (onScrollistener != null) {
            onScrollistener.onScrollChanged(this, l, t, oldl, oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }



}
