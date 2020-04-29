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
import zgt.com.example.myzq.bean.MessageList;

/**
 * Created by ThinkPad on 2019/5/31.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<MessageList> list=new ArrayList<>();
    private Context context;

    public MessageAdapter(Context context, List<MessageList> messageLists){
        this.context=context;
        this.list=messageLists;
    }

     class ViewHolder extends RecyclerView.ViewHolder{

        private TextView Tv_title,Tv_content,Tv_time;
        public ViewHolder(View view){
            super(view);

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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_messagelist,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MessageList messageList=list.get(position);

        holder.Tv_title.setText(messageList.getMsgtitle());
        holder.Tv_time.setText(messageList.getCreatetime()+"");
        holder.Tv_content.setText(messageList.getSummary());
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
