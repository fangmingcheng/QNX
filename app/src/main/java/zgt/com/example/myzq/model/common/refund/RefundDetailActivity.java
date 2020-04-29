package zgt.com.example.myzq.model.common.refund;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import zgt.com.example.myzq.model.common.adapter.courseAdapter.AgreementAdapter;
import zgt.com.example.myzq.model.common.custom_view.MyImageBackgroundView;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class RefundDetailActivity extends BaseActivity {

    @BindView(R.id.Tv_status)
    TextView Tv_status;
    @BindView(R.id.Iv_head)
    MyImageBackgroundView Iv_head;


    @BindView(R.id.Tv_course_title)
    TextView Tv_course_title;
    @BindView(R.id.Tv_teacher)
    TextView Tv_teacher;
    @BindView(R.id.Tv_price)
    TextView Tv_price;
    @BindView(R.id.Tv_num)
    TextView Tv_num;

    @BindView(R.id.Tv_complete)
    TextView Tv_complete;
    @BindView(R.id.Iv_complete)
    ImageView Iv_complete;

    @BindView(R.id.Tv_examine)
    TextView Tv_examine;
    @BindView(R.id.Iv_examine)
    ImageView Iv_examine;

    @BindView(R.id.Tv_date)
    TextView Tv_date;
    @BindView(R.id.Tv_refund_price)
    TextView Tv_refund_price;
    @BindView(R.id.Tv_stop_date)
    TextView Tv_stop_date;

    @BindView(R.id.Tv_reapply)
    TextView Tv_reapply;
    @BindView(R.id.Tv_reason)
    TextView Tv_reason;

    @BindView(R.id.Ll_reason)
    LinearLayout Ll_reason;

    @BindView(R.id.Lv_agreement)
    ListView Lv_agreement;

    private OrderDetail orderDetail;

    private int status;
    private String applytime;
    private String stoptime;
    private double returnmoney;
    private String remark;

    List<Agreement> list = new ArrayList<>();
    private AgreementAdapter adapter;

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {

        adapter =  new AgreementAdapter(RefundDetailActivity.this);
        adapter.addAll(list);
        Lv_agreement.setAdapter(adapter);

        orderDetail=(OrderDetail) getIntent().getSerializableExtra("orderDetail");
        if(orderDetail != null){
            Iv_head.setImageURL(orderDetail.getPicpath());
            Iv_head.setType(1);
            Tv_course_title.setText(orderDetail.getTitle());
            Tv_teacher.setText("主讲老师:"+orderDetail.getLecturer());
            if(orderDetail.getPricelimit()==0) {
                Tv_price.setText(orderDetail.getRealmoney()+"(永久)");
            }else {
                Tv_price.setText(orderDetail.getRealmoney()+"元（"+orderDetail.getPricelimit()+"天)");
            }
            Tv_num.setText("X"+orderDetail.getAmount());
            Tv_status.setText(orderDetail.getOrderno());
        }
        getData();
    }

    private void setData(){
        Tv_date.setText(applytime);
        Tv_refund_price.setText(returnmoney+"元");
        Tv_stop_date.setText(stoptime);
        if(status == 0){

        }else if(status == 1){
            Iv_examine.setImageResource(R.mipmap.ic_cg);
        }else if(status == 2){
            Tv_examine.setText("审核失败");
            Tv_complete.setText("");
            Iv_examine.setImageResource(R.mipmap.ic_shsb);
            Tv_reapply.setVisibility(View.VISIBLE);
            Ll_reason.setVisibility(View.VISIBLE);
            Tv_reason.setText("退款失败原因："+remark);
        }else if(status == 3){
            Iv_examine.setImageResource(R.mipmap.ic_yish);
            Iv_complete.setImageResource(R.mipmap.ic_cg);
        }

        if(list.size()>0){
            adapter.addAll(list);
            setListViewHeightBasedOnChildren(Lv_agreement);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_refund_detail;
    }

    @OnClick({R.id.Iv_back,R.id.Tv_reapply})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
                finish();
                break;
            case R.id.Tv_reapply:
                startActivity(new Intent().setClass(RefundDetailActivity.this, RefundCommitActivity.class).putExtra("orderDetail", orderDetail));
//                this.finish();
                break;

        }
    }

    private void getData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"refundSchedule.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("orderid", orderDetail.getUuid());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        status=json.getInt("applyStatus");
                        returnmoney=json.getDouble("returnmoney");
                        applytime=json.getString("applytime");
                        remark = json.getString("remark");
                        stoptime=json.getString("stopdate");
                        JSONArray jsonArray=json.getJSONArray("listp");
                        for (int i=0;i<jsonArray.length();i++){
                            Agreement agreement = new Agreement();
                            agreement.setFilename(jsonArray.getJSONObject(i).getString("name"));
                            agreement.setFilepath(jsonArray.getJSONObject(i).getString("url"));
                            list.add(agreement);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setData();
                            }
                        });
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(RefundDetailActivity.this, LoginActivity.class));
                                finish();
//                                new AlertDialog.Builder(My_msgActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                startActivity(new Intent().setClass(My_msgActivity.this,LoginActivity.class));
//                                                finish();
//                                                dialog.dismiss();
//                                            }
//                                        }).create().show();
                            }
                        });
                    }else {
                        final String msg=jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(RefundDetailActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(RefundDetailActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(RefundDetailActivity.this, "网络连接异常");
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

}
