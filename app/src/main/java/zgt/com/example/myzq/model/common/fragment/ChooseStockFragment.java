package zgt.com.example.myzq.model.common.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.transformers.Transformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseFragment;
import zgt.com.example.myzq.bean.homepage.Advert;
import zgt.com.example.myzq.bean.stock.HotStock;
import zgt.com.example.myzq.bean.stock.StockProduct;
import zgt.com.example.myzq.model.common.MainActivity;
import zgt.com.example.myzq.model.common.adapter.stock.StockChooseAdapter;
import zgt.com.example.myzq.model.common.adapter.stock.StockChooseTitleAdapter;
import zgt.com.example.myzq.model.common.home.BannerUrlActivity;
import zgt.com.example.myzq.model.common.home.h5.H5Activity;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.SpaceItemDecoration;
import zgt.com.example.myzq.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseStockFragment extends BaseFragment {

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @BindView(R.id.recyclerview_title)
    RecyclerView recyclerview_title;

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.webView1)
    WebView webView1;

    @BindView(R.id.Rl_title)
    RelativeLayout Rl_title;

    @BindView(R.id.Ll_content)
    LinearLayout Ll_content;

    @BindView(R.id.Tv_intro)
    TextView Tv_intro;

    @BindView(R.id.banner)
    XBanner banner;


    private List<StockProduct> stockProductList = new ArrayList<>();
    private StockProduct stockProduct;
    private List<HotStock> hotStockList = new ArrayList<>();
    private List<HotStock> hotStockList1 = new ArrayList<>();
    private HotStock hotStock;

    private StockChooseTitleAdapter adapter;
    private StockChooseAdapter stockChooseAdapter;
    private GridLayoutManager gLayoutManager;
    private LinearLayoutManager layoutManager;
    private MainActivity mainActivity;

    private List<Advert> advertList = new ArrayList<>();
    private Advert advert;


    private String pid,name,intro;

    private int index=-1;


    @Override
    public void initToolBar() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
        }else {
            StatusBarUtil.setLightMode(getActivity());//黑色
            getData(refreshLayout);
        }

    }

    /**
     * 获取通知栏高度
     * @param context 上下文
     * @return 通知栏高度
     */
    private int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_choose_stock;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

        LinearLayout.LayoutParams params=(LinearLayout.LayoutParams) Rl_title.getLayoutParams();
        params.topMargin=getStatusBarHeight(getActivity());

        Rl_title.setLayoutParams(params);

        WindowManager wm1 = getActivity().getWindowManager();
        int width = wm1.getDefaultDisplay().getWidth();

        LinearLayout.LayoutParams params1=(LinearLayout.LayoutParams) banner.getLayoutParams();
        params1.width= width-60;
        params1.height= (width-60)*96/331;
        banner.setLayoutParams(params1);

        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);

        webSettings.setDomStorageEnabled(true);

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置页面自适应手机

        //解决h5网页打开白页
        webSettings.setUseWideViewPort(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setLoadWithOverviewMode(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        webView.loadUrl("https://gqdo5m8xy.lightyy.com/index.html?p=hsjy_1189&h=0&tg=_blank&pid=3#/quoteAnalysisIndex");
        //字体不随系统变化
//        webView.getSettings().setTextZoom(100);
        //设置Web视图
//        webView.setWebViewClient(new webViewClient());
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub

                if("https://gqdo5m8xy.lightyy.com/index.html?p=hsjy_1189&h=0&tg=_blank&pid=3#/quoteAnalysisIndex".equals(url)){
                    view.loadUrl(url);
                }else {
                    startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url", url));
                }

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
//                    pro.setVisibility(View.GONE);
                super.onPageFinished(view, url);

            }
        });

        initWebView();
        setPullRefresher();

        initRecyclerview_title();
        initRecyclerview();

    }




    private void  initWebView(){
        WebSettings webSettings = webView1.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);

        webSettings.setDomStorageEnabled(true);

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置页面自适应手机

        //解决h5网页打开白页
        webSettings.setUseWideViewPort(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setLoadWithOverviewMode(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        webView1.loadUrl("https://gqdo5m8xy.lightyy.com/index.html?p=hsjy_1189&h=0&tg=_blank&pid=4#/quoteAnalysisIndex");
        //字体不随系统变化
//        webView.getSettings().setTextZoom(100);
        //设置Web视图
//        webView.setWebViewClient(new webViewClient());
        webView1.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub

                if("https://gqdo5m8xy.lightyy.com/index.html?p=hsjy_1189&h=0&tg=_blank&pid=4#/quoteAnalysisIndex".equals(url)){
                    view.loadUrl(url);
                }else {
                    startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url", url));
                }

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
//                    pro.setVisibility(View.GONE);
                super.onPageFinished(view, url);

            }
        });
    }

    private void setPullRefresher(){
        //设置 Header 为 Material风格
        refreshLayout.setPrimaryColors(Color.parseColor("#00000000"));
        refreshLayout.setRefreshHeader(new MaterialHeader(getActivity()).setShowBezierWave(true));
//        refreshLayout.setRefreshHeader(BezierRadarHeader(this).setEnableHorizontalDrag(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //在这里执行上拉刷新时的具体操作(网络请求、更新UI等)

                getData(refreshlayout);
                if(webView!=null){
                    webView.reload();
                }
                if(webView1!=null){
                    webView1.reload();
                }
//                adapter.refresh(newList);
                refreshlayout.finishRefresh(2000/*,false*/);
                //不传时间则立即停止刷新    传入false表示刷新失败
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore( RefreshLayout refreshLayout) {
//                ToastUtil.showShortToast(getActivity(),"数据已加载完毕");
                refreshLayout.finishLoadMore(/*,false*/);
            }
        });
//
//        refreshLayout.setEnableLoadmore(false);//屏蔽掉上拉加载的效果
    }

    private void initRecyclerview_title(){
        layoutManager = new LinearLayoutManager(getActivity());
        //调整RecyclerView的排列方向
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview_title.setLayoutManager(layoutManager);
        //设置item间距，30dp
//        recyclerview_title.addItemDecoration(new SpaceItemDecoration(6));
        adapter=new StockChooseTitleAdapter(getActivity(),stockProductList);
        recyclerview_title.setAdapter(adapter);
        recyclerview_title.setNestedScrollingEnabled(false);//NestedScrollView嵌套RecyclerView卡顿解决办法：

        adapter.setOnItemClickListener(new StockChooseTitleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview_title.getChildAdapterPosition(view);
                index = position;
                adapter.setCurrent(position);
                hotStockList1.clear();
                hotStockList1.addAll(stockProductList.get(position).getList());
                stockChooseAdapter.notifyDataSetChanged();
                Tv_intro.setText("简介："+stockProductList.get(position).getIntro());
//                startActivity(new Intent().setClass(getActivity(), CourseDetailActivity.class).putExtra("uuid",recommendedtodays.get(position).getUuid()).putExtra("index",1));
            }
        });

    }

    private void initRecyclerview(){
        gLayoutManager = new GridLayoutManager(getActivity(),3);
        //调整RecyclerView的排列方向
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(gLayoutManager);
        //设置item间距，30dp
        recyclerview.addItemDecoration(new SpaceItemDecoration(10));
        stockChooseAdapter=new StockChooseAdapter(getActivity(),hotStockList1);
        recyclerview.setAdapter(stockChooseAdapter);
        recyclerview.setNestedScrollingEnabled(false);//NestedScrollView嵌套RecyclerView卡顿解决办法：

        stockChooseAdapter.setOnItemClickListener(new StockChooseAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview.getChildAdapterPosition(view);
                //http://192.168.2.147:8080    http://ze3oy5f3q.lightyy.com
                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url","https://ze3oy5f3q.lightyy.com/index.html#/quote?code="+hotStockList1.get(position).getStockcode()+hotStockList1.get(position).getStockexchange()));

            }
        });

    }

    public ChooseStockFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    @OnClick({R.id.Ll_zhengu,R.id.Ll_jinglin,R.id.Ll_xuangu,R.id.Ll_guanzhu,R.id.Ll_fupang,R.id.Bt_stock})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.Ll_zhengu://AI诊股
               clickZD();
                break;
            case R.id.Ll_jinglin://条件选股
                if(!TextUtils.isEmpty(SPUtil.getToken())){
                   checkToken("9","9");
                }else {
                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                    finish();
                }
//                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url", "http://www.mytv365.com/ceshi/button.html"));
//                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url", "https://je7o2az1x.lightyy.com/index.html?h=0&p=hsjy_1189&gmore=shortGhostNews&tg=_blank#/dataStockNews"));
                break;
            case R.id.Ll_xuangu://牵牛股池
                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url", SPUtil.getServerAddress()+"gc.do?token="+SPUtil.getToken()+"&type=0"));
                break;
            case R.id.Ll_guanzhu://数据选股
                if(!TextUtils.isEmpty(SPUtil.getToken())){
                    checkToken("6","6");
                }else {
                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                    finish();
                }
//                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url", "https://je7o2az1x.lightyy.com/index.html?h=0&p=hsjy_1189&gmore=headNews&tg=_blank#/dataStockNews"));
                break;
            case R.id.Ll_fupang://牵牛复盘
                if(!TextUtils.isEmpty(SPUtil.getToken())){
                    checkToken("4","4");
                }else {
                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                    finish();
                }
//                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url", "https://gqdo5m8xy.lightyy.com/index.html?p=hsjy_1189&h=0&tg=_blank"));
                break;
            case R.id.Bt_stock://查看股票
                if(!TextUtils.isEmpty(SPUtil.getToken())){
                    if(index>=0){
                        startActivity(new Intent().setClass(getActivity(), H5Activity.class)
                                .putExtra("url",SPUtil.getServerAddress()+"productguide.do?token="+SPUtil.getToken()+"&pid="+pid+"&uid="+stockProductList.get(index).getUuid()+"&type=0"));
                    }
                }else {
                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                    finish();
                }
//                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url","https://me6oxi61y.lightyy.com/secudiagnosis_plus.html?s=600570&p=hsjy_1189").putExtra("title","智能选股"));
                break;

        }
    }

    private void setBanner(){
        banner.removeAllViews();
        banner.setData(advertList, null);
        // XBanner适配数据
        banner.setmAdapter(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, View view, int position) {
                Glide.with(getActivity()).load(advertList.get(position).getPicpath()).into((ImageView) view);
            }
        });
//        banner.setPointsIsVisible(true);
        // 设置XBanner的页面切换特效
        banner.setPageTransformer(Transformer.Default);
        // 设置XBanner页面切换的时间，即动画时长
        banner.setPageChangeDuration(1000);

        // XBanner中某一项的点击事件
        banner.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, int position) {
                if(TextUtils.isEmpty(advertList.get(position).getUrl())){
//                    String appId = "wx1b3c5979789ba911"; // 填应用AppId
//                    IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);
//
//                    WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
//                    req.userName = "gh_810becb0c8bc"; // 填小程序原始id
//                    req.path = "pages/service/service";                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
//                    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
//                    api.sendReq(req);
                }else {
                    startActivity(new Intent().setClass(getActivity(), BannerUrlActivity.class).putExtra("url",advertList.get(position).getUrl()));
                }
//                startActivity(new Intent().setClass(getActivity(), BannerUrlActivity.class).putExtra("url",bannerList.get(position).getUrl()));
//                Toast.makeText(getActivity(), "点击了第" + (position + 1) + "张图片", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData(RefreshLayout refreshlayout){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"stockSelection.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        stockProductList.clear();
                        hotStockList.clear();
                        advertList.clear();
                        JSONObject json = jsonObject.getJSONObject("data");
                        pid = json.getString("pid");
                        name = json.getString("name");
                        intro = json.getString("intro");
                        JSONArray advertArray=json.getJSONArray("advertList");
                        for (int i=0;i<advertArray.length();i++){
                            advert=new Advert();
                            advert.setPicpath(advertArray.getJSONObject(i).getString("picpath"));
                            advert.setUrl(advertArray.getJSONObject(i).getString("url"));
                            advert.setUuid(advertArray.getJSONObject(i).getString("uuid"));
                            advert.setBsort(advertArray.getJSONObject(i).getInt("bsort"));
                            advert.setBtype(advertArray.getJSONObject(i).getInt("btype"));
                            advert.setStatus(advertArray.getJSONObject(i).getInt("status"));
                            advertList.add(advert);
                        }

                        JSONArray array = json.getJSONArray("allStockSelectionList");
                        for(int n = 0; n<array.length(); n++){
                            stockProduct = new StockProduct();
                            hotStockList.clear();
                            stockProduct.setUuid(array.getJSONObject(n).getString("uuid"));
                            stockProduct.setPname(array.getJSONObject(n).getString("pname"));
                            stockProduct.setIntro(array.getJSONObject(n).getString("intro"));
                            stockProduct.setPicpath(array.getJSONObject(n).getString("picpath"));
//                            stockProduct.setUrl(array.getJSONObject(n).getString("url"));
                            JSONArray jsonArray = array.getJSONObject(n).getJSONArray("stockSelectionList");
                            for(int m = 0; m<jsonArray.length(); m++){
                                if(hotStockList.size()==3){
                                    continue;
                                }
                                hotStock = new HotStock();
                                hotStock.setUuid(jsonArray.getJSONObject(m).getString("uuid"));
                                hotStock.setChg(jsonArray.getJSONObject(m).getString("chg"));
                                hotStock.setIntro(jsonArray.getJSONObject(m).getString("intro"));
                                hotStock.setStockexchange(jsonArray.getJSONObject(m).getString("stockexchange"));
                                Object object = jsonArray.getJSONObject(m).opt("stockexchange");
                                if(object==null||"null".equals(object.toString())){
                                    hotStock.setStockexchange("");
                                }else {
                                    hotStock.setStockexchange(jsonArray.getJSONObject(m).getString("stockexchange"));
                                }
                                hotStock.setStockcode(jsonArray.getJSONObject(m).getString("stockcode"));
                                hotStock.setStockname(jsonArray.getJSONObject(m).getString("stockname"));
                                hotStockList.add(hotStock);
                            }
                            stockProduct.setList(hotStockList);
                            stockProductList.add(stockProduct);
                        }
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                if(stockProductList.size()>0){
                                    index=0;
//                                    Tv_intro.setText("简介:"+intro);
                                    Ll_content.setVisibility(View.VISIBLE);
                                    hotStockList1.clear();
                                    hotStockList1.addAll(stockProductList.get(0).getList());
                                    stockChooseAdapter.notifyDataSetChanged();
                                    Tv_intro.setText("简介："+stockProductList.get(0).getIntro());
                                }else {
                                    Ll_content.setVisibility(View.GONE);
                                }

                                if(advertList.size()>0){
                                    banner.setVisibility(View.VISIBLE);
                                    setBanner();
                                }else {
                                    banner.setVisibility(View.GONE);
                                }
                            }
                        });


                    } else if(a==-1){
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        });

                    }else {
                        if(getActivity()==null){
                            return;
                        }
                        final String msg=jsonObject.getString("message");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(getActivity(), msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    if(getActivity()==null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            ToastUtil.showShortToast(getActivity(), getString(R.string.login_parse_exc));
                        }
                    });
                } finally {
                    refreshLayout.finishRefresh();
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                refreshLayout.finishRefresh();
                if(getActivity()==null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(getActivity(), "网络连接异常");
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

    private void clickZD(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"updateDiagnosisbNum.do");
        requestParams.setConnectTimeout(30 * 1000);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url","https://me6oxi61y.lightyy.com/secudiagnosis_plus.html?s=600519&p=hsjy_1189"));
                            }
                        });

                    }else {
                        if(getActivity()==null){
                            return;
                        }
                        final String msg=jsonObject.getString("message");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(getActivity(), msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    if(getActivity()==null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            ToastUtil.showShortToast(getActivity(), getString(R.string.login_parse_exc));
                        }
                    });
                } finally {

                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(getActivity()==null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(getActivity(), "网络连接异常");
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

    private void checkToken(String pid,String uid){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"checkToken.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(getActivity(), H5Activity.class)
                                        .putExtra("url",SPUtil.getServerAddress()+"productguide.do?token="+SPUtil.getToken()+"&pid="+pid+"&uid="+uid+"&type=0"));
                            }
                        });

                    } else if(a==-1){
                        if (getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
                            }
                        });

                    }else {

                    }
                } catch (JSONException e) {
                    if (getActivity()==null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(getActivity(), getString(R.string.login_parse_exc));
                        }
                    });

                } finally {

                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (getActivity()==null){
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(getActivity(), "网络连接异常");
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
