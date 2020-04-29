package zgt.com.example.myzq.model.common.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.GoldTeacher;
import zgt.com.example.myzq.model.common.adapter.MyfragmentViewpageAdapter;
import zgt.com.example.myzq.model.common.custom_view.MyImageView;
import zgt.com.example.myzq.model.common.home.fragment.InteractionFragment;
import zgt.com.example.myzq.model.common.home.fragment.LiveFragment;
import zgt.com.example.myzq.model.common.home.fragment.StockFragment;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.Log;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class GoldInvestmentActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    @BindView(R.id.Iv_head)
    MyImageView Iv_head;
    @BindView(R.id.Tv_name)
    TextView Tv_name;
    @BindView(R.id.Tv_num)
    TextView Tv_num;
    @BindView(R.id.Tv_status)
    TextView Tv_status;
    @BindView(R.id.Tv_time)
    TextView Tv_time;
    @BindView(R.id.Tv_introduce)
    TextView Tv_introduce;

    @BindView(R.id.Tv_live)
    TextView Tv_live;
    @BindView(R.id.Tv_stock)
    TextView Tv_stock;
    @BindView(R.id.Tv_interaction)
    TextView Tv_interaction;
    @BindView(R.id.Iv_live)
    ImageView Iv_live;
    @BindView(R.id.Iv_stock)
    ImageView Iv_stock;
    @BindView(R.id.Iv_interaction)
    ImageView Iv_interaction;

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

//    private List<Members> list1=new ArrayList<>();
    private GoldTeacher teacher;
    private String uuid;
    private String status;

    @Override
    public void initToolBar() {

    }

    public String getUuid(){
        return uuid;
    }

    public GoldTeacher getTeacher(){
        return teacher;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_gold_investment;
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
        }
        uuid=getIntent().getStringExtra("uuid");
        status=getIntent().getStringExtra("status");
        TextArgs=new TextView[]{Tv_live,Tv_stock,Tv_interaction};

        resetButtonColor();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }).start();

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @OnClick({R.id.Tv_live,R.id.Tv_stock,R.id.Tv_interaction,R.id.Iv_customer})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Tv_live:
                status="1";
                myviewpager.setCurrentItem(0);
                Iv_live.setBackgroundColor(Color.parseColor("#FFAE00"));
                Iv_stock.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Iv_interaction.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                cursorAnim(0);
                break;
            case R.id.Tv_stock:
                status="2";
                myviewpager.setCurrentItem(1);
                Iv_live.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Iv_stock.setBackgroundColor(Color.parseColor("#FFAE00"));
                Iv_interaction.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                cursorAnim(1);
                break;
            case R.id.Tv_interaction:
                status="3";
                myviewpager.setCurrentItem(2);
                Iv_live.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Iv_stock.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Iv_interaction.setBackgroundColor(Color.parseColor("#FFAE00"));
//                cursorAnim(2);
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
        Tv_interaction.setTextColor(Color.parseColor("#333333"));
        Iv_live.setBackgroundColor(Color.parseColor("#FFFFFF"));
        Iv_stock.setBackgroundColor(Color.parseColor("#FFFFFF"));
        Iv_interaction.setBackgroundColor(Color.parseColor("#FFFFFF"));
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
        resetButtonColor();
        // 将滑动到当前的标签下，改动标签颜色
        TextArgs[arg0].setTextColor(Color.parseColor("#FFAE00"));
        if(arg0==0){
            status="1";
            Iv_live.setBackgroundColor(Color.parseColor("#FFAE00"));
            Iv_stock.setBackgroundColor(Color.parseColor("#FFFFFF"));
            Iv_interaction.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else if(arg0==1){
            status="2";
            Iv_live.setBackgroundColor(Color.parseColor("#FFFFFF"));
            Iv_stock.setBackgroundColor(Color.parseColor("#FFAE00"));
            Iv_interaction.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else if(arg0==2){
            status="3";
            Iv_live.setBackgroundColor(Color.parseColor("#FFFFFF"));
            Iv_stock.setBackgroundColor(Color.parseColor("#FFFFFF"));
            Iv_interaction.setBackgroundColor(Color.parseColor("#FFAE00"));
        }
    }

    private void setData(){
        list = new ArrayList<Fragment>();
        list.add(new LiveFragment());
        list.add(new StockFragment());
        list.add(new InteractionFragment());

        adapter = new MyfragmentViewpageAdapter(fm, list);
        myviewpager.setAdapter(adapter);
        // viewpage监听事件，重写onPageSelected()方法，实现左右滑动页面
        myviewpager.setOnPageChangeListener(this);
        Iv_head.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+teacher.getHeadimg());
        Tv_name.setText(teacher.getTruename());
        Tv_num.setText("投顾资格号："+teacher.getQualification());
        Tv_status.setText(teacher.getTypename());
        Tv_time.setText(teacher.getEnddate());

        Tv_introduce.setText(replaceBlank(teacher.getIntro()));

        // 默认第一页
        if("1".equals(status)) {
            Tv_live.setTextColor(Color.parseColor("#FFAE00"));
            Iv_live.setBackgroundColor(Color.parseColor("#FFAE00"));
        }else if("2".equals(status)){
            myviewpager.setCurrentItem(1);
            Tv_stock.setTextColor(Color.parseColor("#FFAE00"));
            Iv_stock.setBackgroundColor(Color.parseColor("#FFAE00"));
        }else if("3".equals(status)){
            myviewpager.setCurrentItem(2);
            Tv_interaction.setTextColor(Color.parseColor("#FFAE00"));
            Iv_interaction.setBackgroundColor(Color.parseColor("#FFAE00"));
        }
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    private void getData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"goldTeacher.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("uuid",SPUtil.getTeacherid() );
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        teacher=new GoldTeacher();
                        teacher.setDuties(json.getString("duties"));
                        teacher.setEmail(json.getString("email"));
                        teacher.setEnddate(json.getString("enddate"));
                        teacher.setTypename(json.getString("typename"));
                        teacher.setEnname(json.getString("enname"));
                        teacher.setHeadimg(json.getString("headimg"));
                        teacher.setIntro(json.getString("intro"));
                        teacher.setMobile(json.getString("mobile"));
                        teacher.setNickname(json.getString("nickname"));
                        teacher.setQualification(json.getString("qualification"));
                        teacher.setSex(json.getInt("sex"));
                        teacher.setTel(json.getString("tel"));
                        teacher.setTruename(json.getString("truename"));
                        teacher.setUsername(json.getString("username"));

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
                                startActivity(new Intent().setClass(GoldInvestmentActivity.this,LoginActivity.class));
                                finish();
//                                new AlertDialog.Builder(GoldInvestmentActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                startActivity(new Intent().setClass(GoldInvestmentActivity.this,LoginActivity.class));
//                                               finish();
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
                                ToastUtil.showShortToast(GoldInvestmentActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(GoldInvestmentActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(GoldInvestmentActivity.this, "网络连接异常");
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
