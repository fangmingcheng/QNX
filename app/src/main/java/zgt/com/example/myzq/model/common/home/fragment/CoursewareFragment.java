package zgt.com.example.myzq.model.common.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseFragment;
import zgt.com.example.myzq.bean.Courseware;
import zgt.com.example.myzq.model.common.adapter.CoursewareListAdapter;
import zgt.com.example.myzq.model.common.home.CoursewareDetailActivity;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

/**
 * 精品课件
 * A simple {@link Fragment} subclass.
 */
public class CoursewareFragment extends BaseFragment {

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private GridLayoutManager mLayoutManager;
    private List<Courseware> list=new ArrayList<>();
    private Courseware courseware;
    private CoursewareListAdapter adapter;
    private int currentpage=1,totalcount=0;

    public CoursewareFragment() {
        // Required empty public constructor
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
    @Override
    public int getLayoutId() {
        return R.layout.fragment_courseware;
    }
    private void initRecyclerView(){

//    设置增加删除item的动画效果
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerview.setLayoutManager(mLayoutManager);

        adapter = new CoursewareListAdapter(getActivity(), list);
        recyclerview.setAdapter(adapter);
        setPullRefresher();
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(getActivity()).setShowBezierWave(true));
//        //设置 Footer 为 球脉冲
        adapter.setOnItemClickListener(new CoursewareListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview.getChildAdapterPosition(view);
                startActivity(new Intent().setClass(getActivity(), CoursewareDetailActivity.class).putExtra("uuid",list.get(position).getUuid()));
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
                    ToastUtil.showShortToast(getActivity(),"数据已加载完毕");
                    refreshLayout.finishLoadMore(/*,false*/);
                }
            }
        });
    }

    private void getData(final RefreshLayout refreshLayout,final int num){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"school.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("page", currentpage);
        requestParams.addParameter("rows", 20);
        requestParams.addParameter("type", 0);
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
                            courseware=new Courseware();
                            courseware.setVideourl(jsonArray.getJSONObject(i).getString("fileurl"));
                            courseware.setDuties(jsonArray.getJSONObject(i).getString("duties"));
                            courseware.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            courseware.setTitle(jsonArray.getJSONObject(i).getString("title"));
                            courseware.setTeachername(jsonArray.getJSONObject(i).getString("teachername"));
                            courseware.setTeacherid(jsonArray.getJSONObject(i).getString("teacherid"));
                            courseware.setIntro(jsonArray.getJSONObject(i).getString("intro"));
                            courseware.setAddtime(jsonArray.getJSONObject(i).getString("addtime"));
//                            courseware.setClick(jsonArray.getJSONObject(i).getInt("click"));
                            courseware.setDescription(jsonArray.getJSONObject(i).getString("description"));
                            courseware.setHeadimg(jsonArray.getJSONObject(i).getString("headimg"));
                            courseware.setPrice(jsonArray.getJSONObject(i).getDouble("price"));
                            courseware.setIscharge(jsonArray.getJSONObject(i).getInt("ischarge"));
                            courseware.setIsend(jsonArray.getJSONObject(i).getInt("isend"));
                            courseware.setPicpath(jsonArray.getJSONObject(i).getString("picpath"));
                            courseware.setStatus(jsonArray.getJSONObject(i).getInt("status"));
                            courseware.setTotalnum(jsonArray.getJSONObject(i).getInt("totalnum"));
//                            courseware.setRealprice(jsonArray.getJSONObject(i).getDouble("videourl"));

                            list.add(courseware);
                        }

                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } else if(a==-1){
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
                                getActivity().finish();
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
                            ToastUtil.showShortToast(getActivity(), getString(R.string.login_parse_exc));
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

}
