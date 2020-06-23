package zgt.com.example.myzq.model.common.home;

import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindView;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.Review;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.StatusBarUtil;

public class ReviewDetailActivity extends BaseActivity {
    @BindView(R.id.webview)
    WebView webview;

    @BindView(R.id.pro)
    ProgressBar progressBar;
//
//    @BindView(R.id.pro)
//    ProgressBar pro;
    private List<Review> list;
    private String url;
    int status = 0;

    @Override
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.statusBarLightMode(this);
        WebSettings webSettings = webview.getSettings();
        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
        webSettings.setDomStorageEnabled(true);
        status = getIntent().getIntExtra("status",0);
        list = (List<Review>) getIntent().getSerializableExtra("list");
        webview.loadUrl(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+list.get(status).getUrl());
            webview.setWebViewClient(new WebViewClient() {
//                @Override
//                public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                    Rl_progress.setVisibility(View.VISIBLE);
//                    super.onPageStarted(view, url, favicon);
//                }

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // TODO Auto-generated method stub
//                    pro.setVisibility(View.GONE);

//                    view.loadUrl(url);
                    super.onPageFinished(view, url);

                }
            });
        webview.setWebChromeClient(new WebChromeClient() {

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
        webview.addJavascriptInterface(new APPInterface(), "android");//增加js接口交互ext
    }


    @Override
    public void initToolBar() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_review_detail;
    }

    @Override
    public void onBackPressed() {
        return;
//        super.onBackPressed();
//        if (webview != null) {
//            webview.stopLoading();
////            webView.setWebViewListener(null);
////            webView.clearHistory();
//            webview.clearCache(true);
//            webview.loadUrl("about:blank");
////            webView.pauseTimers();
////            webView = null;
//        }
    }

    class APPInterface{
        @JavascriptInterface
        public void ConfirmReturnVisit(){
                finish();

        }
    }
}
