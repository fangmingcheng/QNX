package zgt.com.example.myzq.model.common.adapter.stock;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.stock.StockProduct;

/**
 * Created by ThinkPad on 2019/5/28.
 */

public class StockChooseTitleAdapter extends  RecyclerView.Adapter<StockChooseTitleAdapter.ViewHolder> {

    private List<StockProduct> list=new ArrayList<>();
    private Context context;
    private int current;

    public StockChooseTitleAdapter(Context context, List<StockProduct> live_listingses){
        this.context=context;
        this.list=live_listingses;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private TextView Tv_title;
        private ImageView Iv_line,Iv_line_select;
        public ViewHolder(View view){
            super(view);
            Tv_title=(TextView) view.findViewById(R.id.Tv_title);
            Iv_line=view.findViewById(R.id.Iv_line);
            Iv_line_select=view.findViewById(R.id.Iv_line_select);
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

    public  void setCurrent(int index){
        this.current = index;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_stock_choosetitle,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        StockProduct live=list.get(position);

        holder.Tv_title.setText(live.getPname());

        if(current==position){
            holder.Tv_title.setTextColor(Color.parseColor("#FF4444"));
            holder.Iv_line_select.setVisibility(View.VISIBLE);
            holder.Iv_line.setVisibility(View.GONE);
        }else {
            holder.Iv_line_select.setVisibility(View.GONE);
            holder.Iv_line.setVisibility(View.VISIBLE);
            holder.Tv_title.setTextColor(Color.parseColor("#333333"));
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
