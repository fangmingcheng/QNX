package zgt.com.example.myzq.model.common.adapter.stock;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.stock.ReseaRchreportk;
import zgt.com.example.myzq.model.common.custom_view.MyImageBackgroundView;

/**
 * Created by ThinkPad on 2019/5/28.
 */

public class ResearchReportAdapter extends  RecyclerView.Adapter<ResearchReportAdapter.ViewHolder> {

    private List<ReseaRchreportk> list=new ArrayList<>();
    private Context context;

    public ResearchReportAdapter(Context context, List<ReseaRchreportk> live_listingses){
        this.context=context;
        this.list=live_listingses;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private MyImageBackgroundView Iv_pic;
        private TextView Tv_title,Tv_summary,Tv_time,Tv_num;
        public ViewHolder(View view){
            super(view);
            Iv_pic=(MyImageBackgroundView) view.findViewById(R.id.Iv_pic);
            Tv_title=(TextView) view.findViewById(R.id.Tv_title);
            Tv_summary=(TextView) view.findViewById(R.id.Tv_summary);
            Tv_time=(TextView) view.findViewById(R.id.Tv_time);
            Tv_num=(TextView) view.findViewById(R.id.Tv_num);
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_research_report,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ReseaRchreportk live=list.get(position);
        if(live.getIstop()==1){
            Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_zhiding);
            ImageSpan imgSpan = new ImageSpan(context, b);
            SpannableString spanString = new SpannableString("icon");
            spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.Tv_title.setText(spanString);
            holder.Tv_title.append("  "+live.getTitle());
        }else {
            holder.Tv_title.setText(live.getTitle());
        }

        holder.Tv_summary.setText(live.getSummary());
        holder.Tv_time.setText(live.getCreatetime()+" 来自 "+live.getSource());
        holder.Tv_num.setText(live.getClick()+"阅读");

        holder.Iv_pic.setType(1);
        holder.Iv_pic.setRoundRadius(3);
        if(TextUtils.isEmpty(live.getPicpath())){
            holder.Iv_pic.setVisibility(View.GONE);
        }else {
            holder.Iv_pic.setVisibility(View.VISIBLE);
            holder.Iv_pic.setImageURL(live.getPicpath());
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
