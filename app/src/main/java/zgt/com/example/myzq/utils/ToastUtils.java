package zgt.com.example.myzq.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ToastUtils {
    private static Context mContext;
    private static ImageView mIv;
    private static Toast mToast;

    /**
     * 只显示文字吐司
     *
     * @param mContext
     */
    public static void setContext(Context mContext) {
        ToastUtils.mContext = mContext;
    }

    public static void show(String content) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, content, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
        }

        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    /**
     * 显示带图片文字的吐司
     *
     * @param text
     * @param resImag
     */
    public static void show(String text, int resImag) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }

        LinearLayout view = (LinearLayout) mToast.getView();
        if (mIv == null) {
            mIv = new ImageView(mContext);
        }
        mIv.setImageResource(resImag);
        if (view.getChildCount() != 1) {
            view.removeViewAt(1);
        }
        view.addView(mIv);
        mToast.show();
    }
}
