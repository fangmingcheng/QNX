package zgt.com.example.myzq.model.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.ZXBean;
import zgt.com.example.myzq.model.common.custom_view.MyPicImageView;

public class ZXAdapter extends  RecyclerView.Adapter<ZXAdapter.ViewHolder>{
    private List<ZXBean> list=new ArrayList<>();
    private Context context;

    public ZXAdapter(Context context, List<ZXBean> zbBeans){
        this.context=context;
        this.list=zbBeans;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout Ll_text,Ll_picture_text;
        private TextView Tv_title_text,Tv_content_text,Tv_time_text;
        private TextView Tv_title,Tv_content,Tv_time,Tv_title_picture;
        private FrameLayout fl_picture;
        private MyPicImageView Iv_pic,Iv_picture;
        public ViewHolder(View view){
            super(view);
            Ll_text=(LinearLayout) view.findViewById(R.id.Ll_text);
            Ll_picture_text=(LinearLayout) view.findViewById(R.id.Ll_picture_text);

            Tv_title=(TextView) view.findViewById(R.id.Tv_title);
            Tv_content=(TextView) view.findViewById(R.id.Tv_content);
            Tv_time=(TextView) view.findViewById(R.id.Tv_time);

            Tv_title_text=(TextView) view.findViewById(R.id.Tv_title_text);
            Tv_content_text=(TextView) view.findViewById(R.id.Tv_content_text);
            Tv_time_text=(TextView) view.findViewById(R.id.Tv_time_text);

            Tv_title_picture=(TextView) view.findViewById(R.id.Tv_title_picture);
            fl_picture=(FrameLayout) view.findViewById(R.id.fl_picture);
            Iv_pic=(MyPicImageView) view.findViewById(R.id.Iv_pic);
            Iv_picture=(MyPicImageView) view.findViewById(R.id.Iv_picture);

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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_zx,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ZXBean live=list.get(position);
        if(live.getIstop()==1){
            holder.Ll_text.setVisibility(View.GONE);
            holder.Ll_picture_text.setVisibility(View.GONE);
            holder.fl_picture.setVisibility(View.VISIBLE);

            if(!TextUtils.isEmpty(live.getPicpath())) {
                holder.Iv_pic.setImageURL(live.getPicpath());
            }else {
                holder.Iv_pic.setImageResource(R.mipmap.timg1);
            }
            holder.Tv_title_picture.setText(live.getTitle());
        }else {
            if(live.getType()==0){
                holder.Ll_text.setVisibility(View.VISIBLE);
                holder.Ll_picture_text.setVisibility(View.GONE);
                holder.fl_picture.setVisibility(View.GONE);
                holder.Tv_title_text.setText(live.getTitle());
                holder.Tv_content_text.setText(live.getSummary());
                holder.Tv_time_text.setText(live.getCreatetime().substring(5));
            }else if(live.getType()==1){
                holder.Ll_text.setVisibility(View.GONE);
                holder.Ll_picture_text.setVisibility(View.GONE);
                holder.fl_picture.setVisibility(View.VISIBLE);

                if(!TextUtils.isEmpty(live.getPicpath())) {
                    holder.Iv_pic.setImageURL(live.getPicpath());
                }else {
                    holder.Iv_pic.setImageResource(R.mipmap.timg1);
                }
                holder.Tv_title_picture.setText(live.getTitle());

            }else if(live.getType()==2){
                holder.Ll_text.setVisibility(View.GONE);
                holder.Ll_picture_text.setVisibility(View.VISIBLE);
                holder.fl_picture.setVisibility(View.GONE);
                holder.Tv_title.setText(live.getTitle());
                holder.Tv_content.setText(live.getSummary());
                holder.Tv_time.setText(live.getCreatetime().substring(5));
                if(!TextUtils.isEmpty(live.getPicpath())) {
                    holder.Iv_picture.setImageURL(live.getPicpath());
                }else {
                    holder.Iv_picture.setImageResource(R.mipmap.timg1);
                }
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
