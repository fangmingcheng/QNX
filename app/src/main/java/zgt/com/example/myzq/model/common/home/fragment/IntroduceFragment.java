package zgt.com.example.myzq.model.common.home.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.BindView;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseFragment;
import zgt.com.example.myzq.bean.Courseware;
import zgt.com.example.myzq.model.common.home.CoursewareDetailActivity;

/**

 * A simple {@link Fragment} subclass.
 */
public class IntroduceFragment extends BaseFragment {
    @BindView(R.id.Tv_title)
    TextView Tv_title;
    @BindView(R.id.webView)
    WebView webView;

    private Courseware courseware;

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        if(courseware!=null) {
            Tv_title.setText(courseware.getTitle());

            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(new JavascriptInterface(), "mainActivity");
            //替换img属性
            String varjs = "<script type='text/javascript'> \nwindow.onload = function()\n{var $img = document.getElementsByTagName('img');for(var p in  $img){$img[p].style.width = '100%'; $img[p].style.height ='auto'}}</script>";
            //点击查看
            String jsimg = "function()\n { var imgs = document.getElementsByTagName(\"img\");for(var i = 0; i < imgs.length; i++){  imgs[i].onclick = function()\n{mainActivity.startPhotoActivity(this.src);}}}";
            webView.loadDataWithBaseURL("http://stock.bdcgw.cn", varjs+courseware.getDescription(), "text/html", "utf-8", null);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView webView, String s) {
                    webView.loadUrl("javascript:(" + jsimg + ")()");
                }
            });
        }
    }

    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void startPhotoActivity(String imageUrl) {
            //根据URL查看大图逻辑

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        courseware=((CoursewareDetailActivity)activity).getCourseware();

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_introduce;
    }

    public IntroduceFragment() {
        // Required empty public constructor
    }



}
