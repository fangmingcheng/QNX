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
import zgt.com.example.myzq.bean.Training;

/**
 * Created by ThinkPad on 2019/6/11.
 */

public class CurriculumAdapter extends RecyclerView.Adapter<CurriculumAdapter.ViewHolder> {
    private List<Training> list=new ArrayList<>();
    private Context context;

    public CurriculumAdapter(Context context, List<Training> trainings){
        this.context=context;
        this.list=trainings;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private TextView Tv_title,Tv_content,Tv_time,Tv_type;
        public ViewHolder(View view){
            super(view);
            Tv_type=(TextView) view.findViewById(R.id.Tv_type);
            Tv_title=(TextView) view.findViewById(R.id.Tv_title);
            Tv_time=(TextView) view.findViewById(R.id.Tv_time);
            Tv_content=(TextView) view.findViewById(R.id.Tv_content);
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_curriculum,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Training training=list.get(position);
        holder.Tv_title.setText(training.getTitle());
        holder.Tv_content.setText(training.getContent());
        holder.Tv_time.setText(training.getCreatetime());
        holder.Tv_type.setText("证券课程");
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
