package zgt.com.example.myzq.model.common.chooseStock;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import butterknife.BindView;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;

public class ChooseH5Activity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.pro)
    ProgressBar progressBar;
    private String url;
    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {

//        StatusBarUtil.statusBarLightMode(this);
        com.jaeger.library.StatusBarUtil.setLightMode(this);//黑色
        url=getIntent().getStringExtra("url");

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
                super.onPageFinished(view, url);

            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成  
                    if(progressBar!=null){
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    // 网页加载中  
                    if(progressBar!=null){
                        progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                        progressBar.setProgress(newProgress);//设置进度值
                    }

                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_h5;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (webView.canGoBack()) {
            webView.goBack();//返回上个页面
            return;
        } else {
            finish();
        }
    }

}
