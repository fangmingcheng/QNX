package zgt.com.example.myzq.model.common.home;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.homepage.Notice;
import zgt.com.example.myzq.utils.StatusBarUtil;

public class NoticeDetailActivity extends BaseActivity {
    @BindView(R.id.Tv_title_class)
    TextView Tv_title_class;
    @BindView(R.id.Tv_time)
    TextView Tv_time;
    @BindView(R.id.Tv_author)
    TextView Tv_author;
    @BindView(R.id.Tv_title)
    TextView Tv_title;
    @BindView(R.id.webView)
    WebView webView;
    private Notice notice;
    @Override
    public void initToolBar() {

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
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.statusBarLightMode(this);
        notice=(Notice) getIntent().getSerializableExtra("notice");
        Tv_title_class.setText(notice.getTitle());
        Tv_author.setText(notice.getAuthor());
        Tv_time.setText(notice.getCreatetime());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavascriptInterface(), "NoticeDetailActivity");
        //替换img属性
        String varjs = "<script type='text/javascript'> \nwindow.onload = function()\n{var $img = document.getElementsByTagName('img');for(var p in  $img){$img[p].style.width = '100%'; $img[p].style.height ='auto'}}</script>";
        //点击查看
        String jsimg = "function()\n { var imgs = document.getElementsByTagName(\"img\");for(var i = 0; i < imgs.length; i++){  imgs[i].onclick = function()\n{NoticeDetailActivity.startPhotoActivity(this.src);}}}";
        webView.loadDataWithBaseURL("http://stock.bdcgw.cn", varjs+notice.getContent(), "text/html", "utf-8", null);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String s) {
                webView.loadUrl("javascript:(" + jsimg + ")()");
            }
        });
    }

    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void startPhotoActivity(String imageUrl) {
            //根据URL查看大图逻辑

        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_notice_detail;
    }

}
