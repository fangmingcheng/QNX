package zgt.com.example.myzq.model.common.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.order.Agreement;
import zgt.com.example.myzq.bean.order.OrderDetail;
import zgt.com.example.myzq.model.common.MainActivity;
import zgt.com.example.myzq.model.common.adapter.courseAdapter.AgreementAdapter;
import zgt.com.example.myzq.model.common.course.CourseDetailActivity;
import zgt.com.example.myzq.model.common.custom_view.MyImageBackgroundView;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.model.common.personal_center.Purchase_recordActivity;
import zgt.com.example.myzq.model.common.personal_center.mycourse.MyCoursesActivity;
import zgt.com.example.myzq.model.common.refund.RefundCommitActivity;
import zgt.com.example.myzq.model.common.refund.RefundDetailActivity;
import zgt.com.example.myzq.utils.MyListView;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.StatusBarUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class FinishOrderActivity extends BaseActivity {
    @BindView(R.id.Iv_head)
    MyImageBackgroundView Iv_head;

    @BindView(R.id.Tv_title_order)
    TextView Tv_title_order;

    @BindView(R.id.Tv_teacher)
    TextView Tv_teacher;

    @BindView(R.id.Tv_charge)
    TextView Tv_charge;

//    @BindView(R.id.Tv_num)
//    TextView Tv_num;

    @BindView(R.id.Tv_orderNo)
    TextView Tv_orderNo;

    @BindView(R.id.Tv_time)
    TextView Tv_time;

    @BindView(R.id.Tv_order_time)
    TextView Tv_order_time;

    @BindView(R.id.Tv_pay_time)
    TextView Tv_pay_time;

    @BindView(R.id.Tv_pay_type)
    TextView Tv_pay_type;

    @BindView(R.id.Tv_start_time)
    TextView Tv_start_time;

    @BindView(R.id.Tv_end_time)
    TextView Tv_end_time;

    @BindView(R.id.Lv_agreement)
    MyListView Lv_agreement;

    @BindView(R.id.Tv_price)
    TextView Tv_price;

    @BindView(R.id.Tv_refund)
    TextView Tv_refund;

    @BindView(R.id.Bt_commit)
    Button Bt_commit;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.Rl_reload)
    RelativeLayout Rl_reload;
    private OrderDetail orderDetail;

   private List<Agreement> list = new ArrayList<>();
   private AgreementAdapter adapter;
   private Agreement agreement;
   private String orderId;

   private int status;
   private int index;//判断从哪个入口购买，1正常购买，2.我的课程中续费 3.我的订单中购买。
   private int purchase_status;


    @Override
    public void initToolBar() {

    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_finish_order;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.statusBarLightMode(this);

        orderId=getIntent().getStringExtra("orderId");
        purchase_status=getIntent().getIntExtra("purchase_status",1);
        status = getIntent().getIntExtra("status",0);
        index = getIntent().getIntExtra("index",0);
        if(purchase_status ==1){
            Bt_commit.setText("人工审核中…");
            Bt_commit.setBackgroundColor(Color.parseColor("#b2b2b2"));
            Bt_commit.setClickable(false);
        }else if(purchase_status ==4){
            Bt_commit.setText("已退款…");
            Bt_commit.setBackgroundColor(Color.parseColor("#b2b2b2"));
            Bt_commit.setClickable(false);
        }
//        StatusBarUtil.statusBarLightMode(this);
        adapter = new AgreementAdapter(this);
        adapter.addAll(list);
        Lv_agreement.setAdapter(adapter);
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private void initData(OrderDetail orderDetail,List<Agreement> agreementList){
        if(orderDetail != null){
            Iv_head.setImageURL(orderDetail.getPicpath());
            Iv_head.setType(1);
            Tv_title_order.setText(orderDetail.getTitle());
            if(orderDetail.getProducttype()==1){
                Tv_teacher.setText("主讲老师:"+orderDetail.getLecturer());
            }else {
                Tv_teacher.setText(orderDetail.getLecturer());
            }


            if(orderDetail.getIsnewversion()==0){
                if(orderDetail.getPaytype() == 3){
                    Tv_charge.setText(orderDetail.getAmount()*orderDetail.getIosrealprice()+"牵牛币");
                    Tv_price.setText(orderDetail.getIosrealmoney()+"牵牛币");
                }else {
                    Tv_charge.setText(orderDetail.getAmount()*orderDetail.getRealprice()+"元" );
                    Tv_price.setText(orderDetail.getRealmoney()+"元");
                }

                if(orderDetail.getPricelimit()==0){
                    Tv_time.setText("永久");
                }else {
                    Tv_time.setText(orderDetail.getAmount()*orderDetail.getPricelimit()+"天");
//                    if(orderDetail.getPriceunit()==0){
//                        Tv_time.setText(orderDetail.getPricelimit()+"天");
//                    }else if(orderDetail.getPriceunit()==1){
//                        Tv_time.setText("永久");
//                    }
                }
            }else if(orderDetail.getIsnewversion()==1){
                if(orderDetail.getPricelimit()==0){
                    Tv_time.setText("永久");
                }else {
                    if(orderDetail.getPriceunit()==0){
                        Tv_time.setText(orderDetail.getPricenum()+"天");
                    }else if(orderDetail.getPriceunit()==1){
                        Tv_time.setText(orderDetail.getPricenum()+"个月");
                    }else if(orderDetail.getPriceunit()==2){
                        Tv_time.setText(orderDetail.getPricenum()+"季度");
                    }else if(orderDetail.getPriceunit()==3){
                        Tv_time.setText("半年");
                    }else if(orderDetail.getPriceunit()==4){
                        Tv_time.setText(orderDetail.getPricenum()+"年");
                    }
                }

                if(orderDetail.getPaytype() == 3){
                    Tv_charge.setText(orderDetail.getIosrealprice()+"牵牛币");
//                Tv_real_charge.setText(orderDetail.getIosrealprice()+"牵牛币（"+orderDetail.getPricelimit()+"天)");
                    Tv_price.setText(orderDetail.getIosrealmoney()+"牵牛币");
                }else {
                    Tv_charge.setText(orderDetail.getRealprice()+"元");
                    Tv_price.setText(orderDetail.getRealmoney()+"元");
                }

            }
//            Tv_time.setText();
//            if(orderDetail.getPricelimit()==0) {
//                Tv_real_charge.setText(orderDetail.getRealprice()+"(永久)");
//            }else {
//                Tv_real_charge.setText(orderDetail.getRealprice()+"元（"+orderDetail.getPricelimit()+"天)");
//            }

//            Tv_num.setText("X"+orderDetail.getAmount());
            Tv_orderNo.setText(orderDetail.getOrderno());
            Tv_order_time.setText(orderDetail.getOrdertime());
            Tv_pay_time.setText(orderDetail.getPaytime());
            Tv_start_time.setText(orderDetail.getStartdate());
            Tv_end_time.setText(orderDetail.getEnddate());
            if(orderDetail.getPaytype() == 0){
                Tv_pay_type.setText("未知");
            }else if(orderDetail.getPaytype() == 1){
                Tv_pay_type.setText("支付宝");
            }else if(orderDetail.getPaytype() == 2){
                Tv_pay_type.setText("微信");
            }else if(orderDetail.getPaytype() == 3){
                Tv_pay_type.setText("ios虚拟币");
            }else if(orderDetail.getPaytype() == 4){
                Tv_pay_type.setText("线下");
            }
        }
        if(agreementList.size()>0){
            adapter.clear();
            adapter.addAll(agreementList);
            setListViewHeightBasedOnChildren(Lv_agreement);
        }
        if(orderDetail.getStatus()==1){
            Tv_refund.setText("申请退款");
        }else if(orderDetail.getStatus()==2){
            Tv_refund.setText("申请退款");
        }else if(orderDetail.getStatus()==3){
            Tv_refund.setText("申请退款中");
        }else if(orderDetail.getStatus()==4){
            Tv_refund.setText("已退款");
        }
    }

    @OnClick({R.id.Iv_back,R.id.Bt_commit,R.id.Bt_reload,R.id.Tv_refund})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
                if(status == 1){
                    myfinish();
                }else {
                    finish();
                }
                break;
            case R.id.Bt_commit:
                if(orderDetail!=null) {
                    if(orderDetail.getProducttype()==1){
                        if(status == 1){
                            startActivity(new Intent().setClass(FinishOrderActivity.this, CourseDetailActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("uuid", orderDetail.getTypeid()));
//                        FinishOrderActivity.this.finish();
                        }else {
                            startActivity(new Intent().setClass(FinishOrderActivity.this, CourseDetailActivity.class).putExtra("uuid", orderDetail.getTypeid()));
                        }
                    }


                }
            case R.id.Bt_reload:
                scrollView.setVisibility(View.VISIBLE);
                Rl_reload.setVisibility(View.GONE);
                list=new ArrayList<>();
                getData();
                break;
            case R.id.Tv_refund:
                if(orderDetail!=null){
                    if(orderDetail.getPaytype() == 3){
                        ToastUtil.showShortToast(FinishOrderActivity.this,"该订单系虚拟币支付，请于苹果手机中申请退款");
                    }else {
                        if(orderDetail.getApplyStatus() == -1){
                            startActivity(new Intent().setClass(FinishOrderActivity.this, RefundCommitActivity.class).putExtra("orderDetail",orderDetail.getUuid()));
                        }else {
                            startActivity(new Intent().setClass(FinishOrderActivity.this, RefundDetailActivity.class).putExtra("orderDetail",orderDetail.getUuid()));
                        }

                    }
                }
                break;
        }
    }

    private void getData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress() + "getOrderDetailByOrderid.do");
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("orderid", orderId);
        requestParams.setConnectTimeout(60 * 1000);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if (a==1) {
                        orderDetail = new OrderDetail();
                        orderDetail.setUuid(json.getString("uuid"));
                        orderDetail.setOrderno(json.getString("orderno"));
                        orderDetail.setTypeid(json.getString("typeid"));
                        orderDetail.setProducttype(json.getInt("producttype"));
                        orderDetail.setPrice(json.getDouble("price"));
                        orderDetail.setRealprice(json.getDouble("realprice"));
                        orderDetail.setAmount(json.getInt("amount"));
                        orderDetail.setApplyStatus(json.getInt("applyStatus"));
                        orderDetail.setRealmoney(json.getDouble("realmoney"));
                        orderDetail.setIosrealmoney(json.getDouble("iosrealmoney"));
                        orderDetail.setPricelimit(json.getInt("pricelimit"));
                        orderDetail.setRelationno(json.getString("relationno"));
                        orderDetail.setIosrealprice(json.getDouble("iosrealprice"));
                        orderDetail.setStartdate(json.getString("startdate"));
                        orderDetail.setEnddate(json.getString("enddate"));
                        orderDetail.setStatus(json.getInt("status"));
                        orderDetail.setOrdertime(json.getString("ordertime"));
                        orderDetail.setPaytype(json.getInt("paytype"));
                        orderDetail.setPaytime(json.getString("paytime"));
                        orderDetail.setPayid(json.getString("payid"));
                        orderDetail.setPicpath(json.getString("picpath"));
                        orderDetail.setTitle(json.getString("title"));
                        orderDetail.setLecturer(json.getString("lecturer"));
                        orderDetail.setPriceunit(json.getInt("priceunit"));
                        orderDetail.setPricenum(json.getInt("pricenum"));
                        orderDetail.setIsnewversion(json.getInt("isnewversion"));
                        JSONArray array= json.getJSONArray("agreementList");
                        list=new ArrayList<>();
                        for(int i=0;i<array.length();i++){
                            agreement =  new Agreement();
                            agreement.setUuid(array.getJSONObject(i).getString("uuid"));
                            agreement.setFilename(array.getJSONObject(i).getString("filename"));
                            agreement.setFilepath(array.getJSONObject(i).getString("filepath"));
                            agreement.setAddtime(array.getJSONObject(i).getString("addtime"));
                            agreement.setTypename(array.getJSONObject(i).getString("typename"));
                            list.add(agreement);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initData(orderDetail,list);
                            }
                        });
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(FinishOrderActivity.this, LoginActivity.class));
                                finish();
//                                if(TextUtils.isEmpty(SPUtil.getToken())){
//                                    startActivity(new Intent().setClass(DetailActivity.this,LoginActivity.class));
//                                    finish();
//                                }else {
//                                    new AlertDialog.Builder(DetailActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    startActivity(new Intent().setClass(DetailActivity.this,LoginActivity.class));
//                                                    finish();
//                                                    dialog.dismiss();
//                                                }
//                                            }).create().show();
//                                }
                            }
                        });
                    }

                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(FinishOrderActivity.this, "解析异常");
                        }
                    });

                } finally {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(scrollView!=null){
                            scrollView.setVisibility(View.GONE);
                        }
                        if(Rl_reload!=null){
                            Rl_reload.setVisibility(View.VISIBLE);
                        }
                        ToastUtil.showShortToast(FinishOrderActivity.this, "网络连接异常,请点击刷新 按钮");
                    }
                });
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void myfinish(){
        if(orderDetail!=null) {
            if(index == 1){
                startActivity(new Intent().setClass(FinishOrderActivity.this, CourseDetailActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("uuid", orderDetail.getTypeid()).putExtra("index",1));
                FinishOrderActivity.this.finish();
            }else if(index == 2){
                startActivity(new Intent().setClass(FinishOrderActivity.this, MyCoursesActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("uuid", orderDetail.getTypeid()));
                FinishOrderActivity.this.finish();
            }else if(index == 3){
                startActivity(new Intent().setClass(FinishOrderActivity.this, Purchase_recordActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("uuid", orderDetail.getTypeid()));
                FinishOrderActivity.this.finish();
            }else if(index == 4){
                startActivity(new Intent().setClass(FinishOrderActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                FinishOrderActivity.this.finish();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if(status == 1){
            myfinish();
        }else {
            super.onBackPressed();
        }

    }
}
