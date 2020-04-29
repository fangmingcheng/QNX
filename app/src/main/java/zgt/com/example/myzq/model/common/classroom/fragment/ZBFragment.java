package zgt.com.example.myzq.model.common.classroom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import zgt.com.example.myzq.base.BaseFragment;
import zgt.com.example.myzq.bean.ZBBean;
import zgt.com.example.myzq.model.common.Investment_adviser.LiveDetailActivity;
import zgt.com.example.myzq.model.common.adapter.HGAdapter;
import zgt.com.example.myzq.model.common.adapter.ZBAdapter;
import zgt.com.example.myzq.model.common.custom_view.MyImageBackgroundView;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZBFragment extends BaseFragment {

    @BindView(R.id.recyclerview_live)
    RecyclerView recyclerview_live;
    @BindView(R.id.recyclerview_review)
    RecyclerView recyclerview_review;

    @BindView(R.id.Ll_live)
    FrameLayout Ll_live;
    @BindView(R.id.Iv_picture)
    MyImageBackgroundView Iv_picture;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.Ll_review)
    LinearLayout Ll_review;
    private ZBBean zbBean;
    private ZBBean hgBean;
    private LinearLayoutManager mLayoutManager;

    List<ZBBean> list = new ArrayList<>();
    List<ZBBean> list1 = new ArrayList<>();
    private ZBAdapter adapter;
    private HGAdapter adapter1;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_zb;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        initRecyclerview_live();
        initRecyclerview_review();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }).start();
    }
    private void initRecyclerview_live(){
        //    使用默认的api绘制分割线
//        recyclerview_live.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));
//        recyclerview_live.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
//        recyclerview_live.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        mLayoutManager = new LinearLayoutManager(getActivity());
        //调整RecyclerView的排列方向
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_live.setLayoutManager(mLayoutManager);

        //设置item间距，30dp
//        recyclerview_live.addItemDecoration(new SpaceItemDecoration(30));
        adapter=new ZBAdapter(getActivity(),list);
        recyclerview_live.setAdapter(adapter);
        adapter.setOnItemClickListener(new ZBAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview_live.getChildAdapterPosition(view);
                startActivity(new Intent().setClass(getActivity(), LiveDetailActivity.class).putExtra("uuid",list.get(position).getUuid()).putExtra("status","1"));
            }
        });
    }

    private void initRecyclerview_review(){
//        recyclerview_review.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
//        recyclerview_review.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        mLayoutManager = new LinearLayoutManager(getActivity());
        //调整RecyclerView的排列方向
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_review.setLayoutManager(mLayoutManager);
        //设置item间距，30dp

        adapter1=new HGAdapter(getActivity(),list1);
        recyclerview_review.setAdapter(adapter1);
        adapter1.setOnItemClickListener(new HGAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview_review.getChildAdapterPosition(view);
                startActivity(new Intent().setClass(getActivity(), LiveDetailActivity.class).putExtra("uuid",list1.get(position).getUuid()).putExtra("status","1"));
            }
        });
    }

    @OnClick({R.id.Ll_live})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Ll_live:
                if(list.size()>0) {
                    startActivity(new Intent().setClass(getActivity(), LiveDetailActivity.class).putExtra("uuid", list.get(0).getUuid()).putExtra("status", "1"));
                }else {
                    if(list1.size()>0){
                        startActivity(new Intent().setClass(getActivity(), LiveDetailActivity.class).putExtra("uuid", list1.get(0).getUuid()).putExtra("status", "1"));
                    }
                }
                break;
        }
    }


    private void getData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"zgPublicLiveList.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject json=jsonObject.getJSONObject("data");
                    int a=jsonObject.getInt("result");
                    if (a==1) {

                        JSONArray jsonArray=json.getJSONArray("publicLiveList");
                        for (int i=0;i<jsonArray.length();i++){
                            zbBean=new ZBBean();
                            zbBean.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            zbBean.setChannelid(jsonArray.getJSONObject(i).getString("channelid"));
                            zbBean.setTitle(jsonArray.getJSONObject(i).getString("title"));
//                            zbBean.setTeachername(jsonArray.getJSONObject(i).getString("teachername"));
//                            zbBean.setHeadimg(jsonArray.getJSONObject(i).getString("headimg"));
                            zbBean.setPicpath(jsonArray.getJSONObject(i).getString("picpath"));
                            zbBean.setLivetime(jsonArray.getJSONObject(i).getString("livetime"));
                            zbBean.setCreatetime(jsonArray.getJSONObject(i).getString("createtime"));
                            zbBean.setContent(NoHTML(jsonArray.getJSONObject(i).getString("content")));
                            zbBean.setVideourl(jsonArray.getJSONObject(i).getString("videourl"));
                            zbBean.setStatus(jsonArray.getJSONObject(i).getInt("status"));
                            zbBean.setIstop(jsonArray.getJSONObject(i).getInt("istop"));
                            zbBean.setKeyword(jsonArray.getJSONObject(i).getString("keyword"));
                            zbBean.setClick(jsonArray.getJSONObject(i).getInt("click"));
//                            zbBean.setFans(jsonArray.getJSONObject(i).getInt("fans"));
                            list.add(zbBean);
                        }

                        JSONArray array=json.getJSONArray("hgpublicLiveList");
                        for (int j=0;j<array.length();j++){
                            hgBean=new ZBBean();
                            hgBean.setUuid(array.getJSONObject(j).getString("uuid"));
                            hgBean.setChannelid(array.getJSONObject(j).getString("channelid"));
                            hgBean.setTitle(array.getJSONObject(j).getString("title"));
//                            hgBean.setTeachername(array.getJSONObject(i).getString("teachername"));
//                            hgBean.setHeadimg(array.getJSONObject(i).getString("headimg"));
                            hgBean.setPicpath(array.getJSONObject(j).getString("picpath"));
                            hgBean.setLivetime(array.getJSONObject(j).getString("livetime"));
                            hgBean.setCreatetime(array.getJSONObject(j).getString("createtime"));
                            hgBean.setContent(NoHTML(array.getJSONObject(j).getString("content")));
                            hgBean.setVideourl(array.getJSONObject(j).getString("videourl"));
                            hgBean.setStatus(array.getJSONObject(j).getInt("status"));
                            hgBean.setIstop(array.getJSONObject(j).getInt("istop"));
                            hgBean.setKeyword(array.getJSONObject(j).getString("keyword"));
                            hgBean.setClick(array.getJSONObject(j).getInt("click"));
//                            hgBean.setFans(array.getJSONObject(j).getInt("fans"));
                            list1.add(hgBean);
                        }
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(list.size()>0){
                                    if(!TextUtils.isEmpty(list.get(0).getPicpath())) {
                                        Iv_picture.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+list.get(0).getPicpath());
                                    }else {
                                        Iv_picture.setImageResource(R.mipmap.timg1);
                                    }
                                    title.setText(list.get(0).getTitle());
                                    time.setText(list.get(0).getLivetime().substring(5));
                                    adapter.notifyDataSetChanged();
                                }else {
                                    if(list1.size()>0){
                                        if(!TextUtils.isEmpty(list1.get(0).getPicpath())) {
                                            Iv_picture.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+list1.get(0).getPicpath());
                                        }else {
                                            Iv_picture.setImageResource(R.mipmap.timg1);
                                        }
                                        title.setText(list1.get(0).getTitle());
                                        time.setText(list1.get(0).getLivetime().substring(5));
                                    }
                                }

                                if(list1.size() == 0){
                                    Ll_review.setVisibility(View.GONE);
                                }else {
                                    Ll_review.setVisibility(View.VISIBLE);
                                    adapter1.notifyDataSetChanged();
                                }

                            }
                        });
                    } else if(a==-1){
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
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
                                startActivity(new Intent().setClass(getActivity(), LoginActivity.class));
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
//                            ToastUtil.showShortToast(getActivity(), getString(R.string.login_parse_exc));
                        }
                    });

                } finally {
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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
