package zgt.com.example.myzq.model.common.custom_view;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.stx.xhb.xbanner.XBanner;

import zgt.com.example.myzq.R;

public class MyNestedScrollParent extends LinearLayout implements NestedScrollingParent {

    private NestedScrollingParentHelper mParentHelper;
    private int mImgHeight;

    public MyNestedScrollParent(Context context, AttributeSet attrs) {
        super(context, attrs);
        mParentHelper = new NestedScrollingParentHelper(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final XBanner imageView = (XBanner) findViewById(R.id.banner);
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mImgHeight <= 0) {
                    mImgHeight = imageView.getMeasuredHeight();
                }
                imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        boolean isImageVisiable = (dy > 0 && getScrollY() < mImgHeight) || (dy < 0 && target.getScrollY() <= 0);
        if (isImageVisiable) {
            consumed[1] = dy;
            scrollBy(0, dy);
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y > mImgHeight) {
            y = mImgHeight;
        }
        if (y < 0) {
            y = 0;
        }
        super.scrollTo(x, y);
    }

}
