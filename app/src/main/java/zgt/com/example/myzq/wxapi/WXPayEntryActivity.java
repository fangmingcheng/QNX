package zgt.com.example.myzq.wxapi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import zgt.com.example.myzq.MyApp;
import zgt.com.example.myzq.bean.pay.PayCompleteEvent;
import zgt.com.example.myzq.model.common.order.OrderPaymentActivity;
import zgt.com.example.myzq.utils.ToastUtil;


public class WXPayEntryActivity  extends AppCompatActivity implements IWXAPIEventHandler {

    private IWXAPI api;
    //微信支付id
    public static final int PAY_ID_WX = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("pxl===", "loading WXPayEntryActivity");
        api = WXAPIFactory.createWXAPI(this, "wx72ef58b1e2b5e1b6");
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            PayCompleteEvent payCompleteEvent = new PayCompleteEvent(PAY_ID_WX);
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    //H5
                    if (MyApp.payModel != null && MyApp.payModel.equals("H5Pay")) {
                        Uri uri = Uri.parse(MyApp.success_url);
                        String orderId = uri.getQueryParameter("key");
                        String lecturerId = uri.getQueryParameter("lecturer_id");
                        if (!TextUtils.isEmpty(orderId) && !TextUtils.isEmpty(lecturerId)) {
//                            OrderPaymentActivity.start(this, Integer.valueOf(orderId), lecturerId);
//                            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                        }
                    }
                    payCompleteEvent.isSuccess = true;
                    //支付成功
                    ToastUtil.showShortToast(this, "支付成功");
                    OrderPaymentActivity.start(this,MyApp.orderId,MyApp.index);


                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    payCompleteEvent.isSuccess = false;
                    payCompleteEvent.msg = "取消支付";
                    //取消支付
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    //认证失败
                    payCompleteEvent.isSuccess = false;
                    payCompleteEvent.msg = "认证失败";
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    //一般错误
                    payCompleteEvent.isSuccess = false;
                    payCompleteEvent.msg = "一般错误";
                    break;
                default:
                    break;
            }
//            EventBus.getDefault().post(payCompleteEvent);
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}