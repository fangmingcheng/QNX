package zgt.com.example.myzq.model.common.home.lecturer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import zgt.com.example.myzq.bean.classes.Course;
import zgt.com.example.myzq.bean.lecturer.LecturerDetail;
import zgt.com.example.myzq.model.common.adapter.courseAdapter.CourseAdapter;
import zgt.com.example.myzq.model.common.course.CourseDetailActivity;
import zgt.com.example.myzq.model.common.custom_view.MyImageView;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class LecturerActivity extends BaseActivity {

    @BindView(R.id.Tv_name)
    TextView Tv_name;
    @BindView(R.id.Tv_phnoe)
    TextView Tv_phnoe;
    @BindView(R.id.Tv_code)
    TextView Tv_code;
    @BindView(R.id.Tv_content)
    TextView Tv_content;

    @BindView(R.id.Tv_content_1)
    TextView Tv_content1;

    @BindView(R.id.Iv_head)
    MyImageView Iv_head;

    @BindView(R.id.Iv_down)
    ImageView Iv_down;

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private LinearLayoutManager mLayoutManager;
    private int currentpage=1,totalcount=0;
    private CourseAdapter adapter;

    private List<Course> list = new ArrayList<>();

    private Course course;
    private LecturerDetail lecturerDetail;
    private String uuid;
    boolean flag=false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_lecturer;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        StatusBarUtil.statusBarLightMode(this);
        initRecyclerView();
        uuid=getIntent().getStringExtra("uuid");
        new Thread(new Runnable() {
            @Override
            public void run() {
                getViewDetial();
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

        adapter=new CourseAdapter(this,list);
        recyclerview.setAdapter(adapter);
        setPullRefresher();
        adapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview.getChildAdapterPosition(view);
                startActivity(new Intent().setClass(LecturerActivity.this, CourseDetailActivity.class).putExtra("uuid",list.get(position).getUuid()));
            }
        });

    }

    private void setPullRefresher(){
        //设置 Header 为 Material风格
        refreshLayout.setPrimaryColors(Color.parseColor("#00000000"));
        refreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(LecturerActivity.this).setSpinnerStyle(SpinnerStyle.Scale));

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
                    ToastUtil.showShortToast(LecturerActivity.this,"数据已加载完毕");
                    refreshLayout.finishLoadMore(/*,false*/);
                }

            }
        });
    }

    @Override
    public void initToolBar() {

    }

    private void setData(LecturerDetail lecturerDetail){
        Iv_head.setImageURL(lecturerDetail.getHeadimg());
        Tv_name.setText(lecturerDetail.getTruename());
        Tv_phnoe.setText("课程数量："+lecturerDetail.getTeacherFileNum());
        Tv_code.setText("证书编号："+lecturerDetail.getQualification());
        Tv_content.setText(lecturerDetail.getIntro());
        Tv_content1.setText(lecturerDetail.getIntro());
    }

    @OnClick({R.id.Iv_back,R.id.Iv_down})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
                finish();
//                cursorAnim(1);
                break;
            case R.id.Iv_down:
                if(flag){
                    Iv_down.setImageResource(R.mipmap.btn_guanbi);
                    Tv_content.setVisibility(View.VISIBLE);
                    Tv_content1.setVisibility(View.GONE);
                    flag=false;
                }else {
                    flag=true;
                    Iv_down.setImageResource(R.mipmap.btn_dakai);
                    Tv_content.setVisibility(View.GONE);
                    Tv_content1.setVisibility(View.VISIBLE);
                }
//                cursorAnim(1);
                break;
        }
    }

    private void getViewDetial(){
        RequestParams requestParams;
        requestParams = new RequestParams(SPUtil.getServerAddress() + "getTeacherByUuid.do");
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("uuid", uuid);
        requestParams.setConnectTimeout(60 * 1000);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if (a==1) {
                        lecturerDetail=new LecturerDetail();
                        lecturerDetail.setHeadimg(json.getString("headimg"));
                        lecturerDetail.setIntro(json.getString("intro"));
                        lecturerDetail.setNickname(json.getString("nickname"));
                        lecturerDetail.setQualification(json.getString("qualification"));
                        lecturerDetail.setSex(json.getInt("sex"));
                        lecturerDetail.setTeacherFileNum(json.getInt("teacherFileNum"));
                        lecturerDetail.setTruename(json.getString("truename"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setData(lecturerDetail);
                            }
                        });
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(LecturerActivity.this, LoginActivity.class));
                                finish();
//                                if(TextUtils.isEmpty(SPUtil.getToken())){
//                                    startActivity(new Intent().setClass(DetailActivity.this,LoginActivity.class));
//                                    finish();
//                                }else {
//                                    new AlertDialog.Builder(DetailActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    startActivity(new Intent().setClass(DetailActivity.this,LoginActivity.class));
//                                                    finish();
//                                                    dialog.dismiss();
//                                                }
//                                            }).create().show();
//                                }
                            }
                        });
                    }

                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(LecturerActivity.this, "解析异常");
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
                        ToastUtil.showShortToast(LecturerActivity.this, "网络连接异常");
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
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"getZgTeacherFileByTeacheridList.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("page", currentpage);
        requestParams.addParameter("rows", 20);
        requestParams.addParameter("teacherid", uuid);
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
                            course=new Course();
                            course.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            course.setTitle(jsonArray.getJSONObject(i).getString("title"));
                            course.setClick(jsonArray.getJSONObject(i).getInt("click"));
                            course.setPrice(jsonArray.getJSONObject(i).getDouble("price"));
                            course.setRealprice(jsonArray.getJSONObject(i).getDouble("realprice"));
                            course.setPicpath(jsonArray.getJSONObject(i).getString("picpath"));
                            course.setIscharge(jsonArray.getJSONObject(i).getInt("ischarge"));
                            course.setLecturer(jsonArray.getJSONObject(i).getString("lecturer"));
                            list.add(course);
                        }

                        LecturerActivity.this.runOnUiThread(new Runnable() {
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
                                startActivity(new Intent().setClass(LecturerActivity.this, LoginActivity.class));
                               finish();
                            }
                        });

                    }else {

                        final String msg=jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(LecturerActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                   runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(LecturerActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(LecturerActivity.this, "网络连接异常");
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
