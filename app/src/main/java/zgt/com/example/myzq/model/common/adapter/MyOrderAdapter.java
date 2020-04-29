package zgt.com.example.myzq.model.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.Order;

/**
 * Created by ThinkPad on 2019/6/21.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {
    private List<Order> list=new ArrayList<>();
    private Context context;

    public MyOrderAdapter(Context context, List<Order> orders){
        this.context=context;
        this.list=orders;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private TextView Tv_billno,Tv_time,Tv_version_name,Tv_price,Tv_term,Tv_status;
        public ViewHolder(View view){
            super(view);
            Tv_billno=(TextView) view.findViewById(R.id.Tv_billno);
            Tv_time=(TextView) view.findViewById(R.id.Tv_time);
            Tv_version_name=(TextView) view.findViewById(R.id.Tv_version_name);
            Tv_price=(TextView) view.findViewById(R.id.Tv_price);
            Tv_term=(TextView) view.findViewById(R.id.Tv_term);
            Tv_status=(TextView) view.findViewById(R.id.Tv_status);
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_order,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Order order=list.get(position);
        holder.Tv_billno.setText("订单号："+order.getOrderno());
        holder.Tv_time.setText(order.getStartdate());
        holder.Tv_price.setText(order.getPrice());

        if(order.getProducttype()==2){
            holder.Tv_term.setVisibility(View.GONE);
            holder.Tv_version_name.setText(order.getFtitle());
        }else {
            holder.Tv_term.setVisibility(View.VISIBLE);
            holder.Tv_version_name.setText(order.getTypename());
        }

        if(order.getStatus()==-1){
            holder.Tv_status.setText("已取消");
            holder.Tv_status.setTextColor(context.getResources().getColor(R.color.hui));
        }else if(order.getStatus()==0){
            holder.Tv_status.setText("未支付");
            holder.Tv_status.setTextColor(context.getResources().getColor(R.color.hui));
        }else if(order.getStatus()==1){
            holder.Tv_status.setText("已支付");
            holder.Tv_status.setTextColor(context.getResources().getColor(R.color.hong));
        }else if(order.getStatus()==2){
            holder.Tv_status.setText("已到期");
            holder.Tv_status.setTextColor(context.getResources().getColor(R.color.hong));
        }

    }

    public interface OnItemClickListener {
        /**
         * 接口中的点击每一项的实现方法，参数自己定义
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
