package zgt.com.example.myzq.model.common.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
import zgt.com.example.myzq.bean.Live_listings;
import zgt.com.example.myzq.bean.Member;
import zgt.com.example.myzq.bean.PublicView;
import zgt.com.example.myzq.model.common.Investment_adviser.AdviserDetailActivity;
import zgt.com.example.myzq.model.common.Investment_adviser.Investment_centerActivity;
import zgt.com.example.myzq.model.common.Investment_adviser.LiveActivity;
import zgt.com.example.myzq.model.common.Investment_adviser.LiveDetailActivity;
import zgt.com.example.myzq.model.common.adapter.AdviserAdapter;
import zgt.com.example.myzq.model.common.adapter.LivelistingsAdapter;
import zgt.com.example.myzq.model.common.adapter.PublicViewAdapter;
import zgt.com.example.myzq.model.common.home.DetailActivity;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.SpaceItemDecoration;
import zgt.com.example.myzq.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvestmentFragment extends BaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.recyclerview_adviser)
    RecyclerView recyclerview_adviser;
    @BindView(R.id.recyclerview_detailed)
    RecyclerView recyclerview_detailed;

    @BindView(R.id.nestedSV)
    NestedScrollView nestedSV;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private LivelistingsAdapter livelistingsAdapter;
    private AdviserAdapter adviserAdapter;
    private PublicViewAdapter publicViewAdapter;
    private List<Live_listings> list=new ArrayList<>();
    private List<Member> memberList=new ArrayList<>();
    private List<PublicView> publicViews=new ArrayList<>();

    private Live_listings live_listings;
    private Member member;
    private PublicView publicView;

    ProgressDialog loadDialog ;
    private LinearLayoutManager mLayoutManager;

    private int currentpage=1,totalcount=0;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_investment;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        loadDialog = new ProgressDialog(getActivity());
        initRecyclerview();
        initRecyclerview_adviser();
        initRecyclerview_detailed();

        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentpage=1;
                getPublicViewData(refreshLayout,1);
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
                    getData();
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    currentpage=1;
                    getPublicViewData(refreshLayout,1);
                }
            }).start();
        }
    }

    private void initRecyclerview(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        //调整RecyclerView的排列方向
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(mLayoutManager);
        //设置item间距，30dp
        recyclerview.addItemDecoration(new SpaceItemDecoration(30));
        livelistingsAdapter=new LivelistingsAdapter(getActivity(),list);
        recyclerview.setAdapter(livelistingsAdapter);

        livelistingsAdapter.setOnItemClickListener(new LivelistingsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview.getChildAdapterPosition(view);
                if(TextUtils.isEmpty(SPUtil.getToken())) {
                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
                }else {
                    startActivity(new Intent().setClass(getActivity(), LiveDetailActivity.class).putExtra("uuid",list.get(position).getUuid()).putExtra("status","1"));
                }
            }
        });

    }

    private void initRecyclerview_adviser(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        //调整RecyclerView的排列方向
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview_adviser.setLayoutManager(mLayoutManager);
        //设置item间距，30dp
        recyclerview_adviser.addItemDecoration(new SpaceItemDecoration(30));
        adviserAdapter=new AdviserAdapter(getActivity(),memberList);
        recyclerview_adviser.setAdapter(adviserAdapter);

        adviserAdapter.setOnItemClickListener(new AdviserAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview_adviser.getChildAdapterPosition(view);
                startActivity(new Intent().setClass(getActivity(), AdviserDetailActivity.class).putExtra("uuid",memberList.get(position).getUuid()));
            }
        });
    }

    private void initRecyclerview_detailed(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        //调整RecyclerView的排列方向
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_detailed.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));
        recyclerview_detailed.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recyclerview_detailed.setLayoutManager(mLayoutManager);
        publicViewAdapter=new PublicViewAdapter(getActivity(),publicViews);
        recyclerview_detailed.setAdapter(publicViewAdapter);

        recyclerview_detailed.setNestedScrollingEnabled(false);//NestedScrollView嵌套RecyclerView卡顿解决办法：
        publicViewAdapter.setOnItemClickListener(new PublicViewAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview_detailed.getChildAdapterPosition(view);
                startActivity(new Intent().setClass(getActivity(),DetailActivity.class).putExtra("content",publicViews.get(position).getHtmlcontent()).putExtra("title",publicViews.get(position).getTitle())
                        .putExtra("time",publicViews.get(position).getAddtime()).putExtra("name",publicViews.get(position).getTeachername()).putExtra("num",publicViews.get(position).getClick())
                        .putExtra("status","1").putExtra("uuid",publicViews.get(position).getUuid()));
//                startActivity(new Intent().setClass(getActivity(), TrainingBaseDetailActivity.class).putExtra("uuid",publicViews.get(position).getUuid()).putExtra("status","1"));
            }
        });


        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //判断是否滑到的底部
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Log.e("InvestmentFragment","我到底了");
                    if(totalcount>(currentpage-1)*20){
                        getPublicViewData(refreshLayout,2);
                    }else {
                        ToastUtil.showShortToast(getActivity(),"数据已加载完毕");
                        refreshLayout.finishLoadMore(/*,false*/);
                    }
                }
            }
        });
    }

    private void setPullRefresher(){
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(getActivity()).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //在这里执行上拉刷新时的具体操作(网络请求、更新UI等)
                publicViews.clear();
                currentpage=1;
                getPublicViewData(refreshlayout,1);
//                adapter.refresh(newList);
//                refreshlayout.finishRefresh(2000/*,false*/);
                //不传时间则立即停止刷新    传入false表示刷新失败
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(totalcount>(currentpage-1)*20){
                    getPublicViewData(refreshLayout,2);
                }else {
                    ToastUtil.showShortToast(getActivity(),"数据已加载完毕");
                    refreshLayout.finishLoadMore(/*,false*/);
                }

            }
        });
    }

    @OnClick({R.id.Tv_more,R.id.Tv_more_adviser})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Tv_more:
                startActivity(new Intent(getActivity(), LiveActivity.class));

                break;
            case R.id.Tv_more_adviser:
                startActivity(new Intent(getActivity(), Investment_centerActivity.class));
                break;

        }
    }




    @Override
    public void initToolBar() {

    }

    public InvestmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getPublicViewData(final RefreshLayout refreshLayout,final int status){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"getPublicViewList.do");
        requestParams.setConnectTimeout(60 * 1000);
//        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("page", currentpage);
        requestParams.addParameter("rows",20);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if(status==1){
                        publicViews.clear();
                    }
                    if (a==1) {
                        JSONArray array=json.getJSONArray("items");
                        currentpage=json.getInt("currentpage")+1;
                        totalcount=json.getInt("totalcount");
                        for(int i=0;i<array.length();i++){
                            publicView=new PublicView();
                            publicView.setUuid(array.getJSONObject(i).getString("uuid"));
                            publicView.setSummary(array.getJSONObject(i).getString("summary"));
                            publicView.setHeadimg(array.getJSONObject(i).getString("headimg"));
                            publicView.setAddtime(array.getJSONObject(i).getString("addtime"));
                            publicView.setClick(array.getJSONObject(i).getString("click"));
                            publicView.setHtmlcontent(array.getJSONObject(i).getString("content"));
                            publicView.setContent(NoHTML(array.getJSONObject(i).getString("content")));
                            publicView.setTeachername(array.getJSONObject(i).getString("teachername"));
                            publicView.setTitle(array.getJSONObject(i).getString("title"));
                            publicViews.add(publicView);
                        }
                        if (getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                publicViewAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                    }

                } catch (JSONException e) {
                    if (getActivity()==null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(getActivity(), "解析异常");
                        }
                    });

                } finally {
                    if(status==1){
                        refreshLayout.finishRefresh(false);
                    }else if(status==2){
                        refreshLayout.finishLoadMore(false);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(status==1){
                    refreshLayout.finishRefresh(false);
                }else if(status==2){
                    refreshLayout.finishLoadMore(false);
                }
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
    private void getData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"comment.do");
        requestParams.setConnectTimeout(60 * 1000);
//        requestParams.addParameter("token", SPUtil.getToken());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if (a==1) {
                        list.clear();
                        JSONArray array=json.getJSONArray("publicLiveList");
                        for(int i=0;i<array.length();i++){
                            live_listings=new Live_listings();
                            live_listings.setUuid(array.getJSONObject(i).getString("uuid"));
                            live_listings.setKeyword(array.getJSONObject(i).getString("keyword"));
                            live_listings.setChannelid(array.getJSONObject(i).getString("channelid"));
                            live_listings.setClick(array.getJSONObject(i).getInt("click"));
                            live_listings.setContent(NoHTML(array.getJSONObject(i).getString("content")));
                            live_listings.setHtmlcontent(array.getJSONObject(i).getString("content"));
//                            live_listings.setFans(array.getJSONObject(i).getInt("fans"));
                            live_listings.setHeadimg(array.getJSONObject(i).getString("headimg"));
                            live_listings.setLivetime(array.getJSONObject(i).getString("livetime"));
                            live_listings.setPicpath(array.getJSONObject(i).getString("picpath"));
                            live_listings.setStatus(array.getJSONObject(i).getInt("status"));
                            live_listings.setTeachername(array.getJSONObject(i).getString("teachername"));
                            live_listings.setTitle(array.getJSONObject(i).getString("title"));
                            live_listings.setVideourl(array.getJSONObject(i).getString("videourl"));
                            list.add(live_listings);
                        }
                        JSONArray jsonArray=json.getJSONArray("teacherList");
                        memberList.clear();
                        for (int j=0;j<jsonArray.length();j++) {
                            member = new Member();
                            member.setDuties(jsonArray.getJSONObject(j).getString("duties"));
                            member.setUsername(jsonArray.getJSONObject(j).getString("username"));
                            member.setUuid(jsonArray.getJSONObject(j).getString("uuid"));
                            member.setTruename(jsonArray.getJSONObject(j).getString("truename"));
                            member.setSex(jsonArray.getJSONObject(j).getInt("sex"));
                            member.setEmail(jsonArray.getJSONObject(j).getString("email"));
                            member.setEnname(jsonArray.getJSONObject(j).getString("enname"));
                            member.setFans(jsonArray.getJSONObject(j).getInt("fans"));
                            member.setHeadimg(jsonArray.getJSONObject(j).getString("headimg"));
                            member.setIntro(jsonArray.getJSONObject(j).getString("intro"));
                            member.setKeyword(jsonArray.getJSONObject(j).getString("keyword"));
                            member.setMobile(jsonArray.getJSONObject(j).getString("mobile"));
                            member.setNickname(jsonArray.getJSONObject(j).getString("nickname"));
                            member.setQualification(jsonArray.getJSONObject(j).getString("qualification"));
                            member.setTel(jsonArray.getJSONObject(j).getString("tel"));
                            memberList.add(member);
                        }
                        if (getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                livelistingsAdapter.notifyDataSetChanged();
                                adviserAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                    }

                } catch (JSONException e) {
                    if (getActivity()==null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(getActivity(), "解析异常");
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

}
