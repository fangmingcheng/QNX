package zgt.com.example.myzq.model.common.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.Live_listings;
import zgt.com.example.myzq.model.common.custom_view.MyImageView;
import zgt.com.example.myzq.utils.SPUtil;

/**
 * Created by ThinkPad on 2019/5/28.
 */

public class LivelistingsAdapter extends  RecyclerView.Adapter<LivelistingsAdapter.ViewHolder> {

    private List<Live_listings> list=new ArrayList<>();
    private Context context;

    public LivelistingsAdapter(Context context,List<Live_listings> live_listingses){
        this.context=context;
        this.list=live_listingses;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private MyImageView Iv_head;
        private TextView Tv_name,Tv_content,Tv_time_live,Tv_status;
        public ViewHolder(View view){
            super(view);
            Iv_head=(MyImageView) view.findViewById(R.id.Iv_head);
            Tv_name=(TextView) view.findViewById(R.id.Tv_name);
            Tv_content=(TextView) view.findViewById(R.id.Tv_content);
            Tv_time_live=(TextView) view.findViewById(R.id.Tv_time_live);
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_live_listings,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Live_listings live=list.get(position);
        if(TextUtils.isEmpty(live.getHeadimg())){
            holder.Iv_head.setImageResource(R.drawable.replace);
        }else {
            holder.Iv_head.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+live.getHeadimg());
        }

        holder.Tv_name.setText(live.getTeachername());
        holder.Tv_content.setText(live.getContent());
        if(live.getStatus()==0){
            holder.Tv_status.setText("未开播");
            holder.Tv_status.setTextColor(context.getResources().getColor(R.color.hui));
        }else if(live.getStatus()==1){
            holder.Tv_status.setText("直播中");
            holder.Tv_status.setTextColor(context.getResources().getColor(R.color.hong));
        }else if(live.getStatus()==2){
            holder.Tv_status.setText("已结束");
            holder.Tv_status.setTextColor(context.getResources().getColor(R.color.hui));
        }
        String time=live.getLivetime();
        holder.Tv_time_live.setText(time.substring(5,16));
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
