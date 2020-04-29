package zgt.com.example.myzq.model.common.home;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;

public class NoticeDetialActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;

    String url;
    @Override
    public int getLayoutId() {
        return R.layout.activity_notice_detial;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        url=getIntent().getStringExtra("url");
//        WebSettings webSettings = webView.getSettings();
//        //设置WebView属性，能够执行Javascript脚本
//        webSettings.setJavaScriptEnabled(true);
//        //设置可以访问文件
//        webSettings.setAllowFileAccess(true);
//        //设置支持缩放
//        webSettings.setBuiltInZoomControls(true);

        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        webView.loadUrl(url);

        //字体不随系统变化
        webView.getSettings().setTextZoom(100);
        //设置Web视图
        webView.setWebViewClient(new webViewClient());

    }

    @Override
    public void initToolBar() {

    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
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

    @OnClick({R.id.Iv_customer})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_customer:
                onBackPressed();
                break;
        }
    }

}
