package zgt.com.example.myzq.model.common.adapter.order;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.bean.order.PurchaseRecord;
import zgt.com.example.myzq.model.common.custom_view.MyImageBackgroundView;

public class PurchaseRecrdAdapter extends  RecyclerView.Adapter<PurchaseRecrdAdapter.ViewHolder> implements View.OnClickListener{
    private List<PurchaseRecord> list=new ArrayList<>();
    private Context context;

    public PurchaseRecrdAdapter(Context context, List<PurchaseRecord> zbBeans){
        this.context=context;
        this.list=zbBeans;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private MyImageBackgroundView Iv_head;
        private TextView Tv_billno,Tv_teacher,Tv_title,Tv_status,Tv_price,Tv_date,Tv_status_bill;
        private Button Bt_delete,Bt_commit;
        private LinearLayout Ll_course;
        public ViewHolder(View view){
            super(view);
            Iv_head=(MyImageBackgroundView) view.findViewById(R.id.Iv_head);
            Tv_title=(TextView) view.findViewById(R.id.Tv_title);
            Tv_teacher=(TextView) view.findViewById(R.id.Tv_teacher);
            Tv_billno=(TextView) view.findViewById(R.id.Tv_billno);
            Tv_status=(TextView) view.findViewById(R.id.Tv_status);
            Tv_price=(TextView) view.findViewById(R.id.Tv_price);
            Tv_date=(TextView) view.findViewById(R.id.Tv_date);

            Tv_status_bill=(TextView) view.findViewById(R.id.Tv_status_bill);
            Ll_course=(LinearLayout) view.findViewById(R.id.Ll_course);


            Bt_delete=(Button) view.findViewById(R.id.Bt_delete);
            Bt_commit=(Button) view.findViewById(R.id.Bt_commit);

            Bt_delete.setOnClickListener(PurchaseRecrdAdapter.this);
            Bt_commit.setOnClickListener(PurchaseRecrdAdapter.this);
            Ll_course.setOnClickListener(PurchaseRecrdAdapter.this);
            view.setOnClickListener(PurchaseRecrdAdapter.this);

        }
    }

    //用一个枚举类型来表示不同的view
    public enum ViewName{
        DELETE,
        COMMIT,
        COURSE
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        switch (view.getId()) {
            case R.id.Bt_delete:
                onItemClickListener.OnItemClick(view, PurchaseRecrdAdapter.ViewName.DELETE, position);
                break;
            case R.id.Bt_commit:
                onItemClickListener.OnItemClick(view, PurchaseRecrdAdapter.ViewName.COMMIT, position);
                break;
            case R.id.Ll_course:
                onItemClickListener.OnItemClick(view, PurchaseRecrdAdapter.ViewName.COURSE, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_purchase_record,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.itemView.setTag(position);//根据position来绑定 item的位置 判断是哪个item下面的子项的点击事件
        holder.Bt_delete.setTag(position);
        holder.Bt_commit.setTag(position);
        holder.Ll_course.setTag(position);
        final PurchaseRecord live=list.get(position);

        holder.Tv_title.setText(live.getTitle());
        if(live.getProducttype()==1){
            holder.Tv_teacher.setText("主讲老师："+live.getLecturer());
        }else {
            holder.Tv_teacher.setText(live.getLecturer());
        }
//        if(live.getIsnewversion()==0){
//            holder.Tv_teacher.setText("主讲老师："+live.getLecturer());
//        }else if(live.getPaytype() == 1){
//            holder.Tv_teacher.setText(live.getLecturer());
//        }

        if(live.getStatus()==0){
            holder.Tv_status.setText("待付款");
            holder.Bt_commit.setVisibility(View.VISIBLE);
            holder.Bt_delete.setVisibility(View.VISIBLE);
            holder.Tv_status.setTextColor(Color.parseColor("#E46866"));
            holder.Tv_status_bill.setVisibility(View.GONE);
        }else if(live.getStatus()==1){
            holder.Tv_status.setText("已支付");
            holder.Tv_status_bill.setText("已完成支付");
            holder.Bt_commit.setVisibility(View.GONE);
            holder.Bt_delete.setVisibility(View.GONE);
            holder.Tv_status_bill.setVisibility(View.VISIBLE);
            holder.Tv_status.setTextColor(Color.parseColor("#919191"));
        }else if(live.getStatus()==2){
            holder.Tv_status.setText("已签约");
            holder.Tv_status_bill.setVisibility(View.VISIBLE);
            holder.Tv_status.setTextColor(Color.parseColor("#919191"));
            holder.Bt_commit.setVisibility(View.GONE);
            holder.Bt_delete.setVisibility(View.GONE);
            holder.Tv_status_bill.setText("已完成签约");
        }else if(live.getStatus()==-1){
            holder.Tv_status.setText("已取消");
            holder.Tv_status_bill.setVisibility(View.VISIBLE);
            holder.Tv_status.setTextColor(Color.parseColor("#919191"));
            holder.Bt_commit.setVisibility(View.GONE);
            holder.Bt_delete.setVisibility(View.GONE);
            holder.Tv_status_bill.setText("已取消");
        }else if(live.getStatus()==-2){
            holder.Tv_status.setText("已过期");
            holder.Tv_status_bill.setVisibility(View.VISIBLE);
            holder.Tv_status.setTextColor(Color.parseColor("#919191"));
            holder.Bt_commit.setVisibility(View.GONE);
            holder.Bt_delete.setVisibility(View.GONE);
            holder.Tv_status_bill.setText("已过期");
        }else if(live.getStatus()==3){
            holder.Tv_status.setText("申请退款中");
            holder.Tv_status_bill.setVisibility(View.VISIBLE);
            holder.Tv_status.setTextColor(Color.parseColor("#919191"));
            holder.Bt_commit.setVisibility(View.GONE);
            holder.Bt_delete.setVisibility(View.GONE);
            holder.Tv_status_bill.setText("申请退款中");
        } else if(live.getStatus()==4){
            holder.Tv_status.setText("已退款");
            holder.Tv_status_bill.setVisibility(View.VISIBLE);
            holder.Tv_status.setTextColor(Color.parseColor("#919191"));
            holder.Bt_commit.setVisibility(View.GONE);
            holder.Bt_delete.setVisibility(View.GONE);
            holder.Tv_status_bill.setText("已退款");
        }
        holder.Tv_billno.setText("订单号："+live.getOrderno());
//        if(live.getOrdertime().length()>10) {
            holder.Tv_date.setText("下单日期：" + live.getOrdertime().substring(0, 10));
//        }
        if(live.getPaytype()==3){
            holder.Tv_price.setText(live.getAmount()*live.getIosrealprice()+"牵牛币");
        }else {
            holder.Tv_price.setText(live.getAmount()*live.getRealprice()+"元");
        }

        if(!TextUtils.isEmpty(live.getPicpath())) {
            holder.Iv_head.setImageURL(live.getPicpath());
//            holder.Iv_head.setImageURL(SPUtil.getServerAddress().substring(0,SPUtil.getServerAddress().length()-5)+live.getPicpath());
        }else {
//            holder.Iv_head.setImageResource(R.drawable.replace);
        }

        holder.Iv_head.setType(1);

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
