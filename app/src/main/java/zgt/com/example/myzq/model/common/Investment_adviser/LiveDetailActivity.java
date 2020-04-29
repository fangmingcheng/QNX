package zgt.com.example.myzq.model.common.Investment_adviser;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.utils.SPUtil;

/**
 * 公共直播
 */

public class LiveDetailActivity extends BaseActivity {
    @BindView(R.id.Web_H5)
    WebView webView;
    private String uuid;
    private String status;
    @Override
    public void initToolBar() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_live_detail;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        uuid=getIntent().getStringExtra("uuid");
        status=getIntent().getStringExtra("status");

        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        if("1".equals(status)) {
//            webView.loadUrl("http://stock.zgziben.com//liveroom.do?token=" + SPUtil.getToken() + "&publicLiveid=" + uuid);
            webView.loadUrl("http://stock.zgziben.com//zgliveroom.do?token=" + SPUtil.getToken() + "&publicLiveid=" + uuid);
        }else if("2".equals(status)){
            webView.loadUrl("http://stock.zgziben.com//privateliveLiveroom.do?token=" + SPUtil.getToken() + "&privateliveid=" + uuid);
        }
        //字体不随系统变化
        webView.getSettings().setTextZoom(100);
        //设置Web视图
        webView.setWebViewClient(new webViewClient());
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (webView != null) {
            webView.stopLoading();
//            webView.setWebViewListener(null);
//            webView.clearHistory();
            webView.clearCache(true);
            webView.loadUrl("about:blank");
//            webView.pauseTimers();
//            webView = null;
        }
    }

    @OnClick({R.id.Iv_customer})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_customer:
                onBackPressed();
                break;
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // TODO Auto-generated method stub
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (webView.canGoBack()) {
//                webView.goBack();//返回上一页面
//                back true;
//            } else {
//                System.exit(0);//退出程序
//            }
//        }
//        back super.onKeyDown(keyCode, event);
//    }

}
