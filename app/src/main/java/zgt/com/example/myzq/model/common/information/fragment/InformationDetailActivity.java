package zgt.com.example.myzq.model.common.information.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.ZXDetailBean;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class InformationDetailActivity extends BaseActivity {

    @BindView(R.id.web)
    WebView web;
    @BindView(R.id.Tv_title_class)
    TextView Tv_title_class;
    @BindView(R.id.Tv_author)
    TextView Tv_author;
    @BindView(R.id.Tv_time)
    TextView Tv_time;


    @BindView(R.id.Tv_reader)
    TextView Tv_reader;
    private String uuid;
    private ZXDetailBean zxDetailBean;

    @OnClick({R.id.Iv_customer})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_customer:
                onBackPressed();
                break;
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_information_detail;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        StatusBarUtil.statusBarLightMode(this);
        uuid=getIntent().getStringExtra("uuid");
        getViewDetial();
    }

    private void initData(ZXDetailBean zxDetailBean){
        Tv_title_class.setText(zxDetailBean.getTitle());
        Tv_author.setText("来源："+zxDetailBean.getSource());
        Tv_time.setText("时间："+zxDetailBean.getCreatetime());
        Tv_reader.setText(zxDetailBean.getClick()+"人阅读");
        web.getSettings().setJavaScriptEnabled(true);
        web.addJavascriptInterface(new JavascriptInterface(), "InformationDetailActivity");
        //替换img属性
        String varjs = "<script type='text/javascript'> \nwindow.onload = function()\n{var $img = document.getElementsByTagName('img');for(var p in  $img){$img[p].style.width = '100%'; $img[p].style.height ='auto'}}</script>";
        //点击查看
        String jsimg = "function()\n { var imgs = document.getElementsByTagName(\"img\");for(var i = 0; i < imgs.length; i++){  imgs[i].onclick = function()\n{DetailActivity.startPhotoActivity(this.src);}}}";
        web.loadDataWithBaseURL("http://stock.bdcgw.cn", varjs+zxDetailBean.getContent(), "text/html", "utf-8", null);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String s) {
                webView.loadUrl("javascript:(" + jsimg + ")()");
            }
        });
    }

    private void getViewDetial(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress() + "getZxNews.do");
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("uuid", uuid);
        requestParams.setConnectTimeout(60 * 1000);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if (a==1) {
                        zxDetailBean = new ZXDetailBean();
                        zxDetailBean.setContent(json.getString("content"));
                        zxDetailBean.setFtitle(json.getString("ftitle"));
                        zxDetailBean.setSummary(json.getString("summary"));
                        zxDetailBean.setClick(json.getInt("click"));
                        zxDetailBean.setAuthor(json.getString("author"));
                        zxDetailBean.setSource(json.getString("source"));
                        zxDetailBean.setTitle(json.getString("title"));
                        zxDetailBean.setPicpath(json.getString("picpath"));
                        zxDetailBean.setVideoflag(json.getInt("videoflag"));
                        zxDetailBean.setVideopath(json.getString("videopath"));
                        zxDetailBean.setCreatetime(json.getString("createtime"));
                        zxDetailBean.setIstop(json.getInt("istop"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initData(zxDetailBean);
                            }
                        });
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(InformationDetailActivity.this, LoginActivity.class));
                                finish();
//                                if(TextUtils.isEmpty(SPUtil.getToken())){
//                                    startActivity(new Intent().setClass(DetailActivity.this,LoginActivity.class));
//                                    finish();
//                                }else {
//                                    new AlertDialog.Builder(DetailActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    startActivity(new Intent().setClass(DetailActivity.this,LoginActivity.class));
//                                                    finish();
//                                                    dialog.dismiss();
//                                                }
//                                            }).create().show();
//                                }
                            }
                        });
                    }

                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(InformationDetailActivity.this, "解析异常");
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
                        ToastUtil.showShortToast(InformationDetailActivity.this, "网络连接异常");
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

    public  String NoHTML(String Htmlstring) {
        String dest;
        String regMatchTag = "<[^>]*>|\n";
        //删除脚本
        String str= Htmlstring.replaceAll(regMatchTag,"");
        Pattern p = Pattern.compile("&");
        Matcher m = p.matcher(str);
        dest = m.replaceAll("").trim();
        dest=dest.replaceAll("nbsp;","").replaceAll("emsp;","");
        return dest;
    }
    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void startPhotoActivity(String imageUrl) {
            //根据URL查看大图逻辑
        }
    }

}
