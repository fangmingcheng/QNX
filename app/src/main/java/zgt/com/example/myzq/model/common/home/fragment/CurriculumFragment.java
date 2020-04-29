package zgt.com.example.myzq.model.common.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
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
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseFragment;
import zgt.com.example.myzq.bean.Training;
import zgt.com.example.myzq.model.common.adapter.CurriculumAdapter;
import zgt.com.example.myzq.model.common.home.TrainingBaseDetailActivity;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

/**
 * 证券课程
 * A simple {@link Fragment} subclass.
 */
public class CurriculumFragment extends BaseFragment {

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private LinearLayoutManager mLayoutManager;
    private List<Training> list=new ArrayList<>();
    private Training training;
    private CurriculumAdapter adapter;
    private int currentpage=1,totalcount=0;

    public CurriculumFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_curriculum;
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
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));
//    设置增加删除item的动画效果
        recyclerview.setItemAnimator(new DefaultItemAnimator());
//    瀑布流
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(mLayoutManager);
        adapter = new CurriculumAdapter(getActivity(), list);
        recyclerview.setAdapter(adapter);
        setPullRefresher();
        adapter.setOnItemClickListener(new CurriculumAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview.getChildAdapterPosition(view);
                startActivity(new Intent().setClass(getActivity(), TrainingBaseDetailActivity.class).putExtra("uuid",list.get(position).getUuid()).putExtra("status","2"));
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
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"trainingBases.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("page", currentpage);
        requestParams.addParameter("rows", 20);
        requestParams.addParameter("type", 1);
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
                            training=new Training();
                            training.setContent(NoHTML(jsonArray.getJSONObject(i).getString("content")));
                            training.setHtmlContent(jsonArray.getJSONObject(i).getString("content"));
                            training.setClick(jsonArray.getJSONObject(i).getString("click"));
                            training.setAuthor(jsonArray.getJSONObject(i).getString("author"));
                            training.setCreatetime(jsonArray.getJSONObject(i).getString("createtime"));
                            training.setFtitle(jsonArray.getJSONObject(i).getString("ftitle"));
                            training.setNclassid(jsonArray.getJSONObject(i).getString("nclassid"));
                            training.setPicpath(jsonArray.getJSONObject(i).getString("picpath"));
                            training.setSummary(jsonArray.getJSONObject(i).getString("summary"));
                            training.setTitle(jsonArray.getJSONObject(i).getString("title"));
                            training.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            training.setVideoflag(jsonArray.getJSONObject(i).getString("videoflag"));
                            training.setVideopath(jsonArray.getJSONObject(i).getString("videopath"));
                            list.add(training);
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

    public  String NoHTML(String Htmlstring) {
        String regMatchTag = "<[^>]*>|\n";

        //删除脚本
        String str= Htmlstring.replaceAll(regMatchTag,"").trim();
        return str;
    }

}
