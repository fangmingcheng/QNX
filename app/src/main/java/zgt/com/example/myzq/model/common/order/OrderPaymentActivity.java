package zgt.com.example.myzq.model.common.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.MyApp;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.pay.PayResult;
import zgt.com.example.myzq.bean.pay.WXPayInfo;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.StatusBarUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class OrderPaymentActivity extends BaseActivity {

    @BindView(R.id.Tv_price)
    TextView Tv_price;

    @BindView(R.id.Iv_wx_select)
    ImageView Iv_wx_select;

//    @BindView(R.id.Iv_zfb_select)
//    ImageView Iv_zfb_select;

//    @BindView(R.id.Iv_agree)
//    ImageView Iv_agree;

    private Boolean isWXSlect = false;
//    private Boolean isZFBSlect = false;

//    private Boolean isAgree = false;
//    private Course course;
    private String orderid;
    private int price;

    private int index;

    public static OrderPaymentActivity instance = null;

    /**
     * 微信支付
     */
    IWXAPI api;

    private static final String KEY_ORDER_ID = "order_id";
    private static final String KEY_LECTURER_ID = "lecturer_id";

    //是否跳转讲师主页
    private static final String KEY_GO_LECTURER_HOME = "key_go_lecturer_home";

    //订单是否加载
    private boolean isLoading = false;

    private static final int SDK_PAY_FLAG = 1;

    /**
     * 支付宝回调
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
//                        Log.d("zw--buy", price * num * 5 + "===price===" + order_id);
//                        Order order = Order.createOrder(order_id + "", price * num, "CNY");
//                        TCAgent.onOrderPaySucc(mUserId + "", "alipay", order);
//                        //TrackingIO.setPayment(order_id+"", "alipay", "RMB",price * num*5 );
//                        Log.d("zw--buy", price * num * 5 + "===price===" + order_id);
//                        gotoBuyFinishPage();
                        isLoading = false;
                        startActivity(new Intent().setClass(OrderPaymentActivity.this,FinishOrderActivity.class).putExtra("orderId",orderid).putExtra("status",1).putExtra("index",index).putExtra("purchase_status",1));
                        finish();
                        //JY添加
//                        EventBus.getDefault().post(new PayCompleteEvent(PayWaysBean.PayWaysData.PAY_ID_Ali, 1));
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(OrderPaymentActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                            isLoading = false;
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(OrderPaymentActivity.this, "订阅失败，请重试，如有问题请联系在线客服",
                                    Toast.LENGTH_SHORT).show();
                            isLoading = false;
                        }
                    }
                    break;
                }
            }
        }


    };


    @Override
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.statusBarLightMode(this);
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        instance=this;
        index = getIntent().getIntExtra("index",0);
//       course=(Course) getIntent().getSerializableExtra("course");
        orderid = getIntent().getStringExtra("uuid");
        MyApp.orderId = orderid;
        MyApp.index = index;
        price = getIntent().getIntExtra("price",0);
        Tv_price.setText(Html.fromHtml("还需要支付<font color='#E46866'><big>￥"+price+"</big></font>, 请选择支付方式"));
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp("wx72ef58b1e2b5e1b6");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_payment;
    }

    @Override
    public void initToolBar() {

    }

    @OnClick({R.id.Bt_pay,R.id.Iv_back,R.id.Iv_wx_select})
    void onClick(View view) {
        switch (view.getId()){
//            case R.id.Iv_agree:
//                if(isAgree){
//                    isAgree = false;
//                    Iv_agree.setImageResource(R.mipmap.btn_gouxuan1);
//                }else {
//                    Iv_agree.setImageResource(R.mipmap.btn_gouxuan2);
//                    isAgree = true;
//                }
//                break;
            case R.id.Bt_pay:
                if(!isWXSlect){
                    ToastUtil.showShortToast(OrderPaymentActivity.this,"请选择支付方式");
                }
//                if(isAgree){
//                    if(isZFBSlect){
//                        getData(1);
//                    }

                    if(isWXSlect){
                        getData( 2);
                    }
//                }else {
//                    ToastUtil.showShortToast(OrderPaymentActivity.this,"请先同意这些协议");
//                }

                break;
            case R.id.Iv_back:
                finish();
                break;
            case R.id.Iv_wx_select:
                if(isWXSlect==false){
                    isWXSlect=true;
//                    isZFBSlect=false;
                }else {
                    isWXSlect=false;
                }
                if(isWXSlect){
                    Iv_wx_select.setImageResource(R.mipmap.btn_queding);
//                    Iv_zfb_select.setImageResource(R.mipmap.btn_quxiao);
                }else {
                    Iv_wx_select.setImageResource(R.mipmap.btn_quxiao);
                }
                break;
//            case R.id.Iv_zfb_select:
//                if(isZFBSlect==false){
//                    isZFBSlect=true;
//                    isWXSlect=false;
//                }else {
//                    isZFBSlect=false;
//                }
//                if(isZFBSlect){
//                    Iv_zfb_select.setImageResource(R.mipmap.btn_queding);
//                    Iv_wx_select.setImageResource(R.mipmap.btn_quxiao);
//                }else {
//                    Iv_zfb_select.setImageResource(R.mipmap.btn_quxiao);
//                }
//                break;
        }
    }

    private boolean isWXAppInstalledAndSupported(String appid) {
        api.registerApp(appid);
        boolean sIsWXAppInstalledAndSupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        return sIsWXAppInstalledAndSupported;
    }

//
//    public static void start(Context context, int orderId) {
//        start(context, orderId, "", false);
//    }
//
//
//    public static void start(Context context, int orderId, String lecturerId) {
//        start(context, orderId, lecturerId, true);
//    }

    public static void start(Context context,String orderId,int index) {
        Intent intent = new Intent();
        intent.setClass(context, FinishOrderActivity.class);
        intent.putExtra("orderId",orderId);
        intent.putExtra("status",1);
        intent.putExtra("index",index);
        intent.putExtra("purchase_status",1);
//        intent.putExtra(KEY_ORDER_ID, orderId);purchase_status
//        intent.putExtra(KEY_LECTURER_ID, lecturerId);
//        intent.putExtra(KEY_GO_LECTURER_HOME, goLecturerHome);
        context.startActivity(intent);
        OrderPaymentActivity.instance.finish();


    }
    private void getData(int status){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"getOrderDetailByOrderid.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("orderid", orderid);
        requestParams.addParameter("type", 0);
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
                                if(status ==1){
                                    getZFBData();
                                }else {
                                    getWXData();
                                }

                            }
                        });
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                startActivity(new Intent().setClass(OrderPaymentActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {

                        final String msg=jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(OrderPaymentActivity.this, msg);
                            }
                        });
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
                        ToastUtil.showShortToast(OrderPaymentActivity.this, "网络连接异常");
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

    /**
     * 支付宝支付
     * 处理支付宝下单结果
     */
    private void AliPayOrderResult(final String alipay_bizcontent) {
        if (!TextUtils.isEmpty(alipay_bizcontent)) {
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(OrderPaymentActivity.this);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(alipay_bizcontent, true);
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        } else {
            isLoading = false;
            Toast.makeText(this, "请求订单失败", Toast.LENGTH_SHORT).show();

        }
    }

    //支付宝支付
    private void getZFBData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"alipay.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("orderid", orderid);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        String alipay_bizcontent=json.getString("alipay_bizcontent");

                        AliPayOrderResult(alipay_bizcontent);
//                                AliPayOrderResult(alipay_bizcontent);
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                startActivity(new Intent().setClass(OrderPaymentActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {
                        final String msg=jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(OrderPaymentActivity.this, msg);
                            }
                        });
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
                        ToastUtil.showShortToast(OrderPaymentActivity.this, "网络连接异常");
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

    //微信支付
    private void getWXData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"wxpay.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("orderid", orderid);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        WXPayInfo wxPayInfo =new WXPayInfo();
                        wxPayInfo.sign = json.getString("sign");
                        wxPayInfo.appid = json.getString("appid");
                        wxPayInfo.noncestr = json.getString("noncestr");
                        wxPayInfo.packageValue = json.getString("package");
                        wxPayInfo.partnerid = json.getString("partnerid");
                        wxPayInfo.prepayid = json.getString("prepayid");
                        wxPayInfo.timestamp = json.getString("timestamp");

                        WxPayOrderResult(wxPayInfo);
//                                AliPayOrderResult(alipay_bizcontent);

                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(OrderPaymentActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {

                        final String msg=jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(OrderPaymentActivity.this, msg);
                            }
                        });
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
                        ToastUtil.showShortToast(OrderPaymentActivity.this, "网络连接异常");
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

    /**
     * 微信支付
     * 处理微信下单结果
     */
    private void WxPayOrderResult(WXPayInfo wxPayInfo) {
        if (wxPayInfo != null) {
            if (isWXAppInstalledAndSupported(wxPayInfo.appid)) {
                PayReq req = new PayReq();
                MyApp.payModel = null;
                //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                req.appId = wxPayInfo.appid;
                req.partnerId = wxPayInfo.partnerid;
                req.prepayId = wxPayInfo.prepayid;
                req.nonceStr = wxPayInfo.noncestr;
                req.timeStamp = wxPayInfo.timestamp;
                req.packageValue = wxPayInfo.packageValue;
                req.sign = wxPayInfo.sign;
                boolean isSuccess = api.sendReq(req);
                Log.d("OrderPaymentActivity", "isSuccess:" + isSuccess);
            } else {
                isLoading = false;
//                BuryingPointUtil.buryingPoint(BuyVIPActivity.this, "260017");
                Toast.makeText(this, "您未安装最新版本微信，不支持微信支付，请安装或升级微信版本", Toast.LENGTH_SHORT).show();
            }
        } else {
            isLoading = false;
//            BuryingPointUtil.buryingPoint(BuyVIPActivity.this, "260017");
            Toast.makeText(this, "请求订单失败", Toast.LENGTH_SHORT).show();
        }
    }

}
