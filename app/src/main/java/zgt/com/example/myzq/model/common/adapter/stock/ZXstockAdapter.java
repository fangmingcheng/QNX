package zgt.com.example.myzq.model.common.adapter.stock;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.stock.ZXStock;

/**
 * Created by ThinkPad on 2019/5/28.
 */

public class ZXstockAdapter extends  RecyclerView.Adapter<ZXstockAdapter.ViewHolder> {

    private List<ZXStock> list=new ArrayList<>();
    private Context context;

    public ZXstockAdapter(Context context, List<ZXStock> live_listingses){
        this.context=context;
        this.list=live_listingses;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout Ll_rmgp;
        private TextView Tv_name,Tv_describe,Tv_increase,Tv_up_down;
        public ViewHolder(View view){
            super(view);
            Ll_rmgp=(LinearLayout) view.findViewById(R.id.Ll_rmgp);
            Tv_name=(TextView) view.findViewById(R.id.Tv_name);
            Tv_describe=(TextView) view.findViewById(R.id.Tv_describe);
            Tv_up_down=(TextView) view.findViewById(R.id.Tv_up_down);
            Tv_increase=(TextView) view.findViewById(R.id.Tv_increase);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v);
                    }
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.ondelect(v);
                    }
                    return true;
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_zxstock,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ZXStock live=list.get(position);

        holder.Tv_name.setText(live.getStockname());
        holder.Tv_describe.setText(live.getStockcode());
        holder.Tv_up_down.setText(live.getPrice()+"");
        holder.Tv_increase.setText(live.getChg()+"%");
        GradientDrawable drawable =new GradientDrawable();
        drawable.setCornerRadius(3);//设置圆角
        if(live.getChg()==0.0){
            drawable.setColor(Color.parseColor("#9e9e9e"));
        }else if(live.getChg()<0.0){
            drawable.setColor(Color.parseColor("#169433"));
        } else if(live.getChg()>0.0){
            drawable.setColor(Color.parseColor("#FF4444"));
        }
        holder.Tv_increase.setBackground(drawable);

    }

    public interface OnItemClickListener {
        /**
         * 接口中的点击每一项的实现方法，参数自己定义
         *
         * @param view 点击的item的视图

         */
        public void OnItemClick(View view);

        void  ondelect(View view);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}
