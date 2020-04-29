package zgt.com.example.myzq.model.common.Investment_adviser;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.model.common.adapter.MyfragmentViewpageAdapter;
import zgt.com.example.myzq.model.common.fragment.All_adviserFragment;
import zgt.com.example.myzq.model.common.fragment.FollowFragment;
import zgt.com.example.myzq.model.common.fragment.NewFragment;
import zgt.com.example.myzq.model.common.fragment.PopularityFragment;
import zgt.com.example.myzq.utils.Log;

public class Investment_centerActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.Tv_all)
    TextView Tv_all;
    @BindView(R.id.Tv_Popularity)
    TextView Tv_Popularity;
    @BindView(R.id.Tv_new)
    TextView Tv_new;
    @BindView(R.id.Tv_follow)
    TextView Tv_follow;

    @BindView(R.id.cursor_all)
    ImageView cursor_all;
    @BindView(R.id.cursor_Popularity)
    ImageView cursor_Popularity;
    @BindView(R.id.cursor_new)
    ImageView cursor_new;
    @BindView(R.id.cursor_follow)
    ImageView cursor_follow;

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

    @Override
    public int getLayoutId() {
        return R.layout.activity_investment_center;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        TextArgs=new TextView[]{Tv_all,Tv_Popularity,Tv_new,Tv_follow};
        // 将fragment放进集合，并初始化适配器
        list = new ArrayList<Fragment>();
        list.add(new All_adviserFragment());
        list.add(new PopularityFragment());
        list.add(new NewFragment());
        list.add(new FollowFragment());
        adapter = new MyfragmentViewpageAdapter(fm, list);
        myviewpager.setAdapter(adapter);
        // viewpage监听事件，重写onPageSelected()方法，实现左右滑动页面
        myviewpager.setOnPageChangeListener(this);
        // 初始按钮颜色
        resetButtonColor();
        // 默认第一页
        Tv_all.setTextColor(Color.parseColor("#FFAE00"));
        cursor_all.setBackgroundColor(Color.parseColor("#FFAE00"));
    }


    @OnClick({R.id.Tv_all,R.id.Tv_Popularity,R.id.Tv_new,R.id.Tv_follow,R.id.Iv_customer})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Tv_all:
                myviewpager.setCurrentItem(0);
                cursor_all.setBackgroundColor(Color.parseColor("#FFAE00"));
                cursor_Popularity.setBackgroundColor(Color.parseColor("#fbfbfb"));
                cursor_new.setBackgroundColor(Color.parseColor("#fbfbfb"));
                cursor_follow.setBackgroundColor(Color.parseColor("#fbfbfb"));
//                cursorAnim(0);
                break;
            case R.id.Tv_Popularity:
                myviewpager.setCurrentItem(1);
                cursor_all.setBackgroundColor(Color.parseColor("#fbfbfb"));
                cursor_Popularity.setBackgroundColor(Color.parseColor("#FFAE00"));
                cursor_new.setBackgroundColor(Color.parseColor("#fbfbfb"));
                cursor_follow.setBackgroundColor(Color.parseColor("#fbfbfb"));
//                cursorAnim(1);
                break;
            case R.id.Tv_new:
                myviewpager.setCurrentItem(2);
                cursor_all.setBackgroundColor(Color.parseColor("#fbfbfb"));
                cursor_Popularity.setBackgroundColor(Color.parseColor("#fbfbfb"));
                cursor_new.setBackgroundColor(Color.parseColor("#FFAE00"));
                cursor_follow.setBackgroundColor(Color.parseColor("#fbfbfb"));
//                cursorAnim(2);
                break;
            case R.id.Tv_follow:
                myviewpager.setCurrentItem(3);
                cursor_all.setBackgroundColor(Color.parseColor("#fbfbfb"));
                cursor_Popularity.setBackgroundColor(Color.parseColor("#fbfbfb"));
                cursor_new.setBackgroundColor(Color.parseColor("#fbfbfb"));
                cursor_follow.setBackgroundColor(Color.parseColor("#FFAE00"));
//                cursorAnim(3);
                break;
            case R.id.Iv_customer:
                finish();
                break;
        }
    }


    @Override
    public void initToolBar() {

    }
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    // 设置按钮颜色
    public void resetButtonColor() {
        Tv_all.setTextColor(Color.parseColor("#333333"));
        Tv_Popularity.setTextColor(Color.parseColor("#333333"));
        Tv_new.setTextColor(Color.parseColor("#333333"));
        Tv_follow.setTextColor(Color.parseColor("#333333"));
    }


    @Override
    public void onPageSelected(int arg0) {
        Log.d("Investment_centerActivity",arg0+"");
        // 根据每次选中的按钮，重置颜色
        resetButtonColor();
        // 将滑动到当前的标签下，改动标签颜色
        TextArgs[arg0].setTextColor(Color.parseColor("#FFAE00"));
        if(arg0==0){
            cursor_all.setBackgroundColor(Color.parseColor("#FFAE00"));
            cursor_Popularity.setBackgroundColor(Color.parseColor("#fbfbfb"));
            cursor_new.setBackgroundColor(Color.parseColor("#fbfbfb"));
            cursor_follow.setBackgroundColor(Color.parseColor("#fbfbfb"));
        }else if(arg0==1){
            cursor_all.setBackgroundColor(Color.parseColor("#fbfbfb"));
            cursor_Popularity.setBackgroundColor(Color.parseColor("#FFAE00"));
            cursor_new.setBackgroundColor(Color.parseColor("#fbfbfb"));
            cursor_follow.setBackgroundColor(Color.parseColor("#fbfbfb"));
        }else if(arg0==2){
            cursor_all.setBackgroundColor(Color.parseColor("#fbfbfb"));
            cursor_Popularity.setBackgroundColor(Color.parseColor("#fbfbfb"));
            cursor_new.setBackgroundColor(Color.parseColor("#FFAE00"));
            cursor_follow.setBackgroundColor(Color.parseColor("#fbfbfb"));
        }else if(arg0==3){
            cursor_all.setBackgroundColor(Color.parseColor("#fbfbfb"));
            cursor_Popularity.setBackgroundColor(Color.parseColor("#fbfbfb"));
            cursor_new.setBackgroundColor(Color.parseColor("#fbfbfb"));
            cursor_follow.setBackgroundColor(Color.parseColor("#FFAE00"));
        }

    }

}
