package zgt.com.example.myzq.model.common.personal_center;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import zgt.com.example.myzq.bean.order.PurchaseRecord;
import zgt.com.example.myzq.model.common.adapter.order.PurchaseRecrdAdapter;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.model.common.order.FinishOrderActivity;
import zgt.com.example.myzq.model.common.order.OrderPaymentActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class Purchase_recordActivity extends BaseActivity {

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private LinearLayoutManager mLayoutManager;
    private int currentpage=1,totalcount=0;

    private List<PurchaseRecord> list =new ArrayList<>();
    private PurchaseRecord purchaseRecord;

    private PurchaseRecrdAdapter adapter;

    @Override
    public void initToolBar() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_purchase_record;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        StatusBarUtil.statusBarLightMode(this);
        initRecyclerView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentpage=1;
                getData(refreshLayout,1);
            }
        }).start();
    }

    @OnClick({R.id.Iv_back})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
                finish();
//                cursorAnim(1);
                break;
        }
    }

    private void initRecyclerView(){
        recyclerview.addItemDecoration(new DividerItemDecoration(Purchase_recordActivity.this,DividerItemDecoration.VERTICAL));
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        mLayoutManager = new LinearLayoutManager(this);
        //调整RecyclerView的排列方向
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(mLayoutManager);

        adapter=new PurchaseRecrdAdapter(this,list);
        recyclerview.setAdapter(adapter);
        setPullRefresher();
        adapter.setOnItemClickListener(new PurchaseRecrdAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, PurchaseRecrdAdapter.ViewName viewName, int position) {
                switch (view.getId()){
                    case R.id.Bt_commit:
                        startActivity(new Intent().setClass(Purchase_recordActivity.this, OrderPaymentActivity.class).putExtra("uuid",list.get(position).getUuid()).putExtra("price",list.get(position).getRealprice()*list.get(position).getAmount()).putExtra("index",3));
//                        getData(list.get(position).getTypeid(),list.get(position).getAmount());
                        break;
                    case R.id.Bt_delete:
                        commonDialog(position);
                        break;
                    case R.id.Ll_course:
                        if(list.get(position).getStatus()>0){
                            startActivity(new Intent().setClass(Purchase_recordActivity.this, FinishOrderActivity.class).putExtra("orderId",list.get(position).getUuid()).putExtra("status",2).putExtra("purchase_status",list.get(position).getStatus()));
                        }
                        break;
                }

            }
        });

    }

    private void commonDialog(int position) {

        new android.app.AlertDialog.Builder(Purchase_recordActivity.this).setTitle("取消订单").setMessage("是否取消该订单").setCancelable(false)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteOrder(list.get(position).getUuid(), position);
                dialog.dismiss();
            }
        }).show();


    }

    private void setPullRefresher(){
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(Purchase_recordActivity.this).setShowBezierWave(false));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(Purchase_recordActivity.this).setSpinnerStyle(SpinnerStyle.Scale));

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
                    ToastUtil.showShortToast(Purchase_recordActivity.this,"数据已加载完毕");
                    refreshLayout.finishLoadMore(/*,false*/);
                }

            }
        });
    }

    private void getData(String fileid,int num){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"doSubmitOrderAndroid.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("fileid", fileid);
        requestParams.addParameter("amount", num);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    String msg=jsonObject.getString("message");

                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        String orderid = json.getString("orderid");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(Purchase_recordActivity.this, OrderPaymentActivity.class).putExtra("uuid",orderid));

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
                                startActivity(new Intent().setClass(Purchase_recordActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {
                        ToastUtil.showShortToast(Purchase_recordActivity.this, msg);

//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                ToastUtil.showShortToast(OrderDetaiilActivity.this, msg);
//                            }
//                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
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


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(Purchase_recordActivity.this, "网络连接异常");
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

    private void deleteOrder(String uuid,int position){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"deleteOrder.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("uuid", uuid);


        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    String msg=jsonObject.getString("message");
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(Purchase_recordActivity.this, msg);
                                list.get(position).setStatus(-1);
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
                                startActivity(new Intent().setClass(Purchase_recordActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(Purchase_recordActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(Purchase_recordActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(Purchase_recordActivity.this, "网络连接异常");
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
                    if(num==1){
                        list.clear();
                    }
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        totalcount=json.getInt("totalcount");
                        currentpage=json.getInt("currentpage")+1;
                        JSONArray jsonArray=json.getJSONArray("items");
                        for (int i=0;i<jsonArray.length();i++){
                            purchaseRecord=new PurchaseRecord();
                            purchaseRecord.setTypeid(jsonArray.getJSONObject(i).getString("typeid"));
                            purchaseRecord.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            purchaseRecord.setTitle(jsonArray.getJSONObject(i).getString("title"));
                            purchaseRecord.setLecturer(jsonArray.getJSONObject(i).getString("lecturer"));
                            purchaseRecord.setPrice(jsonArray.getJSONObject(i).getDouble("price"));
                            purchaseRecord.setRealprice(jsonArray.getJSONObject(i).getDouble("realprice"));
                            purchaseRecord.setPicpath(jsonArray.getJSONObject(i).getString("picpath"));
                            purchaseRecord.setOrderno(jsonArray.getJSONObject(i).getString("orderno"));
                            purchaseRecord.setOrdertime(jsonArray.getJSONObject(i).getString("ordertime"));
                            purchaseRecord.setProducttype(jsonArray.getJSONObject(i).getInt("producttype"));
                            purchaseRecord.setStatus(jsonArray.getJSONObject(i).getInt("status"));
                            purchaseRecord.setAmount(jsonArray.getJSONObject(i).getInt("amount"));
//                            if(purchaseRecord.getStatus()==-1){
//                                continue;
//                            }
                            list.add(purchaseRecord);
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
                                startActivity(new Intent().setClass(Purchase_recordActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {

                        final String msg=jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(Purchase_recordActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(Purchase_recordActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(Purchase_recordActivity.this, "网络连接异常");
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
