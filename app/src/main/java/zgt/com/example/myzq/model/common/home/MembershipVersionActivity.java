package zgt.com.example.myzq.model.common.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

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
import zgt.com.example.myzq.bean.Members;
import zgt.com.example.myzq.bean.MyMember;
import zgt.com.example.myzq.model.common.adapter.MyfragmentViewpageAdapter;
import zgt.com.example.myzq.model.common.home.fragment.BasicsFragment;
import zgt.com.example.myzq.model.common.home.fragment.FifthFragment;
import zgt.com.example.myzq.model.common.home.fragment.FourthFragment;
import zgt.com.example.myzq.model.common.home.fragment.SecondFragment;
import zgt.com.example.myzq.model.common.home.fragment.ThirdFragment;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.Log;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class MembershipVersionActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
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

    private List<Members> list1=new ArrayList<>();
    private Members members;
    private MyMember myMember;
    @Override
    public void initToolBar() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_membership_version;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        //得到当前界面的装饰视图
        if(Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            //设置让应用主题内容占据状态栏和导航栏
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //设置状态栏和导航栏颜色为透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }

        list = new ArrayList<Fragment>();
        list.add(new BasicsFragment());
        list.add(new SecondFragment());
        list.add(new ThirdFragment());
        list.add(new FourthFragment());
        list.add(new FifthFragment());
        adapter = new MyfragmentViewpageAdapter(fm, list);
        myviewpager.setAdapter(adapter);
        // viewpage监听事件，重写onPageSelected()方法，实现左右滑动页面
        myviewpager.setOnPageChangeListener(this);
        if("基础班".equals(SPUtil.getTypename())){
            myviewpager.setCurrentItem(0);
        }else if("博弈版".equals(SPUtil.getTypename())){
            myviewpager.setCurrentItem(1);
        }else if("至尊版".equals(SPUtil.getTypename())){
            myviewpager.setCurrentItem(2);
        }else if("钻石版".equals(SPUtil.getTypename())){
            myviewpager.setCurrentItem(3);
        }else if("VIP版".equals(SPUtil.getTypename())){
            myviewpager.setCurrentItem(4);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }).start();
    }

    @OnClick({R.id.Iv_customer})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_customer:
                finish();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        Log.d("Investment_centerActivity",arg0+"");
        // 根据每次选中的按钮，重置颜色
    }
    private void getData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"goldtg.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        myMember=new MyMember();
                        myMember.setTypename(json.getString("typename"));
                        myMember.setType(json.getInt("type"));
                        myMember.setEnddate(json.getString("enddate"));
                        myMember.setStartdate(json.getString("startdate"));
                        myMember.setTeacherid(json.getString("teacherid"));
                        myMember.setTsort(json.getInt("tsort"));
                        JSONArray jsonArray=json.getJSONArray("productTypelist");
                        for (int i=0;i<jsonArray.length();i++){
                            members=new Members();
                            members.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            members.setIntro(jsonArray.getJSONObject(i).getString("intro"));
                            members.setModulenamestr(jsonArray.getJSONObject(i).getString("modulenamestr"));
                            members.setPrice(jsonArray.getJSONObject(i).getInt("price"));
                            members.setTypename(jsonArray.getJSONObject(i).getString("typename"));
                            members.setIntro(jsonArray.getJSONObject(i).getString("intro"));

                            if(myMember.getTypename().equals(members.getTypename())){
                                members.setStartDate(myMember.getStartdate());
                                members.setEndDate(myMember.getEnddate());
                            }
                            list1.add(members);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                new AlertDialog.Builder(MembershipVersionActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                startActivity(new Intent().setClass(MembershipVersionActivity.this,LoginActivity.class));
//                                                finish();
//                                                dialog.dismiss();
//                                            }
//                                        }).create().show();
                                startActivity(new Intent().setClass(MembershipVersionActivity.this,LoginActivity.class));
                                finish();
                            }
                        });

                    }else {
                        final String msg=jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(MembershipVersionActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(MembershipVersionActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(MembershipVersionActivity.this, "网络连接异常");
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
