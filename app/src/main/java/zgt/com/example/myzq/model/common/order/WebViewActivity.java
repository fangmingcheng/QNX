package zgt.com.example.myzq.model.common.order;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.StatusBarUtil;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.Tv_title)
    TextView Tv_title;
    @BindView(R.id.pro)
    ProgressBar pro;
//    Dialog mDialog;
//
//    @BindView(R.id.pro)
//    ProgressBar pro;

    private String url,fileUrl;
    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.statusBarLightMode(this);
//        mDialog = CustomProgressDialog.createLoadingDialog(this, "正在加载中...");
//        mDialog.setCancelable(true);//允许返回

        WebSettings webSettings = webview.getSettings();
        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
//        webSettings.setSavePassword(false);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setDomStorageEnabled(true);


        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        /**
         * 简单来说，该项设置决定了JavaScript能否访问来自于任何源的文件标识的URL。
         * 比如我们把PDF.js放在本地assets里，然后通过一个URL跳转访问来自于任何URL的PDF，所以这里我们需要将其设置为true。
         * 而一般情况下，为了安全起见，是需要将其设置为false的。
         */
        webSettings.setAllowUniversalAccessFromFileURLs(true);



        fileUrl = getIntent().getStringExtra("url");
        String s = SPUtil.getServerAddress();
        s = s.substring(0, s.length() - 5);

        url = s+fileUrl;

//        // webview必须设置支持Javascript才可打开
//        webview.getSettings().setJavaScriptEnabled(true);
//        // 设置此属性,可任意比例缩放
//        webview.getSettings().setUseWideViewPort(true);


        if (!fileUrl.startsWith("https://view.officeapps.live.com/op/view.aspx?src=") && (fileUrl.contains(".doc") || fileUrl.contains(".docx") || fileUrl.contains(".xls") || fileUrl.contains(".xlsx") || fileUrl.contains(".ppt") || fileUrl.contains(".pptx"))) {
            webview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    view.loadUrl(url);
//                    mDialog.show();

                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // TODO Auto-generated method stub
//                    pro.setVisibility(View.GONE);
//                    mDialog.dismiss();

                    super.onPageFinished(view, url);

                }
            });
            webview.loadUrl("https://view.officeapps.live.com/op/view.aspx?src=" + fileUrl);
        } else if (fileUrl.contains(".png") || fileUrl.contains(".jpg")) {
            webview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    view.loadUrl(url);
//                    mDialog.show();
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // TODO Auto-generated method stub
//                    pro.setVisibility(View.GONE);
                    super.onPageFinished(view, url);

//                    mDialog.dismiss();

                }
            });
            webview.loadUrl(fileUrl);
        } else if (fileUrl.contains(".pdf")) {
            webview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
//                    mDialog.show();
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // TODO Auto-generated method stub
//                    pro.setVisibility(View.GONE);
                    super.onPageFinished(view, url);
//                    mDialog.dismiss();

                }
            });//file:///android_asset/pdfjs/web/viewer.html?file=
//            webview.loadUrl("file:///android_asset/pdf.html?"+fileUrl);
            webview.loadUrl("file:///android_asset/PDFJS/web/viewer.html?file="+fileUrl);
        }else {
            webview.loadUrl(url);
            webview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
//                    mDialog.show();
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // TODO Auto-generated method stub
//                    pro.setVisibility(View.GONE);
                    super.onPageFinished(view, url);
//                    mDialog.dismiss();

                }
            });
//            webview.setWebChromeClient(new WebChromeClient() {
//                @Override
//                public void onProgressChanged(WebView view, int newProgress) {
//                    if (newProgress == 100) {
//                        // 网页加载完成  
////                        mDialog.dismiss();
//                        Rl_progress.setVisibility(View.GONE);
//                        webview.getSettings().setBlockNetworkImage(false);
//                    } else {
//                        // 网页加载中  
////                        mDialog.show();
//                    }
//                }
//            });
        }
        webview.setWebChromeClient(new WebChromeClient() {


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成  
                    if(pro!=null){
                        pro.setVisibility(View.GONE);
                    }
                } else {
                    // 网页加载中  
                    if(pro!=null){
                        pro.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                        pro.setProgress(newProgress);//设置进度值
                    }

                }
            }
        });

    }
    @OnClick({R.id.Iv_back})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
                if (webview != null) {
                    webview.stopLoading();
                    webview.clearCache(true);
                    webview.loadUrl("about:blank");
                }
                finish();
                break;
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (webview != null) {
            webview.stopLoading();
            webview.clearCache(true);
            webview.loadUrl("about:blank");
        }
    }

}
