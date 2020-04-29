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
import zgt.com.example.myzq.bean.Live_items;
import zgt.com.example.myzq.model.common.custom_view.MyImageBackgroundView;
import zgt.com.example.myzq.model.common.custom_view.MyImageView;
import zgt.com.example.myzq.utils.SPUtil;

/**
 * Created by ThinkPad on 2019/6/11.
 */

public class Live_NoticeAdapter extends RecyclerView.Adapter<Live_NoticeAdapter.ViewHolder> {
    private List<Live_items> list=new ArrayList<>();
    private Context context;

    public Live_NoticeAdapter(Context context, List<Live_items> live_itemses){
        this.context=context;
        this.list=live_itemses;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private MyImageView Iv_head;
         private MyImageBackgroundView Iv_picture;
        private TextView Tv_name,Tv_time_create,Tv_content,Tv_time,Tv_status_live;
        public ViewHolder(View view){
            super(view);
            Iv_head=(MyImageView) view.findViewById(R.id.Iv_head);
            Iv_picture=(MyImageBackgroundView) view.findViewById(R.id.Iv_picture);
            Tv_name=(TextView) view.findViewById(R.id.Tv_name);
            Tv_time_create=(TextView) view.findViewById(R.id.Tv_time_create);
            Tv_content=(TextView) view.findViewById(R.id.Tv_content);
            Tv_time=(TextView) view.findViewById(R.id.Tv_time);
            Tv_status_live=(TextView) view.findViewById(R.id.Tv_status_live);
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_notice,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Live_items live=list.get(position);
        holder.Iv_head.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+live.getHeadimg());
        holder.Iv_picture.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+live.getPicpath());
        holder.Tv_name.setText(live.getTeachername());
        holder.Tv_time_create.setText(live.getCreatetime());
        holder.Tv_content.setText(live.getContent());
        if("0".equals(live.getStatus())){
            holder.Tv_time.setText("开播时间: "+live.getLivetime());
        }else if("1".equals(live.getStatus())){
            holder.Tv_status_live.setVisibility(View.VISIBLE);
            holder.Tv_status_live.setText("直播中");
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
