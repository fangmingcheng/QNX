package zgt.com.example.myzq.model.common.home.h5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.model.common.order.ZBOrderDetailActivity;
import zgt.com.example.myzq.model.common.personal_center.RiskTestActivity;
import zgt.com.example.myzq.model.common.personal_center.basic.MyBasicInformationActivity;
import zgt.com.example.myzq.utils.Log;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.StatusBarUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class H5Activity extends BaseActivity {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.Tv_title)
    TextView Tv_title;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.pro)
    ProgressBar progressBar;

    private String url;
    private String title;
    public static int REQUESTCODE=1;
    public static int REQUESTCODE_1=2;


    @Override
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.statusBarLightMode(this);
        url=getIntent().getStringExtra("url");

        Tv_title.setText(title);

        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);

        webSettings.setDomStorageEnabled(true);
        //设置页面自适应手机

//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webSettings.setDatabaseEnabled(true);
//        webSettings.setAppCacheEnabled(true);
//        webSettings.setSavePassword(true);
//        webSettings.setSupportZoom(true);
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);


        webSettings.setAllowUniversalAccessFromFileURLs(true);
        //H5 中http与https混合加载的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        //解决h5网页打开白页
        webSettings.setUseWideViewPort(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        webView.addJavascriptInterface(new APPInterface(), "android");//增加js接口交互ext


        //加载需要显示的网页
        webView.loadUrl(url);
        webView.loadUrl("javascript:saveUserToekn(\"" + SPUtil.getToken() + "\")");
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
//                String s= "javascript:testResult('" + SPUtil.getToken() + "')";

                super.onPageFinished(view, url);

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
//                super.onReceivedSslError(view, handler, error);/**/
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if(title!=null&&!view.getUrl().contains(title)){
                    if(Tv_title!=null){
                        if(title.contains("http")){

                        }else {
                            Tv_title.setText(title);
                        }
                    }
                }
            }

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
        refreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
//        refreshLayout.setRefreshHeader(BezierRadarHeader(this).setEnableHorizontalDrag(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));

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

    @Override
    public void initToolBar() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_h5;
    }

    @OnClick({R.id.Iv_back})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
                if (webView != null) {
                    webView.stopLoading();
//            webView.setWebViewListener(null);
//            webView.clearHistory();
                    webView.clearCache(true);
                    webView.loadUrl("about:blank");
//            webView.pauseTimers();
//            webView = null;
                }
                finish();
                break;
        }
    }


    class APPInterface {
        @JavascriptInterface
        public void getOtherProduct(String pid,String type) {
            Log.e("MSG","getOtherProduct");
            intoOrder(pid,type);
//            try {
//                JSONObject jsonObject = new JSONObject(s);
//                String pid = jsonObject.getString("pid");
//                String uid = jsonObject.getString("uid");
//                String type = jsonObject.getString("type");
//                intoOrder(pid);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }

        @JavascriptInterface
        public void requsetUserToekn() {
            Log.e("MSG","requsetUserToekn");
//            ToastUtil.showShortToast(H5Activity.this, "执行requsetUserToekn");
            if (TextUtils.isEmpty(SPUtil.getToken())) {
                startActivityForResult(new Intent().setClass(H5Activity.this, LoginActivity.class).putExtra("status", 1), REQUESTCODE);
            } else {
                checkToken();
//                webView.loadUrl("javascript:saveUserToekn(\"" + SPUtil.getToken() + "\")");
            }
        }

        @JavascriptInterface
        public void requsetUserLoginStatus() {
            Log.e("MSG","requsetUserLoginStatus");
//            ToastUtil.showShortToast(H5Activity.this, "执行requsetUserLoginStatus");
            checkFirstToken();
        }

        @JavascriptInterface
        public void addStockSussess(String stockCode) {
            Log.e("MSG","addStockSussess");
//            ToastUtil.showShortToast(H5Activity.this, "执行addStockSussess");
            addStock(stockCode);
        }

        @JavascriptInterface
        public void deleteStockSussess(String stockCode) {
            Log.e("MSG","deleteStockSussess");
//            ToastUtil.showShortToast(H5Activity.this, "执行deleteStockSussess");
            deleteStock(stockCode);
        }

        @JavascriptInterface
        public void pushLoginViewController() {
            Log.e("MSG","pushLoginViewController");
//            ToastUtil.showShortToast(H5Activity.this, "pushLoginViewController");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String s = "";
                    webView.loadUrl("javascript:logoutSuccess('" + s + "')");
                    startActivityForResult(new Intent().setClass(H5Activity.this, LoginActivity.class).putExtra("status", 1), REQUESTCODE);
                }
            });

        }

        @JavascriptInterface
        public void showWebRequsetErrorMsg(String msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(H5Activity.this).setTitle("异常提示").setMessage(msg).setCancelable(false).setNegativeButton("取消", null).
                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                }
            });

        }
    }

    private void commonDialog(int status,String title,String content,String fileid,String type) {
        new android.app.AlertDialog.Builder(H5Activity.this).setTitle(title).setMessage(content).setCancelable(false)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).setPositiveButton("立即前往", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(status==2){//status 判断是续费还是正常购买
                    startActivity(new Intent().setClass(H5Activity.this, MyBasicInformationActivity.class).putExtra("status",4).putExtra("fileid",fileid).putExtra("index",4).putExtra("type",type));
                }else if(status==3){
                    startActivity(new Intent().setClass(H5Activity.this, RiskTestActivity.class).putExtra("status",4).putExtra("fileid",fileid).putExtra("index",4).putExtra("type",type));
                }else if(status==4){
                    startActivity(new Intent().setClass(H5Activity.this, RiskTestActivity.class).putExtra("status",4).putExtra("fileid",fileid).putExtra("index",4).putExtra("type",type));
                }
                dialog.dismiss();
            }
        }).show();
    }



    private void commonDialog(String title,String content,String fileid,String type) {
        new android.app.AlertDialog.Builder(H5Activity.this).setTitle(title).setMessage(content).setCancelable(false)
                .setNegativeButton("重新评测", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent().setClass(H5Activity.this, RiskTestActivity.class).putExtra("status",4).putExtra("fileid",fileid).putExtra("index",4).putExtra("type",type));
                        dialog.dismiss();
                    }
                }).setPositiveButton("前往购买", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent().setClass(H5Activity.this, ZBOrderDetailActivity.class).putExtra("fileid",fileid).putExtra("index",4).putExtra("type",type));
                dialog.dismiss();
            }
        }).show();
    }

    private void addStock(String stock){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"AddZiXuan.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("stkLabel", stock);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        ToastUtil.showShortToast(H5Activity.this,jsonObject.getString("message"));
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(H5Activity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
                                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivityForResult(new Intent().setClass(H5Activity.this,LoginActivity.class).putExtra("status",1),REQUESTCODE);
                                                dialog.dismiss();
                                            }
                                        }).create().show();
                            }
                        });

                    }else {

                    }
                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(H5Activity.this, getString(R.string.login_parse_exc));
                        }
                    });

                } finally {

                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(H5Activity.this, "网络连接异常");
                    }
                });
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
    }

    private void deleteStock(String stock){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"DelZiXuan.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("stkLabel", stock);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        ToastUtil.showShortToast(H5Activity.this,jsonObject.getString("message"));
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(H5Activity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
                                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivityForResult(new Intent().setClass(H5Activity.this,LoginActivity.class).putExtra("status",1),REQUESTCODE);
                                                dialog.dismiss();
                                            }
                                        }).create().show();

                            }
                        });

                    }else {

                    }
                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(H5Activity.this, getString(R.string.login_parse_exc));
                        }
                    });

                } finally {

                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(H5Activity.this, "网络连接异常");
                    }
                });
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
    }

    private void checkFirstToken(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"checkToken.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                webView.loadUrl("javascript:userLoginStatus(\"" + SPUtil.getToken() + "\")");
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String s = "0";
                                webView.loadUrl("javascript:userLoginStatus(\"" + s + "\")");
                            }
                        });
                    }
                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(H5Activity.this, getString(R.string.login_parse_exc));
                        }
                    });

                } finally {

                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(H5Activity.this, "网络连接异常");
                    }
                });
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
    }


    private void checkToken(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"checkToken.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                webView.loadUrl("javascript:saveUserToekn(\"" + SPUtil.getToken() + "\")");
                            }
                        });

                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(H5Activity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
                                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivityForResult(new Intent().setClass(H5Activity.this,LoginActivity.class).putExtra("status",1),REQUESTCODE);
                                                dialog.dismiss();
                                            }
                                        }).create().show();
                            }
                        });

                    }else {

                    }
                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(H5Activity.this, getString(R.string.login_parse_exc));
                        }
                    });

                } finally {

                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(H5Activity.this, "网络连接异常");
                    }
                });
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
    }

    private void intoOrder(String fileid,String type){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"checkMemberInformation0518.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("fileid", fileid);
        requestParams.addParameter("producttype", type);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                commonDialog("您已完成风险评测","是否前往重新评测，或者直接购买",fileid,type);
                            }
                        });

                    } else if(a==2){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                commonDialog(2,"您的个人信息不完整请您完善","是否前往完善",fileid,type);
                            }
                        });
                    } else if(a==3){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                commonDialog(3,"没有风控记录，请您前往填写风控记录","是否前往填写",fileid,type);
                            }
                        });

                    } else if(a==4){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                commonDialog(4," C1(最低类别),不能购买","是否前往重新评测",fileid,type);
                            }
                        });

                    } else if(a==5){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(H5Activity.this,"您已购买过该课程，无需重新购买");
                            }
                        });

                    }else if(a==6){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(H5Activity.this,"您未绑定手机号码，请您先绑定手机号码");
                            }
                        });
                    } else if(a==7){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(H5Activity.this,"您有订单未确认，请联系客服");
                            }
                        });

                    } else if(a==8){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(H5Activity.this,"请勿重复下单");
                            }
                        });
                    }
                    else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivityForResult(new Intent().setClass(H5Activity.this, LoginActivity.class).putExtra("status", 1), REQUESTCODE);
                            }
                        });

                    }else {

                    }
                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(H5Activity.this, getString(R.string.login_parse_exc));
                        }
                    });

                } finally {

                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(H5Activity.this, "网络连接异常");
                    }
                });
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            if(requestCode == REQUESTCODE){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:loginSuccess(\"" + SPUtil.getToken() + "\")");
                        webView.reload();
                    }
                });

            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (webView.canGoBack()) {
            webView.goBack();//返回上个页面
            return;
        } else {
            if (webView != null) {
                webView.stopLoading();
//            webView.setWebViewListener(null);
//            webView.clearHistory();
                webView.clearCache(true);
                webView.loadUrl("about:blank");
//            webView.pauseTimers();
//            webView = null;
            }
            finish();
        }
    }
}
