package zgt.com.example.myzq.model.common.adapter.homeAdapter;

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
import zgt.com.example.myzq.bean.classes.Course;
import zgt.com.example.myzq.model.common.custom_view.MyImageBackgroundView;

public class TeacherFileFeeAdapter extends  RecyclerView.Adapter<TeacherFileFeeAdapter.ViewHolder> {

    private List<Course> list=new ArrayList<>();
    private Context context;

    public TeacherFileFeeAdapter(Context context, List<Course> live_listingses){
        this.context=context;
        this.list=live_listingses;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private MyImageBackgroundView Iv_head;
        private TextView Tv_title,Tv_teacher;
        public ViewHolder(View view){
            super(view);
            Iv_head=(MyImageBackgroundView) view.findViewById(R.id.Iv_head);
            Tv_title=(TextView) view.findViewById(R.id.Tv_title);
            Tv_teacher=(TextView) view.findViewById(R.id.Tv_teacher);

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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_teacher_file_fee,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Course live=list.get(position);

        if(!TextUtils.isEmpty(live.getPicpath())) {
            holder.Iv_head.setImageURL(live.getPicpath());
//            holder.Iv_head.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+live.getPicpath());
        }else {
//            holder.Iv_head.setImageResource(R.drawable.replace);
        }

        holder.Iv_head.setType(1);
        holder.Iv_head.setRoundRadius(15);
        holder.Tv_title.setText(live.getTitle());
        holder.Tv_teacher.setText("主讲老师："+live.getLecturer());
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
