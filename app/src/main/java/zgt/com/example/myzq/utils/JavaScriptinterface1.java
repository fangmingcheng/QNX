package zgt.com.example.myzq.utils;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class JavaScriptinterface1 {
    Context context;
    public JavaScriptinterface1(Context c) {
        context= c;
    }

    /**
     * 与js交互时用到的方法，在js里直接调用的
     */
    @JavascriptInterface
    public void quit() {
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }
}
