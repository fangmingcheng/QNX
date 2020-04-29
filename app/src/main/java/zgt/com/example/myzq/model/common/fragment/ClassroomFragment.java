package zgt.com.example.myzq.model.common.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseFragment;
import zgt.com.example.myzq.model.common.adapter.MyfragmentViewpageAdapter;
import zgt.com.example.myzq.model.common.classroom.fragment.HGFragment;
import zgt.com.example.myzq.model.common.classroom.fragment.ZBFragment;
import zgt.com.example.myzq.utils.Log;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassroomFragment extends BaseFragment implements ViewPager.OnPageChangeListener{

    @BindView(R.id.Tv_zb)
    TextView Tv_zb;
    @BindView(R.id.Tv_hg)
    TextView Tv_hg;
    @BindView(R.id.myviewpager)
    ViewPager myviewpager;

    // 定义所有标题按钮的数组
    private TextView[] TextArgs;
    // fragment的集合
    private ArrayList<Fragment> list;
    // viewpage适配器
    private MyfragmentViewpageAdapter adapter;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_classroom;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        TextArgs=new TextView[]{Tv_zb,Tv_hg};
        list = new ArrayList<Fragment>();
        list.add(new ZBFragment());
        list.add(new HGFragment());
        adapter = new MyfragmentViewpageAdapter(getChildFragmentManager(), list);
        myviewpager.setAdapter(adapter);
        // viewpage监听事件，重写onPageSelected()方法，实现左右滑动页面
        myviewpager.setOnPageChangeListener(this);
        // 默认第一页
        // 初始按钮颜色
        resetButtonColor();
        Tv_zb.setTextColor(Color.parseColor("#FFAE00"));
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void onPageSelected(int arg0) {
        Log.d("InformationFragment",arg0+"");
        // 根据每次选中的按钮，重置颜色
        resetButtonColor();
        // 将滑动到当前的标签下，改动标签颜色
        TextArgs[arg0].setTextColor(Color.parseColor("#FFAE00"));
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }
    // 设置按钮颜色
    public void resetButtonColor() {
        Tv_zb.setTextColor(Color.parseColor("#333333"));
        Tv_hg.setTextColor(Color.parseColor("#333333"));

    }

    @OnClick({R.id.Tv_zb,R.id.Tv_hg})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Tv_zb:
                myviewpager.setCurrentItem(0);
                Tv_zb.setTextColor(Color.parseColor("#FFAE00"));
                Tv_hg.setTextColor(Color.parseColor("#333333"));
                break;
            case R.id.Tv_hg:
                myviewpager.setCurrentItem(1);
                Tv_zb.setTextColor(Color.parseColor("#333333"));
                Tv_hg.setTextColor(Color.parseColor("#FFAE00"));
                break;
        }
    }

}
