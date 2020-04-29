package zgt.com.example.myzq.model.common.personal_center;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class BindingPhoneActivity extends BaseActivity {

    @BindView(R.id.Et_name)
    EditText Et_name;
    @BindView(R.id.Et_code)
    EditText Et_code;

    @BindView(R.id.Et_password)
    EditText Et_password;

    @Override
    public int getLayoutId() {
        return R.layout.activity_binding_phone;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        StatusBarUtil.statusBarLightMode(this);

    }

    private boolean isMobileNum(String mobiles) {
        Pattern p = Pattern.compile("^((1[3|4|5|6|7|8|9][0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    @OnClick({R.id.Iv_back,R.id.Tv_send,R.id.Tv_save})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
                finish();
                break;
            case R.id.Tv_send:
                if(TextUtils.isEmpty(Et_name.getText())){
                    ToastUtil.showShortToast(BindingPhoneActivity.this,"请输入手机号");
                }else {
                    if(Et_name.getText().toString().trim().length()==11) { //^1[3|4|5|8][0-9]\d{4,8}$
                        if(isMobileNum(Et_name.getText().toString().trim())){
                            sendMessage(Et_name.getText().toString());
                        }else {
                            ToastUtil.showShortToast(BindingPhoneActivity.this,"请输入正确的手机号");
                        }
                    }else {
                        ToastUtil.showShortToast(BindingPhoneActivity.this,"请输入正确的手机号");
                    }
                }
                break;
            case R.id.Tv_save:
                if(TextUtils.isEmpty(Et_name.getText())){
                    ToastUtil.showShortToast(BindingPhoneActivity.this,"请输入手机号");
                }else {
                    if(Et_name.getText().toString().trim().length()==11) {
                        if (TextUtils.isEmpty(Et_code.getText())) {
                            ToastUtil.showShortToast(BindingPhoneActivity.this, "请输入验证码");
                        } else {
                            if (TextUtils.isEmpty(Et_password.getText())) {
                                ToastUtil.showShortToast(BindingPhoneActivity.this, "请输入密码");
                            } else {
                                saveName(Et_name.getText().toString(), Et_code.getText().toString(), Et_password.getText().toString());
                            }

                        }
                    }else {
                        ToastUtil.showShortToast(BindingPhoneActivity.this,"请输入正确的手机号");
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
        requestParams.addParameter("flag", 0);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                NoteUtil.logConnect(new RequestParams("登录onSuccess"), LoginActivity.this, "result:" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if (a==1) {

                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(BindingPhoneActivity.this,LoginActivity.class));
                                finish();
                            }
                        });
                    }
                    ToastUtil.showShortToast(BindingPhoneActivity.this, jsonObject.getString("message"));
                } catch (JSONException e) {
                    ToastUtil.showShortToast(BindingPhoneActivity.this, "解析异常");
                } finally {
                    loadDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss();
                ToastUtil.showShortToast(BindingPhoneActivity.this, "网络连接异常");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void saveName(String phone,String msg,String psw){
        final ProgressDialog loadDialog = new ProgressDialog(this);
        loadDialog.setMessage("正在保存");
        loadDialog.setCancelable(false);
        loadDialog.show();

        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"updatePhone.do");
        requestParams.setConnectTimeout(60 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("phone", phone);
        requestParams.addParameter("popoutCode", msg);
        requestParams.addParameter("password", psw);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                NoteUtil.logConnect(new RequestParams("登录onSuccess"), LoginActivity.this, "result:" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if (a==1) {
                        finish();
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(BindingPhoneActivity.this,LoginActivity.class));
                                finish();
                            }
                        });
                    }else {
                    }
                    ToastUtil.showShortToast(BindingPhoneActivity.this, jsonObject.getString("message"));
                } catch (JSONException e) {
                    ToastUtil.showShortToast(BindingPhoneActivity.this, "解析异常");
                } finally {
                    loadDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss();
                ToastUtil.showShortToast(BindingPhoneActivity.this, "网络连接异常");
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
