package zgt.com.example.myzq.model.common.home;

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

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.TrainingDetail;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class TrainingBaseDetailActivity extends BaseActivity {

    @BindView(R.id.Tv_title_class)
    TextView Tv_title_class;
    @BindView(R.id.Tv_time)
    TextView Tv_time;

    @BindView(R.id.Tv_title)
    TextView Tv_title;

    @BindView(R.id.webView)
    WebView webView;


//    @BindView(R.id.Iv_picture)
//    MyImageBackgroundView Iv_picture;

    private String uuid;
    private TrainingDetail trainingDetail;
    private String status;
    @Override
    public void initToolBar() {

    }
    @Override
    public void initViews(Bundle savedInstanceState) {
        status=getIntent().getStringExtra("status");
        uuid=getIntent().getStringExtra("uuid");

//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setHorizontalScrollBarEnabled(false);//水平不显示
//        webView.setVerticalScrollBarEnabled(false); //垂直不显示
//        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);//硬件加

        if(status!=null){
            if("1".equals(status)){
                Tv_title.setText("详情");
            }else if("2".equals(status)){
                Tv_title.setText("证券课程");
            }else if("3".equals(status)){
                Tv_title.setText("投资者教育");
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }).start();
    }

    @OnClick({R.id.Iv_customer})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_customer:
                finish();
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_training_base_detail;
    }

    private void getData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"TrainingBasesNews.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("uuid", uuid);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        trainingDetail=new TrainingDetail();
                        trainingDetail.setVideopath(json.getString("videopath"));
                        trainingDetail.setVideoflag(json.getInt("videoflag"));
//                        trainingDetail.setUuid(json.getString("uuid"));
                        trainingDetail.setTitle(json.getString("title"));
                        trainingDetail.setSummary(json.getString("summary"));
                        trainingDetail.setAuthor(json.getString("author"));
                        trainingDetail.setClick(json.getInt("click"));
                        trainingDetail.setContent(json.getString("content"));
                        trainingDetail.setCreatetime(json.getString("createtime"));
                        trainingDetail.setFtitle(json.getString("ftitle"));
//                        trainingDetail.setNclassid(json.getString("nclassid"));
                        trainingDetail.setPicpath(json.getString("picpath"));
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
                                startActivity(new Intent().setClass(TrainingBaseDetailActivity.this,LoginActivity.class));
                                finish();
//                                if(TextUtils.isEmpty(SPUtil.getToken())){
//                                    startActivity(new Intent().setClass(TrainingBaseDetailActivity.this,LoginActivity.class));
//                                    finish();
//                                }else {
//                                    new AlertDialog.Builder(TrainingBaseDetailActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    startActivity(new Intent().setClass(TrainingBaseDetailActivity.this,LoginActivity.class));
//                                                    finish();
//                                                    dialog.dismiss();
//                                                }
//                                            }).create().show();
//                                }
                            }
                        });
                    }else {
                        final String msg=jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(TrainingBaseDetailActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(TrainingBaseDetailActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(TrainingBaseDetailActivity.this, "网络连接异常");
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

    private void setContent(){
        if(trainingDetail!=null){
            Tv_title_class.setText(trainingDetail.getTitle());
            Tv_time.setText(trainingDetail.getCreatetime());
//            if(TextUtils.isEmpty(trainingDetail.getPicpath())){
//                Iv_picture.setVisibility(View.GONE);
//            }else {
//                Iv_picture.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+trainingDetail.getPicpath());
//            }

            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(new JavascriptInterface(), "TrainingBaseDetailActivity");
            //替换img属性
            String varjs = "<script type='text/javascript'> \nwindow.onload = function()\n{var $img = document.getElementsByTagName('img');for(var p in  $img){$img[p].style.width = '100%'; $img[p].style.height ='auto'}}</script>";
            //点击查看
            String jsimg = "function()\n { var imgs = document.getElementsByTagName(\"img\");for(var i = 0; i < imgs.length; i++){  imgs[i].onclick = function()\n{TrainingBaseDetailActivity.startPhotoActivity(this.src);}}}";
            webView.loadDataWithBaseURL("http://stock.bdcgw.cn", varjs+trainingDetail.getContent(), "text/html", "utf-8", null);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView webView, String s) {
                    webView.loadUrl("javascript:(" + jsimg + ")()");
                }
            });
//
        }

    }

    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void startPhotoActivity(String imageUrl) {
            //根据URL查看大图逻辑

        }
    }

}
