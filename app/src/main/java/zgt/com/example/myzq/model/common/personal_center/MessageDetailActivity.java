package zgt.com.example.myzq.model.common.personal_center;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
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
import zgt.com.example.myzq.bean.MessageList;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class MessageDetailActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.Tv_title_class)
    TextView Tv_title_class;

    @BindView(R.id.Tv_author)
    TextView Tv_author;
    @BindView(R.id.Tv_time)
    TextView Tv_time;

    @BindView(R.id.Ll_title)
    LinearLayout Ll_title;
    private MessageList messageList;
    private String uuid;

    @Override
    public void initViews(Bundle savedInstanceState) {
//        StatusBarUtil.statusBarLightMode(this);
        uuid=getIntent().getStringExtra("uuid");
//        messageList=(MessageList) getIntent().getSerializableExtra("messageList");
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        getData();
    }

    private void setData(){
        if(messageList!=null){
            if(TextUtils.isEmpty(messageList.getUrl())){
                Tv_title_class.setText(messageList.getMsgtitle());
                Tv_time.setText(messageList.getCreatetime());
                if(TextUtils.isEmpty(messageList.getMsgcontent())){
                    ToastUtil.showShortToast(MessageDetailActivity.this,"该消息详细为空");
                }else {
                    webView.addJavascriptInterface(new JavascriptInterface(), "MessageDetailActivity");
                    //替换img属性
                    String varjs = "<script type='text/javascript'> \nwindow.onload = function()\n{var $img = document.getElementsByTagName('img');for(var p in  $img){$img[p].style.width = '100%'; $img[p].style.height ='auto'}}</script>";
//                    String title=" <head>\n" +
//                            "<style type=\"text/css\">\n" +
//                            "body {font-size:15px;}\n" +
//                            "</style>\n" +
//                            "</head>\n";
                    //点击查看
                    String jsimg = "function()\n { var imgs = document.getElementsByTagName(\"img\");for(var i = 0; i < imgs.length; i++){  imgs[i].onclick = function()\n{MessageDetailActivity.startPhotoActivity(this.src);}}}";
                    webView.loadDataWithBaseURL("http://stock.bdcgw.cn", varjs+messageList.getMsgcontent(), "text/html", "utf-8", null);
                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView webView, String s) {
                            webView.loadUrl("javascript:(" + jsimg + ")()");
                        }
                    });
                }
            }else {
                Ll_title.setVisibility(View.GONE);
                //加载需要显示的网页
                webView.loadUrl(messageList.getUrl());
                //字体不随系统变化
                webView.getSettings().setTextZoom(100);
                //设置Web视图
                webView.setWebViewClient(new webViewClient());
            }
        }
    }

    private void getData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"viewSysMsg.do");
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
                        messageList=new MessageList();
                        JSONObject json=jsonObject.getJSONObject("data");
                        messageList.setUrl(json.getString("url"));
                        messageList.setCreatetime(json.getString("createtime"));
                        messageList.setMsgcontent(json.getString("msgcontent"));
                        messageList.setUuid(uuid);
                        messageList.setMsgtitle(json.getString("msgtitle"));
                        setData();
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(MessageDetailActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
                    }else {
                        final String msg=jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(MessageDetailActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(MessageDetailActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(MessageDetailActivity.this, "网络连接异常");
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
    public void initToolBar() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_message_detail;
    }

    @OnClick({R.id.Iv_customer})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_customer:
                finish();
                break;
        }
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void startPhotoActivity(String imageUrl) {
            //根据URL查看大图逻辑
        }
    }

}
