package zgt.com.example.myzq.model.common.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
import zgt.com.example.myzq.bean.Catalog;
import zgt.com.example.myzq.bean.Courseware;
import zgt.com.example.myzq.model.common.adapter.MyfragmentViewpageAdapter;
import zgt.com.example.myzq.model.common.custom_view.MyJzvdStd;
import zgt.com.example.myzq.model.common.home.fragment.CatalogFragment;
import zgt.com.example.myzq.model.common.home.fragment.IntroduceFragment;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.Log;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class CoursewareDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener{


    @BindView(R.id.jz_video)
    MyJzvdStd jz_video;
    @BindView(R.id.Tv_class)
    TextView Tv_class;
    @BindView(R.id.Tv_education)
    TextView Tv_education;
    @BindView(R.id.Iv_live)
    ImageView Iv_live;
    @BindView(R.id.Iv_Stock)
    ImageView Iv_Stock;

    @BindView(R.id.myviewpager)
    ViewPager myviewpager;

    private float cursorX = 0;
    // 定义获取所有按钮的宽度数组
    private int[] WidrhArgs;
    // 定义所有标题按钮的数组
    private TextView[] TextArgs;
    // fragment的集合
    private ArrayList<Fragment> list;
    // viewpage适配器
    private MyfragmentViewpageAdapter adapter;
    FragmentManager fm=getSupportFragmentManager();

    List<Catalog> list1=new ArrayList<>();
    Catalog catalog;
    Courseware courseware;
    private String uuid;

    @Override
    public void initToolBar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        jz_video.goOnPlayOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jz_video.goOnPlayOnPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        jz_video.clearFocus();
        jz_video.onStateAutoComplete();
        if(courseware!=null) {
            jz_video.clearSavedProgress(this, SPUtil.getServerAddress().substring(0, (SPUtil.getServerAddress().length() - 5)) + courseware.getVideourl());
        }
    }


    public List<Catalog> getList1(){
        return list1;
    }

    public Courseware getCourseware(){
        return courseware;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        TextArgs=new TextView[]{Tv_class,Tv_education};
        uuid=getIntent().getStringExtra("uuid");
        // 默认第一页
        // 初始按钮颜色
        resetButtonColor();
        Tv_class.setTextColor(Color.parseColor("#FFAE00"));
        Iv_live.setBackgroundColor(Color.parseColor("#FFAE00"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }).start();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_courseware_detail;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }


    @OnClick({R.id.Tv_class,R.id.Tv_education})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Tv_class:
                myviewpager.setCurrentItem(0);
                Iv_live.setBackgroundColor(Color.parseColor("#FFAE00"));
                Iv_Stock.setBackgroundColor(Color.parseColor("#f1f1f1"));
//                cursorAnim(0);
                break;
            case R.id.Tv_education:
                myviewpager.setCurrentItem(1);
                Iv_live.setBackgroundColor(Color.parseColor("#f1f1f1"));
                Iv_Stock.setBackgroundColor(Color.parseColor("#FFAE00"));
//                cursorAnim(1);
                break;

        }
    }

    @Override
    public void onPageSelected(int arg0) {
        Log.d("Investment_centerActivity",arg0+"");
        // 根据每次选中的按钮，重置颜色
        resetButtonColor();
        // 将滑动到当前的标签下，改动标签颜色
        TextArgs[arg0].setTextColor(Color.parseColor("#FFAE00"));
        if(arg0==0){
            Iv_live.setBackgroundColor(Color.parseColor("#FFAE00"));
            Iv_Stock.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }else if(arg0==1){
            Iv_live.setBackgroundColor(Color.parseColor("#f1f1f1"));
            Iv_Stock.setBackgroundColor(Color.parseColor("#FFAE00"));
        }
    }

    // 设置按钮颜色
    public void resetButtonColor() {
        Tv_class.setTextColor(Color.parseColor("#333333"));
        Tv_education.setTextColor(Color.parseColor("#333333"));
    }

    private void getData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"getTeacherFileByUuid.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("uuid", uuid);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        courseware=new Courseware();
                        courseware.setStatus(json.getInt("status"));
//                        courseware.setDuties(json.getString("duties"));
                        courseware.setIntro(json.getString("intro"));
                        courseware.setIsend(json.getInt("isend"));
                        courseware.setVideourl(json.getString("fileurl"));
                        courseware.setAddtime(json.getString("addtime"));
                        courseware.setClick(json.getInt("click"));
                        courseware.setDescription(json.getString("description"));
//                        courseware.setHeadimg(json.getString("headimg"));
                        courseware.setIscharge(json.getInt("ischarge"));
                        courseware.setPicpath(json.getString("picpath"));
                        courseware.setPrice(json.getDouble("price"));
                        courseware.setRealprice(json.getDouble("realprice"));
                        courseware.setTeacherid(json.getString("teacherid"));
//                        courseware.setTeachername(json.getString("teachername"));
                        courseware.setTitle(json.getString("title"));
                        courseware.setTotalnum(json.getInt("totalnum"));
//                        courseware.setUuid(json.getString("uuid"));

                        JSONArray jsonArray=json.getJSONArray("filemenulist");
                        for (int i=0;i<jsonArray.length();i++){
                            catalog=new Catalog();
                            catalog.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            catalog.setClick(jsonArray.getJSONObject(i).getInt("click"));
                            catalog.setFileid(jsonArray.getJSONObject(i).getString("fileid"));
                            catalog.setIsfee(jsonArray.getJSONObject(i).getInt("isfee"));
                            catalog.setName(jsonArray.getJSONObject(i).getString("name"));
                            catalog.setVideourl(jsonArray.getJSONObject(i).getString("videourl"));
                            list1.add(catalog);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setmVideoView();
                            }
                        });
                    } else if(a==-1){

                       runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(CoursewareDetailActivity.this, LoginActivity.class));
                                finish();
//                                if(!TextUtils.isEmpty(SPUtil.getToken())) {
//                                    new AlertDialog.Builder(CoursewareDetailActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    startActivity(new Intent().setClass(CoursewareDetailActivity.this, LoginActivity.class));
//                                                    finish();
//                                                    dialog.dismiss();
//                                                }
//                                            }).create().show();
//                                }else {
//                                    startActivity(new Intent().setClass(CoursewareDetailActivity.this, LoginActivity.class));
//                                    finish();
//                                }
                            }
                        });

                    }else {
                        final String msg=jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(CoursewareDetailActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(CoursewareDetailActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(CoursewareDetailActivity.this, "网络连接异常");
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

    private void setmVideoView(){
        if(list1.size()>0) {
            if(list1.get(0).getIsfee()==0) {
                jz_video.setUp(SPUtil.getServerAddress().substring(0, (SPUtil.getServerAddress().length() - 5)) + list1.get(0).getVideourl(), "");
            }else {
                ToastUtil.showShortToast(CoursewareDetailActivity.this,"该章节需要付费观看");
            }
//            Glide.with(this).load(SPUtil.getServerAddress().substring(0,(SPUtil.getServerAddress().length()-5))+list1.get(0).getPicpath()).into(jz_video.thumbImageView);
        }


        Glide.with(this).load(SPUtil.getServerAddress().substring(0,(SPUtil.getServerAddress().length()-5))+courseware.getPicpath()).into(jz_video.thumbImageView);

        list = new ArrayList<Fragment>();
        list.add(new IntroduceFragment());
        list.add(new CatalogFragment());
        adapter = new MyfragmentViewpageAdapter(fm, list);
        myviewpager.setAdapter(adapter);
        // viewpage监听事件，重写onPageSelected()方法，实现左右滑动页面
        myviewpager.setOnPageChangeListener(this);
    }

}
