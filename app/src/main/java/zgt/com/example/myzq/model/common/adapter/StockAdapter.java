package zgt.com.example.myzq.model.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.Stock;
import zgt.com.example.myzq.model.common.custom_view.MyImageView;
import zgt.com.example.myzq.utils.SPUtil;

/**
 * Created by ThinkPad on 2019/6/11.
 */

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder>{
    private List<Stock> list=new ArrayList<>();
    private Context context;

    public StockAdapter(Context context, List<Stock> lives){
        this.context=context;
        this.list=lives;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private MyImageView Iv_head;
        private ImageView Iv_see;
        private TextView Tv_name,Tv_content,Tv_time,Tv_status;
        public ViewHolder(View view){
            super(view);
            Iv_head=(MyImageView) view.findViewById(R.id.Iv_head);
            Tv_name=(TextView) view.findViewById(R.id.Tv_name);
            Tv_content=(TextView) view.findViewById(R.id.Tv_content);
            Tv_time=(TextView) view.findViewById(R.id.Tv_time);
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_stock,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Stock live=list.get(position);
        holder.Tv_name.setText(live.getTeachername());
        holder.Tv_content.setText(live.getContent());
        holder.Tv_time.setText(live.getAddtime());
        holder.Tv_status.setText(live.getClick());
        holder.Iv_head.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+live.getHeadimg());
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
