package zgt.com.example.myzq.model.common.refund;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import zgt.com.example.myzq.bean.refund.Refund;
import zgt.com.example.myzq.model.common.adapter.courseAdapter.AgreementAdapter;
import zgt.com.example.myzq.model.common.custom_view.MyImageBackgroundView;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.StatusBarUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class RefundCommitActivity extends BaseActivity {

    @BindView(R.id.Tv_status)
    TextView Tv_status;
    @BindView(R.id.Iv_head)
    MyImageBackgroundView Iv_head;

    @BindView(R.id.Iv_agree)
    ImageView Iv_agree;

    @BindView(R.id.Tv_course_title)
    TextView Tv_course_title;
    @BindView(R.id.Tv_teacher)
    TextView Tv_teacher;
    @BindView(R.id.Tv_price)
    TextView Tv_price;
    @BindView(R.id.Tv_num)
    TextView Tv_num;
    @BindView(R.id.Et_reason)
    EditText Et_reason;

    @BindView(R.id.Lv_agreement)
    ListView Lv_agreement;

    boolean isAgree = false;


    List<Agreement> list = new ArrayList<>();
    private AgreementAdapter adapter;
    private String uuid;

    private Refund refund;
    @Override
    public void initToolBar() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_refund_commit;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.statusBarLightMode(this);
        adapter =  new AgreementAdapter(RefundCommitActivity.this);
        adapter.addAll(list);
        Lv_agreement.setAdapter(adapter);
        uuid = getIntent().getStringExtra("orderDetail");
//        if(orderDetail != null){
//            Iv_head.setImageURL(orderDetail.getPicpath());
//            Iv_head.setType(1);
//            Tv_course_title.setText(orderDetail.getTitle());
//            Tv_teacher.setText("主讲老师:"+orderDetail.getLecturer());
//            if(orderDetail.getPricelimit()==0) {
//                Tv_price.setText(orderDetail.getRealmoney()+"(永久)");
//            }else {
//                Tv_price.setText(orderDetail.getRealmoney()+"元（"+orderDetail.getPricelimit()+"天)");
//            }
//            Tv_num.setText("X"+orderDetail.getAmount());
//            Tv_status.setText(orderDetail.getOrderno());
//        }
        getData();
    }

    @OnClick({R.id.Iv_back,R.id.Bt_commit,R.id.Iv_agree})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
               finish();
                break;
            case R.id.Bt_commit:
                if(isAgree){
                    applyData();
                }else {
                    ToastUtil.showShortToast(RefundCommitActivity.this,"请先同意这些协议");
                }
                break;
            case R.id.Iv_agree:
              if(isAgree){
                  isAgree=false;
                  Iv_agree.setImageResource(R.mipmap.ic_nochoice);
              }else {
                  isAgree=true;
                  Iv_agree.setImageResource(R.mipmap.ic_choice);
              }
                break;
        }
    }

    private void setData(){
//        if(orderDetail != null){
//            Iv_head.setImageURL(orderDetail.getPicpath());
//            Iv_head.setType(1);
//            Tv_course_title.setText(orderDetail.getTitle());
//            Tv_teacher.setText("主讲老师:"+orderDetail.getLecturer());
//            if(orderDetail.getPricelimit()==0) {
//                Tv_price.setText(orderDetail.getRealmoney()+"(永久)");
//            }else {
//                Tv_price.setText(orderDetail.getRealmoney()+"元（"+orderDetail.getPricelimit()+"天)");
//            }
//            Tv_num.setText("X"+orderDetail.getAmount());
//            Tv_status.setText(orderDetail.getOrderno());
//        }
        if(refund!=null){
            Iv_head.setType(1);
            Iv_head.setImageURL(refund.getPicpath());
            Tv_status.setText(refund.getOrderno());
            Tv_course_title.setText(refund.getTitle());
            if(refund.getProducttype()==1){
                Tv_teacher.setText("主讲老师:"+refund.getLecturer());
            }else if(refund.getProducttype()==3){
                Tv_teacher.setText(refund.getLecturer());
            }
            Tv_price.setText(refund.getRealmoney()+"元");
//            if(refund.getIsnewversion()==0){
//                if(refund.getPricelimit()==0) {
//                    Tv_price.setText(refund.getPrice()+"(永久)");
//                    Tv_num.setText("");
//                }else {
//                    Tv_price.setText(refund.getPrice()+"元（"+refund.getPricelimit()+"天)");
////                    Tv_num.setText("X"+refund.get());
//                }
//            }else {
//                Tv_price.setText(refund.getPrice()+"元");
//            }
        }
    }

    private void getData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"applyRefund0610.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("orderid", uuid);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        refund = new Refund();
//                        refund.setIosprice(json.getDouble("iosprice"));
                        refund.setIsnewversion(json.getInt("isnewversion"));
                        refund.setLecturer(json.getString("lecturer"));
//                        refund.setOrderid(json.getString("orderid"));
                        refund.setOrderno(json.getString("orderno"));
                        refund.setPicpath(json.getString("picpath"));
                        refund.setPrice(json.getDouble("price"));
                        refund.setRealmoney(json.getDouble("realmoney"));
                        refund.setPricelimit(json.getInt("pricelimit"));
                        refund.setPricenum(json.getInt("pricenum"));
                        refund.setPriceunit(json.getInt("priceunit"));
                        refund.setProducttype(json.getInt("producttype"));
                        refund.setTitle(json.getString("title"));
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
                                if(list.size()>0){
                                    adapter.addAll(list);
                                    setListViewHeightBasedOnChildren(Lv_agreement);
                                    setData();
                                }
                            }
                        });
                    } else if(a==2){
                        startActivity(new Intent().setClass(RefundCommitActivity.this, RefundDetailActivity.class).putExtra("orderDetail", uuid));
                        RefundCommitActivity.this.finish();
                    }else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(RefundCommitActivity.this, LoginActivity.class));
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
                                ToastUtil.showShortToast(RefundCommitActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(RefundCommitActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(RefundCommitActivity.this, "网络连接异常");
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

    private void applyData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"saveApplyRefund.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("orderid", uuid);
        requestParams.addParameter("returnreason", Et_reason.getText().toString().trim());
        requestParams.addParameter("returntype", 0);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(RefundCommitActivity.this, RefundDetailActivity.class).putExtra("orderDetail", uuid));
                                RefundCommitActivity.this.finish();
                            }
                        });
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(RefundCommitActivity.this, LoginActivity.class));
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
                                ToastUtil.showShortToast(RefundCommitActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(RefundCommitActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(RefundCommitActivity.this, "网络连接异常");
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
