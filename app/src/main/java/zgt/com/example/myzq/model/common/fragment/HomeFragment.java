package zgt.com.example.myzq.model.common.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseFragment;
import zgt.com.example.myzq.bean.Dynamic;
import zgt.com.example.myzq.bean.classes.Course;
import zgt.com.example.myzq.bean.homepage.Advert;
import zgt.com.example.myzq.bean.homepage.Notice;
import zgt.com.example.myzq.bean.homepage.Suspend;
import zgt.com.example.myzq.bean.homepage.Teacher;
import zgt.com.example.myzq.bean.homepage.banner;
import zgt.com.example.myzq.bean.stock.AllOProductModule;
import zgt.com.example.myzq.bean.stock.FeaturedStock;
import zgt.com.example.myzq.bean.stock.HotStock;
import zgt.com.example.myzq.bean.stock.ReseaRchreportk;
import zgt.com.example.myzq.bean.stock.StockProduct;
import zgt.com.example.myzq.model.common.MainActivity;
import zgt.com.example.myzq.model.common.adapter.homeAdapter.RecommendAdapter;
import zgt.com.example.myzq.model.common.adapter.homeAdapter.TeacherAdapter;
import zgt.com.example.myzq.model.common.adapter.homeAdapter.TeacherFileBoutiqueAdapter;
import zgt.com.example.myzq.model.common.adapter.homeAdapter.TeacherFileFeeAdapter;
import zgt.com.example.myzq.model.common.adapter.stock.HotStockTitleAdapter;
import zgt.com.example.myzq.model.common.adapter.stock.HotstockAdapter;
import zgt.com.example.myzq.model.common.adapter.stock.ResearchReportAdapter;
import zgt.com.example.myzq.model.common.course.BoutiqueActivity;
import zgt.com.example.myzq.model.common.course.CourseDetailActivity;
import zgt.com.example.myzq.model.common.course.FreeCourseActivity;
import zgt.com.example.myzq.model.common.custom_view.ENoticeView;
import zgt.com.example.myzq.model.common.custom_view.MyImageView;
import zgt.com.example.myzq.model.common.custom_view.NoticeAdapter;
import zgt.com.example.myzq.model.common.home.BannerUrlActivity;
import zgt.com.example.myzq.model.common.home.h5.H5Activity;
import zgt.com.example.myzq.model.common.home.lecturer.LecturerActivity;
import zgt.com.example.myzq.model.common.home.live.LiveItemActivity;
import zgt.com.example.myzq.model.common.home.my.MyActivity;
import zgt.com.example.myzq.model.common.home.researchreport.ReseaRchreportDetailActivity;
import zgt.com.example.myzq.model.common.home.researchreport.ReseaRchreportListActivity;
import zgt.com.example.myzq.model.common.home.znxg.SelectStockActivity;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.model.common.personal_center.MessageActivity;
import zgt.com.example.myzq.utils.GlideImageLoader;
import zgt.com.example.myzq.utils.Log;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ScrollViewUtil;
import zgt.com.example.myzq.utils.SpaceItemDecoration;
import zgt.com.example.myzq.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment{

//    @BindView(R.id.refreshLayout)
//    RefreshLayout refreshLayout;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.nestedSV)
    ScrollViewUtil nestedSV;

    @BindView(R.id.recyclerview_tuijian)
    RecyclerView recyclerview_tuijian;

    @BindView(R.id.recyclerview_laoshi)
    RecyclerView recyclerview_laoshi;

    @BindView(R.id.recyclerview_haoke)
    RecyclerView recyclerview_haoke;

    @BindView(R.id.recyclerview_kecheng)
    RecyclerView recyclerview_kecheng;

    @BindView(R.id.recyclerview_hot_stock)
    RecyclerView recyclerview_hot_stock;

    @BindView(R.id.recyclerview_hot_stock_title)
    RecyclerView recyclerview_hot_stock_title;

    @BindView(R.id.recyclerview_yanbao)
    RecyclerView recyclerview_yanbao;


    @BindView(R.id.tv_notice)
    ENoticeView noticeView;

    @BindView(R.id.banner)
    Banner banner1;

    @BindView(R.id.banner1)
    Banner banner2;

    @BindView(R.id.Fl_title)
    LinearLayout Fl_title;

    @BindView(R.id.Ll_search)
    LinearLayout Ll_search;

    @BindView(R.id.Iv_message)
    ImageView Iv_message;

    @BindView(R.id.Et_search)
    EditText Et_search;

    @BindView(R.id.Ll_tsxg)
    LinearLayout Ll_tsxg;//

    @BindView(R.id.Tv_intro)
    TextView Tv_intro;//
    @BindView(R.id.Tv_record)
    TextView Tv_record;//
    @BindView(R.id.Tv_name)
    TextView Tv_name;//
    @BindView(R.id.Tv_code)
    TextView Tv_code;//
    @BindView(R.id.Bt_ljck)
    Button Bt_ljck;


    @BindView(R.id.Tv_intro1)
    TextView Tv_intro1;//
    @BindView(R.id.Tv_record1)
    TextView Tv_record1;//
    @BindView(R.id.Tv_name1)
    TextView Tv_name1;//
    @BindView(R.id.Tv_code1)
    TextView Tv_code1;//
    @BindView(R.id.Bt_ljck1)
    Button Bt_ljck1;

    @BindView(R.id.Tv_intro2)
    TextView Tv_intro2;//
    @BindView(R.id.Tv_record2)
    TextView Tv_record2;//
    @BindView(R.id.Tv_name2)
    TextView Tv_name2;//
    @BindView(R.id.Tv_code2)
    TextView Tv_code2;//

    @BindView(R.id.image)
    ImageView image;//
    @BindView(R.id.image2)
    ImageView image2;//

    @BindView(R.id.imageView)
    MyImageView imageView;//


    @BindView(R.id.Tv_diagnosisbNum)
    TextView Tv_diagnosisbNum;//
    @BindView(R.id.Bt_ljck2)
    Button Bt_ljck2;

    @BindView(R.id.Ll_zxgg)
    LinearLayout Ll_zxgg;//最新公告

    @BindView(R.id.Ll_lszd)
    LinearLayout Ll_lszd;//我要诊股

    @BindView(R.id.Ll_jrtj)
    LinearLayout Ll_jrtj;//今日推荐

    @BindView(R.id.Ll_rmgp)
    LinearLayout Ll_rmgp;//热门股票

    @BindView(R.id.Iv_head)
    MyImageView Iv_head;//热门股票


//    @BindView(R.id.Rg_rmgp)
//    RadioGroup Rg_rmgp;
//    @BindView(R.id.Rb_1)
//    RadioButton Rb_1;
//    @BindView(R.id.Rb_2)
//    RadioButton Rb_2;
//    @BindView(R.id.Rb_3)
//    RadioButton Rb_3;

    @BindView(R.id.Ll_ybjx)
    LinearLayout Ll_ybjx;//研报精选

    @BindView(R.id.Ll_skls)
    LinearLayout Ll_skls;//授课老师

    @BindView(R.id.Ll_mfhk)
    LinearLayout Ll_mfhk;//免费课程

    @BindView(R.id.Ll_jpkc)
    LinearLayout Ll_jpkc;//精品课程


    private LinearLayoutManager mLayoutManager;
    private GridLayoutManager gLayoutManager;
    private List<Dynamic> list=new ArrayList<>();
    private List titles = new ArrayList<>();

    private  List<Course> classesList = new ArrayList<>();
    private List<String> list1=new ArrayList<>();
    private List<String> list2=new ArrayList<>();
    private List<Notice> newsList=new ArrayList<>();
    private List<Teacher> teachersList=new ArrayList<>();
    private List<Course> teacherFileFrees=new ArrayList<>();
    private List<Course> recommendedtodays = new ArrayList<>();
    private List<FeaturedStock> featuredStockList = new ArrayList<>();
    private List<ReseaRchreportk> reseaRchreportkList = new ArrayList<>();
    private List<StockProduct> stockProductList  = new ArrayList();
    private List<HotStock> hotStockList  = new ArrayList();
    private List<HotStock> hotStockList1  = new ArrayList();
    private String ban;
    private List<banner> bannerList=new ArrayList<>();

    private List<Advert> advertList=new ArrayList<>();
    private Advert advert;
    private List<Suspend> suspendList = new ArrayList<>();


    private TeacherAdapter teacherAdapter;//授课老师
    private RecommendAdapter recommendAdapter;//今日推荐
    private TeacherFileBoutiqueAdapter teacherFileBoutiqueAdapter;//精品课程
    private TeacherFileFeeAdapter teacherFileFeeAdapter;//免费好客
    private HotstockAdapter hotstockAdapter;//热门股票
    private HotStockTitleAdapter hotStockTitleAdapter;//热门股票
    private ResearchReportAdapter researchReportAdapter;//

    private List<AllOProductModule> allOProductModuleList = new ArrayList<>();


    private FeaturedStock featuredStock;//特设股票
    private ReseaRchreportk reseaRchreportk;//
    private StockProduct stockProduct;
    private HotStock hotStock;
    private AllOProductModule allOProductModule;


    private banner banners;
    private Notice notice;
    private Teacher teacher;
    private Course teacherFileFree;
    private Course classes;
    private Course recommendedtoday;
    private int height=0;

    private String pid;

    int diagnosisbNum = 0;

    private MainActivity mainActivity;




//    private String [] textArrays;
//
//    private int currentpage=1,totalcount=0;
//    int toolBarPositionY = 0;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.setDarkMode(getActivity());//白色

        FrameLayout.LayoutParams params=(FrameLayout.LayoutParams) Ll_search.getLayoutParams();
        params.topMargin=getStatusBarHeight(getActivity());

        Ll_search.setLayoutParams(params);

        WindowManager wm1 = getActivity().getWindowManager();
        int width = wm1.getDefaultDisplay().getWidth();

        LinearLayout.LayoutParams params1=(LinearLayout.LayoutParams) banner2.getLayoutParams();
        params1.width= width-60;
        params1.height= (width-60)*96/331;
        banner2.setLayoutParams(params1);

        if(TextUtils.isEmpty(SPUtil.getHeadimg())){
            Iv_head.setImageDrawable(getContext().getResources().getDrawable(R.drawable.replace));
        }else {
            Iv_head.setImageURL(SPUtil.getHeadimg());
        }

        setPullRefresher();
        initRecyclerview_tuijian();
        initRecyclerview_yanbao();
        initRecyclerview_laoshi();
        initRecyclerview_haoke();
        initRecyclerview_kecheng();
        initRecyclerview_hot_stock();
        initRecyclerview_hot_stock_title();
        addScrollViewListener();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
        }else {
            if (height <120) {
                StatusBarUtil.setDarkMode(getActivity());//白色
            } else {
                StatusBarUtil.setLightMode(getActivity());//黑色
            }
            getData(refreshLayout,0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        getData(refreshLayout,0);
//        banner1.startAutoPlay();
//        banner2.startAutoPlay();

    }

    @Override
    public void onStop() {
        super.onStop();
//        banner1.stopAutoPlay();
//        banner2.stopAutoPlay();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    private void addScrollViewListener() {
        int h= 120;
        int statusBarHeight = getStatusBarHeight(getActivity());
        Log.e("TAG","hight="+statusBarHeight);

        nestedSV.setOnScrollistener(new ScrollViewUtil.OnScrollistener() {
            @Override
            public void onScrollChanged(ScrollViewUtil scrollView, int x, int y, int oldx, int oldy) {
                Log.e("x="+x+",y="+y+",oldx="+oldx+",oldy="+oldy);
                height= y;
                if (y <= 0) {
                    Fl_title.setAlpha(0);
//                    Fl_title.setBackgroundColor(Color.argb((int) 0, 227, 29, 26));
                } else if (y > 0 && y <= h) {
                    float scale = (float) y / h;
                    float alpha = (255 * scale);
                    // 只是layout背景透明(仿知乎滑动效果)
                    Fl_title.setAlpha(scale);
//                    Fl_title.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                } else {
                    Fl_title.setAlpha(1);
//                    Fl_title.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                }

                if(y>statusBarHeight){
                    Iv_message.setImageResource(R.mipmap.btn_xiaoxi2);
                    Drawable drawable = getResources().getDrawable(R.mipmap.ic_sousuo2);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示

                    Et_search.setCompoundDrawables(drawable,null,null,null);
                    Et_search.setHintTextColor(Color.parseColor("#9E9E9E"));
                }else {
                    Iv_message.setImageResource(R.mipmap.btn_xiaoxi);
                    Drawable drawable = getResources().getDrawable(R.mipmap.ic_sousuo);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示

                    Et_search.setCompoundDrawables(drawable,null,null,null);
                    Et_search.setHintTextColor(Color.parseColor("#ffffff"));
                }

                if (y < h) {
                    StatusBarUtil.setDarkMode(getActivity());//白色
                } else {
                    StatusBarUtil.setLightMode(getActivity());//黑色
                }
            }

        });
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

                getData(refreshlayout,1);
//                adapter.refresh(newList);

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

    private void initRecyclerview_tuijian(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        //调整RecyclerView的排列方向
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview_tuijian.setLayoutManager(mLayoutManager);
        //设置item间距，30dp
//        recyclerview_tuijian.addItemDecoration(new SpaceItemDecoration(30));
        recommendAdapter=new RecommendAdapter(getActivity(),recommendedtodays);
        recyclerview_tuijian.setAdapter(recommendAdapter);
        recyclerview_tuijian.setNestedScrollingEnabled(false);//NestedScrollView嵌套RecyclerView卡顿解决办法：

        recommendAdapter.setOnItemClickListener(new RecommendAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview_tuijian.getChildAdapterPosition(view);
                startActivity(new Intent().setClass(getActivity(), CourseDetailActivity.class).putExtra("uuid",recommendedtodays.get(position).getUuid()).putExtra("index",1));
            }
        });
    }

    private void initRecyclerview_hot_stock(){
        gLayoutManager = new GridLayoutManager(getActivity(),2);
        //调整RecyclerView的排列方向
//        recyclerview_hot_stock.addItemDecoration(new SpaceItemDecoration(10));
//        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview_hot_stock.setLayoutManager(gLayoutManager);
        //设置item间距，30dp
        recyclerview_tuijian.addItemDecoration(new SpaceItemDecoration(6));
        hotstockAdapter=new HotstockAdapter(getActivity(),hotStockList1);
        recyclerview_hot_stock.setAdapter(hotstockAdapter);
        recyclerview_hot_stock.setNestedScrollingEnabled(false);//NestedScrollView嵌套RecyclerView卡顿解决办法：

        hotstockAdapter.setOnItemClickListener(new HotstockAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview_tuijian.getChildAdapterPosition(view);
                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url","https://ze3oy5f3q.lightyy.com/index.html#/quote?code="+hotStockList1.get(position).getStockcode()+hotStockList1.get(position).getStockexchange()));
            }
        });
    }

    private void initRecyclerview_hot_stock_title(){
        mLayoutManager = new LinearLayoutManager(getActivity());
            //调整RecyclerView的排列方向
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview_hot_stock_title.setLayoutManager(mLayoutManager);
        //设置item间距，30dp
        recyclerview_hot_stock_title.addItemDecoration(new SpaceItemDecoration(12));
        hotStockTitleAdapter=new HotStockTitleAdapter(getActivity(),stockProductList);
        recyclerview_hot_stock_title.setAdapter(hotStockTitleAdapter);
        recyclerview_hot_stock_title.setNestedScrollingEnabled(false);//NestedScrollView嵌套RecyclerView卡顿解决办法：

        hotStockTitleAdapter.setOnItemClickListener(new HotStockTitleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview_hot_stock_title.getChildAdapterPosition(view);
                hotStockTitleAdapter.setCurrent(position);
                hotStockList1.clear();
                hotStockList1.addAll(stockProductList.get(position).getList());
                hotstockAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initRecyclerview_yanbao(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        //调整RecyclerView的排列方向
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_yanbao.setLayoutManager(mLayoutManager);
        //设置item间距，30dp
//        recyclerview_laoshi.addItemDecoration(new SpaceItemDecoration(30));

        researchReportAdapter=new ResearchReportAdapter(getActivity(),reseaRchreportkList);
        recyclerview_yanbao.setAdapter(researchReportAdapter);
        recyclerview_yanbao.setNestedScrollingEnabled(false);//NestedScrollView嵌套RecyclerView卡顿解决办法：

        researchReportAdapter.setOnItemClickListener(new ResearchReportAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview_yanbao.getChildAdapterPosition(view);
                startActivity(new Intent().setClass(getActivity(), ReseaRchreportDetailActivity.class).putExtra("uuid",reseaRchreportkList.get(position).getUuid()).putExtra("pid",pid));


            }
        });

    }

    private void initRecyclerview_laoshi(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        //调整RecyclerView的排列方向
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview_laoshi.setLayoutManager(mLayoutManager);
        //设置item间距，30dp
//        recyclerview_laoshi.addItemDecoration(new SpaceItemDecoration(30));

        teacherAdapter=new TeacherAdapter(getActivity(),teachersList);
        recyclerview_laoshi.setAdapter(teacherAdapter);
        recyclerview_laoshi.setNestedScrollingEnabled(false);//NestedScrollView嵌套RecyclerView卡顿解决办法：

        teacherAdapter.setOnItemClickListener(new TeacherAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview_laoshi.getChildAdapterPosition(view);
                startActivity(new Intent().setClass(getActivity(), LecturerActivity.class).putExtra("uuid",teachersList.get(position).getUuid()));
            }
        });

    }



    private void initRecyclerview_haoke(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        //调整RecyclerView的排列方向
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_haoke.setLayoutManager(mLayoutManager);
        //设置item间距，30dp
//        recyclerview_haoke.addItemDecoration(new SpaceItemDecoration(30));
        teacherFileFeeAdapter=new TeacherFileFeeAdapter(getActivity(),teacherFileFrees);
        recyclerview_haoke.setAdapter(teacherFileFeeAdapter);
        recyclerview_haoke.setNestedScrollingEnabled(false);//NestedScrollView嵌套RecyclerView卡顿解决办法：

        teacherFileFeeAdapter.setOnItemClickListener(new TeacherFileFeeAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview_haoke.getChildAdapterPosition(view);
                startActivity(new Intent().setClass(getActivity(), CourseDetailActivity.class).putExtra("uuid",teacherFileFrees.get(position).getUuid()).putExtra("index",1));
            }
        });

    }

    private void initRecyclerview_kecheng(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        //调整RecyclerView的排列方向
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_kecheng.setLayoutManager(mLayoutManager);
        //设置item间距，30dp
//        recyclerview_kecheng.addItemDecoration(new SpaceItemDecoration(30));
        teacherFileBoutiqueAdapter=new TeacherFileBoutiqueAdapter(getActivity(),classesList);
        recyclerview_kecheng.setAdapter(teacherFileBoutiqueAdapter);
        recyclerview_kecheng.setNestedScrollingEnabled(false);//NestedScrollView嵌套RecyclerView卡顿解决办法：
        teacherFileBoutiqueAdapter.setOnItemClickListener(new TeacherFileBoutiqueAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview_kecheng.getChildAdapterPosition(view);
                startActivity(new Intent().setClass(getActivity(), CourseDetailActivity.class).putExtra("uuid",classesList.get(position).getUuid()).putExtra("index",1));
            }
        });
    }



    @OnClick({R.id.imageView,R.id.Bt_ljck,R.id.Bt_ljck1,R.id.Bt_ljck2,R.id.Et_search,R.id.Iv_message,R.id.Iv_head,R.id.Ll_live,R.id.Ll_advisor,R.id.Ll_index,R.id.Ll_class,R.id.Ll_base,R.id.Ll_sjxg,R.id.Ll_tsxg,R.id.Ll_ztjm,R.id.Ll_sjld,R.id.Ll_qnfp,R.id.Ll_znxg,R.id.Iv_wyzg,R.id.Ll_ybjx,R.id.Ll_mfhk,R.id.Ll_jpkc})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.imageView://
                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url", suspendList.get(0).getUrl()));
                break;
            case R.id.Bt_ljck://

                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url", SPUtil.getServerAddress()+"gc.do?token="+SPUtil.getToken()+"&type=0"));
                break;
            case R.id.Bt_ljck1://
                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url", SPUtil.getServerAddress()+"gc.do?token="+SPUtil.getToken()+"&type=0"));
                break;
            case R.id.Bt_ljck2://
                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url", SPUtil.getServerAddress()+"gc.do?token="+SPUtil.getToken()+"&type=0"));
                break;
            case R.id.Et_search://
                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url","https://ze3oy5f3q.lightyy.com/#/search"));
                break;
            case R.id.Iv_head:
                startActivity(new Intent().setClass(getActivity(), MyActivity.class));
                break;
            case R.id.Iv_message:
                startActivity(new Intent().setClass(getActivity(), MessageActivity.class));
                break;
            case R.id.Ll_live://名师直播
                startActivity(new Intent().setClass(getActivity(), LiveItemActivity.class));
                break;
            case R.id.Ll_advisor://免费好课
                startActivity(new Intent().setClass(getActivity(), FreeCourseActivity.class));
                break;
            case R.id.Ll_mfhk://免费好课
                startActivity(new Intent().setClass(getActivity(), FreeCourseActivity.class));
                break;
            case R.id.Ll_index://牵牛榜
                if(!TextUtils.isEmpty(SPUtil.getToken())){
                    checkToken("3","3");
                }else {
                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                    finish();
                }
//                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url","https://me6oxi61y.lightyy.com/hero.html?p=hsjy_1189"));
                break;
            case R.id.Ll_class://条件选股
                if(!TextUtils.isEmpty(SPUtil.getToken())){
                   checkToken("9","9");
                }else {
                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                    finish();
                }
//                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url","https://8usod7usp.lightyy.com/index.html?p=HSJY_1189&h=0#/index/condition"));
//                startActivity(new Intent().setClass(getActivity(), InformationActivity.class));https://8usod7usp.lightyy.com/index.html?p=HSJY_1189&h=0#/index/condition
                break;
            case R.id.Ll_sjxg://数据选股
                if(!TextUtils.isEmpty(SPUtil.getToken())){
                   checkToken("6","6");
                }else {
                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                    finish();
                }
//                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url","https://je7o2az1x.lightyy.com/index.html?p=hsjy_1189&h=0&tg=_blank"));
                break;
            case R.id.Ll_ztjm://涨停揭秘
                if(!TextUtils.isEmpty(SPUtil.getToken())){
                    checkToken("2","2");
                }else {
                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                    finish();
                }
//                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url","https://ji7o5m8xn.lightyy.com/index.html?p=hsjy_1189"));
                break;
            case R.id.Ll_sjld://时间雷达
                if(!TextUtils.isEmpty(SPUtil.getToken())){
                    checkToken("5","5");
                }else {
                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                    finish();
                }
//                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url","https://nidojrs05.lightyy.com/index.html?p=hsjy_1189&h=0&tg=_blank"));
                break;
            case R.id.Ll_qnfp://牵牛复盘
                if(!TextUtils.isEmpty(SPUtil.getToken())){
                    checkToken("4","4");
                }else {
                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                    finish();
                }
//                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url","https://gqdo5m8xy.lightyy.com/index.html?p=hsjy_1189&h=0&tg=_blank"));
                break;
            case R.id.Ll_znxg://智能选股
                startActivity(new Intent().setClass(getActivity(), SelectStockActivity.class));
                break;
            case R.id.Iv_wyzg://我要诊股
                clickZD();
                break;
            case R.id.Ll_tsxg://特色选股
//                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url","https://ji1o3e6nu.lightyy.com/index.html#/decision-detail/turn-signal"));
                break;
            case R.id.Ll_ybjx://研报精选
                startActivity(new Intent().setClass(getActivity(), ReseaRchreportListActivity.class).putExtra("pid",pid));
                break ;
            case R.id.Ll_base://精品课程
                startActivity(new Intent().setClass(getActivity(), BoutiqueActivity.class));
                break;
            case R.id.Ll_jpkc://精品课程
                startActivity(new Intent().setClass(getActivity(), BoutiqueActivity.class));
                break;

        }
    }

    private void setBanner(){
//        banner1.removeAllViews();
        banner1.setImageLoader(new GlideImageLoader());
        banner1.setIndicatorGravity(BannerConfig.CENTER);//圆点的位置
        banner1.setImages(list1).//加载的图片
                setBannerStyle(BannerConfig.CIRCLE_INDICATOR).
                setDelayTime(5000).start();//图片循环滑动的时间2秒
        banner1.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (bannerList.get(position).getRedirecttype() == 1) {
                    if(TextUtils.isEmpty(bannerList.get(position).getUrl())){

                    }else {
                        startActivity(new Intent().setClass(getActivity(), BannerUrlActivity.class).putExtra("url", bannerList.get(position).getUrl()));
                    }

                } else if (bannerList.get(position).getRedirecttype() == 2) {
                    String appId = "wx72ef58b1e2b5e1b6"; // 填应用AppId
                    IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);
                    WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                    req.userName = "gh_810becb0c8bc"; // 填小程序原始id
                    req.path = bannerList.get(position).getUrl();                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
                    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
                    api.sendReq(req);
                }else {

                }
            }
        });
        banner1.start();
    }

    private void setadvertBanner(){
        list2.clear();
        for (int i=0;i<advertList.size();i++){
            list2.add(advertList.get(i).getPicpath());
        }
        banner2.setImageLoader(new GlideImageLoader());
        banner2.setIndicatorGravity(BannerConfig.CENTER);//圆点的位置
        banner2.setImages(list2).//加载的图片
                setBannerStyle(BannerConfig.CIRCLE_INDICATOR).
                setDelayTime(5000).start();//图片循环滑动的时间2秒
        banner2.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (advertList.get(position).getRedirecttype() == 1) {
                    if(TextUtils.isEmpty(advertList.get(position).getUrl())){

                    }else {
                        startActivity(new Intent().setClass(getActivity(), BannerUrlActivity.class).putExtra("url", advertList.get(position).getUrl()));
                    }
                } else if (advertList.get(position).getRedirecttype() == 2) {
                    String appId = "wx72ef58b1e2b5e1b6"; // 填应用AppId
                    IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);
                    WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                    req.userName = "gh_810becb0c8bc"; // 填小程序原始id
                    req.path = advertList.get(position).getUrl();                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
                    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
                    api.sendReq(req);
                }else {

                }
            }
        });
        banner2.start();

//        banner2.setData(advertList, null);
//        // XBanner适配数据
//        banner2.setmAdapter(new XBanner.XBannerAdapter() {
//            @Override
//            public void loadBanner(XBanner banner, View view, int position) {
//                Glide.with(getActivity()).load(advertList.get(position).getPicpath()).into((ImageView) view);
//            }
//        });
//        banner2.setPointsIsVisible(true);
//        // 设置XBanner的页面切换特效
//        banner2.setPageTransformer(Transformer.Default);
//        // 设置XBanner页面切换的时间，即动画时长
//        banner2.setPageChangeDuration(1000);
//
//        // XBanner中某一项的点击事件
//        banner2.setOnItemClickListener(new XBanner.OnItemClickListener() {
//            @Override
//            public void onItemClick(XBanner banner, int position) {
//                if(TextUtils.isEmpty(advertList.get(position).getUrl())){
//
//                }else {
//                    if (advertList.get(position).getRedirecttype() == 1) {
//                        startActivity(new Intent().setClass(getActivity(), BannerUrlActivity.class).putExtra("url", advertList.get(position).getUrl()));
//                    } else if (advertList.get(position).getRedirecttype() == 2) {
//                        String appId = "wx72ef58b1e2b5e1b6"; // 填应用AppId
//                        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);
//                        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
//                        req.userName = "gh_810becb0c8bc"; // 填小程序原始id
//                        req.path = advertList.get(position).getUrl();                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
//                        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
//                        api.sendReq(req);
//                    }
//                }
////                startActivity(new Intent().setClass(getActivity(), BannerUrlActivity.class).putExtra("url",bannerList.get(position).getUrl()));
////                Toast.makeText(getActivity(), "点击了第" + (position + 1) + "张图片", Toast.LENGTH_SHORT).show();
//            }
//        });
//        banner2.startAutoPlay();
    }

    private void getpicture(int type){
        // 为XBanner绑定数据
        if(banner1!=null){
            setBanner();
        }

        if(banner2!=null){
            setadvertBanner();
        }
        if(advertList.size()==0){
            banner2.setVisibility(View.GONE);
        }else {
            banner2.setVisibility(View.VISIBLE);
        }
//
        noticeView.setFlipIntervalTime(3000);
        noticeView.setDurationTime(1000);
        noticeView.setOnItemClickListener(new ENoticeView.OnItemClickListener() {//通知详情页
            @Override
            public void onClick(int position) {
//                startActivity(new Intent().setClass(getActivity(), NoticeDetailActivity.class).putExtra("notice",(Serializable) newsList.get(position)));
//                Toast.makeText(MainActivity.this,"item 被点击"+position,Toast.LENGTH_SHORT).show();
            }
        });
        noticeView.setAdapter(new NoticeAdapter() {
            @Override
            public int getCount() {
                return newsList.size();
            }

            @Override
            public View getView(Context context, int position) {
                View view = View.inflate(context,R.layout.item,null);
                ((TextView)view.findViewById(R.id.textView1)).setText(newsList.get(position).getTitle());
                return view;
            }
        });
        noticeView.startFlipping();
        String string= diagnosisbNum+"";
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            stringBuffer.append(string.substring(i,i+1)+" ");
        }

        if(suspendList.size()>0){
            Glide.with(this).load(suspendList.get(0).getPicpath()).into(imageView);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView.getLayoutParams();
            params.height=suspendList.get(0).getHeight();
            params.width =suspendList.get(0).getWidth();
            imageView.setLayoutParams(params);
        }else {
            imageView.setVisibility(View.GONE);
        }

        setPicture("历史诊断 "+stringBuffer+"次");

        if(diagnosisbNum==0){
            Ll_lszd.setVisibility(View.GONE);
        }else {
            Ll_lszd.setVisibility(View.VISIBLE);
        }

        if(newsList.size()==0){
            Ll_zxgg.setVisibility(View.GONE);
        }else {
            Ll_zxgg.setVisibility(View.VISIBLE);
        }


        if(recommendedtodays.size()==0){
            Ll_jrtj.setVisibility(View.GONE);
        }else {
            Ll_jrtj.setVisibility(View.VISIBLE);
        }

        if(reseaRchreportkList.size()==0){
            Ll_ybjx.setVisibility(View.GONE);
        }else {
            Ll_ybjx.setVisibility(View.VISIBLE);
        }

        if(teacherFileFrees.size()==0){
            Ll_mfhk.setVisibility(View.GONE);
        }else {
            Ll_mfhk.setVisibility(View.VISIBLE);
        }

        if(teachersList.size()==0){
            Ll_skls.setVisibility(View.GONE);
        }else {
            Ll_skls.setVisibility(View.VISIBLE);
        }

        if(classesList.size()==0){
            Ll_jpkc.setVisibility(View.GONE);
        }else {
            Ll_jpkc.setVisibility(View.VISIBLE);
        }
        if(stockProductList.size()==0){
            Ll_rmgp.setVisibility(View.GONE);
        }else {
            Ll_rmgp.setVisibility(View.VISIBLE);
            hotStockList1.clear();
            hotStockList1.addAll(stockProductList.get(0).getList());
            hotstockAdapter.notifyDataSetChanged();
        }

        if(allOProductModuleList.size()==1){
            Ll_tsxg.setVisibility(View.VISIBLE);
            image.setVisibility(View.GONE);
            image2.setVisibility(View.GONE);
            Tv_intro.setText(allOProductModuleList.get(0).getList().get(0).getStockpoolname());
            Tv_code.setText("入选日期:"+allOProductModuleList.get(0).getList().get(0).getChosendate());
            Tv_name.setText(allOProductModuleList.get(0).getList().get(0).getIntro());
            Tv_record.setText(allOProductModuleList.get(0).getList().get(0).getHistoryrecord());
            if(allOProductModuleList.get(0).getList().get(0).getHistoryrecord().contains("+")){
                Tv_record.setTextColor(Color.parseColor("#FF4444"));
            }else if(allOProductModuleList.get(0).getList().get(0).getHistoryrecord().contains("-")){
                Tv_record.setTextColor(Color.parseColor("#169433"));
            }else{
                Tv_record.setTextColor(Color.parseColor("#333333"));
            }
            Bt_ljck.setVisibility(View.VISIBLE);
            Bt_ljck1.setVisibility(View.GONE);
            Bt_ljck2.setVisibility(View.GONE);
        }else if(featuredStockList.size()==2){
            image.setVisibility(View.VISIBLE);
            image2.setVisibility(View.GONE);

            Tv_intro.setText(allOProductModuleList.get(0).getList().get(0).getStockpoolname());
            Tv_code.setText("入选日期:"+allOProductModuleList.get(0).getList().get(0).getChosendate());
            Tv_name.setText(allOProductModuleList.get(0).getList().get(0).getIntro());
            Tv_record.setText(allOProductModuleList.get(0).getList().get(0).getHistoryrecord());
            if(allOProductModuleList.get(0).getList().get(0).getHistoryrecord().contains("+")){
                Tv_record.setTextColor(Color.parseColor("#FF4444"));
            }else if(allOProductModuleList.get(0).getList().get(0).getHistoryrecord().contains("-")){
                Tv_record.setTextColor(Color.parseColor("#169433"));
            }else{
                Tv_record.setTextColor(Color.parseColor("#333333"));
            }

            Tv_intro1.setText(allOProductModuleList.get(1).getList().get(0).getStockpoolname());
            Tv_code1.setText("入选日期:"+allOProductModuleList.get(1).getList().get(0).getChosendate());
            Tv_name1.setText(allOProductModuleList.get(1).getList().get(0).getIntro());
            Tv_record1.setText(allOProductModuleList.get(1).getList().get(0).getHistoryrecord());
            if(allOProductModuleList.get(1).getList().get(0).getHistoryrecord().contains("+")){
                Tv_record1.setTextColor(Color.parseColor("#FF4444"));
            }else if(allOProductModuleList.get(1).getList().get(0).getHistoryrecord().contains("-")){
                Tv_record1.setTextColor(Color.parseColor("#169433"));
            }else{
                Tv_record1.setTextColor(Color.parseColor("#333333"));
            }
            Bt_ljck.setVisibility(View.VISIBLE);
            Bt_ljck1.setVisibility(View.VISIBLE);
            Bt_ljck2.setVisibility(View.GONE);
        }else if(allOProductModuleList.size()>=3){
            image.setVisibility(View.VISIBLE);
            image2.setVisibility(View.VISIBLE);

            Tv_intro.setText(allOProductModuleList.get(0).getList().get(0).getStockpoolname());
            Tv_code.setText("入选日期:"+allOProductModuleList.get(0).getList().get(0).getChosendate());
            Tv_name.setText(allOProductModuleList.get(0).getList().get(0).getIntro());
            Tv_record.setText(allOProductModuleList.get(0).getList().get(0).getHistoryrecord());
            if(allOProductModuleList.get(0).getList().get(0).getHistoryrecord().contains("+")){
                Tv_record.setTextColor(Color.parseColor("#FF4444"));
            }else if(allOProductModuleList.get(0).getList().get(0).getHistoryrecord().contains("-")){
                Tv_record.setTextColor(Color.parseColor("#169433"));
            }else{
                Tv_record.setTextColor(Color.parseColor("#333333"));
            }

            Tv_intro1.setText(allOProductModuleList.get(1).getList().get(0).getStockpoolname());
            Tv_code1.setText("入选日期:"+allOProductModuleList.get(1).getList().get(0).getChosendate());
            Tv_name1.setText(allOProductModuleList.get(1).getList().get(0).getIntro());
            Tv_record1.setText(allOProductModuleList.get(1).getList().get(0).getHistoryrecord());
            if(allOProductModuleList.get(1).getList().get(0).getHistoryrecord().contains("+")){
                Tv_record1.setTextColor(Color.parseColor("#FF4444"));
            }else if(allOProductModuleList.get(1).getList().get(0).getHistoryrecord().contains("-")){
                Tv_record1.setTextColor(Color.parseColor("#169433"));
            }else{
                Tv_record1.setTextColor(Color.parseColor("#333333"));
            }
            Tv_intro2.setText(allOProductModuleList.get(2).getList().get(0).getStockpoolname());
            Tv_code2.setText("入选日期:"+allOProductModuleList.get(2).getList().get(0).getChosendate());
            Tv_name2.setText(allOProductModuleList.get(2).getList().get(0).getIntro());
            Tv_record2.setText(allOProductModuleList.get(2).getList().get(0).getHistoryrecord());
            if(allOProductModuleList.get(2).getList().get(0).getHistoryrecord().contains("+")){
                Tv_record2.setTextColor(Color.parseColor("#FF4444"));
            }else if(allOProductModuleList.get(2).getList().get(0).getHistoryrecord().contains("-")){
                Tv_record2.setTextColor(Color.parseColor("#169433"));
            }else{
                Tv_record2.setTextColor(Color.parseColor("#333333"));
            }
            Bt_ljck.setVisibility(View.VISIBLE);
            Bt_ljck2.setVisibility(View.VISIBLE);
            Bt_ljck1.setVisibility(View.VISIBLE);
        }else {
            Ll_tsxg.setVisibility(View.GONE);
        }
    }

    private void setPicture(String s){

        String[] str = new String[s.length()];//利用toCharArray方法转换

        for (int i = 0; i < s.length(); i++) {
            str[i] = s.substring(i,i+1);
        }

        SpannableString spanString =  new SpannableString(s);
        for(int i=0;i<str.length;i++) {
            if("0".equals(str[i])){
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.img_0);
                ImageSpan imgSpan = new ImageSpan(getActivity(), b);
                spanString.setSpan(imgSpan, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                Tv_diagnosisbNum.setText(spanString);
            }else if("1".equals(str[i])){
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.img_1);
                ImageSpan imgSpan = new ImageSpan(getActivity(), b);

                spanString.setSpan(imgSpan, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                Tv_diagnosisbNum.setText(spanString);
            }else if("2".equals(str[i])){
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.img_2);
                ImageSpan imgSpan = new ImageSpan(getActivity(), b);

                spanString.setSpan(imgSpan, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                Tv_diagnosisbNum.setText(spanString);
            }else if("3".equals(str[i])){
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.img_3);
                ImageSpan imgSpan = new ImageSpan(getActivity(), b);

                spanString.setSpan(imgSpan, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                Tv_diagnosisbNum.setText(spanString);
            }else if("4".equals(str[i])){
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.img_4);
                ImageSpan imgSpan = new ImageSpan(getActivity(), b);

                spanString.setSpan(imgSpan, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                Tv_diagnosisbNum.setText(spanString);
            }else if("5".equals(str[i])){
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.img_5);
                ImageSpan imgSpan = new ImageSpan(getActivity(), b);

                spanString.setSpan(imgSpan, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                Tv_diagnosisbNum.setText(spanString);
            }else if("6".equals(str[i])){
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.img_6);
                ImageSpan imgSpan = new ImageSpan(getActivity(), b);

                spanString.setSpan(imgSpan, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                Tv_diagnosisbNum.setText(spanString);
            }else if("7".equals(str[i])){
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.img_7);
                ImageSpan imgSpan = new ImageSpan(getActivity(), b);

                spanString.setSpan(imgSpan, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                Tv_diagnosisbNum.setText(spanString);
            }else if("8".equals(str[i])){
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.img_8);
                ImageSpan imgSpan = new ImageSpan(getActivity(), b);

                spanString.setSpan(imgSpan, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                Tv_diagnosisbNum.setText(spanString);
            }else if("9".equals(str[i])){
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.img_9);
                ImageSpan imgSpan = new ImageSpan(getActivity(), b);

                spanString.setSpan(imgSpan, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                Tv_diagnosisbNum.setText(spanString);
            }
//            SpannableString spanString = new SpannableString(s);
//            spanString.setSpan(imgSpan, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            Tv_diagnosisbNum.setText(spanString);
        }
        Tv_diagnosisbNum.setText(spanString);
    }

    private void getData(final RefreshLayout refreshLayout,int type){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"homePage0518.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        diagnosisbNum = json.getInt("diagnosisbNum");
                        String intro =json.getString("intro");
                        pid = json.getString("pid");
                        list1.clear();
                        titles.clear();
                        classesList.clear();
                        allOProductModuleList.clear();
                        teachersList.clear();
                        newsList.clear();
                        stockProductList.clear();
                        reseaRchreportkList.clear();
                        recommendedtodays.clear();
                        teacherFileFrees.clear();
                        bannerList.clear();
                        advertList.clear();
                        suspendList.clear();

                        JSONArray suspendArray=json.getJSONArray("suspendlist");
                        for (int i=0;i<suspendArray.length();i++){
                            Suspend suspend = new Suspend();
                            suspend.setPicpath(suspendArray.getJSONObject(i).getString("picpath"));
                            suspend.setUrl(suspendArray.getJSONObject(i).getString("url"));
                            suspend.setUuid(suspendArray.getJSONObject(i).getString("uuid"));
                            suspend.setExt(suspendArray.getJSONObject(i).getString("ext"));
                            suspend.setHeight(suspendArray.getJSONObject(i).getInt("height"));
                            suspend.setWidth(suspendArray.getJSONObject(i).getInt("width"));
                            suspend.setStatus(suspendArray.getJSONObject(i).getInt("status"));
                            suspendList.add(suspend);
                        }

                        JSONArray jsonArray=json.getJSONArray("bannerList");
                        for (int i=0;i<jsonArray.length();i++){
                            banners=new banner();
                            banners.setPicpath(jsonArray.getJSONObject(i).getString("picpath"));
                            banners.setUrl(jsonArray.getJSONObject(i).getString("url"));
                            banners.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            banners.setBsort(jsonArray.getJSONObject(i).getInt("bsort"));
                            banners.setBtype(jsonArray.getJSONObject(i).getInt("btype"));
                            banners.setStatus(jsonArray.getJSONObject(i).getInt("status"));
                            banners.setRedirecttype(jsonArray.getJSONObject(i).getInt("redirecttype"));
                            banners.setApptype(jsonArray.getJSONObject(i).getString("apptype"));
                            bannerList.add(banners);
                            ban=jsonArray.getJSONObject(i).getString("picpath");
                            list1.add(ban);
                            titles.add("");
                        }

                        JSONArray advertArray=json.getJSONArray("advertList");
                        for (int i=0;i<advertArray.length();i++){
                            advert=new Advert();
                            advert.setPicpath(advertArray.getJSONObject(i).getString("picpath"));
                            advert.setUrl(advertArray.getJSONObject(i).getString("url"));
                            advert.setUuid(advertArray.getJSONObject(i).getString("uuid"));
                            advert.setBsort(advertArray.getJSONObject(i).getInt("bsort"));
                            advert.setBtype(advertArray.getJSONObject(i).getInt("btype"));
                            advert.setStatus(advertArray.getJSONObject(i).getInt("status"));
                            advert.setRedirecttype(advertArray.getJSONObject(i).getInt("redirecttype"));
                            advert.setApptype(advertArray.getJSONObject(i).getString("apptype"));
                            advertList.add(advert);
                        }

                        JSONArray array=json.getJSONArray("newsList");
                        for (int j=0;j<array.length();j++){
                            notice=new Notice();
                            notice.setUuid(array.getJSONObject(j).getString("uuid"));
//                            notice.setNclassid(array.getJSONObject(j).getString("nclassid"));
                            notice.setTitle(array.getJSONObject(j).getString("title"));
//                            notice.setFtitle(array.getJSONObject(j).getString("ftitle"));
//                            notice.setSummary(array.getJSONObject(j).getString("summary"));
//                            notice.setAuthor(array.getJSONObject(j).getString("author"));
//                            notice.setPicpath(array.getJSONObject(j).getString("picpath"));
//                            notice.setVideoflag(array.getJSONObject(j).getInt("videoflag"));
//                            notice.setVideopath(array.getJSONObject(j).getString("videopath"));
//                            notice.setContent(array.getJSONObject(j).getString("content"));
//                            notice.setCreatetime(array.getJSONObject(j).getString("createtime"));
//                            notice.setClick(array.getJSONObject(j).getInt("click"));
                            newsList.add(notice);
                        }
                        JSONArray array1=json.getJSONArray("teacherList");
                        for (int k=0;k<array1.length();k++){
                            teacher = new Teacher();
                            teacher.setUuid(array1.getJSONObject(k).getString("uuid"));
                            teacher.setPicpath(array1.getJSONObject(k).getString("picpath"));
                            teachersList.add(teacher);
                        }

                        JSONArray array2=json.getJSONArray("teacherFileBoutique");
                        for(int m = 0;m<array2.length();m++){
                            classes = new Course();
                            classes.setUuid(array2.getJSONObject(m).getString("uuid"));
                            classes.setClick(array2.getJSONObject(m).getInt("click"));
                            classes.setIscharge(array2.getJSONObject(m).getInt("ischarge"));
                            classes.setPicpath(array2.getJSONObject(m).getString("picpath"));
                            classes.setTitle(array2.getJSONObject(m).getString("title"));
                            classes.setLecturer(array2.getJSONObject(m).getString("lecturer"));
                            classes.setPrice(array2.getJSONObject(m).getDouble("price"));
                            classes.setRealprice(array2.getJSONObject(m).getDouble("realprice"));
                            classesList.add(classes);
                        }

                        JSONArray array3=json.getJSONArray("recommendedtodayList");
                        for(int n = 0; n<array3.length(); n++){
                            recommendedtoday = new Course();
                            recommendedtoday.setUuid(array3.getJSONObject(n).getString("uuid"));
                            recommendedtoday.setClick(array3.getJSONObject(n).getInt("click"));
                            recommendedtoday.setRecommendpicpath(array3.getJSONObject(n).getString("recommendpicpath"));
                            recommendedtoday.setIscharge(array3.getJSONObject(n).getInt("ischarge"));
                            recommendedtoday.setPicpath(array3.getJSONObject(n).getString("recommendpicpath"));
                            recommendedtoday.setTitle(array3.getJSONObject(n).getString("title"));
                            recommendedtoday.setLecturer(array3.getJSONObject(n).getString("lecturer"));
                            recommendedtoday.setPrice(array3.getJSONObject(n).getDouble("price"));
                            recommendedtoday.setIntro(array3.getJSONObject(n).getString("intro"));
                            recommendedtoday.setRealprice(array3.getJSONObject(n).getDouble("realprice"));
                            recommendedtodays.add(recommendedtoday);
                        }

                        JSONArray array4=json.getJSONArray("teacherFileFree");
                        for(int n = 0; n<array4.length(); n++){
                            teacherFileFree = new Course();
                            teacherFileFree.setUuid(array4.getJSONObject(n).getString("uuid"));
                            teacherFileFree.setTitle(array4.getJSONObject(n).getString("title"));
                            teacherFileFree.setClick(array4.getJSONObject(n).getInt("click"));
                            teacherFileFree.setPicpath(array4.getJSONObject(n).getString("picpath"));
                            teacherFileFree.setIscharge(array4.getJSONObject(n).getInt("ischarge"));
                            teacherFileFree.setLecturer(array4.getJSONObject(n).getString("lecturer"));
                            teacherFileFrees.add(teacherFileFree);
                        }

                        JSONArray array5=json.getJSONArray("allOProductModuleList");//特色股票
                        for(int n = 0; n<array5.length(); n++){
                            allOProductModule = new AllOProductModule();
                            featuredStockList.clear();
                            allOProductModule.setUuid(array5.getJSONObject(n).getString("uuid"));
                            JSONArray jsonArray1 = array5.getJSONObject(n).getJSONArray("featuredStockList");
                                for(int m = 0; m<jsonArray1.length(); m++){
                                    featuredStock = new FeaturedStock();
//                                    featuredStock.setUuid(jsonArray1.getJSONObject(n).getString("uuid"));
                                    featuredStock.setChosendate(jsonArray1.getJSONObject(m).getString("chosendate"));
//                                    featuredStock.setStockcode(jsonArray1.getJSONObject(n).getString("stockcode"));
                                    featuredStock.setStockpoolname(jsonArray1.getJSONObject(m).getString("stockpoolname"));
                                    featuredStock.setHistoryrecord(jsonArray1.getJSONObject(m).getString("historyrecord"));
                                    featuredStock.setIntro(jsonArray1.getJSONObject(m).getString("intro"));
                                    featuredStockList.add(featuredStock);
                                }
                                allOProductModule.setList(featuredStockList);
                                allOProductModuleList.add(allOProductModule);
                        }

                        JSONArray array6=json.getJSONArray("reseaRchreportkList");//研报
                        for(int n = 0; n<array6.length(); n++){
                            reseaRchreportk = new ReseaRchreportk();
                            reseaRchreportk.setUuid(array6.getJSONObject(n).getString("uuid"));
                            reseaRchreportk.setTitle(array6.getJSONObject(n).getString("title"));
                            reseaRchreportk.setAuthor(array6.getJSONObject(n).getString("author"));
                            reseaRchreportk.setClick(array6.getJSONObject(n).getInt("click"));
                            reseaRchreportk.setCreatetime(array6.getJSONObject(n).getString("createtime"));
                            reseaRchreportk.setFtitle(array6.getJSONObject(n).getString("ftitle"));
                            reseaRchreportk.setIstop(array6.getJSONObject(n).getInt("istop"));

                            reseaRchreportk.setPicpath(array6.getJSONObject(n).getString("picpath"));
                            reseaRchreportk.setSource(array6.getJSONObject(n).getString("source"));
                            reseaRchreportk.setSummary(array6.getJSONObject(n).getString("summary"));
                            reseaRchreportk.setStockcode(array6.getJSONObject(n).getString("stockcode"));
                            reseaRchreportk.setStockname(array6.getJSONObject(n).getString("stockname"));
                            reseaRchreportk.setStockexchange(array6.getJSONObject(n).getString("stockexchange"));
                            reseaRchreportkList.add(reseaRchreportk);
                        }

                        JSONArray array7=json.getJSONArray("allOProductList");//热门股票
                        for(int n = 0; n<array7.length(); n++){
                            stockProduct = new StockProduct();
                            hotStockList.clear();
                            stockProduct.setUuid(array7.getJSONObject(n).getString("uuid"));
                            stockProduct.setPname(array7.getJSONObject(n).getString("pname"));
                            JSONArray array8 = array7.getJSONObject(n).getJSONArray("hotStockList");
                            for(int m = 0; m<array8.length(); m++){
                                hotStock = new HotStock();
                                hotStock.setUuid(array8.getJSONObject(m).getString("uuid"));
                                hotStock.setChg(array8.getJSONObject(m).getString("chg"));
                                Object object = array8.getJSONObject(m).opt("stockexchange");
                                if(object==null||"null".equals(object.toString())){
                                    hotStock.setStockexchange("");
                                }else {
                                    hotStock.setStockexchange(array8.getJSONObject(m).getString("stockexchange"));
                                }
                                Object object1 = array8.getJSONObject(m).opt("type");
                                if(object1==null||"null".equals(object1.toString())){
                                    hotStock.setType(0);
                                }else {
                                    hotStock.setType(array8.getJSONObject(m).getInt("type"));
                                }
//                                hotStock.setStockexchange(array8.getJSONObject(m).getString("stockexchange"));
//                                hotStock.setType(array8.getJSONObject(m).getInt("type"));
                                hotStock.setIntro(array8.getJSONObject(m).getString("intro"));
                                hotStock.setStockcode(array8.getJSONObject(m).getString("stockcode"));
                                hotStock.setStockname(array8.getJSONObject(m).getString("stockname"));
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
                                researchReportAdapter.notifyDataSetChanged();
                                recommendAdapter.notifyDataSetChanged();
                                teacherAdapter.notifyDataSetChanged();
                                teacherFileBoutiqueAdapter.notifyDataSetChanged();
                                teacherFileFeeAdapter.notifyDataSetChanged();
                                hotStockTitleAdapter.notifyDataSetChanged();
                                getpicture(type);
                            }
                        });
                    } else if(a==-1){
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                                                getActivity().finish();
//                                                dialog.dismiss();
//                                            }
//                                        }).create().show();
//                                startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                                getActivity().finish();
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
                if(getActivity()==null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Ll_jpkc.setVisibility(View.GONE);
//                        Ll_jrtj.setVisibility(View.GONE);
//                        Ll_mfhk.setVisibility(View.GONE);
//                        Ll_rmgp.setVisibility(View.GONE);
//                        Ll_skls.setVisibility(View.GONE);
//                        Ll_tsxg.setVisibility(View.GONE);
//                        Ll_ybjx.setVisibility(View.GONE);
//                        Ll_lszd.setVisibility(View.GONE);
//                        Ll_zxgg.setVisibility(View.GONE);
//                        ToastUtil.showShortToast(getActivity(), "网络连接异常");
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
                                diagnosisbNum+=1;
                                String string= diagnosisbNum+"";
                                StringBuffer stringBuffer = new StringBuffer();
                                for (int i = 0; i < string.length(); i++) {
                                    stringBuffer.append(string.substring(i,i+1)+" ");
                                }

                                setPicture("历史诊断 "+stringBuffer+"次");
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


    @Override
    public void onDestroy() {
        super.onDestroy();
//        tv_notice.releaseResources();
    }
}
