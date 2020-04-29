package zgt.com.example.myzq.wxapi;


import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;


public class WXEntryActivity extends WXCallbackActivity {

    private final String TAG = "WXEntryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.mWXHandler = (WXHandler)api.getSSOHandler(PlatformType.WEIXIN);
//        this.mWXHandler.onCreate(this.getApplicationContext(), PlatformConfig.getPlatformConfig(PlatformType.WEIXIN));
//        this.mWXHandler.getWXApi().handleIntent(this.getIntent(), this);

    }

    @Override
    public void onResp(BaseResp resp) {
        super.onResp(resp);
        Log.d(TAG, "respType===" + resp.getType());
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {//支付成功
            Log.d(TAG, "onPayFinish,errCode=" + resp.errCode);
        }
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case 0:
                    Toast.makeText(this, "支付成功！", Toast.LENGTH_LONG).show();
//                    Toast.makeText(this, "回调成功！", Toast.LENGTH_LONG).show();
                    break;
                case -2:
                    Toast.makeText(this,"支付取消！",Toast.LENGTH_LONG).show();
                    break;
                case -1:
                    Toast.makeText(this,"支付失败！",Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(this,"支付出错！",Toast.LENGTH_LONG).show();
                    break;
            }
        } else {
            super.onResp(resp);//一定要加super，实现我们的方法，否则不能回调
        }

                Toast.makeText(this, "收到回调  code: "+resp.errCode+"  msg:"+resp.errStr, Toast.LENGTH_SHORT).show();

    }
}
