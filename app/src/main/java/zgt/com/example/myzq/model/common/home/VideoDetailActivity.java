package zgt.com.example.myzq.model.common.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.Video;
import zgt.com.example.myzq.model.common.custom_view.MyJzvdStd;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;


public class VideoDetailActivity extends BaseActivity {

    @BindView(R.id.jz_video)
    MyJzvdStd jz_video;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.Tv_title)
    TextView Tv_title;

    private Video video;
    private String uuid;
    private Uri uri;

    @Override
    public void initToolBar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        jz_video.goOnPlayOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jz_video.goOnPlayOnPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        jz_video.clearFocus();
        jz_video.onStateAutoComplete();
        if(video!=null) {
            jz_video.clearSavedProgress(this, SPUtil.getServerAddress().substring(0, (SPUtil.getServerAddress().length() - 5)) + video.getVideourl());
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_detail;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        uuid=getIntent().getStringExtra("uuid");
        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }).start();
    }

    private void getData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"TeacherVideo.do");
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
                        video=new Video();
                        video.setPicpath(json.getString("picpath"));
//                        video.setDuties(json.getString("duties"));
                        video.setClick(json.getInt("click"));
                        video.setAddtime(json.getString("addtime"));
                        video.setDescription(json.getString("description"));
                        video.setHeadimg(json.getString("headimg"));
                        video.setIscharge(json.getInt("ischarge"));
                        video.setPrice(json.getDouble("price"));
                        video.setStatus(json.getString("status"));
                        video.setTeachername(json.getString("truename"));
                        video.setTitle(json.getString("title"));
//                        video.setUuid(json.getString("uuid"));
                        video.setVideourl(json.getString("videourl"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(video!=null) {
                                    setmVideoView();
                                }
                            }
                        });
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(VideoDetailActivity.this, LoginActivity.class));
                                finish();
//                                if(!TextUtils.isEmpty(SPUtil.getToken())) {
//                                    new AlertDialog.Builder(VideoDetailActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    startActivity(new Intent().setClass(VideoDetailActivity.this, LoginActivity.class));
//                                                    finish();
//                                                    dialog.dismiss();
//                                                }
//                                            }).create().show();
//                                }else {
//                                    startActivity(new Intent().setClass(VideoDetailActivity.this, LoginActivity.class));
//                                    finish();
//                                }
                            }
                        });

                    }else {
                        final String msg=jsonObject.getString("message");
                      runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(VideoDetailActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(VideoDetailActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(VideoDetailActivity.this, "网络连接异常");
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
    private void setmVideoView(){
        Tv_title.setText(video.getTitle());
//        Tv_content.setText(NoHTML(video.getDescription()));

//        String a="<p style=\\\"color:#444444;font-size:14px;font-family:sans-serif;text-indent:32px;\\\">\\r\\n\\t<span style=\\\"font-family:宋体;font-size:18px;\\\">原始股因具有上市后翻倍增长的财富效应而备受投资者追捧。然而，对于大多数人来说，原始股是那么的可望而不可即。在这种矛盾之下，围绕原始股的各种骗局频频出现。如今，在<span>IPO<\\/span>扩容加速，尤其是创业板、中小板新股纷至沓来的情况下，原始股诈骗卷土重来，且手段更新、花样百出。<\\/span> \\r\\n<\\/p>\\r\\n<p style=\\\"color:#444444;font-size:14px;font-family:sans-serif;text-indent:32px;\\\">\\r\\n\\t<strong><span style=\\\"font-family:宋体;\\\"><span style=\\\"font-size:18px;\\\">伎俩一：以货币充当原始股<span><\\/span><\\/span><\\/span><\\/strong> \\r\\n<\\/p>\\r\\n<p style=\\\"color:#444444;font-size:14px;font-family:sans-serif;text-indent:32px;\\\">\\r\\n\\t<span style=\\\"font-family:宋体;font-size:18px;\\\">美元、加元、日元等外币估计不少投资者都曾见过，但印度卢比、南非币、秘鲁币<span>……<\\/span>这些流通较为稀有的货币则认识者寥寥。有些行骗者利用这个漏洞，以稀有货币充当境外原始股，骗取钱财。<\\/span> \\r\\n<\\/p>\\r\\n<p style=\\\"color:#444444;font-size:14px;font-family:sans-serif;text-indent:32px;\\\">\\r\\n\\t<span style=\\\"font-family:宋体;font-size:18px;\\\">近期，一起以秘鲁币充当港股原始股的行骗案件在浙江告破。据悉，<span>4<\\/span>名犯罪嫌疑人短短一个月，以同样手段合谋骗得了<span>50<\\/span>多万元。报案人何女士在陈述被骗过程时表示，开始，两名犯罪嫌疑人以结交朋友为名请她吃饭。席间，其中一人称另一人是香港老板，手里有原始股，他们这次碰面就是为了交易。随后，<span>“<\\/span>香港老板<span>”<\\/span>将一摞看起来很正规的票子拿出来，而另一人立刻拿起手机，给冒充收购大户的另一犯罪嫌疑人打起了电话，几句话下来，貌似其从中轻松赚取了<span>4000<\\/span>元差价。陈女士也心动了，拿出<span>10<\\/span>万元买了<span>2<\\/span>万股。然而，当她去收购大户那里出手时，才发现人去楼空，联系方式中断，后经验证，这些<span>“<\\/span>原始股<span>”<\\/span>为兑换价值很低的秘鲁币。<\\/span> \\r\\n<\\/p>\\r\\n<p style=\\\"color:#444444;font-size:14px;font-family:sans-serif;text-indent:32px;\\\">\\r\\n\\t<strong><span style=\\\"font-family:宋体;\\\"><span style=\\\"font-size:18px;\\\">伎俩二：以私募名义大肆撒网<span><\\/span><\\/span><\\/span><\\/strong> \\r\\n<\\/p>\\r\\n<p style=\\\"color:#444444;font-size:14px;font-family:sans-serif;text-indent:32px;\\\">\\r\\n\\t<span style=\\\"font-family:宋体;font-size:18px;\\\">近两年来，私募因业绩明显好于公募而逐渐取得了投资者的信任，更有投资者认为，私募<span>“<\\/span>野<span>”<\\/span>路子或内幕消息较多，投资私募更有<span>“<\\/span>钱途<span>”<\\/span>。因此，有些行骗者索性垫资成立一家<span>“<\\/span>私募<span>”<\\/span>，然后购买股民的联系信息，广布销售网络。<\\/span> \\r\\n<\\/p>\\r\\n<p style=\\\"color:#444444;font-size:14px;font-family:sans-serif;text-indent:32px;\\\">\\r\\n\\t<span style=\\\"font-family:宋体;font-size:18px;\\\">日前，投资者姚先生讲述了自己的被骗经历。今年<span>5<\\/span>月，他接到一个自称是深圳一家私募性质的投资公司打来的电话，说有即将登陆创业板的一家北京公司原始股出售，问他是否有兴趣，并声称公司将在<span>7<\\/span>月底前上市。原本只是好奇地随便聊聊，结果在询问了一些原始股的来源、购买方式等问题之后，对方听起来天衣无缝的回答让他有些心动。而当对方把自己公司和原始股公司的网页发给他，尤其是在他按上面的联系方式致电了北京的这家公司得知上市正在进行中之后，他开始相信真的是<span>“<\\/span>馅饼<span>”<\\/span>掉到了自己的脑袋上。随后，姚先生毫不犹豫地分别以<span>4.8<\\/span>元和<span>6.8<\\/span>元的价格合计买入了<span>4.5<\\/span>万股原始股，共汇出<span>26<\\/span>万多元。<\\/span> \\r\\n<\\/p>\\r\\n<p style=\\\"color:#444444;font-size:14px;font-family:sans-serif;text-indent:32px;\\\">\\r\\n\\t<span style=\\\"font-family:宋体;font-size:18px;\\\">随着公司上市的日期越来越近，姚先生再次致电公司了解进展，结果发现对方已经停机。<img src=\\\"/attached/image/20190716/20190716170936_792.jpg\\\" alt=\\\"\\\" /><\\/span> \\r\\n<\\/p>\\r\\n<p style=\\\"color:#444444;font-size:14px;font-family:sans-serif;text-indent:32px;\\\">\\r\\n\\t<strong><span style=\\\"font-family:宋体;\\\"><span style=\\\"font-size:18px;\\\">伎俩三：明着澄清暗里诱人<span><\\/span><\\/span><\\/span><\\/strong> \\r\\n<\\/p>\\r\\n<p style=\\\"color:#444444;font-size:14px;font-family:sans-serif;text-indent:32px;\\\">\\r\\n\\t<span style=\\\"font-family:宋体;font-size:18px;\\\">在<span>“<\\/span>老式<span>”<\\/span>原始股骗局成为过街老鼠并远遁江湖后，骗子们又想出了新方法。单纯设立网站营销已经骗不了人，但是加上煞有介事的澄清声明，作用就不一般了。<\\/span> \\r\\n<\\/p>\\r\\n<p style=\\\"color:#444444;font-size:14px;font-family:sans-serif;text-indent:32px;\\\">\\r\\n\\t<span style=\\\"font-family:宋体;font-size:18px;\\\">今年<span>5<\\/span>月，投资者向女士在一股票论坛中看到一则转让原始股的信息，好奇的她随手点下了下面的链接，首先映入眼帘的就是很大幅的《郑重声明》，称<span>“<\\/span>公司上市工作正在有条不紊地进行，近日发现有机构冒用本公司名义进行股份转让，目前公司只转让第三大股东蒋女士的股份，除此之外没有转让其他任何股东股份<span>……”<\\/span>这引起了向女士进一步了解这家公司的兴趣。在看过网页之后，她致电公司询问原始股如何购买，原本也只是简单的询问，但当公司说蒋女士的股份已转让完毕后，向女士认定是自己错过了机会，感到很失望。于是，在公司让她留下电话说如果有其他股东转让再通知她时，她毫不犹豫地答应了。<span>4<\\/span>天后，公司打来电话说有新股东出售原始股，但股价稍高，且数量不多，赚钱心切的向女士最终花费<span>16<\\/span>万元买进了<span>2<\\/span>万股。<\\/span> \\r\\n<\\/p>\\r\\n<p style=\\\"color:#444444;font-size:14px;font-family:sans-serif;text-indent:32px;\\\">\\r\\n\\t<span style=\\\"font-family:宋体;font-size:18px;\\\">过了一段时期，向女士发现公司网页已无法打开，原来的电话却没存下；<span>3<\\/span>个月过去了，网站一直没有打开，她不得不相信自己被骗了。<\\/span> \\r\\n<\\/p>\\r\\n<p style=\\\"color:#444444;font-size:14px;font-family:sans-serif;text-indent:32px;\\\">\\r\\n\\t<span style=\\\"font-size:18px;\\\"><span style=\\\"font-family:宋体;font-size:14px;\\\"><span style=\\\"font-size:18px;\\\">提醒：时下，新股市场火爆和原始股的不菲收益，让投资者趋之若鹜，这给了行骗者可乘之机。投资者千万不要相信所谓的暴富神话，同时还应加强相关证券法律法规的学习，借此提高甄别能力。<\\/span><\\/span><span style=\\\"font-family:\\\" font-size:14px;\\\"=\\\"\\\"><span>1998<\\/span><\\/span><span style=\\\"font-family:宋体;font-size:14px;\\\"><span style=\\\"font-size:18px;\\\">年国务院颁布的《非法金融机构和非法金融业务活动取缔办法》规定，<\\/span><\\/span><span style=\\\"font-family:\\\" font-size:14px;\\\"=\\\"\\\"><span>“<\\/span><\\/span><span style=\\\"font-family:宋体;font-size:14px;\\\"><span style=\\\"font-size:18px;\\\">因参与非法金融业务活动受到的损失，由参与者自行承担<\\/span><\\/span><span style=\\\"font-family:\\\" font-size:14px;\\\"=\\\"\\\"><span>”<\\/span><\\/span><span style=\\\"font-family:宋体;font-size:14px;\\\"><span style=\\\"font-size:18px;\\\">。因此，投资者千万不要抱有侥幸心理，买卖证券应到依法设立的证券交易所进行。<\\/span><\\/span><\\/span> \\r\\n<\\/p>\",\"summary\":\"原始股因具有上市后翻倍增长的财富效应而备受投资者追捧。然而，对于大多数人来说，原始股是那么的可望而不可即。在这种矛盾之下，围绕原始股的各种骗局频频出现";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavascriptInterface(), "VideoDetailActivity");
        //替换img属性
        String varjs = "<script type='text/javascript'> \nwindow.onload = function()\n{var $img = document.getElementsByTagName('img');for(var p in  $img){$img[p].style.width = '100%'; $img[p].style.height ='auto'}}</script>";
        //点击查看
        String jsimg = "function()\n { var imgs = document.getElementsByTagName(\"img\");for(var i = 0; i < imgs.length; i++){  imgs[i].onclick = function()\n{VideoDetailActivity.startPhotoActivity(this.src);}}}";
        webView.loadDataWithBaseURL("http://stock.bdcgw.cn", varjs+video.getDescription(), "text/html", "utf-8", null);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String s) {
                webView.loadUrl("javascript:(" + jsimg + ")()");
            }
        });
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setHorizontalScrollBarEnabled(false);//水平不显示
//        webView.setVerticalScrollBarEnabled(false); //垂直不显示
//        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);//硬件加
////        web.loadDataWithBaseURL("", "html", "text/html", "utf-8", "");
//        DisplayMetrics dm = this.getResources().getDisplayMetrics();
//        float density = dm.density;
//        String css_style = "<head><style>img{max-width:"+density+" !important;}</style></head>";
//        webView.loadDataWithBaseURL("http://stock.bdcgw.cn", css_style +video.getDescription(), "text/html", "utf-8", "");
        jz_video.setUp(SPUtil.getServerAddress().substring(0,(SPUtil.getServerAddress().length()-5))+video.getVideourl(), "");
        Glide.with(this).load(SPUtil.getServerAddress().substring(0,(SPUtil.getServerAddress().length()-5))+video.getPicpath()).into(jz_video.thumbImageView);
    }

    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void startPhotoActivity(String imageUrl) {
            //根据URL查看大图逻辑

        }
    }

}
