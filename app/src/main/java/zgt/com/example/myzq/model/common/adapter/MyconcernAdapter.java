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
import zgt.com.example.myzq.bean.My_concern;
import zgt.com.example.myzq.model.common.custom_view.MyImageView;
import zgt.com.example.myzq.utils.SPUtil;

/**
 * Created by ThinkPad on 2019/5/31.
 */

public class MyconcernAdapter extends RecyclerView.Adapter<MyconcernAdapter.ViewHolder> {
    private List<My_concern> list=new ArrayList<>();
    private Context context;

    public MyconcernAdapter(Context context, List<My_concern> investment_advisors){
        this.context=context;
        this.list=investment_advisors;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private MyImageView Iv_head;
        private TextView Tv_name,Tv_num,Tv_content,Tv_status;
        public ViewHolder(final View view){
            super(view);
            Iv_head=(MyImageView) view.findViewById(R.id.Iv_head);
            Tv_name=(TextView) view.findViewById(R.id.Tv_name);
            Tv_num=(TextView) view.findViewById(R.id.Tv_num);
            Tv_content=(TextView) view.findViewById(R.id.Tv_content);
            Tv_status=(TextView) view.findViewById(R.id.Tv_status);
            Tv_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener1!=null){
                        onItemClickListener1.OnChildItemClick(view);
                    }
                }
            });
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_my_concern,parent,false);
        ViewHolder holder=new ViewHolder(view);
//        holder.Tv_status.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        My_concern live=list.get(position);
        holder.Iv_head.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+live.getHeadimg());
        holder.Tv_name.setText(live.getTeachername());
        holder.Tv_num.setText(live.getFans()+"");
        holder.Tv_content.setText(live.getIntro());
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

    public interface OnChildItemClickListener {
        /**
         * 接口中的点击每一项的实现方法，参数自己定义
         *
         * @param view 点击的item的视图

         */
        public void OnChildItemClick(View view);

    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnChildItemClickListener onItemClickListener1;

    public void setOnChildItemClickListener(OnChildItemClickListener onItemClickListener) {
        this.onItemClickListener1 = onItemClickListener;
    }
}
