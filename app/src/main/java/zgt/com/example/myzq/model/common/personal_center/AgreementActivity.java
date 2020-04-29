package zgt.com.example.myzq.model.common.personal_center;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;

public class AgreementActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;
    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        StatusBarUtil.statusBarLightMode(this);
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
        webView.loadUrl("http://www.zgziben.com/index.php/Index/user_agreement");

        //字体不随系统变化
        webView.getSettings().setTextZoom(100);
        //设置Web视图
        webView.setWebViewClient(new webViewClient());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_agreement;
    }


    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @OnClick({R.id.Iv_customer})
    void onClick(View view) {
        switch (view.getId()){

            case R.id.Iv_customer:
                finish();
                break;
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



}
