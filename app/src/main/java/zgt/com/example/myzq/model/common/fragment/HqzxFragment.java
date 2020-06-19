package zgt.com.example.myzq.model.common.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jaeger.library.StatusBarUtil;

import java.lang.reflect.Field;

import butterknife.BindView;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseFragment;

/**
 * 行情自选
 * A simple {@link Fragment} subclass.
 */
public class HqzxFragment extends BaseFragment {

    @BindView(R.id.group)
    RadioGroup group;

    @BindView(R.id.button_hq)
    RadioButton button_hq;
    @BindView(R.id.button_zx)
    RadioButton button_zx;

    @BindView(R.id.fragment_container)
    FrameLayout fragment_container;

    HqFragment hqFragment;
    ZxFragment zxFragment;

    FragmentManager fm;
    FragmentTransaction transaction;
    @Override
    public void initToolBar() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
        }else {
            StatusBarUtil.setLightMode(getActivity());//黑色
        }

    }

    /**
     * 获取通知栏高度
     * @param context 上下文
     * @return 通知栏高度
     */
    private int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_hqzx;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        LinearLayout.LayoutParams params=( LinearLayout.LayoutParams) group.getLayoutParams();
        params.topMargin=getStatusBarHeight(getActivity())+4;

        group.setLayoutParams(params);
        setDefaultFragment();
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.button_hq){
                    button_hq.setTextColor(Color.parseColor("#ffffff"));
                    button_zx.setTextColor(Color.parseColor("#333333"));
                    transaction = fm.beginTransaction();
                    if (hqFragment == null) {
                        hqFragment = new HqFragment();
                        transaction.add(R.id.fragment_container,hqFragment);
                    }
                    hideFragment(transaction);
                    transaction.show(hqFragment);

                    transaction.commit();
                }else if(checkedId == R.id.button_zx){
                    button_hq.setTextColor(Color.parseColor("#333333"));
                    button_zx.setTextColor(Color.parseColor("#ffffff"));
                    transaction = fm.beginTransaction();
                    if (zxFragment == null) {
                        zxFragment= new ZxFragment();
                        transaction.add(R.id.fragment_container,zxFragment);
                    }
                    hideFragment(transaction);
                    transaction.show(zxFragment);

                    transaction.commit();
                }
            }
        });
    }

    public HqzxFragment() {
        // Required empty public constructor
    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        fm = getChildFragmentManager();
        transaction = fm.beginTransaction();
            if (hqFragment == null) {
                hqFragment = new HqFragment();
                transaction.add(R.id.fragment_container,hqFragment);
            }
            hideFragment(transaction);
            transaction.show(hqFragment);
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction){
        if (hqFragment != null) {
            transaction.hide(hqFragment);
        }
        if (zxFragment != null) {
            transaction.hide(zxFragment);
        }
    }

}
