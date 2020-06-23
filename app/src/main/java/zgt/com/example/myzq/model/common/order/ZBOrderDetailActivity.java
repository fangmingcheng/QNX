package zgt.com.example.myzq.model.common.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import zgt.com.example.myzq.bean.order.Agreement;
import zgt.com.example.myzq.bean.order.Price;
import zgt.com.example.myzq.model.common.adapter.price.PriceAdapter;
import zgt.com.example.myzq.model.common.custom_view.MyImageBackgroundView;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.SpaceItemDecoration;
import zgt.com.example.myzq.utils.StatusBarUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class ZBOrderDetailActivity extends BaseActivity {

    @BindView(R.id.Iv_head)
    MyImageBackgroundView Iv_head;

    @BindView(R.id.Tv_title_order)
    TextView Tv_title_order;

    @BindView(R.id.Tv_teacher)
    TextView Tv_teacher;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @BindView(R.id.Iv_agree)
    ImageView Iv_agree;

    @BindView(R.id.Iv_choice)
    ImageView Iv_choice;

    @BindView(R.id.Et_email)
    EditText Et_email;

    @BindView(R.id.Bt_commit)
    Button Bt_commit;

    @BindView(R.id.Tv_agreement)
    TextView Tv_agreement;

    @BindView(R.id.Ll_choice)
    LinearLayout Ll_choice;

    private LinearLayoutManager layoutManager;
    private List<Price> list = new ArrayList<>();
    private Price price;

    private Agreement agreement;
    List<Agreement> agreementList = new ArrayList<>();

    PriceAdapter adapter;

    private int current = -1;
    private int iseinvoice,index;
    private Boolean isAgree = false;
    private Boolean isChoice = false;


    private String fileId,type,title,picpath,lecturer;

    int index1;

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.statusBarLightMode(this);
        Bt_commit.setClickable(false);
        index = getIntent().getIntExtra("index",0);
        fileId=getIntent().getStringExtra("fileid");
        type=getIntent().getStringExtra("type");
        getPrice(fileId,type);
        initRecyclerview();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_zborder_detail;
    }

    private void initRecyclerview(){
        layoutManager = new LinearLayoutManager(ZBOrderDetailActivity.this);
        //调整RecyclerView的排列方向
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(layoutManager);
        //设置item间距，30dp
        recyclerview.addItemDecoration(new SpaceItemDecoration(20));
        adapter=new PriceAdapter(this,list);
        recyclerview.setAdapter(adapter);
        recyclerview.setNestedScrollingEnabled(false);//NestedScrollView嵌套RecyclerView卡顿解决办法：

        adapter.setOnItemClickListener(new PriceAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview.getChildAdapterPosition(view);
                current=position;
                adapter.setCurrent(current);
                Bt_commit.setText("签署合同");
                Bt_commit.setBackgroundColor(Color.parseColor("#ff4444"));
                Bt_commit.setClickable(true);
                adapter.notifyDataSetChanged();

//                startActivity(new Intent().setClass(getActivity(), CourseDetailActivity.class).putExtra("uuid",recommendedtodays.get(position).getUuid()).putExtra("index",1));
            }
        });

    }


    @OnClick({R.id.Iv_back,R.id.Bt_commit,R.id.Iv_agree,R.id.Iv_choice})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.Iv_back://
                finish();
                break;
            case R.id.Bt_commit://
                if(isAgree) {

                    if(current!=-1){
                        if(isChoice){
                            if(isEmailNum(Et_email.getText().toString().trim())){
                                getData(fileId);
                            }else {
                                ToastUtil.showShortToast(ZBOrderDetailActivity.this,"请输入正确的邮箱格式");
                            }
                        }else {
                            getData(fileId);
                        }
                    }else {
                        ToastUtil.showShortToast(ZBOrderDetailActivity.this,"请选择套餐");
                    }


                }else {
                    ToastUtil.showShortToast(ZBOrderDetailActivity.this,"请先同意这些协议");
                }
                break;
            case R.id.Iv_agree://
                if(isAgree){
                    isAgree = false;
                    Iv_agree.setImageResource(R.mipmap.btn_gouxuan1);
                }else {
                    Iv_agree.setImageResource(R.mipmap.btn_gouxuan2);
                    isAgree = true;
                }
                break;
            case R.id.Iv_choice://
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
        }
    }

    private void setAgreement(){
        Tv_title_order.setText(title);
        if("1".equals(type)){
            Tv_teacher.setText("主讲老师："+lecturer);
        }else if("3".equals(type)){
            Tv_teacher.setText("简介："+lecturer);
        }

        Iv_head.setImageURL(picpath);
        Iv_head.setType(1);
        StringBuilder sbBuilder = new StringBuilder();
        for (int i = 0; i < agreementList.size(); i++) {
            sbBuilder.append("《"+agreementList.get(i).getFilename() + "》、");
        }

        String likeUsers = sbBuilder.substring(0, sbBuilder.lastIndexOf("、")).toString();
        Tv_agreement.setMovementMethod(LinkMovementMethod.getInstance());
        Tv_agreement.setText(addClickablePart(likeUsers), TextView.BufferType.SPANNABLE);
    }

    private int getIndex(String name){

        for(int i=0;i<agreementList.size();i++){
            if(name.contains(agreementList.get(i).getFilename())){
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
                        startActivity(new Intent().setClass(ZBOrderDetailActivity.this, WebViewActivity.class).putExtra("url",agreementList.get(index1).getFilepath()));
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

    private void getPrice(String  typeid,String type){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"getOrderConfirmInfo.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("fileid", typeid);
        requestParams.addParameter("producttype", type);
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
                        title = json.getString("title");
                        picpath = json.getString("picpath");
                        lecturer = json.getString("lecturer");
                        JSONArray array = json.getJSONArray("bplist");
                        for(int i = 0;i<array.length();i++){
                            agreement = new Agreement();
                            agreement.setFilename(array.getJSONObject(i).getString("name"));
                            agreement.setFilepath(array.getJSONObject(i).getString("url"));
                            agreementList.add(agreement);
                        }

                        JSONArray jsonArray = json.getJSONArray("pricelist");
                        for(int i = 0;i<jsonArray.length();i++){
                            price = new Price();
                            price.setIsdefault(jsonArray.getJSONObject(i).getInt("isdefault"));
                            price.setPrice(jsonArray.getJSONObject(i).getInt("price"));
                            price.setPricelimit(jsonArray.getJSONObject(i).getInt("pricelimit"));
                            price.setPricenum(jsonArray.getJSONObject(i).getInt("pricenum"));
                            price.setPricesort(jsonArray.getJSONObject(i).getInt("pricesort"));
//                            price.setPricestatus(jsonArray.getJSONObject(i).getInt("pricestatus"));
                            price.setPriceunit(jsonArray.getJSONObject(i).getInt("priceunit"));
                            price.setProductid(jsonArray.getJSONObject(i).getString("productid"));
                            price.setPricename(jsonArray.getJSONObject(i).getString("pricename"));
                            price.setPtype(jsonArray.getJSONObject(i).getInt("ptype"));
                            price.setRealprice(jsonArray.getJSONObject(i).getInt("realprice"));
                            price.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            list.add(price);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setAgreement();
                                adapter.notifyDataSetChanged();
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
                                startActivity(new Intent().setClass(ZBOrderDetailActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {

                        ToastUtil.showShortToast(ZBOrderDetailActivity.this, msg);
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
                        ToastUtil.showShortToast(ZBOrderDetailActivity.this, "网络连接异常");
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
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"doSubmitOrderAndroid0518.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("fileid", fileid);
        requestParams.addParameter("priceid", list.get(current).getUuid());
        requestParams.addParameter("producttype", type);
        requestParams.addParameter("type", 0);
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
                                startActivity(new Intent().setClass(ZBOrderDetailActivity.this, OrderPaymentActivity.class).putExtra("uuid",orderid).putExtra("price",list.get(current).getRealprice()).putExtra("index",index));
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
                                startActivity(new Intent().setClass(ZBOrderDetailActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {

                        ToastUtil.showShortToast(ZBOrderDetailActivity.this, msg);
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
                        ToastUtil.showShortToast(ZBOrderDetailActivity.this, "网络连接异常");
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
