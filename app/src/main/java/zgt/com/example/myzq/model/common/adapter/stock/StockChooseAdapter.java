package zgt.com.example.myzq.model.common.adapter.stock;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.stock.HotStock;

/**
 * Created by ThinkPad on 2019/5/28.
 */

public class StockChooseAdapter extends  RecyclerView.Adapter<StockChooseAdapter.ViewHolder> {

    private List<HotStock> list=new ArrayList<>();
    private Context context;

    public StockChooseAdapter(Context context, List<HotStock> live_listingses){
        this.context=context;
        this.list=live_listingses;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private TextView Tv_title,Tv_code,Tv_intro,Tv_content;
        public ViewHolder(View view){
            super(view);
            Tv_title=(TextView) view.findViewById(R.id.Tv_title);
            Tv_code=(TextView) view.findViewById(R.id.Tv_code);
            Tv_intro=(TextView) view.findViewById(R.id.Tv_intro);
            Tv_content=(TextView) view.findViewById(R.id.Tv_content);
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_stock_choose,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HotStock live=list.get(position);

        holder.Tv_title.setText(live.getStockname());
        holder.Tv_code.setText(live.getStockcode());
        holder.Tv_intro.setText(live.getChg());
        holder.Tv_content.setText(live.getIntro());
        if(live.getChg().contains("+")){
            holder.Tv_intro.setTextColor(Color.parseColor("#FF4444"));
        }else if(live.getChg().contains("-")){
            holder.Tv_intro.setTextColor(Color.parseColor("#169433"));
        } else{
            holder.Tv_intro.setTextColor(Color.parseColor("#333333"));
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