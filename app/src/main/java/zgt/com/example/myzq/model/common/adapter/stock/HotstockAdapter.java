package zgt.com.example.myzq.model.common.adapter.stock;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.stock.HotStock;

/**
 * Created by ThinkPad on 2019/5/28.
 */

public class HotstockAdapter extends  RecyclerView.Adapter<HotstockAdapter.ViewHolder> {

    private List<HotStock> list=new ArrayList<>();
    private Context context;

    public HotstockAdapter(Context context, List<HotStock> live_listingses){
        this.context=context;
        this.list=live_listingses;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout Ll_content_type,Ll_content;
        private TextView Tv_name,Tv_describe,Tv_up_down;
         private TextView Tv_name_1,Tv_describe_1,Tv_up_down_1;
        public ViewHolder(View view){
            super(view);
            Ll_content_type=(LinearLayout) view.findViewById(R.id.Ll_content_type);
            Tv_name=(TextView) view.findViewById(R.id.Tv_name);
            Tv_describe=(TextView) view.findViewById(R.id.Tv_describe);
            Tv_up_down=(TextView) view.findViewById(R.id.Tv_up_down);

            Ll_content=(LinearLayout) view.findViewById(R.id.Ll_content);
            Tv_name_1=(TextView) view.findViewById(R.id.Tv_name_1);
            Tv_describe_1=(TextView) view.findViewById(R.id.Tv_describe_1);
            Tv_up_down_1=(TextView) view.findViewById(R.id.Tv_up_down_1);
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_hotstock,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HotStock live=list.get(position);

        if(live.getType()==0){
            holder.Ll_content.setVisibility(View.VISIBLE);
            holder.Ll_content_type.setVisibility(View.GONE);
            holder.Tv_name.setText(live.getStockname());
            holder.Tv_describe.setText(live.getIntro());
            holder.Tv_up_down.setText(live.getChg());

            if(live.getChg().contains("+")){
                holder.Tv_up_down.setTextColor(Color.parseColor("#FF4444"));
            }else if(live.getChg().contains("-")){
                holder.Tv_up_down.setTextColor(Color.parseColor("#169433"));
            } else{
                holder.Tv_up_down.setTextColor(Color.parseColor("#333333"));
            }
        }else if(live.getType()==1){
            holder.Ll_content.setVisibility(View.GONE);
            holder.Ll_content_type.setVisibility(View.VISIBLE);
            holder.Tv_name_1.setText(live.getStockname());
            holder.Tv_describe_1.setText(live.getIntro());
            holder.Tv_up_down_1.setText(live.getChg());
            holder.Tv_up_down_1.setTextColor(Color.parseColor("#FF4444"));
            holder.Tv_describe_1.setTextColor(Color.parseColor("#9e9e9e"));
            holder.Tv_describe_1.setTextSize(12);
        }



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
