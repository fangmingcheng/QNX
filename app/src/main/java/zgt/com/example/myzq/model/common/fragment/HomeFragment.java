package zgt.com.example.myzq.model.common.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
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
import zgt.com.example.myzq.bean.homepage.Notice;
import zgt.com.example.myzq.bean.homepage.Teacher;
import zgt.com.example.myzq.bean.homepage.banner;
import zgt.com.example.myzq.model.common.adapter.DynamicAdapter;
import zgt.com.example.myzq.model.common.adapter.homeAdapter.RecommendAdapter;
import zgt.com.example.myzq.model.common.adapter.homeAdapter.TeacherAdapter;
import zgt.com.example.myzq.model.common.adapter.homeAdapter.TeacherFileBoutiqueAdapter;
import zgt.com.example.myzq.model.common.adapter.homeAdapter.TeacherFileFeeAdapter;
import zgt.com.example.myzq.model.common.course.BoutiqueActivity;
import zgt.com.example.myzq.model.common.course.CourseDetailActivity;
import zgt.com.example.myzq.model.common.course.FreeCourseActivity;
import zgt.com.example.myzq.model.common.custom_view.ENoticeView;
import zgt.com.example.myzq.model.common.custom_view.NoticeAdapter;
import zgt.com.example.myzq.model.common.home.BannerUrlActivity;
import zgt.com.example.myzq.model.common.home.VideoWebViewActivity;
import zgt.com.example.myzq.model.common.home.lecturer.LecturerActivity;
import zgt.com.example.myzq.model.common.home.live.LiveItemActivity;
import zgt.com.example.myzq.model.common.information.InformationActivity;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

import static zgt.com.example.myzq.R.id.tv_notice;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment{

//    @BindView(R.id.refreshLayout)
//    RefreshLayout refreshLayout;
//    @BindView(R.id.recyclerview)
//    RecyclerView recyclerview;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.nestedSV)
    NestedScrollView nestedSV;

    @BindView(R.id.recyclerview_tuijian)
    RecyclerView recyclerview_tuijian;

    @BindView(R.id.recyclerview_laoshi)
    RecyclerView recyclerview_laoshi;

    @BindView(R.id.recyclerview_haoke)
    RecyclerView recyclerview_haoke;

    @BindView(R.id.recyclerview_kecheng)
    RecyclerView recyclerview_kecheng;


    @BindView(tv_notice)
    ENoticeView noticeView;

    @BindView(R.id.banner)
    XBanner banner1;

    private DynamicAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private List<Dynamic> list=new ArrayList<>();
    private List titles = new ArrayList<>();

    private  List<Course> classesList = new ArrayList<>();
    private List<String> list1=new ArrayList<>();
    private List<Notice> newsList=new ArrayList<>();
    private List<Teacher> teachersList=new ArrayList<>();
    private List<Course> teacherFileFrees=new ArrayList<>();
    private List<Course> recommendedtodays = new ArrayList<>();
    private String ban;
    private List<banner> bannerList=new ArrayList<>();

    private TeacherAdapter teacherAdapter;//授课老师
    private RecommendAdapter recommendAdapter;//今日推荐
    private TeacherFileBoutiqueAdapter teacherFileBoutiqueAdapter;//精品课程
    private TeacherFileFeeAdapter teacherFileFeeAdapter;//免费好客

    private banner banners;
    private Notice notice;
    private Teacher teacher;
    private Course teacherFileFree;
    private Course classes;
    private Course recommendedtoday;


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
        setPullRefresher();
        initRecyclerview_tuijian();
        initRecyclerview_laoshi();
        initRecyclerview_haoke();
        initRecyclerview_kecheng();
        new Thread(new Runnable() {
            @Override
            public void run() {
//                currentpage=1;
//                getData(refreshLayout,1);
                getData(refreshLayout);
            }
        }).start();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    currentpage=1;
//                    getData(refreshLayout,1);
                    getData(refreshLayout);
                }
            }).start();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        banner1.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner1.stopAutoPlay();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
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
//                scrollView.setNeedScroll(false);
//                list.clear();
//                currentpage=1;
                getData(refreshlayout);
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

    @OnClick({R.id.Ll_live,R.id.Ll_advisor,R.id.Ll_index,R.id.Ll_class,R.id.Ll_base,R.id.Tv_fee,R.id.Tv_class})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Ll_live:
                startActivity(new Intent().setClass(getActivity(), LiveItemActivity.class));
                break;
            case R.id.Ll_advisor://免费好课
                startActivity(new Intent().setClass(getActivity(), FreeCourseActivity.class));
                break;
            case R.id.Tv_fee://免费好课
                startActivity(new Intent().setClass(getActivity(), FreeCourseActivity.class));
                break;
            case R.id.Ll_index://指标优选
                startActivity(new Intent().setClass(getActivity(), VideoWebViewActivity.class));
                break;
            case R.id.Ll_class://资讯热文
                startActivity(new Intent().setClass(getActivity(), InformationActivity.class));
                break;
            case R.id.Ll_base://精品课程
                startActivity(new Intent().setClass(getActivity(), BoutiqueActivity.class));
                break;
            case R.id.Tv_class://精品课程
                startActivity(new Intent().setClass(getActivity(), BoutiqueActivity.class));
                break;
        }
    }


    private void getpicture(){
//        adapter1.notifyDataSetChanged();
        // 为XBanner绑定数据
        banner1.setData(list1, titles);
        // XBanner适配数据
        banner1.setmAdapter(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, View view, int position) {
                Glide.with(getActivity()).load(list1.get(position)).into((ImageView) view);
            }
        });
        // 设置XBanner的页面切换特效
        banner1.setPageTransformer(Transformer.Default);
        // 设置XBanner页面切换的时间，即动画时长
        banner1.setPageChangeDuration(1000);

        // XBanner中某一项的点击事件
        banner1.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, int position) {
                if(TextUtils.isEmpty(bannerList.get(position).getUrl())){

                }else {
                    startActivity(new Intent().setClass(getActivity(), BannerUrlActivity.class).putExtra("url",bannerList.get(position).getUrl()));
                }
//                startActivity(new Intent().setClass(getActivity(), BannerUrlActivity.class).putExtra("url",bannerList.get(position).getUrl()));
//                Toast.makeText(getActivity(), "点击了第" + (position + 1) + "张图片", Toast.LENGTH_SHORT).show();
            }
        });
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
    }

    private void getData(final RefreshLayout refreshLayout){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"zgHomePage.do");
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
                        JSONArray jsonArray=json.getJSONArray("bannerList");
                        list1.clear();
                        classesList.clear();
                        teachersList.clear();
                        newsList.clear();
                        recommendedtodays.clear();
                        teacherFileFrees.clear();
                        bannerList.clear();
                        for (int i=0;i<jsonArray.length();i++){
                            banners=new banner();
                            banners.setPicpath(jsonArray.getJSONObject(i).getString("picpath"));
                            banners.setUrl(jsonArray.getJSONObject(i).getString("url"));
                            banners.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            banners.setBsort(jsonArray.getJSONObject(i).getInt("bsort"));
                            banners.setBtype(jsonArray.getJSONObject(i).getInt("btype"));
                            banners.setStatus(jsonArray.getJSONObject(i).getInt("status"));
                            bannerList.add(banners);
                            ban=jsonArray.getJSONObject(i).getString("picpath");
                            list1.add(ban);
                            titles.add("");
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
                            recommendedtoday.setIscharge(array3.getJSONObject(n).getInt("ischarge"));
                            recommendedtoday.setPicpath(array3.getJSONObject(n).getString("recommendpicpath"));
                            recommendedtoday.setTitle(array3.getJSONObject(n).getString("title"));
                            recommendedtoday.setLecturer(array3.getJSONObject(n).getString("lecturer"));
                            recommendedtoday.setPrice(array3.getJSONObject(n).getDouble("price"));
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

                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recommendAdapter.notifyDataSetChanged();
                                teacherAdapter.notifyDataSetChanged();
                                teacherFileBoutiqueAdapter.notifyDataSetChanged();
                                teacherFileFeeAdapter.notifyDataSetChanged();
                                getpicture();
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
                                startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
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
                if(getActivity()==null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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


    @Override
    public void onDestroy() {
        super.onDestroy();
//        tv_notice.releaseResources();
    }
}
