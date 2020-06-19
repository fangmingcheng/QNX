package zgt.com.example.myzq.model.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.model.common.adapter.GuideViewAdapter;
import zgt.com.example.myzq.utils.Log;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.StatusBarUtil;

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.view_page)
    ViewPager viewPager;

    // 底部小点的图片
    @BindView(R.id.llPoint)
     LinearLayout llPoint;
    //立即进入按钮
    @BindView(R.id.guideTv)
     TextView guideTv;

    @BindView(R.id.Tv_into)
    TextView Tv_into;

    private List<View> list;
    private int[] imageView = { R.mipmap.y1, R.mipmap.y2, R.mipmap.y3};

    private boolean isSwitchPager = false; //默认不切换
    private int previousPosition = 0; //默认为0

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            if (viewPager.getCurrentItem() <= 1) {
            if(viewPager!=null) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
//            }
        }
    };



    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.statusBarLightMode(this);
        if(SPUtil.isRead()){
            startActivity(new Intent().setClass(WelcomeActivity.this,StartActivity.class));
            finish();
        }
        initoper();
        addView();
        addPoint();
    }

    private void initoper() {
        // 进入按钮
        guideTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SPUtil.getConfigSharedPreferences().edit().putBoolean("isRead", true).commit();
                startActivity(new Intent().setClass(WelcomeActivity.this,StartActivity.class));
                finish();
            }
        });

        Tv_into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SPUtil.getConfigSharedPreferences().edit().putBoolean("isRead", true).commit();
                startActivity(new Intent().setClass(WelcomeActivity.this,StartActivity.class));
                finish();
            }
        });

        // 2.监听当前显示的页面，将对应的小圆点设置为选中状态，其它设置为未选中状态
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                monitorPoint(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                Log.e("arg0=" + arg0+"arg1="+arg1+"arg2="+arg2);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                Log.e("arg0--" + arg0);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isSwitchPager) {
                    SystemClock.sleep(3000);
                    previousPosition+=1;
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();

    }



    /**
     * 添加图片到view
     */
    private void addView() {
        list = new ArrayList<View>();
        // 将imageview添加到view
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < imageView.length; i++) {
            ImageView iv = new ImageView(WelcomeActivity.this);
            iv.setLayoutParams(params);
            iv.setAdjustViewBounds(true);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(imageView[i]);
            list.add(iv);
        }
        // 加入适配器
        viewPager.setAdapter(new GuideViewAdapter(list));
    }

    /**
     * 添加小圆点
     */
    private void addPoint() {
        // 1.根据图片多少，添加多少小圆点
        for (int i = 0; i < imageView.length; i++) {
            LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i < 1) {
                pointParams.setMargins(0, 0, 0, 0);
            } else {
                pointParams.setMargins(10, 0, 0, 0);
            }
            ImageView iv = new ImageView(WelcomeActivity.this);
            iv.setLayoutParams(pointParams);
            iv.setBackgroundResource(R.mipmap.ic_weixuanze);
            llPoint.addView(iv);
        }
        llPoint.getChildAt(0).setBackgroundResource(R.mipmap.ic_xuanze);

    }

    /**
     * 判断小圆点
     *
     * @param position
     */
    private void monitorPoint(int position) {
        for (int i = 0; i < imageView.length; i++) {
            if (i == position) {
                llPoint.getChildAt(position).setBackgroundResource(R.mipmap.ic_xuanze);
            } else {
                llPoint.getChildAt(i).setBackgroundResource(R.mipmap.ic_weixuanze);
            }
        }
        // 3.当滑动到最后一个添加按钮点击进入，
        if (position == imageView.length - 1) {
            guideTv.setVisibility(View.VISIBLE);
            Tv_into.setVisibility(View.GONE);
        } else {
            guideTv.setVisibility(View.GONE);
            Tv_into.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        isSwitchPager = false;
        super.onDestroy();
    }
}
