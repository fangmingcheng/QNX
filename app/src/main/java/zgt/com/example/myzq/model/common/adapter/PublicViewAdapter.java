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
import zgt.com.example.myzq.bean.PublicView;
import zgt.com.example.myzq.model.common.custom_view.MyImageView;
import zgt.com.example.myzq.utils.SPUtil;

/**
 * Created by ThinkPad on 2019/6/3.
 */

public class PublicViewAdapter extends  RecyclerView.Adapter<PublicViewAdapter.ViewHolder>{
    private List<PublicView> list=new ArrayList<>();
    private Context context;

    public PublicViewAdapter(Context context, List<PublicView> publicViews){
        this.context=context;
        this.list=publicViews;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private MyImageView Iv_head;
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_publicview,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        PublicView publicView=list.get(position);
        if(!TextUtils.isEmpty(publicView.getHeadimg())) {
            holder.Iv_head.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+publicView.getHeadimg());
        }else {
            holder.Iv_head.setImageResource(R.drawable.replace);
        }
        if(TextUtils.isEmpty(publicView.getSummary())){
            holder.Tv_content.setText(publicView.getContent());
        }else {
            holder.Tv_content.setText(publicView.getSummary());
        }
        holder.Tv_name.setText(publicView.getTeachername());
        holder.Tv_time.setText(publicView.getAddtime());
        holder.Tv_status.setText(publicView.getClick());
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
