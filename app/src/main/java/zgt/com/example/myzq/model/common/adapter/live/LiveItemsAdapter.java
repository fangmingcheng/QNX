package zgt.com.example.myzq.model.common.adapter.live;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.live.LiveTitle;


/**
 * 会员列表
 * Created by ThinkPad on 2019/5/31.
 */

public class LiveItemsAdapter extends RecyclerView.Adapter<LiveItemsAdapter.ViewHolder> {
    private List<LiveTitle> list=new ArrayList<>();
    private Context context;

    private LiveContentAdapter adapter;


    public LiveItemsAdapter(Context context, List<LiveTitle> memberses){
        this.context=context;
        this.list=memberses;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private TextView Tv_week,Tv_date;
       private ListView Lv_live;
        public ViewHolder(View view){
            super(view);
            Tv_week=(TextView) view.findViewById(R.id.Tv_week);
            Tv_date=(TextView) view.findViewById(R.id.Tv_date);
            Lv_live=(ListView) view.findViewById(R.id.Lv_live);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_live_items,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        LiveTitle live=list.get(position);
        holder.Tv_week.setText(getWeek(live.time));
        if(live.data.length()>0){
            holder.Tv_date.setText(live.data.substring(5,10));
//            holder.Tv_time.setText(live.getLivetime().substring(11,16));
        }
        adapter = new LiveContentAdapter(context);
        adapter.addAll(live.list);
        holder.Lv_live.setAdapter(adapter);
        setListViewHeightBasedOnChildren(holder.Lv_live);
//        holder.Tv_title.setText(live.getKname());
//        holder.Tv_teacher.setText("主讲："+live.getTeachername());
//        if(live.getLivestatus()==1){
//            holder.Iv_btn.setImageResource(R.mipmap.img_zhibo);
//        }else if(live.getLivestatus()==0){
//            holder.Iv_btn.setImageResource(R.mipmap.img_yugao);
//        }

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public static String getWeek(long time) {

        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(time*1000));

        int year  = cd.get(Calendar.YEAR); //获取年份
        int month = cd.get(Calendar.MONTH); //获取月份
        int day   = cd.get(Calendar.DAY_OF_MONTH); //获取日期
        int week  = cd.get(Calendar.DAY_OF_WEEK); //获取星期

        String weekString;
        switch (week) {
            case Calendar.SUNDAY:
                weekString = "周日";
                break;
            case Calendar.MONDAY:
                weekString = "周一";
                break;
            case Calendar.TUESDAY:
                weekString = "周二";
                break;
            case Calendar.WEDNESDAY:
                weekString = "周三";
                break;
            case Calendar.THURSDAY:
                weekString = "周四";
                break;
            case Calendar.FRIDAY:
                weekString = "周五";
                break;
            default:
                weekString = "周六";
                break;

        }

        return weekString;
    }

    public interface OnItemClickListener {
        /**
         * 接口中的点击每一项的实现方法，参数自己定义
         *
         * @param view 点击的item的视图

         */
        public void OnItemClick(View view);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
