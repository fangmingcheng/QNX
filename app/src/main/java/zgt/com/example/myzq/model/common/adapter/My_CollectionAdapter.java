package zgt.com.example.myzq.model.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ashokvarma.bottomnavigation.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.My_Collection;
import zgt.com.example.myzq.model.common.custom_view.LeftSlideView;
import zgt.com.example.myzq.model.common.custom_view.MyImageView;
import zgt.com.example.myzq.utils.SPUtil;

/**
 * Created by ThinkPad on 2019/6/3.
 */

public class My_CollectionAdapter extends  RecyclerView.Adapter<My_CollectionAdapter.ViewHolder> implements LeftSlideView.IonSlidingButtonListener{
    private List<My_Collection> list=new ArrayList<>();
    private Context context;
    private IonSlidingViewClickListener mIDeleteBtnClickListener;
    private LeftSlideView mMenu = null;


    public My_CollectionAdapter(Context context, List<My_Collection> collectionList){
        this.context=context;
        this.list=collectionList;
        this.mIDeleteBtnClickListener = (IonSlidingViewClickListener) context;

    }

     class ViewHolder extends RecyclerView.ViewHolder{
        private MyImageView Iv_head;
        private TextView Tv_name,Tv_content,Tv_time,Tv_num,tv_delete;
        private ViewGroup layout_content;
        public ViewHolder(View view){
            super(view);
            Iv_head=(MyImageView) view.findViewById(R.id.Iv_head);
            Tv_name=(TextView) view.findViewById(R.id.Tv_name);
            Tv_content=(TextView) view.findViewById(R.id.Tv_content);
            Tv_time=(TextView) view.findViewById(R.id.Tv_time);
            Tv_num=(TextView) view.findViewById(R.id.Tv_num);
            tv_delete=(TextView) view.findViewById(R.id.tv_delete);
            layout_content = (ViewGroup) view.findViewById(R.id.layout_content);
            ((LeftSlideView) view).setSlidingButtonListener(My_CollectionAdapter.this);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_my_collection,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        My_Collection collection=list.get(position);
        holder.layout_content.getLayoutParams().width = Utils.getScreenWidth(context);

        if(!TextUtils.isEmpty(collection.getTeacherheadimg())) {
            holder.Iv_head.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+collection.getTeacherheadimg());
        }else {
            holder.Iv_head.setImageResource(R.drawable.replace);
        }
        if(TextUtils.isEmpty(collection.getSummary())){
            holder.Tv_content.setText(collection.getContent());
        }else {
            holder.Tv_content.setText(collection.getSummary());
        }
        holder.Tv_name.setText(collection.getTeachername());
        holder.Tv_time.setText(collection.getCollectiontime());
        holder.Tv_num.setText(collection.getClick()+"");

        //item正文点击事件
        holder.layout_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    int n = holder.getLayoutPosition();
                    mIDeleteBtnClickListener.onItemClick(v, n);
                }
            }
        });

        //左滑删除点击事件
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = holder.getLayoutPosition();
                mIDeleteBtnClickListener.onDeleteBtnCilck(view, n);
            }
        });

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

    /**
     * 删除item
     * @param position
     */
    public void removeData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }
    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (LeftSlideView) view;
    }

    /**
     * 滑动或者点击了Item监听
     *
     * @param leftSlideView
     */
    @Override
    public void onDownOrMove(LeftSlideView leftSlideView) {
        if (menuIsOpen()) {
            if (mMenu != leftSlideView) {
                closeMenu();
            }
        }
    }
    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;

    }
    /**
     * 判断菜单是否打开
     *
     * @return
     */
    public Boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        return false;
    }

    /**
     * 注册接口的方法：点击事件。在Mactivity.java实现这些方法。
     */
    public interface IonSlidingViewClickListener {
        void onItemClick(View view, int position);//点击item正文

        void onDeleteBtnCilck(View view, int position);//点击“删除”

    }

}
