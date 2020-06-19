package zgt.com.example.myzq.model.common.home.znxg;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import zgt.com.example.myzq.bean.stock.AiStock;
import zgt.com.example.myzq.model.common.adapter.stock.SelectStockAdapter;
import zgt.com.example.myzq.model.common.home.h5.H5Activity;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.StatusBarUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class SelectStockActivity extends BaseActivity {

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private LinearLayoutManager mLayoutManager;
    private List<AiStock> list = new ArrayList<>();
    private AiStock aiStock;
//    private AiStockPrice aiStockPrice;

    String pid;


    private SelectStockAdapter adapter;
    @Override
    public void initToolBar() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_stock;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.statusBarLightMode(this);
        initRecyclerview();
        getData(refreshLayout);
    }


    private void initRecyclerview(){
        mLayoutManager = new LinearLayoutManager(this);
        //调整RecyclerView的排列方向
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(mLayoutManager);
        //设置item间距，30dp
//        recyclerview_tuijian.addItemDecoration(new SpaceItemDecoration(30));
        adapter=new SelectStockAdapter(this,list);
        recyclerview.setAdapter(adapter);
        recyclerview.setNestedScrollingEnabled(false);//NestedScrollView嵌套RecyclerView卡顿解决办法：
        setPullRefresher();
        adapter.setOnItemClickListener(new SelectStockAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview.getChildAdapterPosition(view);
                if(!TextUtils.isEmpty(SPUtil.getToken())){
                    startActivity(new Intent().setClass(SelectStockActivity.this, H5Activity.class).putExtra("url",SPUtil.getServerAddress()+"productguide.do?token="+SPUtil.getToken()+"&pid="+pid+"&uid="+list.get(position).getUuid()+"&type=0"));
                }else {
                    startActivity(new Intent().setClass(SelectStockActivity.this,LoginActivity.class));
//                    finish();
                }
            }
        });

    }

    private void setPullRefresher(){
        //设置 Header 为 Material风格
        refreshLayout.setPrimaryColors(Color.parseColor("#00000000"));
        refreshLayout.setRefreshHeader(new MaterialHeader(SelectStockActivity.this).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(SelectStockActivity.this).setSpinnerStyle(SpinnerStyle.Translate));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //在这里执行上拉刷新时的具体操作(网络请求、更新UI等)
                list.clear();
                getData(refreshlayout);
//                adapter.refresh(newList);
//                refreshlayout.finishRefresh(2000/*,false*/);
                //不传时间则立即停止刷新    传入false表示刷新失败
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                if(totalcount>(currentpage-1)*20){
//                    getData(refreshLayout,2);
//                }else {
                    ToastUtil.showShortToast(SelectStockActivity.this,"数据已加载完毕");
                    refreshLayout.finishLoadMore(/*,false*/);
//                }

            }
        });
    }

    @OnClick({R.id.Iv_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.Iv_back://
                finish();
                break;
        }
    }


    private void getData(final RefreshLayout refreshLayout){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"aiStockSelection.do");
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
                        pid=json.getString("pid");
//                        aiStockPrice= new AiStockPrice();
//                        aiStockPrice.setAiStockId(json.getString("aiStockId"));
//                        aiStockPrice.setIsbuy(json.getInt("isbuy"));
//                        aiStockPrice.setName(json.getString("name"));
//                        aiStockPrice.setPrice(json.getDouble("price"));
//                        aiStockPrice.setPricenum(json.getInt("pricenum"));
//                        aiStockPrice.setPriceunit(json.getInt("priceunit"));
//                        aiStockPrice.setRealprice(json.getDouble("realprice"));
                        JSONArray jsonArray=json.getJSONArray("aiStockList");
                        for (int i=0;i<jsonArray.length();i++){
                            aiStock = new AiStock();
                            aiStock.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            aiStock.setPname(jsonArray.getJSONObject(i).getString("pname"));
                            aiStock.setIntro(jsonArray.getJSONObject(i).getString("intro"));
                            aiStock.setPicpath(jsonArray.getJSONObject(i).getString("picpath"));
                            list.add(aiStock);
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
//                                if(TextUtils.isEmpty(SPUtil.getToken())){
//                                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                                    getActivity().finish();
//                                }else {
//                                    new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                                                    getActivity().finish();
//                                                    dialog.dismiss();
//                                                }
//                                            }).create().show();
//                                }
                                startActivity(new Intent().setClass(SelectStockActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {

                        final String msg=jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(SelectStockActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            ToastUtil.showShortToast(getActivity(), getString(R.string.login_parse_exc));
                        }
                    });

                } finally {
                    refreshLayout.finishRefresh(false);
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                refreshLayout.finishRefresh(false);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(SelectStockActivity.this, "网络连接异常");
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
