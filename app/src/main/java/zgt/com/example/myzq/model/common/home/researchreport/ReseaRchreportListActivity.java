package zgt.com.example.myzq.model.common.home.researchreport;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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

import butterknife.BindView;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.stock.ReseaRchreportk;
import zgt.com.example.myzq.model.common.adapter.stock.ResearchReportAdapter;
import zgt.com.example.myzq.model.common.custom_view.MyImageBackgroundView;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.StatusBarUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class ReseaRchreportListActivity extends BaseActivity {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.image)
    MyImageBackgroundView image;

    @BindView(R.id.Tv_title_yanbao)
    TextView Tv_title_yanbao;

    @BindView(R.id.Tv_content)
    TextView Tv_content;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private ResearchReportAdapter adapter;
    private LinearLayoutManager mLayoutManager;

    private int currentpage=1,totalcount=0;

    private List<ReseaRchreportk> list =new ArrayList<>();
    private  ReseaRchreportk reseaRchreportk;
    private String pid;
    @Override
    public void initToolBar() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_resea_rchreport_list;
    }

    @Override
    protected void onResume() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentpage=1;
                getData(refreshLayout,1);
            }
        }).start();
        super.onResume();
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.statusBarLightMode(this);
        pid = getIntent().getStringExtra("pid");
        initRecyclerview();
    }

    private void setPullRefresher(){
        //设置 Header 为 Material风格
        refreshLayout.setPrimaryColors(Color.parseColor("#00000000"));
        refreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));

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
                    ToastUtil.showShortToast(ReseaRchreportListActivity.this,"数据已加载完毕");
                    refreshLayout.finishLoadMore(/*,false*/);
                }

            }
        });
    }

    private void initRecyclerview(){
        mLayoutManager = new LinearLayoutManager(this);
        //调整RecyclerView的排列方向
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(mLayoutManager);
        //设置item间距，30dp
//        recyclerview_laoshi.addItemDecoration(new SpaceItemDecoration(30));

        adapter=new ResearchReportAdapter(this,list);
        recyclerview.setAdapter(adapter);
        setPullRefresher();
        adapter.setOnItemClickListener(new ResearchReportAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview.getChildAdapterPosition(view);
                    startActivity(new Intent().setClass(ReseaRchreportListActivity.this, ReseaRchreportDetailActivity.class)
                            .putExtra("uuid",list.get(position).getUuid()).putExtra("pid",pid));
            }
        });

    }

    private void setData(String string,String string1,String string2){
        Tv_title_yanbao.setText(string);
        Tv_content.setText(string2);
        image.setType(1);
        image.setRoundRadius(3);
        image.setImageURL(string1);
    }

    private void getData(final RefreshLayout refreshLayout, final int num){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"getReseaRchreportList.do");
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
                        String pname= json.getString("pname");
                        String picpath= json.getString("picpath");
                        String intro= json.getString("intro");

                        JSONObject jsonObject1 = json.getJSONObject("reseaRchreportList");
                        totalcount=jsonObject1.getInt("totalCount");
                        currentpage=jsonObject1.getInt("currentPage")+1;

                        JSONArray jsonArray=jsonObject1.getJSONArray("items");//研报
                        for(int n = 0; n<jsonArray.length(); n++){
                            reseaRchreportk = new ReseaRchreportk();
                            reseaRchreportk.setUuid(jsonArray.getJSONObject(n).getString("uuid"));
                            reseaRchreportk.setTitle(jsonArray.getJSONObject(n).getString("title"));
                            reseaRchreportk.setAuthor(jsonArray.getJSONObject(n).getString("author"));
                            reseaRchreportk.setClick(jsonArray.getJSONObject(n).getInt("click"));
                            reseaRchreportk.setCreatetime(jsonArray.getJSONObject(n).getString("createtime"));
                            reseaRchreportk.setFtitle(jsonArray.getJSONObject(n).getString("ftitle"));
//                            reseaRchreportk.setContent(jsonArray.getJSONObject(n).getString("content"));
                            reseaRchreportk.setIstop(jsonArray.getJSONObject(n).getInt("istop"));
                            reseaRchreportk.setPicpath(jsonArray.getJSONObject(n).getString("picpath"));
                            reseaRchreportk.setSource(jsonArray.getJSONObject(n).getString("source"));
                            reseaRchreportk.setSummary(jsonArray.getJSONObject(n).getString("summary"));
                            reseaRchreportk.setStockexchange(jsonArray.getJSONObject(n).getString("stockexchange"));
                            reseaRchreportk.setStockname(jsonArray.getJSONObject(n).getString("stockname"));
                            reseaRchreportk.setStockcode(jsonArray.getJSONObject(n).getString("stockcode"));
                            list.add(reseaRchreportk);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setData(pname,picpath,intro);
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
                                startActivity(new Intent().setClass(ReseaRchreportListActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {

                        final String msg=jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(ReseaRchreportListActivity.this, msg);

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
                        ToastUtil.showShortToast(ReseaRchreportListActivity.this, "网络连接异常");
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
