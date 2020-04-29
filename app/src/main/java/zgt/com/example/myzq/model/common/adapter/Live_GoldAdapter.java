package zgt.com.example.myzq.model.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.Live_Gold;
import zgt.com.example.myzq.model.common.custom_view.MyImageBackgroundView;
import zgt.com.example.myzq.model.common.custom_view.MyImageView;
import zgt.com.example.myzq.utils.SPUtil;

/**
 * Created by ThinkPad on 2019/6/11.
 */

public class Live_GoldAdapter extends RecyclerView.Adapter<Live_GoldAdapter.ViewHolder> {

    private List<Live_Gold> list=new ArrayList<>();
    private Context context;

    public Live_GoldAdapter(Context context, List<Live_Gold> lives){
        this.context=context;
        this.list=lives;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private MyImageView Iv_head;
         private MyImageBackgroundView Iv_picture;
         private ImageView Iv_status;
        private TextView Tv_name,Tv_time_create,Tv_content,Tv_time;
        public ViewHolder(View view){
            super(view);
            Iv_head=(MyImageView)view.findViewById(R.id.Iv_head);
            Iv_picture=(MyImageBackgroundView)view.findViewById(R.id.Iv_picture);
            Iv_status=(ImageView) view.findViewById(R.id.Iv_status);
            Tv_name=(TextView) view.findViewById(R.id.Tv_name);
            Tv_content=(TextView) view.findViewById(R.id.Tv_content);
            Tv_time=(TextView) view.findViewById(R.id.Tv_time);
            Tv_time_create=(TextView) view.findViewById(R.id.Tv_time_create);
//            Tv_status_live=(TextView) view.findViewById(R.id.Tv_status_live);
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_live,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Live_Gold live=list.get(position);
        if(TextUtils.isEmpty(live.getHeadimg())){
            holder.Iv_head.setImageResource(R.drawable.replace);
        }else {
            holder.Iv_head.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+live.getHeadimg());
        }

        if(TextUtils.isEmpty(live.getPicpath())){
            holder.Iv_picture.setImageResource(R.drawable.investment_background);
        }else {
            holder.Iv_picture.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+live.getPicpath());
        }

        holder.Tv_name.setText(live.getTeachername());
        holder.Tv_time_create.setText(live.getApplytime());
        holder.Tv_content.setText(live.getContent());
        if(live.getStatus()==1||live.getStatus()==0){
            holder.Iv_status.setVisibility(View.GONE);
            holder.Tv_time.setText("开播时间: "+live.getLivetime());
        }else if(live.getStatus()==2){
            holder.Iv_status.setImageResource(R.mipmap.ic_zhibozhong);
            holder.Tv_time.setText("直播中");
        }else if(live.getStatus()==3){
            holder.Iv_status.setImageResource(R.mipmap.ic_huigu);
            holder.Tv_time.setText("回顾");
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
