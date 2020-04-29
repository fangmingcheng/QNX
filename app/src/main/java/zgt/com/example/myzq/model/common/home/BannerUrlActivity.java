package zgt.com.example.myzq.model.common.home;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.utils.StatusBarUtil;

public class BannerUrlActivity extends BaseActivity{
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.Rl_progress)
    RelativeLayout Rl_progress;

    @BindView(R.id.Tv_title)
    TextView Tv_title;

    private String url;
    private int status;

    @Override
    public void initViews(Bundle savedInstanceState) {
//        mDialog.show();//显示


        StatusBarUtil.statusBarLightMode(this);
        url=getIntent().getStringExtra("url");

//        status = getIntent().getIntExtra("status",0);
//
//        if(status == 1){
//            Tv_title.setText("");
//        }else {
//            Tv_title.setText("指标优选");
//        }
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);

        webSettings.setDomStorageEnabled(true);
        //设置页面自适应手机

        //解决h5网页打开白页
        webSettings.setUseWideViewPort(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
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
//        webView.setWebViewClient(new webViewClient());
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
//                    pro.setVisibility(View.GONE);
                if(Rl_progress !=null) {
                    Rl_progress.setVisibility(View.GONE);
                }
                super.onPageFinished(view, url);

            }
        });

//        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    // 网页加载完成  
//                    Rl_progress.setVisibility(View.GONE);
//                    webView.getSettings().setBlockNetworkImage(false);
//                } else {
//                    // 网页加载中  
//
//                }
//            }
//        });
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_banner_url;
    }

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
