package zgt.com.example.myzq.model.common.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseFragment;
import zgt.com.example.myzq.model.common.MainActivity;
import zgt.com.example.myzq.model.common.home.h5.H5Activity;
import zgt.com.example.myzq.utils.Log;

/**
 * A simple {@link Fragment} subclass.
 */
public class HqFragment extends BaseFragment {

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.Ll_bottom)
    LinearLayout Ll_bottom;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.pro)
    ProgressBar progressBar;

//    @BindView(R.id.Rl_title)
//    RelativeLayout Rl_title;

    private MainActivity mainActivity;

    private boolean isSuccess = true;

    public HqFragment() {
        // Required empty public constructor
    }

    @Override
    public void initToolBar() {

    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
        }else {
            if(isSuccess){

            }else {
                if(webView != null){
                    webView.reload();
                }
            }

//            StatusBarUtil.setLightMode(getActivity());//黑色
        }

    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_hq;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        LinearLayout.LayoutParams params=(LinearLayout.LayoutParams) Rl_title.getLayoutParams();
//        params.topMargin=getStatusBarHeight(getActivity());
//
//        Rl_title.setLayoutParams(params);
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);

        webSettings.setDomStorageEnabled(true);

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置页面自适应手机

        //解决h5网页打开白页
        webSettings.setUseWideViewPort(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setLoadWithOverviewMode(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);

//        webView.loadUrl("https://1e5o3e6n0.lightyy.com/index.html#/");
        webView.loadUrl("https://1e5o3e6n0.lightyy.com/index.html#/");

        webView.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                if("https://1e5o3e6n0.lightyy.com/index.html#/".equals(url)){
                    view.loadUrl(url);
                }else {
                    startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url", url));
                }

                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.e("TAG","onReceivedError");
                isSuccess = false;
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
//                    pro.setVisibility(View.GONE);
                if("https://1e5o3e6n0.lightyy.com/index.html#/".equals(url)){
                    if(Ll_bottom!=null){
                        Ll_bottom.setVisibility(View.GONE);
                    }

                }else {
                    if(Ll_bottom!=null){
                        Ll_bottom.setVisibility(View.VISIBLE);
                    }
                }
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

        setPullRefresher();

    }

    private void setPullRefresher(){
        //设置 Header 为 Material风格
        refreshLayout.setPrimaryColors(Color.parseColor("#00000000"));
        refreshLayout.setRefreshHeader(new MaterialHeader(getActivity()).setShowBezierWave(true));
//        refreshLayout.setRefreshHeader(BezierRadarHeader(this).setEnableHorizontalDrag(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //在这里执行上拉刷新时的具体操作(网络请求、更新UI等)

              if(webView!=null){
                  webView.reload();
              }
//                adapter.refresh(newList);
                refreshlayout.finishRefresh(/*,false*/);
                //不传时间则立即停止刷新    传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore( RefreshLayout refreshLayout) {
//                ToastUtil.showShortToast(getActivity(),"数据已加载完毕");
                refreshLayout.finishLoadMore(/*,false*/);
            }
        });
//
//        refreshLayout.setEnableLoadmore(false);//屏蔽掉上拉加载的效果
    }

    @OnClick({R.id.Iv_zuo,R.id.Iv_you})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.Iv_zuo://资讯
                if(webView.canGoBack()){
                    webView.goBack();
                }
//                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url", "https://je7o2az1x.lightyy.com/index.html?p=hsjy_1189&h=0&tg=_blank").putExtra("title", "数据选股"));
                break;
            case R.id.Iv_you://
                if(webView.canGoForward()){
                    webView.goForward();
                }
//                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url", "https://je7o2az1x.lightyy.com/index.html?p=hsjy_1189&h=0&tg=_blank").putExtra("title", "数据选股"));
                break;
        }
    }

    /**
     * 获取通知栏高度
     * @param context 上下文
     * @return 通知栏高度
     */
    private int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

}
