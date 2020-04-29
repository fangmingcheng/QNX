package zgt.com.example.myzq.model.common.adapter.courseAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.classes.CourseCatalog;
import zgt.com.example.myzq.model.common.custom_view.MyListView;

public class CourseCatalogAdapter extends  RecyclerView.Adapter<CourseCatalogAdapter.ViewHolder> implements View.OnClickListener {
    private List<CourseCatalog> list=new ArrayList<>();
    private Context context;
    private int isbuy=0;
    private AttachAdapter adapter ;

    public CourseCatalogAdapter(Context context, List<CourseCatalog> zbBeans,int status){
        this.context=context;
        this.list=zbBeans;
        this.isbuy=status;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView Iv_image;
        private TextView Tv_title,Tv_status;
        private LinearLayout Ll_catalog;
        private MyListView Lv_catalog;
        public ViewHolder(View view){
            super(view);
            Iv_image=(ImageView) view.findViewById(R.id.Iv_image);
            Tv_title=(TextView) view.findViewById(R.id.Tv_title);
            Tv_status=(TextView) view.findViewById(R.id.Tv_status);
            Lv_catalog=view.findViewById(R.id.Lv_catalog);
            Ll_catalog=(LinearLayout) view.findViewById(R.id.Ll_catalog);
            Ll_catalog.setOnClickListener(CourseCatalogAdapter.this);
            view.setOnClickListener(CourseCatalogAdapter.this);
//

        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_course_catalog,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setTag(position);//根据position来绑定 item的位置 判断是哪个item下面的子项的点击事件
        holder.Ll_catalog.setTag(position);

        final CourseCatalog live=list.get(position);

        adapter = new AttachAdapter(context);
        holder.Tv_title.setText(live.getName());
        if(isbuy == 0){
            if(live.getIsfee()==0){
                holder.Tv_status.setText("【免费】");
                holder.Tv_status.setTextColor(Color.parseColor("#97e960"));
            }else {
                holder.Tv_status.setText("【VIP付费】");
                holder.Tv_status.setTextColor(Color.parseColor("#e46866"));
            }
        }else {
            holder.Tv_status.setText("【可观看】");
            holder.Tv_status.setTextColor(Color.parseColor("#97e960"));
        }

        adapter.addAll(live.getList());
        if(live.isOpen()){
            holder.Iv_image.setImageResource(R.mipmap.btn_dakai);
            holder.Lv_catalog.setVisibility(View.VISIBLE);
            holder.Lv_catalog.setAdapter(adapter);
            setListViewHeightBasedOnChildren( holder.Lv_catalog);

        }else {
            holder.Iv_image.setImageResource(R.mipmap.btn_guanbi);
            holder.Lv_catalog.setVisibility(View.GONE);
        }

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    //用一个枚举类型来表示不同的view
    public enum ViewName{
        CATALOG,
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        switch (view.getId()) {
            case R.id.Ll_catalog:
                onItemClickListener.OnItemClick(view, ViewName.CATALOG, position);
                break;
        }
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
    private OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(CourseCatalogAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
