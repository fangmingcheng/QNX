package zgt.com.example.myzq.model.common.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThinkPad on 2019/5/30.
 */

public class MyfragmentViewpageAdapter extends FragmentPagerAdapter{
    private List<Fragment> list;
    public MyfragmentViewpageAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list=list;
    }

    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public int getCount() {
        return list.size();
    }


}
