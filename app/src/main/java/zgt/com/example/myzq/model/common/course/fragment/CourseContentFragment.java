package zgt.com.example.myzq.model.common.course.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import zgt.com.example.myzq.MyApp;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseFragment;
import zgt.com.example.myzq.utils.Log;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseContentFragment extends BaseFragment {

    @BindView(R.id.webview)
    WebView webview;

    @BindView(R.id.Tv_text)
    TextView Tv_text;

    @BindView(R.id.Rl_progress)
    RelativeLayout Rl_progress;

    private String url="";

    WebSettings webSettings;

    ProgressDialog loadingDialog;



    public CourseContentFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_course_content;
    }

    @Override
    public void initToolBar() {

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            Log.e("onHiddenChanged="+hidden);
            Log.e("url="+url);
        }else {
            url= MyApp.fileUrl;
            Log.e("onHiddenChanged="+hidden);
            Log.e("url="+url);
            if(url==null||url.length()==0){
            }else {
                setData(url);
            }
        }
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        Log.e("initViews");

        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setMessage("正在加载...");
        loadingDialog.setCancelable(false);

        webSettings = webview.getSettings();

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);



        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setSavePassword(false);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setSupportZoom(true);//
        webSettings.setDisplayZoomControls(true);

        webSettings.setBuiltInZoomControls(true);
        webSettings.setDomStorageEnabled(true);

        webSettings.setUseWideViewPort(true);


//        //缓存
//        webSettings.setAppCacheEnabled(true);
//        webSettings.setDatabaseEnabled(true);
//        webSettings.setDomStorageEnabled(true);//开启DOM缓存，关闭的话H5自身的一些操作是无效的
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

//        if(url==null||url.length()==0){
//
//        }else {
//            setData(url);
//        }
//        url = getActivity().getIntent().getStringExtra("url");
//        String s = SPUtil.getServerAddress();
//        s = s.substring(0, s.length() - 5);
//
//        fileUrl = s + url;

//        webview.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    // 网页加载完成  
//                    loadingDialog.dismiss();
////                    webview.getSettings().setBlockNetworkImage(false);
//                } else {
//                    // 网页加载中  
//                    loadingDialog.show();
//                }
//            }
//        });

    }


    private void setData(String fileUrl){
        Tv_text.setVisibility(View.GONE);
        webview.setVisibility(View.VISIBLE);
        webview.setBackgroundColor(Color.parseColor("#ffffff"));

        if (!"https://view.officeapps.live.com/op/view.aspx?src=".startsWith(fileUrl) && (fileUrl.contains(".doc") || fileUrl.contains(".docx") || fileUrl.contains( ".xls") || fileUrl.contains(".xlsx") || fileUrl.contains( ".ppt") || fileUrl.contains(".pptx"))) {
            webview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    view.loadUrl(url);
                    if(Rl_progress!=null){
                        Rl_progress.setVisibility(View.VISIBLE);
                    }
//                    loadingDialog.show();
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // TODO Auto-generated method stub
//                    pro.setVisibility(View.GONE);
                    super.onPageFinished(view, url);
                    if(Rl_progress!=null){
                        Rl_progress.setVisibility(View.GONE);
                    }


                }
            });
            webview.loadUrl("https://view.officeapps.live.com/op/view.aspx?src=" + fileUrl);
        } else if (fileUrl.contains(".png") || fileUrl.contains(".jpg")) {
            webview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    view.loadUrl(url);
                    Rl_progress.setVisibility(View.VISIBLE);
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
            webview.loadUrl(fileUrl);
        } else if (fileUrl.contains(".pdf")) {
            webview.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        // 网页加载完成  
                        Rl_progress.setVisibility(View.GONE);
//                    webview.getSettings().setBlockNetworkImage(false);
                    } else {
                        // 网页加载中  
                        Rl_progress.setVisibility(View.VISIBLE);
                    }
                }
            });
//            webview.setWebChromeClient(new WebChromeClient());
            webview.loadUrl("file:///android_asset/pdf.html?"+fileUrl);

//            }
        }else {
            webview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    view.loadUrl(url);
                    Rl_progress.setVisibility(View.VISIBLE);
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
        }
    }


    @Override
    public void onAttach(Activity activity) {
        Log.e("onAttach");
        super.onAttach(activity);
//        url=((CourseDetailActivity)activity).getFile();
    }



}
