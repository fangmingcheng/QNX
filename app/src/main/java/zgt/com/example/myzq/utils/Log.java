package zgt.com.example.myzq.utils;

/**
 * Created by hasee on 2017/9/22.
 */

public class Log{
    static boolean isDebug = true;

    public static void d(String msg) {
        if (isDebug) {
            android.util.Log.d("fhkj", msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            android.util.Log.e("fhkj", msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            android.util.Log.e(tag, msg);
        }
    }
}
