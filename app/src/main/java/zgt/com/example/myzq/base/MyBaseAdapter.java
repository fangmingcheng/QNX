package zgt.com.example.myzq.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fhkj on 2017/9/11.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {
    protected List<T> dataList;
    protected Context context;
    protected LayoutInflater inflater;

    public MyBaseAdapter(Context context) {
        this.context = context;
        dataList = new ArrayList<T>();
        inflater = LayoutInflater.from(context);
    }

    public void add(T t) {
        dataList.add(t);
    }

    public void remove(T t) {
        dataList.remove(t);
    }

    public void remove(int position) {
        dataList.remove(position);
    }

    public void addAll(List<T> list) {
        dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(List<T> list, boolean isPullDown) {
        if (isPullDown) {
            dataList.addAll(0, list);
        } else {
            dataList.addAll(list);
        }
    }

    public void removeAll(List<T> list) {
        dataList.removeAll(list);
    }

    public void clear() {
        dataList.clear();
    }

    public List<T> getDataList() {
        return dataList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataList.size();
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView,
                                 ViewGroup parent);

}
