package zgt.com.example.myzq.model.common.order;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.classes.CourseDetail;
import zgt.com.example.myzq.bean.classes.Mycourse;
import zgt.com.example.myzq.bean.order.Agreement;
import zgt.com.example.myzq.model.common.adapter.courseAdapter.AgreementAdapter;
import zgt.com.example.myzq.model.common.custom_view.MyImageBackgroundView;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.StatusBarUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class OrderDetaiilActivity extends BaseActivity {

    @BindView(R.id.Tv_title_order)
    TextView Tv_title_order;

    @BindView(R.id.Tv_teacher)
    TextView Tv_teacher;

    @BindView(R.id.Tv_charge)
    TextView Tv_charge;

    @BindView(R.id.Tv_real_charge)
    TextView Tv_real_charge;

    @BindView(R.id.Tv_price)
    TextView Tv_price;

    @BindView(R.id.Tv_commit)
    TextView Tv_commit;

//    @BindView(R.id.Tv_num)
//    TextView Tv_num;

    @BindView(R.id.Tv_day)
    TextView Tv_day;

    @BindView(R.id.Et_email)
    EditText Et_email;

    @BindView(R.id.Iv_head)
    MyImageBackgroundView Iv_head;

    @BindView(R.id.spinner)
    Spinner spinner;

    @BindView(R.id.Iv_choice)
    ImageView Iv_choice;

    @BindView(R.id.Ll_choice)
    LinearLayout Ll_choice;

    @BindView(R.id.Iv_agree)
    ImageView Iv_agree;

    @BindView(R.id.Tv_agreement)
    TextView Tv_agreement;

//    @BindView(R.id.Lv_agreement)
//    ListView Lv_agreement;

    private CourseDetail course = null;
    private Mycourse mycourse = null;
    private String status;
    private int index;
    double num;
    String fileId;

    private String[] nums ={"1","3","6"};
    private int coursenum = 1;

    private Agreement agreement;
    private AgreementAdapter adapter;
    List<Agreement> list = new ArrayList<>();

    private Boolean isAgree = false;
    private Boolean isChoice = false;
    private int iseinvoice = 0;
    int index1;

//    private

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_detaiil;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {

        StatusBarUtil.statusBarLightMode(this);
        status=getIntent().getStringExtra("status");
        Et_email.setText(SPUtil.getEmail());
        index = getIntent().getIntExtra("index",0);
        if("1".equals(status)){
            course = (CourseDetail) getIntent().getSerializableExtra("course");
            Tv_title_order.setText(course.getTitle());
            Tv_teacher.setText("主讲老师："+course.getLecturer());
            Iv_head.setImageURL(course.getPicpath());
            Iv_head.setType(1);
            if(course.getPricelimit()==0){
                Tv_day.setText("永久 X");
                spinner.setEnabled(false);
            }else {
                spinner.setEnabled(true);
                Tv_day.setText(course.getPricelimit()+"天 X");
            }
            num= course.getPrice()-course.getRealprice();
            if(num>0){
                Tv_real_charge.setText(course.getRealprice()+"元");
                Tv_charge.setText(course.getPrice()+"元");
                Tv_charge.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
                Tv_price.setText(Html.fromHtml("<font color='#333333'>需支付：</font><font color='#E46866'><big>￥"+course.getRealprice()+"</big></font><font color='#333333'>   已优惠"+num+"元</font>"));
            }else {
                Tv_real_charge.setText(course.getRealprice()+"元");
                Tv_price.setText(Html.fromHtml("<font color='#333333'>需支付：</font><font color='#E46866'><big>￥"+course.getRealprice()+"</big></font>"));
            }
            fileId = course.getUuid();
        }else if("2".equals(status)){
            mycourse = (Mycourse) getIntent().getSerializableExtra("course");
            Tv_title_order.setText(mycourse.getTitle());
            Tv_teacher.setText("主讲老师："+mycourse.getLecturer());
            Iv_head.setImageURL(mycourse.getPicpath());
            Iv_head.setType(1);

            if(mycourse.getPricelimit()==0){
                Tv_day.setText("永久 X");
            }else {
                Tv_day.setText(mycourse.getPricelimit()+"天 X");
            }

            num= mycourse.getPrice()-mycourse.getRealprice();
            if(num>0){
                Tv_real_charge.setText(mycourse.getRealprice()+"元");
                Tv_charge.setText(mycourse.getPrice()+"元");
                Tv_charge.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
                Tv_price.setText(Html.fromHtml("<font color='#333333'>需支付：</font><font color='#E46866'><big>￥"+mycourse.getRealprice()+"</big></font><font color='#333333'>   已优惠"+num+"元</font>"));
            }else {
                Tv_real_charge.setText(mycourse.getRealprice()+"元");
                Tv_price.setText(Html.fromHtml("<font color='#333333'>需支付：</font><font color='#E46866'><big>￥"+mycourse.getRealprice()+"</big></font>"));
            }

            fileId = mycourse.getUuid();
        }
        getBuyProto(fileId);

        ArrayAdapter<String> rolesadapter=new ArrayAdapter(this,R.layout.adapter_spinner,nums);
        spinner.setAdapter(rolesadapter);
        spinner.setDropDownWidth(180); //下拉宽度
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                coursenum=Integer.parseInt(parent.getItemAtPosition(position).toString().substring(0,1));

                if("1".equals(status)){
                    if(num>0){
                        Tv_price.setText(Html.fromHtml("<font color='#333333'>需支付：</font><font color='#E46866'><big>￥"+(course.getRealprice()*coursenum)+"</big></font><font color='#333333'>   已优惠"+(num*coursenum)+"元</font>"));
                    }else {
                        Tv_price.setText(Html.fromHtml("<font color='#333333'>需支付：</font><font color='#E46866'><big>￥"+course.getRealprice()*coursenum+"</big></font>"));
                    }

                }else if("2".equals(status)) {
                    if(num>0){
                        Tv_price.setText(Html.fromHtml("<font color='#333333'>需支付：</font><font color='#E46866'><big>￥"+(mycourse.getRealprice()*coursenum)+"</big></font><font color='#333333'>   已优惠"+(num*coursenum)+"元</font>"));
                    }else {
                        Tv_price.setText(Html.fromHtml("<font color='#333333'>需支付：</font><font color='#E46866'><big>￥"+mycourse.getRealprice()*coursenum+"</big></font>"));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @OnClick({R.id.Iv_agree,R.id.Iv_choice,R.id.Iv_back,R.id.Tv_commit})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_agree:
                if(isAgree){
                    isAgree = false;
                    Iv_agree.setImageResource(R.mipmap.btn_gouxuan1);
                }else {
                    Iv_agree.setImageResource(R.mipmap.btn_gouxuan2);
                    isAgree = true;
                }
                break;
            case R.id.Iv_choice:
                if(isChoice){
                    isChoice = false;
                    iseinvoice = 0;
                    Iv_choice.setImageResource(R.mipmap.ic_nochoice);
                    Ll_choice.setVisibility(View.GONE);
                }else {
                    Iv_choice.setImageResource(R.mipmap.ic_choice);
                    isChoice = true;
                    iseinvoice = 1;
                    Ll_choice.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.Tv_commit:
                if(isAgree) {
                    String uuid = "";
                    if ("1".equals(status)) {
                        uuid = course.getUuid();
                    } else if ("2".equals(status)) {
                        uuid = mycourse.getTypeid();
                    }
                    if(isChoice){
                        if(isEmailNum(Et_email.getText().toString().trim())){
                            getData(uuid);
                        }else {
                            ToastUtil.showShortToast(OrderDetaiilActivity.this,"请输入正确的邮箱格式");
                        }
                    }else {
                        getData(uuid);
                    }

                }else {
                    ToastUtil.showShortToast(OrderDetaiilActivity.this,"请先同意这些协议");
                }
                break;

            case R.id.Iv_back:
                finish();
                break;
        }
    }

    private void setAgreement(){
        StringBuilder sbBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sbBuilder.append("《"+list.get(i).getFilename() + "》、");
        }

        String likeUsers = sbBuilder.substring(0, sbBuilder.lastIndexOf("、")).toString();
        Tv_agreement.setMovementMethod(LinkMovementMethod.getInstance());
        Tv_agreement.setText(addClickablePart(likeUsers), TextView.BufferType.SPANNABLE);
    }

    private int getIndex(String name){

        for(int i=0;i<list.size();i++){
            if(name.contains(list.get(i).getFilename())){
                return i;
            }
        }
        return 0;
    }

    private SpannableStringBuilder addClickablePart(String str) {
        // 第一个赞图标
//        ImageSpan span = new ImageSpan(OrderDetaiilActivity.this, R.drawable.umeng_comm_like);
        SpannableString spanStr = new SpannableString("我已阅读并同意牵牛星");
//        spanStr.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder ssb = new SpannableStringBuilder(spanStr);
        ssb.append(str) ;

        String[] likeUsers = str.split("、");
        if (likeUsers.length > 0) {
            // 最后一个
            for (int i = 0; i < likeUsers.length; i++) {
                final String name = likeUsers[i];
                final int start = str.indexOf(name) + spanStr.length();
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
//                        Toast.makeText(OrderDetaiilActivity.this, name, Toast.LENGTH_SHORT).show();
                        index1 = getIndex(name);
                        startActivity(new Intent().setClass(OrderDetaiilActivity.this, WebViewActivity.class).putExtra("url",list.get(index1).getFilepath()));
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                         ds.setColor(Color.parseColor("#39BAFC")); // 设置文本颜色
                        // 去掉下划线
                        ds.setUnderlineText(false);
                    }

                }, start, start + name.length(), 0);
            }
        }
        return ssb.append("");
    } //


    //邮箱正则表达式
    private boolean isEmailNum(String mobiles) {
        Pattern p = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

//    private void setAgreement(List<Agreement> list){
//        setAgreement();
//        adapter = new AgreementAdapter(OrderDetaiilActivity.this);
//        adapter.addAll(list);
//        Lv_agreement.setAdapter(adapter);
//    }

    private void getBuyProto(String  fileid){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"getBuyProtocol.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("fileid", fileid);
        requestParams.addParameter("type", 0);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    String msg=jsonObject.getString("message");

                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        JSONArray array = json.getJSONArray("bplist");
                        for(int i = 0;i<array.length();i++){
                            agreement = new Agreement();
                            agreement.setFilename(array.getJSONObject(i).getString("name"));
                            agreement.setFilepath(array.getJSONObject(i).getString("url"));
                            list.add(agreement);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setAgreement();
                            }
                        });

                    } else if(a==-1){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                if(TextUtils.isEmpty(SPUtil.getToken())){
//                                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                                    getActivity().finish();
//                                }else {
//                                    new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                                                    getActivity().finish();
//                                                    dialog.dismiss();
//                                                }
//                                            }).create().show();
//                                }
                                startActivity(new Intent().setClass(OrderDetaiilActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {

                        ToastUtil.showShortToast(OrderDetaiilActivity.this, msg);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                ToastUtil.showShortToast(OrderDetaiilActivity.this, msg);
//                            }
//                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            ToastUtil.showShortToast(getActivity(), getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(OrderDetaiilActivity.this, "网络连接异常");
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

    private void getData(String fileid){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"doSubmitOrderAndroid.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("fileid", fileid);
        requestParams.addParameter("amount", coursenum);
        requestParams.addParameter("iseinvoice", iseinvoice);
        requestParams.addParameter("email", Et_email.getText().toString().trim());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    String msg=jsonObject.getString("message");

                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        String orderid = json.getString("orderid");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if("1".equals(status)){
                                    startActivity(new Intent().setClass(OrderDetaiilActivity.this, OrderPaymentActivity.class).putExtra("uuid",orderid).putExtra("price",coursenum * course.getRealprice()).putExtra("index",index));
                                }else if("2".equals(status)){
                                    startActivity(new Intent().setClass(OrderDetaiilActivity.this, OrderPaymentActivity.class).putExtra("uuid",orderid).putExtra("price",coursenum * mycourse.getRealprice()).putExtra("index",index));
                                }

                            }
                        });
                    } else if(a==-1){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                if(TextUtils.isEmpty(SPUtil.getToken())){
//                                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                                    getActivity().finish();
//                                }else {
//                                    new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                                                    getActivity().finish();
//                                                    dialog.dismiss();
//                                                }
//                                            }).create().show();
//                                }
                                startActivity(new Intent().setClass(OrderDetaiilActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {

                        ToastUtil.showShortToast(OrderDetaiilActivity.this, msg);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                ToastUtil.showShortToast(OrderDetaiilActivity.this, msg);
//                            }
//                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            ToastUtil.showShortToast(getActivity(), getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(OrderDetaiilActivity.this, "网络连接异常");
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


}
