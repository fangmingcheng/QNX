package zgt.com.example.myzq.model.common.personal_center.mycourse;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import zgt.com.example.myzq.bean.classes.Mycourse;
import zgt.com.example.myzq.model.common.adapter.courseAdapter.MyCourseAdapter;
import zgt.com.example.myzq.model.common.course.CourseDetailActivity;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.model.common.order.ZBOrderDetailActivity;
import zgt.com.example.myzq.model.common.personal_center.RiskTestActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.StatusBarUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class MyCoursesActivity extends BaseActivity {

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private LinearLayoutManager mLayoutManager;
    private int currentpage=1,totalcount=0;
    private MyCourseAdapter adapter;

    private Mycourse course;

    private List<Mycourse> list = new ArrayList<>();
    @Override
    public void initToolBar() {

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_my_courses;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.statusBarLightMode(this);
        initRecyclerView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentpage=1;
                getData(refreshLayout,1);
            }
        }).start();
    }

    private void initRecyclerView(){
//        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
//        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        mLayoutManager = new LinearLayoutManager(this);
        //调整RecyclerView的排列方向
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(mLayoutManager);

        adapter=new MyCourseAdapter(this,list);
        recyclerview.setAdapter(adapter);
        setPullRefresher();


        adapter.setOnItemClickListener(new MyCourseAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view,MyCourseAdapter.ViewName viewName,int position) {
//                int position = recyclerview.getChildAdapterPosition(view);
                switch (view.getId()){
                    case R.id.Ll_content:
                        startActivity(new Intent().setClass(MyCoursesActivity.this, CourseDetailActivity.class).putExtra("uuid",list.get(position).getTypeid()).putExtra("index",1));
                        break;
                    case R.id.Tv_status:
                        intoOrder(position);
//                        startActivity(new Intent().setClass(MyCoursesActivity.this, OrderDetaiilActivity.class).putExtra("course",list.get(position)).putExtra("status","2"));
                        break;
                }

            }
        });

    }

    private void setPullRefresher(){
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(false));
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
                    ToastUtil.showShortToast(MyCoursesActivity.this,"数据已加载完毕");
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
//                cursorAnim(1);
                break;
        }
    }

    private void getData(final RefreshLayout refreshLayout,final int num){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"memberProductPageList.do");
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
                            course=new Mycourse();
                            course.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            course.setTitle(jsonArray.getJSONObject(i).getString("title"));
                            course.setExpiredate(jsonArray.getJSONObject(i).getString("expiredate"));
                            course.setPrice(jsonArray.getJSONObject(i).getDouble("price"));
                            course.setRealprice(jsonArray.getJSONObject(i).getDouble("realprice"));
                            course.setPicpath(jsonArray.getJSONObject(i).getString("picpath"));
                            course.setPricelimit(jsonArray.getJSONObject(i).getInt("pricelimit"));
                            course.setMemberid(jsonArray.getJSONObject(i).getString("memberid"));
                            course.setLecturer(jsonArray.getJSONObject(i).getString("lecturer"));
                            course.setTypeid(jsonArray.getJSONObject(i).getString("typeid"));
                            course.setStatus(jsonArray.getJSONObject(i).getInt("status"));
                            course.setProducttype(jsonArray.getJSONObject(i).getInt("producttype"));
                            list.add(course);
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
                                startActivity(new Intent().setClass(MyCoursesActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {

                        final String msg=jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(MyCoursesActivity.this, msg);
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
                        ToastUtil.showShortToast(MyCoursesActivity.this, "网络连接异常");
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

//    private void commonDialog(int status,String title,String content) {
//
//        new android.app.AlertDialog.Builder(MyCoursesActivity.this).setTitle(title).setMessage(content).setCancelable(false)
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        dialog.dismiss();
//                    }
//                }).setPositiveButton("立即前往", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                if(status==2){
//                    startActivity(new Intent().setClass(MyCoursesActivity.this, MyBasicInformationActivity.class).putExtra("status",2).putExtra("course",courseDetail));
//                }else if(status==3){
//                    startActivity(new Intent().setClass(MyCoursesActivity.this, RiskTestActivity.class).putExtra("status",2).putExtra("course",courseDetail));
//                }else if(status==4){
//                    startActivity(new Intent().setClass(MyCoursesActivity.this, RiskTestActivity.class).putExtra("status",2).putExtra("course",courseDetail));
//                }
//                dialog.dismiss();
//            }
//        }).show();
//
//    }

    private void commonDialog(String title,String content,int position) {
        new android.app.AlertDialog.Builder(MyCoursesActivity.this).setTitle(title).setMessage(content).setCancelable(false)
                .setNegativeButton("重新评测", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent().setClass(MyCoursesActivity.this, RiskTestActivity.class).putExtra("fileid",list.get(position).getTypeid()).putExtra("index",2).putExtra("type",1));
                        dialog.dismiss();
                    }
                }).setPositiveButton("前往购买", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent().setClass(MyCoursesActivity.this, ZBOrderDetailActivity.class).putExtra("fileid",list.get(position).getTypeid()).putExtra("index",2).putExtra("type",1));
                dialog.dismiss();
            }
        }).show();
    }

    private void intoOrder(int posotion){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"checkMemberInformation.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("fileid", list.get(posotion).getTypeid());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        commonDialog("您已完成风险评测","是否前往重新评测，或者直接购买",posotion);

                    }else if(a==5){
                        ToastUtil.showShortToast(MyCoursesActivity.this,"您已购买过该课程，无需重新购买");
                    }else if(a==7){
                        ToastUtil.showShortToast(MyCoursesActivity.this,"您有订单未确认，请联系客服");
                    } else if(a==8){
                        ToastUtil.showShortToast(MyCoursesActivity.this,"请勿重复下单");
                    }
//                    else if(a==2){
//                        commonDialog(2,"您的个人信息不完整请您完善","是否前往完善");
//                    } else if(a==3){
//                        commonDialog(3,"没有风控记录，请您前往填写风控记录","是否前往填写");
//                    }
//                    else if(a==4){
//                        commonDialog(4," C1(最低类别),不能购买","是否前往重新评测");
//                    }
                    else if(a==-1){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(MyCoursesActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {

                    }
                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(MyCoursesActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(MyCoursesActivity.this, "网络连接异常");
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
