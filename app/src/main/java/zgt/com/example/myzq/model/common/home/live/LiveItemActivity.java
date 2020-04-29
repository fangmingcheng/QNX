package zgt.com.example.myzq.model.common.home.live;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

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
import zgt.com.example.myzq.bean.live.LiveItems;
import zgt.com.example.myzq.bean.live.LiveTitle;
import zgt.com.example.myzq.model.common.adapter.live.LiveItemsAdapter;
import zgt.com.example.myzq.model.common.custom_view.MyPicImageView;
import zgt.com.example.myzq.model.common.home.BannerUrlActivity;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class LiveItemActivity extends BaseActivity {

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @BindView(R.id.Iv_picture)
    MyPicImageView Iv_picture;

    private LinearLayoutManager mLayoutManager;
    private int currentpage=1,totalcount=0;
    private LiveItemsAdapter adapter;

    private LiveItems liveItems;

    private List<LiveItems> list = new ArrayList();

    private List<LiveTitle> liveTitles = new ArrayList();

    String url;

    @Override
    public int getLayoutId() {
        return R.layout.activity_live_item;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        StatusBarUtil.statusBarLightMode(this);
        setPullRefresher();
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentpage=1;
                getData();
                getData(refreshLayout,1);

            }
        }).start();
    }

    @Override
    public void initToolBar() {

    }

    private void initRecyclerView(){
//        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
//        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        mLayoutManager = new LinearLayoutManager(this);
        //调整RecyclerView的排列方向
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(mLayoutManager);

        adapter=new LiveItemsAdapter(this,liveTitles);
        recyclerview.setAdapter(adapter);


//        adapter.setOnItemClickListener(new LiveItemsAdapter.OnItemClickListener() {
//            @Override
//            public void OnItemClick(View view) {
//                int position = recyclerview.getChildAdapterPosition(view);
//                getData(list.get(position).getUuid());
////                startActivity(new Intent().setClass(LiveItemActivity.this, CourseDetailActivity.class));
//
//            }
//        });

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
                    ToastUtil.showShortToast(LiveItemActivity.this,"数据已加载完毕");
                    refreshLayout.finishLoadMore(/*,false*/);
                }

            }
        });
    }

    private void createView(){
        List list1 = new ArrayList();
        liveTitles = new ArrayList<>();
//        String [] strings = new String[list.size()];
        for(int i = 0;i<list.size();i++){
            if(!TextUtils.isEmpty(list.get(i).getLivetime())){
                if(!list1.contains(list.get(i).getLivetime().substring(0,10))){
                    LiveTitle liveTitle = new LiveTitle();
                    liveTitle.data = list.get(i).getLivetime().substring(0,10);
                    liveTitle.time = list.get(i).getLivetime1();
                    liveTitles.add(liveTitle);
                    list1.add(list.get(i).getLivetime().substring(0,10));
                }
//                strings[i]= list.get(i).getLivetime().substring(0,10);
            }
        }
        for(int i = 0;i<liveTitles.size();i++){
            List<LiveItems> liveList = new ArrayList<>();
            for(int j = 0;j<list.size();j++){
                if(!TextUtils.isEmpty(list.get(j).getLivetime())){
                    if(liveTitles.get(i).data.equals(list.get(j).getLivetime().substring(0,10))){
                        liveList.add(list.get(j));
                    }
//                strings[i]= list.get(i).getLivetime().substring(0,10);
                }
            }
            liveTitles.get(i).list.addAll(liveList);
        }
        initRecyclerView();
//        adapter.notifyDataSetChanged();


//        List<List<LiveItems>> listList = new ArrayList<>(list1.size());
//        List<LiveItems> liveItems = new ArrayList<>();
//        Map<Integer,List<LiveItems>> tmpMap =new HashMap();
//        for(int j=0 ;j<list1.size();j++){
//            List<LiveItems> liveItems = new ArrayList<>();
//            for(int k=0 ; k<list.size();k++){
//                if(!TextUtils.isEmpty(list.get(k).getLivetime())){
//                    if(list.get(k).getLivetime().substring(0,10).equals(list1.get(j))){
//                        liveItems.add(list.get(k));
//                    }
//                }
//            }
////            listList.get(j).addAll(liveItems);
//            tmpMap.put(j,liveItems);
//        }

    }



    @OnClick({R.id.Iv_back,R.id.Iv_picture})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
                finish();
//                cursorAnim(1);
                break;
            case R.id.Iv_picture:
                if(TextUtils.isEmpty(url)){

                }else {
                    startActivity(new Intent().setClass(LiveItemActivity.this, BannerUrlActivity.class).putExtra("url",url));
                }
//                cursorAnim(1);
                break;
        }
    }

    private void getData(final RefreshLayout refreshLayout,final int num){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"zgliveList.do");
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
                            liveItems = new LiveItems();
                            liveItems.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            liveItems.setLivetime(jsonArray.getJSONObject(i).getString("livetime"));
                            liveItems.setKname(jsonArray.getJSONObject(i).getString("kname"));
                            liveItems.setLivetime1(jsonArray.getJSONObject(i).getInt("livetime1"));
                            liveItems.setTeachername(jsonArray.getJSONObject(i).getString("teachername"));
                            liveItems.setLivestatus(jsonArray.getJSONObject(i).getInt("livestatus"));
                            liveItems.setFileid(jsonArray.getJSONObject(i).getString("fileid"));
                            list.add(liveItems);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                createView();
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
                                startActivity(new Intent().setClass(LiveItemActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {

                        final String msg=jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(LiveItemActivity.this, msg);
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
                        ToastUtil.showShortToast(LiveItemActivity.this, "网络连接异常");
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


    private  void getData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"getLiveRoomBanner.do");
        requestParams.setConnectTimeout(30 * 1000);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    String msg=jsonObject.getString("message");
//                    Toast.makeText(CourseDetailActivity.this, msg, Toast.LENGTH_SHORT);
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        String picpath= json.getString("picpath");
                        url = json.getString("url");
                        Iv_picture.setImageURL(picpath);

                    } else{
                        Toast.makeText(LiveItemActivity.this, msg, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(LiveItemActivity.this, "数据获取异常", Toast.LENGTH_SHORT);


                } finally {

                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                ToastUtil.showShortToast( LiveItemActivity.this, "连接服务器失败");

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
