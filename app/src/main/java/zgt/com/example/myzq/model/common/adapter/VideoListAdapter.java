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
import zgt.com.example.myzq.bean.Video;
import zgt.com.example.myzq.model.common.custom_view.MyImageBackgroundView;
import zgt.com.example.myzq.model.common.custom_view.MyImageView;
import zgt.com.example.myzq.utils.SPUtil;

/**
 * Created by ThinkPad on 2019/5/31.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {
    private List<Video> list=new ArrayList<>();
    private Context context;

    public VideoListAdapter(Context context, List<Video> videos){
        this.context=context;
        this.list=videos;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private MyImageView Iv_head;
         private MyImageBackgroundView Iv_background;
        private TextView Tv_name,Tv_Member;
        public ViewHolder(View view){
            super(view);
            Iv_head=(MyImageView) view.findViewById(R.id.Iv_head);
            Iv_background=(MyImageBackgroundView) view.findViewById(R.id.Iv_background);
            Tv_name=(TextView) view.findViewById(R.id.Tv_name);
            Tv_Member=(TextView) view.findViewById(R.id.Tv_Member);

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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_videos,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Video live=list.get(position);
        if(TextUtils.isEmpty(live.getHeadimg())){
            holder.Iv_head.setImageResource(R.drawable.replace);
        }else {
            holder.Iv_head.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+live.getHeadimg());
        }
        if(TextUtils.isEmpty(live.getPicpath())){
            holder.Iv_background.setImageResource(R.drawable.investment_background);
        }else {
            holder.Iv_background.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+live.getPicpath());
        }

        holder.Tv_name.setText(live.getTeachername());
        if(live.getIscharge()==0){
            holder.Tv_Member.setText("免费");
        }else if(live.getIscharge()==1){
            holder.Tv_Member.setText("收费");
        }else if(live.getIscharge()==2){
            holder.Tv_Member.setText("收费会员");
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
