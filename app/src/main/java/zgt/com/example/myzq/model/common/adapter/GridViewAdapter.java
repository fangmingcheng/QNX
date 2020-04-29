package zgt.com.example.myzq.model.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.Catalog;

/**
 * Created by ThinkPad on 2019/3/17.
 */

public class GridViewAdapter extends BaseAdapter {

    private int current = -1;
    private List<Catalog> data;//数据

    private Context context;//上下文
//    private Callback mCallback;

    public int count;

    public GridViewAdapter(List<Catalog> data, Context context) {
        this.data = data;
        this.context = context;
//        this.mCallback=callback;
    }

    /**
     * 设置当前选择的项。
     * @param position 当前位置
     */
    public void setSelection(int position) {
        current = position;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int num;
        ViewHolder viewHolder;
        if (convertView == null) {
            //加载子布局
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_gridview, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.Rl_catalog = (RelativeLayout) convertView.findViewById(R.id.Rl_catalog);
            viewHolder.Tv_num = (TextView) convertView.findViewById(R.id.Tv_num);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        num=position+1;
        viewHolder.Tv_num.setText(num+"");
        if (position == current) {
            viewHolder.Rl_catalog.setBackgroundResource(R.color.lan);
        } else {
            viewHolder.Rl_catalog.setBackgroundResource(R.color.grid);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView Tv_num;
        RelativeLayout Rl_catalog;
    }
    public void refresh(List<Catalog> newList) {
        //刷新数据
        data.removeAll(data);
        data.addAll(newList);
        notifyDataSetChanged();
    }

//    public interface OnImageClickListener {
//        /**
//         * 接口中的点击每一项的实现方法，参数自己定义
//         *
//         * @param view 点击的item的视图
//         * @param index 点击的item的位置
//         */
//        public void OnImageClick(View view, int index);
//    }
//
//    //需要外部访问，所以需要设置set方法，方便调用
//    private OnImageClickListener onImageClickListener;
//
//    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
//        this.onImageClickListener = onImageClickListener;
//    }
}
