package zgt.com.example.myzq.model.common.custom_view;

import android.content.Context;
import android.view.View;

/**
 * Created by ThinkPad on 2019/7/19.
 */

 public abstract class NoticeAdapter{
    public abstract int getCount();
    public abstract View getView(Context context, int position);
}
