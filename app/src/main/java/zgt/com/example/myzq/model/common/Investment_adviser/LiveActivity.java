package zgt.com.example.myzq.model.common.Investment_adviser;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.header.MaterialHeader;
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
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.Live_items;
import zgt.com.example.myzq.model.common.adapter.Live_NoticeAdapter;
import zgt.com.example.myzq.model.common.adapter.Live_reviewAdapter;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class LiveActivity extends BaseActivity {

    @BindView(R.id.refreshLayout_live)
    RefreshLayout refreshLayout_live;
    @BindView(R.id.recyclerview_live)
    RecyclerView recyclerview_live;

    @BindView(R.id.refreshLayout_notice)
    RefreshLayout refreshLayout_notice;
    @BindView(R.id.recyclerview_notice)
    RecyclerView recyclerview_notice;

    @BindView(R.id.refreshLayout_review)
    RefreshLayout refreshLayout_review;
    @BindView(R.id.recyclerview_review)
    RecyclerView recyclerview_review;

    @BindView(R.id.Ll_live)
    LinearLayout Ll_live;
    @BindView(R.id.Ll_notice)
    LinearLayout Ll_notice;
    @BindView(R.id.Ll_review)
    LinearLayout Ll_review;

    private Live_NoticeAdapter adapter;
    private Live_reviewAdapter adapter1;
    private Live_NoticeAdapter adapter2;
    private LinearLayoutManager mLayoutManager;
    private List<Live_items> list=new ArrayList<>();
    private List<Live_items> list1=new ArrayList<>();
    private List<Live_items> list2=new ArrayList<>();
    private Live_items live_items;
    private int currentpage=1,totalcount=0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_live;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        initRecyclerView();
        initRecyclerView1();
        initRecyclerView2();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getData(refreshLayout_review,1);
            }
        }).start();
    }

    @Override
    public void initToolBar() {

    }

    private void initRecyclerView2(){
//    使用默认的api绘制分割线
        recyclerview_live.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
//    设置增加删除item的动画效果
        recyclerview_live.setItemAnimator(new DefaultItemAnimator());
//    瀑布流
        recyclerview_live.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerview_live.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        mLayoutManager = new LinearLayoutManager(this);
        recyclerview_live.setLayoutManager(mLayoutManager);
        adapter2 = new Live_NoticeAdapter(this, list2);
        recyclerview_live.setAdapter(adapter2);
        setPullRefresher2();
//        //设置 Footer 为 球脉冲
        adapter2.setOnItemClickListener(new Live_NoticeAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview_live.getChildAdapterPosition(view);
                if(TextUtils.isEmpty(SPUtil.getToken())) {
                    startActivity(new Intent().setClass(LiveActivity.this,LoginActivity.class));
                }else {
                    startActivity(new Intent().setClass(LiveActivity.this, LiveDetailActivity.class).putExtra("uuid", list2.get(position).getUuid()).putExtra("status", "1"));
                }
            }
        });

    }

    private void initRecyclerView(){
//    使用默认的api绘制分割线
        recyclerview_notice.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
//    设置增加删除item的动画效果
        recyclerview_notice.setItemAnimator(new DefaultItemAnimator());
//    瀑布流
        recyclerview_notice.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerview_notice.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        mLayoutManager = new LinearLayoutManager(this);
        recyclerview_notice.setLayoutManager(mLayoutManager);
        adapter = new Live_NoticeAdapter(this, list);
        recyclerview_notice.setAdapter(adapter);
        setPullRefresher();
//        //设置 Footer 为 球脉冲
        adapter.setOnItemClickListener(new Live_NoticeAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview_notice.getChildAdapterPosition(view);
                if(TextUtils.isEmpty(SPUtil.getToken())) {
                    startActivity(new Intent().setClass(LiveActivity.this,LoginActivity.class));
                }else {
                    startActivity(new Intent().setClass(LiveActivity.this, LiveDetailActivity.class).putExtra("uuid", list.get(position).getUuid()).putExtra("status", "1"));
                }
            }
        });

    }

    private void initRecyclerView1(){
//    使用默认的api绘制分割线
        recyclerview_review.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
//    设置增加删除item的动画效果
        recyclerview_review.setItemAnimator(new DefaultItemAnimator());
//    瀑布流
        recyclerview_review.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerview_review.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        mLayoutManager = new LinearLayoutManager(this);
        recyclerview_review.setLayoutManager(mLayoutManager);
        adapter1 = new Live_reviewAdapter(this, list1);
        recyclerview_review.setAdapter(adapter1);
        setPullRefresher1();
//        //设置 Footer 为 球脉冲
        adapter1.setOnItemClickListener(new Live_reviewAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview_review.getChildAdapterPosition(view);
                if(TextUtils.isEmpty(SPUtil.getToken())) {
                    startActivity(new Intent().setClass(LiveActivity.this,LoginActivity.class));
                }else {
                    startActivity(new Intent().setClass(LiveActivity.this, LiveDetailActivity.class).putExtra("uuid",list1.get(position).getUuid()).putExtra("status","1"));
                }

            }
        });

    }

    private void setPullRefresher2(){
        //设置 Header 为 Material风格
        refreshLayout_live.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        refreshLayout_live.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout_live.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //在这里执行上拉刷新时的具体操作(网络请求、更新UI等)
                list.clear();
                list1.clear();
                list2.clear();
                currentpage=1;
                getData(refreshlayout,1);
                //不传时间则立即停止刷新    传入false表示刷新失败
            }
        });

        refreshLayout_live.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(totalcount>(currentpage-1)*20){
                    getData(refreshLayout,2);
                }else {
                    ToastUtil.showShortToast(LiveActivity.this,"数据已加载完毕");
                    refreshLayout.finishLoadMore(/*,false*/);
                }

            }
        });
    }

    private void setPullRefresher(){
        //设置 Header 为 Material风格
        refreshLayout_notice.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        refreshLayout_notice.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout_notice.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //在这里执行上拉刷新时的具体操作(网络请求、更新UI等)
                list.clear();
                list1.clear();
                list2.clear();
                currentpage=1;
                getData(refreshlayout,1);
                //不传时间则立即停止刷新    传入false表示刷新失败
            }
        });

        refreshLayout_notice.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(totalcount>(currentpage-1)*20){
                    getData(refreshLayout,2);
                }else {
                    ToastUtil.showShortToast(LiveActivity.this,"数据已加载完毕");
                    refreshLayout.finishLoadMore(/*,false*/);
                }

            }
        });
    }

    private void setPullRefresher1(){
        //设置 Header 为 Material风格
        refreshLayout_review.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        refreshLayout_review.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout_review.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //在这里执行上拉刷新时的具体操作(网络请求、更新UI等)
                list.clear();
                list1.clear();
                list2.clear();
                currentpage=1;
                getData(refreshlayout,1);
                //不传时间则立即停止刷新    传入false表示刷新失败
            }
        });

        refreshLayout_review.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(totalcount>(currentpage-1)*20){
                    getData(refreshLayout,2);
                }else {
                    ToastUtil.showShortToast(LiveActivity.this,"数据已加载完毕");
                    refreshLayout.finishLoadMore(/*,false*/);
                }

            }
        });
    }

    @OnClick({R.id.Iv_customer})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_customer:
                finish();
                break;
        }
    }

    private void getData(final RefreshLayout refreshLayout,final int num){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"publicLiveList.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("page", currentpage);
        requestParams.addParameter("rows", 20);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        totalcount=json.getInt("totalcount");
                        currentpage=json.getInt("currentpage")+1;
                        JSONArray jsonArray=json.getJSONArray("items");
                        for (int i=0;i<jsonArray.length();i++){
                            live_items=new Live_items();
                            live_items.setHeadimg(jsonArray.getJSONObject(i).getString("headimg"));
                            live_items.setContent(NoHTML(jsonArray.getJSONObject(i).getString("content")));
                            live_items.setHtmlcontent(jsonArray.getJSONObject(i).getString("content"));
                            live_items.setClick(jsonArray.getJSONObject(i).getString("click"));
                            live_items.setCreatetime(jsonArray.getJSONObject(i).getString("createtime"));
                            live_items.setTeachername(jsonArray.getJSONObject(i).getString("teachername"));
                            live_items.setTeacherid(jsonArray.getJSONObject(i).getString("teacherid"));
                            live_items.setChannelid(jsonArray.getJSONObject(i).getString("channelid"));
                            live_items.setFans(jsonArray.getJSONObject(i).getInt("fans"));
                            live_items.setKeyword(jsonArray.getJSONObject(i).getString("keyword"));
                            live_items.setLivetime(jsonArray.getJSONObject(i).getString("livetime"));
                            live_items.setPicpath(jsonArray.getJSONObject(i).getString("picpath"));
                            live_items.setStatus(jsonArray.getJSONObject(i).getString("status"));
                            live_items.setTitle(jsonArray.getJSONObject(i).getString("title"));
                            live_items.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            live_items.setVideourl(jsonArray.getJSONObject(i).getString("videourl"));
                            if("0".equals(live_items.getStatus())){
                                list.add(live_items);
                            }else if("1".equals(live_items.getStatus())){
                                list2.add(live_items);
                            }else if("2".equals(live_items.getStatus())){
                                list1.add(live_items);
                            }
                        }
                      runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                adapter1.notifyDataSetChanged();
                                adapter2.notifyDataSetChanged();
                                if(list2.size()==0){
                                    Ll_live.setVisibility(View.GONE);
                                }else if(list.size()==0){
                                    Ll_notice.setVisibility(View.GONE);
                                }else if(list1.size()==0){
                                    Ll_review.setVisibility(View.GONE);
                                }
                            }
                        });
                    } else if(a==-1){
                       runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(LiveActivity.this,LoginActivity.class));
                                finish();
//                                if(TextUtils.isEmpty(SPUtil.getToken())){
//                                    startActivity(new Intent().setClass(LiveActivity.this,LoginActivity.class));
//                                    finish();
//                                }else {
//                                    new AlertDialog.Builder(LiveActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    startActivity(new Intent().setClass(LiveActivity.this,LoginActivity.class));
//                                                    finish();
//                                                    dialog.dismiss();
//                                                }
//                                            }).create().show();
//                                }
                            }
                        });
                    }else {
                        final String msg=jsonObject.getString("message");
                      runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(LiveActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(LiveActivity.this, getString(R.string.login_parse_exc));
                        }
                    });

                } finally {
                    if(num==1){
                        refreshLayout.finishRefresh(false);
                    }else if(num==2){
                        refreshLayout.finishLoadMore(false);
                    }
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(num==1){
                    refreshLayout.finishRefresh(false);
                }else if(num==2){
                    refreshLayout.finishLoadMore(false);
                }

               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(LiveActivity.this, "网络连接异常");
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
