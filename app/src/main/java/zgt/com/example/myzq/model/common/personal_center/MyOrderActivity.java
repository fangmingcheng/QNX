package zgt.com.example.myzq.model.common.personal_center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

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

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.Order;
import zgt.com.example.myzq.model.common.adapter.MyOrderAdapter;
import zgt.com.example.myzq.model.common.home.CoursewareDetailActivity;
import zgt.com.example.myzq.model.common.home.GoldInvestmentActivity;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.SpaceItemDecoration;
import zgt.com.example.myzq.utils.ToastUtil;

public class MyOrderActivity extends BaseActivity {

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private MyOrderAdapter adapter;
    private List<Order> list=new ArrayList<>();
    private Order order;
    private int currentpage=1,totalcount=0;
    private LinearLayoutManager mLayoutManager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_order;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        initRecyclerView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getData(refreshLayout,1);
            }
        }).start();
    }

    private void initRecyclerView(){
//    使用默认的api绘制分割线
//        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
//    设置增加删除item的动画效果
        recyclerview.setItemAnimator(new DefaultItemAnimator());
//    瀑布流
//        recyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
//        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        mLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.addItemDecoration(new SpaceItemDecoration(10));
        adapter = new MyOrderAdapter(this, list);
        recyclerview.setAdapter(adapter);
        setPullRefresher();

        adapter.setOnItemClickListener(new MyOrderAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview.getChildAdapterPosition(view);
                if(list.get(position).getStatus()==1) {
                    if(list.get(position).getProducttype()==1) {
                        if ("基础版".equals(list.get(position).getTypename()) || "博弈版".equals(list.get(position).getTypename())) {
                            ToastUtil.showShortToast(MyOrderActivity.this, "抱歉，您没有该权限！");
                        } else {
                            if(TextUtils.isEmpty(list.get(position).getTeacherid())) {
                                ToastUtil.showShortToast(MyOrderActivity.this, "版本尚未指定！");
                            }else {
                                startActivity(new Intent().setClass(MyOrderActivity.this, GoldInvestmentActivity.class).putExtra("uuid", list.get(position).getTeacherid()));
                            }
                        }
                    }else if(list.get(position).getProducttype()==2) {
                        startActivity(new Intent().setClass(MyOrderActivity.this, CoursewareDetailActivity.class).putExtra("uuid", list.get(position).getTypeid()));
                    }
                }else {
                    ToastUtil.showShortToast(MyOrderActivity.this,"抱歉，您没有改权限！");
                }
            }
        });
    }

    private void setPullRefresher(){
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(MyOrderActivity.this).setShowBezierWave(false));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(MyOrderActivity.this).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //在这里执行上拉刷新时的具体操作(网络请求、更新UI等)
                list.clear();
                currentpage=1;
                getData(refreshlayout,1);
//                adapter.refresh(newList);
//                refreshlayout.finishRefresh(2000/*,false*/);
                //不传时间则立即停止刷新    传入false表示刷新失败
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(totalcount>(currentpage-1)*20){
                    getData(refreshLayout,2);
                }else {
                    ToastUtil.showShortToast(MyOrderActivity.this,"数据已加载完毕");
                    refreshLayout.finishLoadMore(/*,false*/);
                }

            }
        });
    }

    @OnClick({R.id.Iv_back})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
                finish();
                break;

        }
    }

    private void getData(final RefreshLayout refreshLayout,final int num){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"getMyOrderList.do");
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
                            order=new Order();
                            order.setTypename(jsonArray.getJSONObject(i).getString("typename"));
                            order.setStartdate(jsonArray.getJSONObject(i).getString("startdate"));
                            order.setEnddate(jsonArray.getJSONObject(i).getString("enddate"));
                            order.setMemberid(jsonArray.getJSONObject(i).getString("memberid"));
                            order.setTypeid(jsonArray.getJSONObject(i).getString("typeid"));
                            order.setFtitle(jsonArray.getJSONObject(i).getString("ftitle"));
                            order.setOrderno(jsonArray.getJSONObject(i).getString("orderno"));
                            order.setPaytype(jsonArray.getJSONObject(i).getInt("paytype"));
                            order.setPrice(jsonArray.getJSONObject(i).getString("price"));
                            order.setProducttype(jsonArray.getJSONObject(i).getInt("producttype"));
                            order.setRealprice(jsonArray.getJSONObject(i).getString("realprice"));
                            order.setStatus(jsonArray.getJSONObject(i).getInt("status"));
                            order.setTeacherid(jsonArray.getJSONObject(i).getString("teacherid"));
                            order.setTeachername(jsonArray.getJSONObject(i).getString("teachername"));
                            order.setTheadimg(jsonArray.getJSONObject(i).getString("theadimg"));
                            order.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            list.add(order);
                        }
                       runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } else if(a==-1){
                      runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(MyOrderActivity.this,LoginActivity.class));
                                finish();
//                                new AlertDialog.Builder(MyOrderActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                startActivity(new Intent().setClass(MyOrderActivity.this,LoginActivity.class));
//                                               finish();
//                                                dialog.dismiss();
//                                            }
//                                        }).create().show();
                            }
                        });
                    }else {
                        final String msg=jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(MyOrderActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                   runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(MyOrderActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(MyOrderActivity.this, "网络连接异常");
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
