package zgt.com.example.myzq.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ThinkPad on 2019/4/14.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration  {
    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
//        outRect.right = space;
//        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
//        if (parent.getChildPosition(view) == 0)
//            outRect.top = space;
    }
}
