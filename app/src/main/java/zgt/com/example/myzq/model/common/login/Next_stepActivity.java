package zgt.com.example.myzq.model.common.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class Next_stepActivity extends BaseActivity {

    @BindView(R.id.Et_phone)
    EditText Et_phone;
    @BindView(R.id.Et_code)
    EditText Et_code;

    @Override
    public int getLayoutId() {
        return R.layout.activity_next_step;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        //得到当前界面的装饰视图
        if(Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            //设置让应用主题内容占据状态栏和导航栏
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //设置状态栏和导航栏颜色为透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    @OnClick({R.id.Bt_login,R.id.button})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Bt_login:
                if(TextUtils.isEmpty(Et_phone.getText())){
                    ToastUtil.showShortToast(Next_stepActivity.this,"请输入手机号");
                }else {
                    if(TextUtils.isEmpty(Et_code.getText())){
                        ToastUtil.showShortToast(Next_stepActivity.this,"请输入验证码");
                    }else {
                       startActivity(new Intent().setClass(Next_stepActivity.this,ChangePasswordActivity.class).putExtra("code",Et_code.getText().toString()).putExtra("phone",Et_phone.getText().toString()));
                    }
                }
                break;
            case R.id.button:
                if(TextUtils.isEmpty(Et_phone.getText())){
                    ToastUtil.showShortToast(Next_stepActivity.this,"请输入手机号");
                }else {
                    if(Et_phone.getText().toString().length()==11){
                        sendMessage(Et_phone.getText().toString());
                    }else {
                        ToastUtil.showShortToast(Next_stepActivity.this,"请输入正确的手机号");
                    }

                }
                break;
        }
    }

    private void sendMessage(String phone){
        final ProgressDialog loadDialog = new ProgressDialog(this);
        loadDialog.setMessage("正在获取");
        loadDialog.setCancelable(false);
        loadDialog.show();

        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"verificationCode.do");
        requestParams.setConnectTimeout(60 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("phone", phone);
        requestParams.addParameter("flag", 2);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                NoteUtil.logConnect(new RequestParams("登录onSuccess"), LoginActivity.this, "result:" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if (a==1) {

                    } else {
                    }
                    ToastUtil.showShortToast(Next_stepActivity.this, jsonObject.getString("message"));
                } catch (JSONException e) {
                    ToastUtil.showShortToast(Next_stepActivity.this, "解析异常");
                } finally {
                    loadDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss();
                ToastUtil.showShortToast(Next_stepActivity.this, "网络连接异常");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void login(final String phone, String passWord){
        final ProgressDialog loadDialog = new ProgressDialog(this);
        loadDialog.setMessage("正在验证");
        loadDialog.setCancelable(false);
        loadDialog.show();

        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"pswCheckMobile.do");
        requestParams.setConnectTimeout(60 * 1000);
        requestParams.addParameter("mobile", phone);
        requestParams.addParameter("popoutCode", passWord);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                NoteUtil.logConnect(new RequestParams("登录onSuccess"), LoginActivity.this, "result:" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if (a==1) {
                        startActivity(new Intent(Next_stepActivity.this, ChangePasswordActivity.class).putExtra("phone",phone));
                        finish();
                    } else {
                    }
                    ToastUtil.showShortToast(Next_stepActivity.this, jsonObject.getString("message"));
                } catch (JSONException e) {
                    ToastUtil.showShortToast(Next_stepActivity.this, "解析异常");
                } finally {
                    loadDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss();
                ToastUtil.showShortToast(Next_stepActivity.this, "网络连接异常");
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
