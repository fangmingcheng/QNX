package zgt.com.example.myzq.model.common.adapter.price;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.order.Price;

/**
 * Created by ThinkPad on 2019/5/28.
 */

public class PriceAdapter extends  RecyclerView.Adapter<PriceAdapter.ViewHolder> {

    private List<Price> list=new ArrayList<>();
    private Context context;
    private int current =-1;

    public PriceAdapter(Context context, List<Price> live_listingses){
        this.context=context;
        this.list=live_listingses;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout Ll_background;
        private TextView Tv_time,Tv_price_real,Tv_price;
        public ViewHolder(View view){
            super(view);
            Ll_background=(LinearLayout) view.findViewById(R.id.Ll_background);
            Tv_time=(TextView) view.findViewById(R.id.Tv_time);
            Tv_price_real=(TextView) view.findViewById(R.id.Tv_price_real);
            Tv_price=(TextView) view.findViewById(R.id.Tv_price);
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
    public void setCurrent(int current){
        this.current = current;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_price,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Price live=list.get(position);
        if(live.getPricelimit()==0){
            holder.Tv_time.setText("永久购买");
        }else {
            if(live.getPriceunit()==0){
                holder.Tv_time.setText(live.getPricenum()+"天");
            }else if(live.getPriceunit()==1){
                holder.Tv_time.setText(live.getPricenum()+"个月");
            }else if(live.getPriceunit()==2){
                holder.Tv_time.setText(live.getPricenum()+"季度");
            }else if(live.getPriceunit()==3){
                holder.Tv_time.setText("半年");
            }else if(live.getPriceunit()==4){
                holder.Tv_time.setText("一年");
            }
        }


//        if(live.getIsdefault()==0){
//            if(current == position){
//                holder.Ll_background.setBackgroundResource(R.mipmap.bg_taocan4);
//                holder.Tv_price_real.setText(live.getRealprice());
//                holder.Tv_price.setText(live.getPrice());
//            }else {
//                holder.Ll_background.setBackgroundResource(R.mipmap.bg_taocan3);
//                holder.Tv_price_real.setText(live.getRealprice());
//                holder.Tv_price.setText(live.getPrice());
//            }
//        }else {
            if(current == position){
                holder.Ll_background.setBackgroundResource(R.mipmap.bg_taocan2);

                holder.Tv_price_real.setText(Html.fromHtml("<font>¥</font><font><big>"+live.getRealprice()+"</big></font>"));
                holder.Tv_price_real.setTextColor(Color.parseColor("#FF4444"));
                holder.Tv_price.setText("");
            }else {
                holder.Ll_background.setBackgroundResource(R.mipmap.bg_taocan1);

                holder.Tv_price_real.setText(Html.fromHtml("<font>¥</font><font><big>"+live.getRealprice()+"</big></font>"));
                holder.Tv_price_real.setTextColor(Color.parseColor("#333333"));
                holder.Tv_price.setText("");
            }
//        }


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
