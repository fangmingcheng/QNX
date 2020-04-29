package zgt.com.example.myzq.model.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.Dynamic;
import zgt.com.example.myzq.model.common.custom_view.MyImageBackgroundView;
import zgt.com.example.myzq.model.common.custom_view.MyImageView;
import zgt.com.example.myzq.utils.SPUtil;

/**
 * Created by ThinkPad on 2019/6/11.
 */

public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.ViewHolder> {
    private List<Dynamic> list=new ArrayList<>();
    private Context context;

    public DynamicAdapter(Context context, List<Dynamic> dynamicList){
        this.context=context;
        this.list=dynamicList;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout Ll_picture,Ll_live;
         private MyImageBackgroundView Iv_picture;
        private MyImageView Iv_head;
        private ImageView Iv_num,Iv_status;
        private TextView Tv_name,Tv_time,Tv_num,Tv_content,Tv_time_create;
        public ViewHolder(View view){
            super(view);
            Ll_picture=(LinearLayout) view.findViewById(R.id.Ll_picture);
            Ll_live=(LinearLayout) view.findViewById(R.id.Ll_live);
            Iv_head=(MyImageView) view.findViewById(R.id.Iv_head);
            Iv_picture=(MyImageBackgroundView) view.findViewById(R.id.Iv_picture);
            Iv_num=(ImageView) view.findViewById(R.id.Iv_num);
            Iv_status=(ImageView) view.findViewById(R.id.Iv_status);
            Tv_name=(TextView) view.findViewById(R.id.Tv_name);
            Tv_num=(TextView) view.findViewById(R.id.Tv_num);
            Tv_content=(TextView) view.findViewById(R.id.Tv_content);
            Tv_time=(TextView) view.findViewById(R.id.Tv_time);
            Tv_time_create=(TextView) view.findViewById(R.id.Tv_time_create);

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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dynamic,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Dynamic dynamic=list.get(position);
        if(TextUtils.isEmpty(dynamic.getHeadimg())){
            holder.Iv_head.setImageResource(R.drawable.replace);
        }else {
            holder.Iv_head.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+dynamic.getHeadimg());
        }

        holder.Tv_name.setText(dynamic.getTeachername());
        holder.Tv_time_create.setText(dynamic.getTime());
        if(TextUtils.isEmpty(dynamic.getSummary())){
            holder.Tv_content.setText(dynamic.getContent());
        }else {
            holder.Tv_content.setText(dynamic.getSummary());
        }
        if(dynamic.getType()==1){
            holder.Iv_status.setVisibility(View.VISIBLE);
            holder.Tv_time_create.setText(dynamic.getCreatetime());
            holder.Ll_picture.setVisibility(View.VISIBLE);
            holder.Ll_live.setVisibility(View.VISIBLE);
            holder.Iv_num.setVisibility(View.GONE);
            holder.Tv_num.setVisibility(View.GONE);
            if(TextUtils.isEmpty(dynamic.getPicpath())) {
               holder.Iv_picture.setImageResource(R.drawable.investment_background);
            }else {
                holder.Iv_picture.setImageURL(SPUtil.getServerAddress().substring(0, SPUtil.getServerAddress().length() - 5) + dynamic.getPicpath());
            }
            if(dynamic.getLivestatus()==0){
                holder.Tv_time.setText("开播时间: "+dynamic.getTime());
            }else if(dynamic.getLivestatus()==1){
                holder.Iv_status.setImageResource(R.mipmap.ic_zhibozhong);
                holder.Tv_time.setText("直播中");
            }else if(dynamic.getLivestatus()==2){
                holder.Iv_status.setImageResource(R.mipmap.ic_huigu);
                holder.Tv_time.setText("回顾");
            }
        }else if(dynamic.getType()==2){
            holder.Iv_status.setVisibility(View.GONE);
            holder.Iv_num.setVisibility(View.VISIBLE);
            holder.Tv_num.setVisibility(View.VISIBLE);
            holder.Ll_picture.setVisibility(View.GONE);
            holder.Ll_live.setVisibility(View.GONE);
            holder.Tv_num.setText(dynamic.getFans()+"");
        }else if(dynamic.getType()==3){
            holder.Iv_status.setVisibility(View.GONE);
            holder.Ll_picture.setVisibility(View.VISIBLE);
            holder.Ll_live.setVisibility(View.VISIBLE);
            if(TextUtils.isEmpty(dynamic.getPicpath())) {
                holder.Iv_picture.setImageResource(R.drawable.investment_background);
            }else {
                holder.Iv_picture.setImageURL(SPUtil.getServerAddress().substring(0, SPUtil.getServerAddress().length() - 5) + dynamic.getPicpath());
            }
            holder.Iv_num.setVisibility(View.GONE);
            holder.Tv_num.setVisibility(View.GONE);
            holder.Tv_time.setText("精品课程");

        }else if(dynamic.getType()==4){
            holder.Iv_status.setVisibility(View.GONE);
            holder.Ll_picture.setVisibility(View.VISIBLE);
            holder.Ll_live.setVisibility(View.VISIBLE);
            if(TextUtils.isEmpty(dynamic.getPicpath())) {
                holder.Iv_picture.setImageResource(R.drawable.investment_background);
            }else {
                holder.Iv_picture.setImageURL(SPUtil.getServerAddress().substring(0, SPUtil.getServerAddress().length() - 5) + dynamic.getPicpath());
            }
            holder.Iv_num.setVisibility(View.GONE);
            holder.Tv_num.setVisibility(View.GONE);
            holder.Tv_time.setText("精品视频");
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
