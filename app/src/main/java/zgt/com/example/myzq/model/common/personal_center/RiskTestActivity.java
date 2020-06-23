package zgt.com.example.myzq.model.common.personal_center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

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
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.StatusBarUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class RiskTestActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.pro)
    ProgressBar pro;

    private int status,index;

    String s,type,fileId;



    @Override
    public void initToolBar() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_risk_test;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        status = getIntent().getIntExtra("status",0);
        index = getIntent().getIntExtra("index",0);
        type = getIntent().getStringExtra("type");
        fileId = getIntent().getStringExtra("fileid");

        StatusBarUtil.statusBarLightMode(this);
//        url=getIntent().getStringExtra("url");
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
        //加载需要显示的网页
//        if(status==1){
//            s = SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+"/fxcp/cpScore.do?token="+SPUtil.getToken();
//        }else {
        s = SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+"/fxcp/score.do?token="+SPUtil.getToken();
//        }
//        String s = SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+"/fxcp/score.do?token="+SPUtil.getToken();
        webView.loadUrl(s);
        //字体不随系统变化
        webView.getSettings().setTextZoom(100);
        webView.addJavascriptInterface(new APPInterface(), "android");//增加js接口交互ext
//        webView.addJavascriptInterface(new APPQuitInterface(), "android");//增加js接口交互ext
        //设置Web视图
        webView.setWebViewClient(new webViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

            }

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

    private class webViewClient extends WebViewClient {
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
    }

    @OnClick({R.id.Iv_back})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
                myfinish();
                break;
        }
    }

    class APPInterface{
        @JavascriptInterface
        public void Determine(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(status == 1){
                        finish();
                    }else{
                        intoOrder();
                    }
                }
            }).start();
        }

        @JavascriptInterface
        public void quit(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    finish();
//                }
//            }).start();
        }
    }



//    private void commonDialog(String title,String content) {
//        new android.app.AlertDialog.Builder(RiskTestActivity.this).setTitle(title).setMessage(content).setCancelable(false)
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).setPositiveButton("重新评测", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                webView.reload();
//
//                dialog.dismiss();
//            }
//        }).show();
//
//    }

//    private void commonDialog1(String title,String content) {
//
//        new android.app.AlertDialog.Builder(RiskTestActivity.this).setTitle(title).setMessage(content).setCancelable(false)
//                .setNegativeButton("重新评测", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        startActivity(new Intent().setClass(RiskTestActivity.this, RiskTestActivity.class));
////                        RiskTestActivity.this.finish();
//                        webView.reload();
//                        dialog.dismiss();
//                    }
//                }).setPositiveButton("前往购买", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent().setClass(RiskTestActivity.this, OrderDetaiilActivity.class).putExtra("course",courseDetail).putExtra("status","1"));
//                RiskTestActivity.this.finish();
//                dialog.dismiss();
//            }
//        }).show();
//
//    }


    private void intoOrder(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"checkMemberInformation0518.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("fileid", fileId);
        requestParams.addParameter("producttype", type);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        startActivity(new Intent().setClass(RiskTestActivity.this, ZBOrderDetailActivity.class).putExtra("fileid",fileId).putExtra("index",index).putExtra("type",type));

                        RiskTestActivity.this.finish();
                    }else if(a == 5){
                        ToastUtil.showShortToast(RiskTestActivity.this,"您已购买过该课程，无需重新购买");
                    }else if(a==6){
                        ToastUtil.showShortToast(RiskTestActivity.this,"您未绑定手机号码，请您先绑定手机号码");
                    } else if(a==7){
                        ToastUtil.showShortToast(RiskTestActivity.this,"您有订单未确认，请联系客服");
                    } else if(a==8){
                        ToastUtil.showShortToast(RiskTestActivity.this,"请勿重复下单");
                    }
                    else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(RiskTestActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {

                    }
                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(RiskTestActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(RiskTestActivity.this, "网络连接异常");
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

    public void  myfinish(){
        if (webView != null) {
            webView.stopLoading();
//            webView.setWebViewListener(null);
//            webView.clearHistory();
            webView.clearCache(true);
            webView.loadUrl("about:blank");
//            webView.pauseTimers();
//            webView = null;
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myfinish();
    }

}


