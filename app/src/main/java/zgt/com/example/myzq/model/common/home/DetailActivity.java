package zgt.com.example.myzq.model.common.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
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
import zgt.com.example.myzq.bean.Stock;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.StatusBarUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class DetailActivity extends BaseActivity {

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
    @BindView(R.id.Iv_collection)
    ImageView Iv_collection;
    @BindView(R.id.collection_num)
    TextView collection_num;
    @BindView(R.id.Iv_zan)
    ImageView Iv_zan;
    @BindView(R.id.Tv_num_zan)
    TextView Tv_num_zan;

    private String readnum,uuid,status;
    private String content,name,title,time;
    private Stock stock;


    int isCollection,isSupport,supportCount,collectionCount;
    @Override
    public int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public void initToolBar() {

    }

    @OnClick({R.id.Iv_customer,R.id.Iv_collection,R.id.Iv_zan})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_customer:
                onBackPressed();
                break;
            case R.id.Iv_collection:
                if(isCollection==0){
                    getCollectData(1);
                }else if(isCollection==1){
                    getCollectData(2);
                }
                break;
            case R.id.Iv_zan:
                if(isSupport==0){
                    getDianZanData(1);
                }else if(isSupport==1){
                    getDianZanData(2);
                }
                break;
        }
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.statusBarLightMode(this);
        status=getIntent().getStringExtra("status");
        uuid=getIntent().getStringExtra("uuid");
        getViewDetial();
        isCollectionOrSupport();
    }

    private void initData(Stock stock){
        Tv_title_class.setText(stock.getTitle());
        Tv_author.setText(stock.getTeachername());
        Tv_time.setText(stock.getAddtime());
        Tv_reader.setText(stock.getClick()+"人阅读");
        web.getSettings().setJavaScriptEnabled(true);
        web.addJavascriptInterface(new JavascriptInterface(), "DetailActivity");
        //替换img属性
        String varjs = "<script type='text/javascript'> \nwindow.onload = function()\n{var $img = document.getElementsByTagName('img');for(var p in  $img){$img[p].style.width = '100%'; $img[p].style.height ='auto'}}</script>";
        //点击查看
        String jsimg = "function()\n { var imgs = document.getElementsByTagName(\"img\");for(var i = 0; i < imgs.length; i++){  imgs[i].onclick = function()\n{DetailActivity.startPhotoActivity(this.src);}}}";
        web.loadDataWithBaseURL("http://stock.bdcgw.cn", varjs+stock.getContent(), "text/html", "utf-8", null);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String s) {
                webView.loadUrl("javascript:(" + jsimg + ")()");
            }
        });
    }

    private void getViewDetial(){
        RequestParams requestParams;
        if("1".equals(status)) {
            requestParams = new RequestParams(SPUtil.getServerAddress() + "getPublicViewByUuid.do");
        }else{
            requestParams = new RequestParams(SPUtil.getServerAddress() + "getPrivateViewByUuid.do");
        }
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
                        stock=new Stock();
                        stock.setHtmlcontent(NoHTML(json.getString("content")));
                        stock.setHeadimg(json.getString("headimg"));
                        stock.setContent(json.getString("content"));
                        stock.setClick(json.getInt("click")+"");
                        stock.setAddtime(json.getString("addtime"));
                        stock.setTeachername(json.getString("truename"));
                        stock.setTitle(json.getString("title"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initData(stock);
                            }
                        });
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(DetailActivity.this,LoginActivity.class));
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
                            ToastUtil.showShortToast(DetailActivity.this, "解析异常");
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
                        ToastUtil.showShortToast(DetailActivity.this, "网络连接异常");
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
        dest=dest.replaceAll("nbsp;","");
        return dest;
    }



    private void isCollectionOrSupport(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"isCollectionOrSupport.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        if("1".equals(status)) {
            requestParams.addParameter("publicviewid", uuid);
        }else if("2".equals(status)){
            requestParams.addParameter("privateviewid", uuid);
        }
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    final String msg=jsonObject.getString("message");
//                    ToastUtil.showShortToast(DetailActivity.this, msg);
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        isCollection=json.getInt("isCollection");
                        collectionCount=json.getInt("collectionCount");
                        supportCount=json.getInt("supportCount");
                        isSupport=json.getInt("isSupport");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                collection_num.setText(collectionCount+"收藏");
                                if(isCollection==1){
                                    Iv_collection.setImageResource(R.mipmap.btn_shoucang2);
                                }else if(isCollection==0){
                                    Iv_collection.setImageResource(R.mipmap.btn_shoucang1);
                                }

                                Tv_num_zan.setText(supportCount+"点赞");
                                if(isSupport==1){
                                    Iv_zan.setImageResource(R.mipmap.btn_dianzan2);
                                }else if(isSupport==0){
                                    Iv_zan.setImageResource(R.mipmap.btn_dianzan1);
                                }
                            }
                        });
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(DetailActivity.this, LoginActivity.class));
                                finish();
//                                if(!TextUtils.isEmpty(SPUtil.getToken())) {
//                                    new AlertDialog.Builder(DetailActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    startActivity(new Intent().setClass(DetailActivity.this, LoginActivity.class));
//                                                    finish();
//                                                    dialog.dismiss();
//                                                }
//                                            }).create().show();
//                                }else {
//                                    startActivity(new Intent().setClass(DetailActivity.this, LoginActivity.class));
//                                    finish();
//                                }
                            }
                        });

                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(DetailActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(DetailActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(DetailActivity.this, "网络连接异常");
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

    private void getCollectData(int status1){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"collection.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        if("1".equals(status)) {
            requestParams.addParameter("publicviewid", uuid);
        }else if("2".equals(status)){
            requestParams.addParameter("privateviewid", uuid);
        }
        requestParams.addParameter("status", status1);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    final String msg=jsonObject.getString("message");
                    ToastUtil.showShortToast(DetailActivity.this, msg);
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        isCollection=json.getInt("isCollection");
                        collectionCount=json.getInt("collectionCount");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                collection_num.setText(collectionCount+"收藏");
                                if(isCollection==1){
                                    Iv_collection.setImageResource(R.mipmap.btn_shoucang2);
                                }else if(isCollection==0){
                                    Iv_collection.setImageResource(R.mipmap.btn_shoucang1);
                                }
                            }
                        });
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(DetailActivity.this, LoginActivity.class));
                                finish();
//                                if(!TextUtils.isEmpty(SPUtil.getToken())) {
//                                    new AlertDialog.Builder(DetailActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    startActivity(new Intent().setClass(DetailActivity.this, LoginActivity.class));
//                                                    finish();
//                                                    dialog.dismiss();
//                                                }
//                                            }).create().show();
//                                }else {
//                                    startActivity(new Intent().setClass(DetailActivity.this, LoginActivity.class));
//                                    finish();
//                                }
                            }
                        });

                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(DetailActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(DetailActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(DetailActivity.this, "网络连接异常");
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

    private void getDianZanData(int status1){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"support.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        if("1".equals(status)) {
            requestParams.addParameter("publicviewid", uuid);
        }else if("2".equals(status)){
            requestParams.addParameter("privateviewid", uuid);
        }
        requestParams.addParameter("status", status1);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    final String msg=jsonObject.getString("message");
                    ToastUtil.showShortToast(DetailActivity.this, msg);
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        supportCount=json.getInt("supportCount");
                        isSupport=json.getInt("isSupport");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Tv_num_zan.setText(supportCount+"点赞");
                                if(isSupport==1){
                                    Iv_zan.setImageResource(R.mipmap.btn_dianzan2);
                                }else if(isSupport==0){
                                    Iv_zan.setImageResource(R.mipmap.btn_dianzan1);
                                }
                            }
                        });
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(DetailActivity.this, LoginActivity.class));
                                finish();
//                                if(!TextUtils.isEmpty(SPUtil.getToken())) {
//                                    new AlertDialog.Builder(DetailActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    startActivity(new Intent().setClass(DetailActivity.this, LoginActivity.class));
//                                                    finish();
//                                                    dialog.dismiss();
//                                                }
//                                            }).create().show();
//                                }else {
//                                    startActivity(new Intent().setClass(DetailActivity.this, LoginActivity.class));
//                                    finish();
//                                }
                            }
                        });

                    }else {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() { ToastUtil.showShortToast(DetailActivity.this, msg);
//
//                            }
//                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(DetailActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(DetailActivity.this, "网络连接异常");
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
