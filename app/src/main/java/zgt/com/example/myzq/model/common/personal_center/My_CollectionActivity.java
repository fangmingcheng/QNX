package zgt.com.example.myzq.model.common.personal_center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.My_Collection;
import zgt.com.example.myzq.model.common.adapter.My_CollectionAdapter;
import zgt.com.example.myzq.model.common.home.DetailActivity;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class My_CollectionActivity extends BaseActivity implements My_CollectionAdapter.IonSlidingViewClickListener{

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private My_CollectionAdapter adapter;
    private List<My_Collection> list=new ArrayList<>();
    private My_Collection collection;
    private int currentpage=1,totalcount=0;
    private LinearLayoutManager mLayoutManager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my__collection;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        initRecyclerView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentpage=1;
                getData(refreshLayout,1);
            }
        }).start();
    }

    private void initRecyclerView(){
//    使用默认的api绘制分割线
        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
//    设置增加删除item的动画效果
        recyclerview.setItemAnimator(new DefaultItemAnimator());
//    瀑布流
        recyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        mLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(mLayoutManager);
        adapter = new My_CollectionAdapter(this, list);
        recyclerview.setAdapter(adapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());//设置控制Item增删的动画
        setPullRefresher();
    }

    private void setPullRefresher(){
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(My_CollectionActivity.this).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(My_CollectionActivity.this).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //在这里执行上拉刷新时的具体操作(网络请求、更新UI等)
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
                    ToastUtil.showShortToast(My_CollectionActivity.this,"数据已加载完毕");
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
    /**
     * item正文的点击事件
     *
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        //点击item正文的代码逻辑
        if(list.get(position).getType()==1) {
            startActivity(new Intent().setClass(My_CollectionActivity.this, DetailActivity.class).putExtra("status", list.get(position).getType()+"").putExtra("uuid", list.get(position).getPublicviewid()));
        }else if(list.get(position).getType()==2){
            startActivity(new Intent().setClass(My_CollectionActivity.this, DetailActivity.class).putExtra("status", list.get(position).getType()+"").putExtra("uuid", list.get(position).getPrivateviewid()));
        }

    }

    /**
     * item的左滑删除
     *
     * @param view
     * @param position
     */
    @Override
    public void onDeleteBtnCilck(View view, int position) {
        if(list.get(position).getType()==1){
            remove_collection(list.get(position).getPublicviewid(),position,1);
        }else if(list.get(position).getType()==2){
            remove_collection(list.get(position).getPrivateviewid(),position,2);
        }
    }



    private void getData(final RefreshLayout refreshLayout,final int num){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"getCollectionList.do");
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
                        if(num==1){
                            list.clear();
                        }
                        JSONArray jsonArray=json.getJSONArray("items");
                        for (int i=0;i<jsonArray.length();i++){
                            collection=new My_Collection();
                            collection.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            collection.setTeachername(jsonArray.getJSONObject(i).getString("teachername"));
                            collection.setAddtime(jsonArray.getJSONObject(i).getString("addtime"));
                            collection.setContent(NoHTML(jsonArray.getJSONObject(i).getString("content")));
                            collection.setHtmlContent(jsonArray.getJSONObject(i).getString("content"));
                            collection.setMemberid(jsonArray.getJSONObject(i).getString("memberid"));
                            collection.setCollectiontime(jsonArray.getJSONObject(i).getString("collectiontime"));
                            collection.setPrivateviewid(jsonArray.getJSONObject(i).getString("privateviewid"));
                            collection.setPublicviewid(jsonArray.getJSONObject(i).getString("publicviewid"));
                            collection.setTeacherheadimg(jsonArray.getJSONObject(i).getString("teacherheadimg"));
                            collection.setClick(jsonArray.getJSONObject(i).getInt("click"));
                            collection.setType(jsonArray.getJSONObject(i).getInt("type"));
                            list.add(collection);
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
                                startActivity(new Intent().setClass(My_CollectionActivity.this,LoginActivity.class));
                                finish();
//                                new AlertDialog.Builder(MyconcernActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                startActivity(new Intent().setClass(MyconcernActivity.this,LoginActivity.class));
//                                                finish();
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
                                ToastUtil.showShortToast(My_CollectionActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(My_CollectionActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(My_CollectionActivity.this, "网络连接异常");
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

    private void remove_collection(String uuid,final int position,int status){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"collection.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        if(status==1){
            requestParams.addParameter("publicviewid", uuid);
        }else if(status==2){
            requestParams.addParameter("privateviewid", uuid);
        }
        requestParams.addParameter("status", 2);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
//                        JSONObject json=jsonObject.getJSONObject("data");
                        list.remove(position);
                        adapter.closeMenu();
                        adapter.notifyDataSetChanged();
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(My_CollectionActivity.this,LoginActivity.class));
                                finish();
//                                new AlertDialog.Builder(MyconcernActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                startActivity(new Intent().setClass(MyconcernActivity.this,LoginActivity.class));
//                                                finish();
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
                                ToastUtil.showShortToast(My_CollectionActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(My_CollectionActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(My_CollectionActivity.this, "网络连接异常");
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
