package zgt.com.example.myzq.model.common.home.researchreport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.stock.ReseaRchreportk;
import zgt.com.example.myzq.model.common.home.h5.H5Activity;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.StatusBarUtil;
import zgt.com.example.myzq.utils.ToastUtil;


public class ReseaRchreportDetailActivity extends BaseActivity {
    @BindView(R.id.Tv_title_yanbao)
    TextView Tv_title_yanbao;
    @BindView(R.id.Tv_time)
    TextView Tv_time;
    @BindView(R.id.Tv_source)
    TextView Tv_source;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.webView)
    WebView webView;

    private ReseaRchreportk reseaRchreportk = null;
    private String uuid;
    private String pid;
    @Override
    public void initToolBar() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_resea_rchreport_detail;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.statusBarLightMode(this);
        uuid=getIntent().getStringExtra("uuid");
        pid=getIntent().getStringExtra("pid");
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        getData();
//        webSettings.setDomStorageEnabled(true);
//        //设置页面自适应手机
//
//        //解决h5网页打开白页
//        webSettings.setUseWideViewPort(true);
//        webSettings.setPluginState(WebSettings.PluginState.ON);
//        webSettings.setLoadWithOverviewMode(true);
//        //设置可以访问文件
//        webSettings.setAllowFileAccess(true);
//        //设置支持缩放
////        webSettings.setBuiltInZoomControls(true);
//        webSettings.setTextZoom(100);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setContent(){
        if(reseaRchreportk!=null){
            Tv_title_yanbao.setText(reseaRchreportk.getTitle());
            Tv_time.setText(reseaRchreportk.getCreatetime());
            Tv_source.setText("来源："+reseaRchreportk.getSource());
//            if(TextUtils.isEmpty(trainingDetail.getPicpath())){
//                Iv_picture.setVisibility(View.GONE);
//            }else {
//                Iv_picture.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+trainingDetail.getPicpath());
//            }
            if(reseaRchreportk.getIsbuy()==0){
                webView.setVisibility(View.GONE);
                startActivity(new Intent().setClass(ReseaRchreportDetailActivity.this, H5Activity.class).putExtra("url",SPUtil.getServerAddress()+"yanbao.do?token="+SPUtil.getToken()+"&pid="+pid));
                finish();
            }else {
                webView.setVisibility(View.VISIBLE);
            }

            //设置页面自适应手机
            webView.addJavascriptInterface(new ReseaRchreportDetailActivity.JavascriptInterface(), "ReseaRchreportDetailActivity");
            String s="<body width=320px style=\\\"word-wrap:break-word; font-family:Arial\\\">";
            //替换img属性
            String varjs = "<script type='text/javascript'> \nwindow.onload = function()\n{var $img = document.getElementsByTagName('img');for(var p in  $img){$img[p].style.width = '100%'; $img[p].style.height ='auto'}}</script>";
            //点击查看
            String jsimg = "function()\n { var imgs = document.getElementsByTagName(\"img\");for(var i = 0; i < imgs.length; i++){  imgs[i].onclick = function()\n{ReseaRchreportDetailActivity.startPhotoActivity(this.src);}}}";
            webView.loadDataWithBaseURL("http://stock.bdcgw.cn", s+varjs+reseaRchreportk.getContent(), "text/html", "utf-8", null);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
                @Override
                public void onPageFinished(WebView webView, String s) {
                    webView.loadUrl("javascript:(" + jsimg + ")()");
                }
            });


        }

    }
    @OnClick({R.id.Iv_back})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
                if(webView.canGoBack()){
                    webView.goBack();
                }else {
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            finish();
        }
    }

    private void getData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"getReseaRchreportView.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("uuid", uuid);
        requestParams.addParameter("pid", pid);
        requestParams.addParameter("type", 0);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        reseaRchreportk = new ReseaRchreportk();
//                        reseaRchreportk.setUuid(json.getString("uuid"));
                        reseaRchreportk.setTitle(json.getString("title"));
                        reseaRchreportk.setAuthor(json.getString("author"));
                        reseaRchreportk.setClick(json.getInt("click"));
                        reseaRchreportk.setCreatetime(json.getString("createtime"));
                        reseaRchreportk.setFtitle(json.getString("ftitle"));
                        reseaRchreportk.setContent(json.getString("content"));
                        reseaRchreportk.setIstop(json.getInt("istop"));
                        reseaRchreportk.setPicpath(json.getString("picpath"));
                        reseaRchreportk.setSource(json.getString("source"));
                        reseaRchreportk.setSummary(json.getString("summary"));
                        reseaRchreportk.setIsbuy(json.getInt("isbuy"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setContent();
                            }
                        });
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                if(TextUtils.isEmpty(SPUtil.getToken())){
//                                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                                    getActivity().finish();
//                                }else {
//                                    new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                                                    getActivity().finish();
//                                                    dialog.dismiss();
//                                                }
//                                            }).create().show();
//                                }
                                startActivity(new Intent().setClass(ReseaRchreportDetailActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
                    }else {
                        final String msg=jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(ReseaRchreportDetailActivity.this, msg);

                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            ToastUtil.showShortToast(getActivity(), getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(ReseaRchreportDetailActivity.this, "网络连接异常");
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



    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void startPhotoActivity(String imageUrl) {
            //根据URL查看大图逻辑

        }
    }

}
