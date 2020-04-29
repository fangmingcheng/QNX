package zgt.com.example.myzq.model.common.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import zgt.com.example.myzq.MyApp;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseFragment;
import zgt.com.example.myzq.bean.HttpResult;
import zgt.com.example.myzq.model.common.MainActivity;
import zgt.com.example.myzq.utils.JavaScriptinterface;
import zgt.com.example.myzq.utils.SPUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuotationFragment extends BaseFragment {

    @BindView(R.id.webView)
     WebView webView;
//    public static WebView webView;
    private HttpResult result=null;
    private  MainActivity mainActivity;

    private boolean isLogin=true;
    private boolean isRefresh=false;
    private boolean isReload=true;
    List<String> cookies=null;
    String js;

    private DateFormat date;
    private String s;
    private int status=0;
    private final int version=Build.VERSION.SDK_INT;

    private boolean isInjection=true;
    public QuotationFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_quotation;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

//        webView=mViewRoot.findViewById(R.id.webView);
//        webView.clearCache(true);
//        removeCookie(getActivity());
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
        //设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //字体不随系统变化
        webView.getSettings().setTextZoom(100);
        if(TextUtils.isEmpty(SPUtil.getToken())){
            status=1;//未登录
        }else {
            status=2;//已登录
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
        Date today = calendar.getTime();
        Log.e("FMC",""+status);
        date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        s = date.format(today) + " +0000";
        initData();
        webView.addJavascriptInterface(new JavaScriptinterface(getActivity()), "android");
        //设置Web视图
        webView.setWebViewClient(new webViewClient());
    }

    private void initData(){
        cookies = new ArrayList<>();
        //键值对类型  用等号（"="）连接    具体根据后台给定
        cookies.add("hangqing_token=" + SPUtil.getToken());//根据后台协商而定
        cookies.add("domain=" + "hq.zgziben.com");//根据后台协商而定
        cookies.add("path=" + "/");//根据后台协商而定
        cookies.add("expires=" + s);//根据后台协商而定
    }

    @Override
    public void onResume() {
        Log.e("QuotationFragment","onResume");
        if(status==1){
            if(webView!=null){
                if(TextUtils.isEmpty(SPUtil.getToken())){
                    webView.loadUrl("https://hq.zgziben.com/hsStock/toIndex?token=" + SPUtil.getToken());
                }else {
                    String s1 = "hangqing_token=" + SPUtil.getToken() + ";domain=hq.zgziben.com;path=/;expires=" + s + ";";
                    js = "document.cookie ='" + s1 + "';";
                    Log.e("cookie=", js);
                    if (version < 18) {
                        webView.loadUrl("javascript:" + js);
                    } else {
                        webView.evaluateJavascript("javascript:" + js, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                Log.e("evaluateJavascript", value);
                            }
                        });
                    }

                    if(MyApp.httpResult !=null) {
                        if(!isRefresh) {
                            initData();
                            syncCookieToWebView(getActivity(), "https://hq.zgziben.com/", cookies);
                            webView.loadUrl(MyApp.httpResult.getUrl());
                            Log.e("WC", MyApp.httpResult.getUrl() + "");
                        }else {
//                            webView.reload();
                            Log.e("WC",  "44444444444444444444");
                        }
                    }
                }
            }
        }else if(status==2){//已登录
            if(webView!=null){
                String s1 = "hangqing_token=" + SPUtil.getToken() + ";domain=hq.zgziben.com;path=/;expires=" + s + ";";
                js = "document.cookie ='" + s1 + "';";
                Log.e("cookie=", js);
                webView.evaluateJavascript("javascript:" + js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.e("evaluateJavascript", value);
                    }
                });
                if(isLogin) {
                    syncCookieToWebView(getActivity(),"https://hq.zgziben.com/", cookies);
                    //http://hq.bdcgw.cn/hsStock/toIndex?token=
                    webView.loadUrl("https://hq.zgziben.com/hsStock/toIndex?token=" + SPUtil.getToken());
                    webView.reload();
                    Log.e("WC",  "22222222222222222222222");
                }else {
//                    webView.reload();
                    Log.e("WC",  "3333333333333333333333333");
                }
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("QuotationFragment","onPause");
        super.onPause();
        if (webView != null) {
            isLogin=false;
            webView.stopLoading();
            webView.clearCache(true);
            removeCookie(getActivity());
            if(TextUtils.isEmpty(SPUtil.getToken())){
                isRefresh=false;
            }else{
                isRefresh=true;
            }
        }
    }

    private void syncCookieToWebView(   Context context,String url, List<String> cookies) {
//        if(!TextUtils.isEmpty(url)){
//            if(!TextUtils.isEmpty(cookies)){
//                if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
//                    CookieSyncManager.createInstance(context);
//                }
//                CookieManager cookieManager=CookieManager.getInstance();
//                cookieManager.setAcceptCookie(true);
//                cookieManager.removeSessionCookie();
//                cookieManager.removeAllCookie();
//                StringBuilder sbCookie=new StringBuilder();
//                sbCookie.append(cookies);
////                sbCookie.append(String.format("cookie =",""));
//                sbCookie.append(String.format("hangqing_token="+SPUtil.getToken(),""));
//                sbCookie.append(String.format(";domain=http://192.168.91.107/",""));
//                sbCookie.append(String.format(";path=/",""));
//                sbCookie.append(String.format(";expires="+s,""));
//                String cookieValue=sbCookie.toString();
//                cookieManager.setCookie(url,cookieValue);
//                CookieSyncManager.getInstance().sync();
//                String newCookie=cookieManager.getCookie(url);
////                Log.e("同步后cookie",newCookie);
//            }
//        }
        CookieSyncManager.createInstance(context);
        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
//        cm.removeSessionCookie();
//        cm.removeAllCookie();

        if (cookies != null) {
            for (String cookie : cookies) {
                cm.setCookie(url, cookie);//注意端口号和域名，这种方式可以同步所有cookie，包括sessionid
//                Log.e("cookie=",cookie);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager.getInstance().sync();
        }
    }


        @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        result=((MainActivity)activity).getUrl();

    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void onStart() {
        Log.e("QuotationFragment","onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.e("QuotationFragment","onStop");
        super.onStop();
    }



    @Override
    public void onDestroyView() {
        MyApp.httpResult=null;
        Log.e("QuotationFragment","onDestroyView");
        super.onDestroyView();
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            if (url.equals("YOUR URL")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            } else {
                return false;
            }



//            back true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.e("onPageFinished",url);
            Log.e("isInjection=",isInjection+"");
            if(webView!=null) {
                if(TextUtils.isEmpty(SPUtil.getToken())) {

                }else {
                    view.loadUrl("javascript:settoken('" + SPUtil.getToken() + "')");
                }
                if(status==1){
                    Log.e("QuotationFragment","MyApp.httpResult="+MyApp.httpResult);
                    if (MyApp.httpResult != null&&!(TextUtils.isEmpty(SPUtil.getToken()))) {
                        if(isInjection) {
                            if ("1".equals(MyApp.httpResult.getType())) {
                                webView.evaluateJavascript("javascript:jiaohu_xqAddZixuan('" + MyApp.httpResult.getMark() + "','" + MyApp.httpResult.getCode() + "','" + MyApp.httpResult.getType() + "')", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        isInjection=false;
                                    }
                                });
//                                view.loadUrl("javascript:jiaohu_xqAddZixuan('" + MyApp.httpResult.getMark() + "','" + MyApp.httpResult.getCode() + "','" + MyApp.httpResult.getType() + "')");
                                Log.e("FMC","我执行了");
                            } else if ("2".equals(MyApp.httpResult.getType())) {
                                view.loadUrl("javascript:jiaohu_xqdeleteZixuan('" + MyApp.httpResult.getMark() + "','" + MyApp.httpResult.getCode() + "','" + MyApp.httpResult.getType() + "')");
                            } else if ("3".equals(MyApp.httpResult.getType())) {
                                webView.evaluateJavascript("javascript:jiaohu_tableAddZixuan('" + MyApp.httpResult.getMark() + "','" + MyApp.httpResult.getCode() + "','" + MyApp.httpResult.getType() + "')", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        isInjection=false;
                                    }
                                });
//                                view.loadUrl("javascript:jiaohu_tableAddZixuan('" + MyApp.httpResult.getMark() + "','" + MyApp.httpResult.getCode() + "','" + MyApp.httpResult.getType() + "')");
                                Log.e("FMC","我执行了");
                            } else if ("4".equals(MyApp.httpResult.getType())) {
                                view.loadUrl("javascript:jiaohu_tableDeleteZixuan('" + MyApp.httpResult.getMark() + "','" + MyApp.httpResult.getCode() + "','" + MyApp.httpResult.getType() + "')");
                            }
                            Log.e("QuotationFragment", "result.getMark()=" + MyApp.httpResult.getMark());
                            Log.e("QuotationFragment", "result.getCode()=" + MyApp.httpResult.getCode());
                            Log.e("QuotationFragment", "result.getType()=" + MyApp.httpResult.getType());
//                            view.reload();
//                            isInjection=false;
                        }
                    }
                }
//                else if(){
//
//                }
            }
            super.onPageFinished(view, url);
        }
    }



    public  boolean clickBack(int keycode,KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK ) {
            webView.evaluateJavascript("javascript:phoneBackButtonListener()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.e("FMC",value);
                    // value的值为"false"时，H5页面屏蔽手机返回键
                    // value的值为"true"或"null"时，H5页面不屏蔽手机返回键
                    // phoneBackButtonListener()未定义或没有返回任何数据，则value的值为"null"
                    if("true".equals(value)){
                        mainActivity=(MainActivity)getActivity();
                        mainActivity.exitApp();
                    }else if("null".equals(value)){
                        webView.goBack();
                    }
                }
            });
        }
        return true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
        }else {
            if(webView!=null){
               webView.reload();
            }
        }
        Log.e("fmc",hidden+"");
    }

    private void removeCookie(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
        Log.e("FMC","我清理cookie了");
    }

}


