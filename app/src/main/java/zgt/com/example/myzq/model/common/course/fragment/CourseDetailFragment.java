package zgt.com.example.myzq.model.common.course.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.BindView;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseFragment;
import zgt.com.example.myzq.bean.classes.CourseDetail;
import zgt.com.example.myzq.model.common.course.CourseDetailActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseDetailFragment extends BaseFragment {

    @BindView(R.id.Tv_title)
    TextView Tv_title;
    @BindView(R.id.Tv_price)
    TextView Tv_price;
    @BindView(R.id.Tv_teacher)
    TextView Tv_teacher;

    @BindView(R.id.webView)
    WebView webView;

    private CourseDetail courseDetail;


    public CourseDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_course_detail;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        if(courseDetail!=null) {
            Tv_title.setText(courseDetail.getTitle());
            if(courseDetail.getPrice() == 0.0){
                Tv_price.setText("免费");
            }else {
                Tv_price.setText(courseDetail.getPrice()+"元");
            }
            Tv_teacher.setText("主讲老师："+courseDetail.getLecturer());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(new JavascriptInterface(), "mainActivity");
            //替换img属性
            String varjs = "<script type='text/javascript'> \nwindow.onload = function()\n{var $img = document.getElementsByTagName('img');for(var p in  $img){$img[p].style.width = '100%'; $img[p].style.height ='auto'}}</script>";
            //点击查看
            String jsimg = "function()\n { var imgs = document.getElementsByTagName(\"img\");for(var i = 0; i < imgs.length; i++){  imgs[i].onclick = function()\n{mainActivity.startPhotoActivity(this.src);}}}";
            webView.loadDataWithBaseURL("http://118.190.105.66:12580", varjs+courseDetail.getDescription(), "text/html", "utf-8", null);

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
        courseDetail=((CourseDetailActivity)activity).getCourseDetail();
    }


}
