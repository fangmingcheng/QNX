package zgt.com.example.myzq.model.common.order;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import butterknife.BindView;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;

public class WebView_AgreemetActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webview;

    @BindView(R.id.Rl_progress)
    RelativeLayout Rl_progress;
//
//    @BindView(R.id.pro)
//    ProgressBar pro;

    private String fileUrl;


    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        StatusBarUtil.statusBarLightMode(this);


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

        fileUrl = getIntent().getStringExtra("url");

//
//        // webview必须设置支持Javascript才可打开
//        webview.getSettings().setJavaScriptEnabled(true);
//        // 设置此属性,可任意比例缩放
//        webview.getSettings().setUseWideViewPort(true);


        if (!fileUrl.startsWith("https://view.officeapps.live.com/op/view.aspx?src=") && (fileUrl.contains(".doc") || fileUrl.contains(".docx") || fileUrl.contains(".xls") || fileUrl.contains(".xlsx") || fileUrl.contains(".ppt") || fileUrl.contains(".pptx"))) {
            webview.setWebViewClient(new WebViewClient() {
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
                    Rl_progress.setVisibility(View.GONE);

                }
            });
            webview.loadUrl("https://view.officeapps.live.com/op/view.aspx?src=" + fileUrl);
        } else if (fileUrl.contains(".png") || fileUrl.contains(".jpg")) {
            webview.setWebViewClient(new WebViewClient() {
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
                    Rl_progress.setVisibility(View.GONE);

                }
            });
            webview.loadUrl(fileUrl);
        } else if (fileUrl.contains(".pdf")) {
            webview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    view.loadUrl(url);

                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // TODO Auto-generated method stub
//                    pro.setVisibility(View.GONE);
                    Rl_progress.setVisibility(View.GONE);
                    super.onPageFinished(view, url);

                }
            });
            webview.setWebChromeClient(new WebChromeClient());

            webview.loadUrl("file:///android_asset/show_pdf.html?"+fileUrl);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//api >= 19
//
//                webview.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=" + fileUrl);
//
//            } else {
//                if (!TextUtils.isEmpty(fileUrl)) {
//                    byte[] bytes = null;
//                    try {// 获取以字符编码为utf-8的字符
//                        bytes = fileUrl.getBytes("UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    if (bytes != null) {
////                        fileUrl = new BASE64Encoder().encode(bytes);// BASE64转码
//                    }
//                }
//                webview.loadUrl("file:///android_asset/pdfjs_compatibility/web/viewer.html?file=" + fileUrl);
//            }
        }else {
            webview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    view.loadUrl(url);

                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // TODO Auto-generated method stub
//                    pro.setVisibility(View.GONE);
                    Rl_progress.setVisibility(View.GONE);
                    super.onPageFinished(view, url);

                }
            });
//            webview.setWebChromeClient(new WebChromeClient() {
//                @Override
//                public void onProgressChanged(WebView view, int newProgress) {
//                    if (newProgress == 100) {
//                        // 网页加载完成  
//
//                        webview.getSettings().setBlockNetworkImage(false);
//                    } else {
//                        // 网页加载中  
//
//                    }
//                }
//            });
            webview.loadUrl(fileUrl);
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (webview != null) {
            webview.stopLoading();
//            webView.setWebViewListener(null);
//            webView.clearHistory();
            webview.clearCache(true);
            webview.loadUrl("about:blank");
//            webView.pauseTimers();
//            webView = null;
        }
    }

}
