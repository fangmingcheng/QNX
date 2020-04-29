package zgt.com.example.myzq.model.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.Members;

/**
 * 会员列表
 * Created by ThinkPad on 2019/5/31.
 */

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {
    private List<Members> list=new ArrayList<>();
    private Context context;


    public MembersAdapter(Context context, List<Members> memberses){
        this.context=context;
        this.list=memberses;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private TextView Tv_version,Tv_price,Tv_status,Tv_time;
        public ViewHolder(View view){
            super(view);
            Tv_version=(TextView) view.findViewById(R.id.Tv_version);
            Tv_price=(TextView) view.findViewById(R.id.Tv_price);
            Tv_status=(TextView) view.findViewById(R.id.Tv_status);
            Tv_time=(TextView) view.findViewById(R.id.Tv_time);
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_member,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Members live=list.get(position);

        holder.Tv_version.setText(live.getTypename());
        holder.Tv_price.setText(Html.fromHtml("价格：<font color='#FFFFFF' size='20dp'>"+live.getPrice()+"</font>元/年"));
        if(TextUtils.isEmpty(live.getStartDate())||TextUtils.isEmpty(live.getEndDate())){
            holder.Tv_time.setVisibility(View.GONE);
            holder.Tv_status.setVisibility(View.GONE);
        }else {
            holder.Tv_status.setText("已开通");
            holder.Tv_time.setText(live.getStartDate()+" - "+live.getEndDate());
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
