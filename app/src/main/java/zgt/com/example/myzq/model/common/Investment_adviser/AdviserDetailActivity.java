package zgt.com.example.myzq.model.common.Investment_adviser;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.AdviserDetail;
import zgt.com.example.myzq.model.common.Investment_adviser.fragment.LiveFragment;
import zgt.com.example.myzq.model.common.Investment_adviser.fragment.StockFragment;
import zgt.com.example.myzq.model.common.adapter.MyfragmentViewpageAdapter;
import zgt.com.example.myzq.model.common.custom_view.MyImageBackgroundView;
import zgt.com.example.myzq.model.common.custom_view.MyImageView;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.Log;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;


public class AdviserDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.Iv_background)
    MyImageBackgroundView Iv_background;
    @BindView(R.id.Iv_head)
    MyImageView Iv_head;
    @BindView(R.id.Tv_name)
    TextView Tv_name;
    @BindView(R.id.Tv_num)
    TextView Tv_num;
    @BindView(R.id.Tv_adviser_num)
    TextView Tv_adviser_num;
    @BindView(R.id.Tv_content)
    TextView Tv_content;
    @BindView(R.id.Tv_title)
    TextView Tv_title;
    @BindView(R.id.Iv_live)
    ImageView Iv_live;
    @BindView(R.id.Iv_Stock)
    ImageView Iv_Stock;
    @BindView(R.id.Tv_live)
    TextView Tv_live;
    @BindView(R.id.Tv_stock)
    TextView Tv_stock;
    @BindView(R.id.myviewpager)
    ViewPager myviewpager;
    private AdviserDetail adviserDetail;
    private String uuid;
    private ArrayList<Fragment> list;
    // viewpage适配器
    private MyfragmentViewpageAdapter adapter;
    FragmentManager fm=getSupportFragmentManager();
    // 定义所有标题按钮的数组
    private TextView[] TextArgs;
    @Override
    public int getLayoutId() {
        return R.layout.activity_adviser_detail;
    }

    @Override
    public void initToolBar() {

    }

    public String getUuid(){
        return uuid;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        TextArgs=new TextView[]{Tv_live,Tv_stock};
        list = new ArrayList<Fragment>();
        list.add(new LiveFragment());
        list.add(new StockFragment());
        adapter = new MyfragmentViewpageAdapter(fm, list);
        myviewpager.setAdapter(adapter);
        // viewpage监听事件，重写onPageSelected()方法，实现左右滑动页面
        myviewpager.setOnPageChangeListener(this);
        resetButtonColor();
        Tv_live.setTextColor(Color.parseColor("#FFAE00"));
        Iv_live.setBackgroundColor(Color.parseColor("#FFAE00"));
        uuid=getIntent().getStringExtra("uuid");
        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }).start();
    }


    @OnClick({R.id.Tv_live,R.id.Tv_stock,R.id.Tv_title,R.id.Iv_customer})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Tv_live:
                myviewpager.setCurrentItem(0);
                Iv_live.setBackgroundColor(Color.parseColor("#FFAE00"));
                Iv_Stock.setBackgroundColor(Color.parseColor("#f1f1f1"));
                break;
            case R.id.Tv_stock:
                myviewpager.setCurrentItem(1);
                Iv_live.setBackgroundColor(Color.parseColor("#f1f1f1"));
                Iv_Stock.setBackgroundColor(Color.parseColor("#FFAE00"));
                break;
            case R.id.Tv_title:
                if("取消关注".equals(Tv_title.getText().toString())){
                    cancelFollow();
                }else {
                    addFollow();
                }
                break;
            case R.id.Iv_customer:
                finish();
                break;
        }
    }

    // 设置按钮颜色
    public void resetButtonColor() {
        Tv_live.setTextColor(Color.parseColor("#333333"));
        Tv_stock.setTextColor(Color.parseColor("#333333"));
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

    private void setData(){
        Tv_name.setText(adviserDetail.getTruename());
        Tv_num.setText("（粉丝数:"+adviserDetail.getFans()+")");
        Tv_adviser_num.setText("投顾资格号:"+adviserDetail.getQualification());
        Tv_content.setText(adviserDetail.getIntro());
        if(TextUtils.isEmpty(adviserDetail.getHeadimg())){
            Iv_background.setImageResource(R.drawable.replace);
            Iv_head.setImageResource(R.drawable.replace);
        }else {
            Iv_background.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+adviserDetail.getHeadimg());
            Iv_head.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+adviserDetail.getHeadimg());
        }
        if(adviserDetail.getFanscount()==0){
            Tv_title.setText("+关注");
        }else if(adviserDetail.getFanscount()==1) {
            Tv_title.setText("取消关注");
        }
    }

    private void getData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"getTeacherByUuid.do");
        requestParams.setConnectTimeout(60 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("uuid", uuid);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if (a==1) {
                        adviserDetail=new AdviserDetail();
                        adviserDetail.setDuties(json.getString("duties"));
                        adviserDetail.setEmail(json.getString("email"));
                        adviserDetail.setFanscount(json.getInt("fanscount"));
                        adviserDetail.setFans(json.getInt("fans"));
                        adviserDetail.setHeadimg(json.getString("headimg"));
                        adviserDetail.setIntro(json.getString("intro"));
                        adviserDetail.setMobile(json.getString("mobile"));
                        adviserDetail.setNickname(json.getString("nickname"));
                        adviserDetail.setQualification(json.getString("qualification"));
                        adviserDetail.setSex(json.getInt("sex"));
                        adviserDetail.setTel(json.getString("tel"));
                        adviserDetail.setTruename(json.getString("truename"));
                        adviserDetail.setUsername(json.getString("username"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setData();
                            }
                        });
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(AdviserDetailActivity.this,LoginActivity.class));
                                finish();
//                                if(TextUtils.isEmpty(SPUtil.getToken())){
//                                    startActivity(new Intent().setClass(AdviserDetailActivity.this,LoginActivity.class));
//                                    finish();
//                                }else {
//                                    new AlertDialog.Builder(AdviserDetailActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    startActivity(new Intent().setClass(AdviserDetailActivity.this,LoginActivity.class));
//                                                    finish();
//                                                    dialog.dismiss();
//                                                }
//                                            }).create().show();
//                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    ToastUtil.showShortToast(AdviserDetailActivity.this, "解析异常");
                } finally {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtil.showShortToast(AdviserDetailActivity.this, "网络连接异常");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    private void addFollow(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"attention.do");
        requestParams.setConnectTimeout(60 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("teacherid", uuid);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if (a==1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(AdviserDetailActivity.this,"关注成功");
                                Tv_title.setText("取消关注");
                            }
                        });
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(AdviserDetailActivity.this,LoginActivity.class));
                                finish();
                            }
                        });

//                        new AlertDialog.Builder(AdviserDetailActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        startActivity(new Intent().setClass(AdviserDetailActivity.this,LoginActivity.class));
//                                        finish();
//                                        dialog.dismiss();
//                                    }
//                                }).create().show();
                    }  else{
                        ToastUtil.showShortToast(AdviserDetailActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    ToastUtil.showShortToast(AdviserDetailActivity.this, "解析异常");
                } finally {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtil.showShortToast(AdviserDetailActivity.this, "网络连接异常");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void cancelFollow(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"delAttention.do");
        requestParams.setConnectTimeout(60 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("teacherid", uuid);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if (a==1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(AdviserDetailActivity.this,"已取消");
                                Tv_title.setText("+关注");
                            }
                        });
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(AdviserDetailActivity.this,LoginActivity.class));
                                finish();
                            }
                        });
//                        new AlertDialog.Builder(AdviserDetailActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        startActivity(new Intent().setClass(AdviserDetailActivity.this,LoginActivity.class));
//                                        finish();
//                                        dialog.dismiss();
//                                    }
//                                }).create().show();
                    } else {
                        ToastUtil.showShortToast(AdviserDetailActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    ToastUtil.showShortToast(AdviserDetailActivity.this, "解析异常");
                } finally {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtil.showShortToast(AdviserDetailActivity.this, "网络连接异常");
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
