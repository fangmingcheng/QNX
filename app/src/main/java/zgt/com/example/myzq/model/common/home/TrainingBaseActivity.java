package zgt.com.example.myzq.model.common.home;

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
import zgt.com.example.myzq.model.common.home.fragment.CurriculumFragment;
import zgt.com.example.myzq.model.common.home.fragment.Investor_EducationFragment;
import zgt.com.example.myzq.utils.Log;

public class TrainingBaseActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

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

    @Override
    public void initViews(Bundle savedInstanceState) {
        TextArgs=new TextView[]{Tv_class,Tv_education};
        list = new ArrayList<Fragment>();
        list.add(new CurriculumFragment());
        list.add(new Investor_EducationFragment());
        adapter = new MyfragmentViewpageAdapter(fm, list);
        myviewpager.setAdapter(adapter);
        // viewpage监听事件，重写onPageSelected()方法，实现左右滑动页面
        myviewpager.setOnPageChangeListener(this);
        // 默认第一页
        // 初始按钮颜色
        resetButtonColor();
        Tv_class.setTextColor(Color.parseColor("#FFAE00"));
        Iv_live.setBackgroundColor(Color.parseColor("#FFAE00"));
    }

    @OnClick({R.id.Tv_class,R.id.Tv_education,R.id.Iv_customer})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Tv_class:
                myviewpager.setCurrentItem(0);
                Iv_live.setBackgroundColor(Color.parseColor("#FFAE00"));
                Iv_Stock.setBackgroundColor(Color.parseColor("#fbfbfb"));
                break;
            case R.id.Tv_education:
                myviewpager.setCurrentItem(1);
                Iv_live.setBackgroundColor(Color.parseColor("#fbfbfb"));
                Iv_Stock.setBackgroundColor(Color.parseColor("#FFAE00"));
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_training_base;
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
            Iv_Stock.setBackgroundColor(Color.parseColor("#fbfbfb"));
        }else if(arg0==1){
            Iv_live.setBackgroundColor(Color.parseColor("#fbfbfb"));
            Iv_Stock.setBackgroundColor(Color.parseColor("#FFAE00"));
        }
    }

    // 设置按钮颜色
    public void resetButtonColor() {
        Tv_class.setTextColor(Color.parseColor("#333333"));
        Tv_education.setTextColor(Color.parseColor("#333333"));
    }
}
