package zgt.com.example.myzq.model.common.adapter.courseAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.classes.Mycourse;
import zgt.com.example.myzq.model.common.custom_view.MyImageBackgroundView;

public class MyCourseAdapter extends  RecyclerView.Adapter<MyCourseAdapter.ViewHolder> implements View.OnClickListener {
    private List<Mycourse> list=new ArrayList<>();
    private Context context;

    public MyCourseAdapter(Context context, List<Mycourse> zbBeans){
        this.context=context;
        this.list=zbBeans;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private MyImageBackgroundView Iv_head;
        private TextView Tv_title,Tv_teacher,Tv_status,Tv_billno;
        private LinearLayout Ll_content;
        public ViewHolder(View view){
            super(view);
            Iv_head=(MyImageBackgroundView) view.findViewById(R.id.Iv_head);
            Tv_billno=(TextView) view.findViewById(R.id.Tv_billno);
            Tv_title=(TextView) view.findViewById(R.id.Tv_title);
            Tv_teacher=(TextView) view.findViewById(R.id.Tv_teacher);
            Tv_status=(TextView) view.findViewById(R.id.Tv_status);
            Ll_content= view.findViewById(R.id.Ll_content);
            Ll_content.setOnClickListener(MyCourseAdapter.this);
            Tv_status.setOnClickListener(MyCourseAdapter.this);

        }
    }

    //用一个枚举类型来表示不同的view
    public enum ViewName{
        RENEW,
        CONTENT
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        switch (view.getId()) {
            case R.id.Tv_status:
                onItemClickListener.OnItemClick(view, ViewName.RENEW, position);
                break;
            case R.id.Ll_content:
                onItemClickListener.OnItemClick(view, ViewName.CONTENT, position);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_my_course,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.Tv_status.setTag(position);
        holder.Ll_content.setTag(position);
        final Mycourse live=list.get(position);
        if(!TextUtils.isEmpty(live.getPicpath())) {
            holder.Iv_head.setImageURL(live.getPicpath());
//            holder.Iv_head.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+live.getPicpath());
        }else {
//            holder.Iv_head.setImageResource(R.drawable.replace);
        }
        holder.Iv_head.setType(1);
        holder.Iv_head.setRoundRadius(10);
        holder.Tv_billno.setText("课程到期时间："+live.getExpiredate());
        holder.Tv_title.setText(live.getTitle());
        holder.Tv_teacher.setText("主讲老师："+live.getLecturer());
        if(live.getStatus()==0){
            holder.Tv_billno.setTextColor(Color.parseColor("#919191"));
            holder.Tv_status.setText("续课");
        }else {
            holder.Tv_billno.setTextColor(Color.parseColor("#333333"));
            holder.Tv_status.setText("");
        }
//        if(live.getPricelimit()==0){
//           holder.Tv_status.setText("");
//        }else {
//            holder.Tv_status.setText("续课");
//        }

    }

    public interface OnItemClickListener {
        /**
         * 接口中的点击每一项的实现方法，参数自己定义
         *
         * @param view 点击的item的视图

         */
        void OnItemClick(View view,ViewName VIEW,int position);


    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
