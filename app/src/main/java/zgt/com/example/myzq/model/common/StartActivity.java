package zgt.com.example.myzq.model.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.model.common.home.BannerUrlActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class StartActivity extends BaseActivity {

    @BindView(R.id.imageView)
    ImageView imageView;

    private String url,apptype;
    private int redirecttype;
    Runnable runnable =null;
    @Override
    public int getLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        //去掉状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

         runnable =new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };

        //延时3秒发送一个消息给主进程,让主进程执行next()方法
        handler.postDelayed(runnable, 5000);
        intoOrder();
    }

    @Override
    public void initToolBar() {

    }

    //向主进程发送消息的句柄
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            next();
        }
    };

    /**
     * 自定义方法
     * 启动登录界面
     */
    private void next() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("status",0);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.Tv_title,R.id.imageView})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Tv_title:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("status",0);
                startActivity(intent);
                handler.removeCallbacks(runnable);
                finish();
                break;
            case R.id.imageView:
                if(redirecttype == 1){
                    startActivity(new Intent().setClass(this, BannerUrlActivity.class).putExtra("url",url));
                }else if(redirecttype == 2){
                    String appId = "wx72ef58b1e2b5e1b6"; // 填应用AppId
                    IWXAPI api = WXAPIFactory.createWXAPI(this, appId);
                    WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                    req.userName = "gh_810becb0c8bc"; // 填小程序原始id
                    req.path = url;                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
                    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
                    api.sendReq(req);
                }

                break;
        }
    }

    private void intoOrder(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"openingAdvertisement.do");
        requestParams.setConnectTimeout(30 * 1000);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        JSONObject json = jsonObject.getJSONObject("data");
                        String advertPicpath = json.getString("advertPicpath");
                        url = json.getString("url");
                        apptype = json.getString("apptype");
                        redirecttype = json.getInt("redirecttype");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!TextUtils.isEmpty(advertPicpath)) {
                                    imageView.setVisibility(View.VISIBLE);
                                    Glide.with(StartActivity.this).load(advertPicpath).into(imageView);
                                }else {
                                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                                    intent.putExtra("status",0);
                                    startActivity(intent);
                                    handler.removeCallbacks(runnable);
                                    finish();
                                }
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                                intent.putExtra("status",0);
                                startActivity(intent);
                                if(handler!=null){
                                    handler.removeCallbacks(runnable);
                                }
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(StartActivity.this, getString(R.string.login_parse_exc));
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
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        intent.putExtra("status",0);
                        startActivity(intent);
                        if(handler!=null){
                            handler.removeCallbacks(runnable);
                        }
                        finish();
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

}
