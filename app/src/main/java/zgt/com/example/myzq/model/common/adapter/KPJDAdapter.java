package zgt.com.example.myzq.model.common.adapter;

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
import zgt.com.example.myzq.bean.KPJDBean;
import zgt.com.example.myzq.model.common.custom_view.MyImageBackgroundView;
import zgt.com.example.myzq.utils.SPUtil;

public class KPJDAdapter extends  RecyclerView.Adapter<KPJDAdapter.ViewHolder> {

    private List<KPJDBean> list=new ArrayList<>();
    private Context context;

    public KPJDAdapter(Context context, List<KPJDBean> list){
        this.context=context;
        this.list=list;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private MyImageBackgroundView Iv_picture;
        private TextView Tv_title,Tv_content,Tv_time;
        public ViewHolder(View view){
            super(view);
            Iv_picture=(MyImageBackgroundView) view.findViewById(R.id.Iv_picture);
            Tv_title=(TextView) view.findViewById(R.id.Tv_title);
            Tv_content=(TextView) view.findViewById(R.id.Tv_content);
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_kpjd,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final KPJDBean live=list.get(position);
        if(!TextUtils.isEmpty(live.getPicpath())) {
            holder.Iv_picture.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+live.getPicpath());
        }else {
            holder.Iv_picture.setImageResource(R.mipmap.timg1);
        }
        holder.Tv_title.setText(live.getTitle());
        holder.Tv_content.setText(live.getContent());
        holder.Tv_time.setText(live.getCreatetime());
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
