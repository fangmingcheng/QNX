package zgt.com.example.myzq.model.common.home;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;

public class VideoWebViewActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;
//    @BindView(R.id.Rl_progress)
//    RelativeLayout Rl_progress;

    @BindView(R.id.Tv_title)
    TextView Tv_title;

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_web_view;
    }


    @Override
    public void initViews(Bundle savedInstanceState) {
        WebSettings webSettings = webView.getSettings();

        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);

        //设置页面自适应手机
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //加载需要显示的网页
        webView.loadUrl("https://api.zgziben.com/zhibiao/youxuan.html");
        //字体不随系统变化
        webView.getSettings().setTextZoom(100);
        //设置Web视图
        webView.setWebViewClient(new webViewClient());
//        WebSettings webSettings = webView.getSettings();
//        //设置WebView属性，能够执行Javascript脚本
//        webSettings.setJavaScriptEnabled(true);
//
//        webSettings.setDomStorageEnabled(true);
//        //设置可以访问文件
//        webSettings.setAllowFileAccess(true);
//        //设置支持缩放
//        webSettings.setBuiltInZoomControls(true);
//
//        //解决h5网页打开白页
//        webSettings.setUseWideViewPort(true);
//        //加载需要显示的网页
//        webView.loadUrl("https://api.zgziben.com/zhibiao/youxuan.html");
//
//        //字体不随系统变化
//        webView.getSettings().setTextZoom(100);
//        //设置Web视图
//        webView.setWebViewClient(new webViewClient());



    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }



    @Override
    public void initToolBar() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (webView != null) {
            webView.stopLoading();
//            webView.setWebViewListener(null);
//            webView.clearHistory();
            webView.clearCache(true);
            webView.loadUrl("about:blank");
//            webView.pauseTimers();
//            webView = null;
        }
    }

    @OnClick({R.id.Iv_back})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
                if (webView.canGoBack()) {
                    webView.goBack();//返回上个页面
                    return;
                } else {
                    finish();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();//返回上个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
