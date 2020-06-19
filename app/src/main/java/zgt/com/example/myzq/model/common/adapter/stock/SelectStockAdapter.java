package zgt.com.example.myzq.model.common.adapter.stock;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.stock.AiStock;
import zgt.com.example.myzq.model.common.custom_view.MyPicImageView;

/**
 * Created by ThinkPad on 2019/5/28.
 */

public class SelectStockAdapter extends  RecyclerView.Adapter<SelectStockAdapter.ViewHolder> {

    private List<AiStock> list=new ArrayList<>();
    private Context context;

    public SelectStockAdapter(Context context, List<AiStock> live_listingses){
        this.context=context;
        this.list=live_listingses;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private MyPicImageView imageView;
        private TextView Tv_title,Tv_content;
        public ViewHolder(View view){
            super(view);
            imageView= view.findViewById(R.id.imageView);
            Tv_title=(TextView) view.findViewById(R.id.Tv_title);
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_selectstock,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        AiStock live=list.get(position);
        holder.Tv_title.setText(live.getPname());
        holder.Tv_content.setText(live.getIntro());
        holder.imageView.setImageURL(live.getPicpath());
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
