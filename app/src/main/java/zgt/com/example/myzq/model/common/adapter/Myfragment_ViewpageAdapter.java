package zgt.com.example.myzq.model.common.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThinkPad on 2019/5/30.
 */

public class Myfragment_ViewpageAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> list;
    private List<String> titleList;
    public Myfragment_ViewpageAdapter(FragmentManager fm, ArrayList<Fragment> list,List<String> titleList) {
        super(fm);
        this.list = list;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
