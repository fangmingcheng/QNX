package zgt.com.example.myzq.model.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.Interaction;
import zgt.com.example.myzq.model.common.custom_view.MyImageView;
import zgt.com.example.myzq.utils.SPUtil;

/**
 * Created by ThinkPad on 2019/6/11.
 */

public class InteractionAdapter extends RecyclerView.Adapter<InteractionAdapter.ViewHolder> {

    private List<Interaction> list=new ArrayList<>();
    private Context context;

    public InteractionAdapter(Context context, List<Interaction> lives){
        this.context=context;
        this.list=lives;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout Ll_custom;
        private MyImageView Iv_head;
        private TextView Tv_name,Tv_time,Tv_advisier,Tv_content,Tv_custom;
        public ViewHolder(View view){
            super(view);
            Ll_custom=(LinearLayout) view.findViewById(R.id.Ll_custom);
            Iv_head=(MyImageView) view.findViewById(R.id.Iv_head);
            Tv_name=(TextView) view.findViewById(R.id.Tv_name);
            Tv_content=(TextView) view.findViewById(R.id.Tv_content);
            Tv_time=(TextView) view.findViewById(R.id.Tv_time);
            Tv_advisier=(TextView) view.findViewById(R.id.Tv_advisier);
            Tv_custom=(TextView) view.findViewById(R.id.Tv_custom);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_interation,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Interaction live=list.get(position);
        if(live.getFlag()==0){
            if(live.getIsreply()==0){
                if(TextUtils.isEmpty(live.getMheadimg())) {
                    holder.Iv_head.setImageResource(R.drawable.replace);
                }else {
                    holder.Iv_head.setImageURL(SPUtil.getServerAddress().substring(0, SPUtil.getServerAddress().length() - 5) + live.getMheadimg());
                }
                holder.Tv_name.setText(live.getMembername());
                holder.Ll_custom.setVisibility(View.GONE);
                holder.Tv_time.setText(live.getMsgtime1());
                holder.Tv_content.setText(live.getMsgcontent());
            }else {
                if(TextUtils.isEmpty(live.getTheadimg())){
                    holder.Iv_head.setImageResource(R.drawable.replace);
                }else {
                    holder.Iv_head.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+live.getTheadimg());
                }
                holder.Tv_name.setText(live.getTeachername());
                holder.Ll_custom.setVisibility(View.VISIBLE);
                holder.Tv_time.setText(live.getReplytime());
                holder.Tv_custom.setText(Html.fromHtml("<font color='#FFAE00'>"+live.getMembername()+":</font>")+live.getMsgcontent());
                holder.Tv_content.setText(live.getReplycontent());
            }
        }else {

            if(TextUtils.isEmpty(live.getTheadimg())){
                holder.Iv_head.setImageResource(R.drawable.replace);
            }else {
                holder.Iv_head.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+live.getTheadimg());
            }
            holder.Ll_custom.setVisibility(View.GONE);
            holder.Tv_name.setText(live.getTeachername());
            holder.Tv_time.setText(live.getMsgtime1());
            if(live.getReplystatus()==1) {
                holder.Tv_content.setText(live.getMsgcontent());
            }
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
