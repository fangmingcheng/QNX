package zgt.com.example.myzq.utils;


import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 */
public class ToastUtil {
    /**
     * 短吐司
     */
    private static Toast toa;
    public static void showShortToast(Context context, CharSequence text) {
        if (toa == null) {
            toa = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toa.setText(text);
        }
        toa.show();
    }
}
