package zgt.com.example.myzq.model.common.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.igexin.sdk.PushManager;

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
import zgt.com.example.myzq.model.common.MainActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.Et_name)
    EditText Et_name;
    @BindView(R.id.Et_phone)
    EditText Et_phone;
    @BindView(R.id.Et_password)
    EditText Et_password;
    @BindView(R.id.Et_code)
    EditText Et_code;
    @BindView(R.id.Et_invite)
    EditText Et_invite;
    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

    }

    @Override
    public void initToolBar() {

    }
    @OnClick({R.id.Bt_login,R.id.button})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Bt_login:
                if(TextUtils.isEmpty(Et_name.getText())){
                    ToastUtil.showShortToast(RegisterActivity.this,"请输入用户号");
                }else {
                    if(TextUtils.isEmpty(Et_phone.getText())){
                        ToastUtil.showShortToast(RegisterActivity.this,"请输入手机号");
                    }else {
                        if(Et_phone.getText().toString().trim().length()==11) { //^1[3|4|5|8][0-9]\d{4,8}$
                            if (isMobileNum(Et_phone.getText().toString().trim())) {
                                if(TextUtils.isEmpty(Et_code.getText())){
                                    ToastUtil.showShortToast(RegisterActivity.this,"请输入验证码");
                                }else {
                                    if(TextUtils.isEmpty(Et_password.getText())){
                                        ToastUtil.showShortToast(RegisterActivity.this,"请输入密码");
                                    }else {
                                        register(Et_name.getText().toString().trim(),Et_phone.getText().toString().trim(),Et_code.getText().toString().trim(),Et_password.getText().toString());
                                    }
                                }
                            } else {
                                ToastUtil.showShortToast(RegisterActivity.this, "请输入正确的手机号");
                            }
                        }else {
                            ToastUtil.showShortToast(RegisterActivity.this, "请输入正确的手机号");
                        }
                    }

                }
                break;
            case R.id.button:
                if(Et_phone.getText().toString().trim().length()==11) { //^1[3|4|5|8][0-9]\d{4,8}$
                    if (isMobileNum(Et_phone.getText().toString().trim())) {
                        sendMessage(Et_phone.getText().toString().trim());
                    } else {
                        ToastUtil.showShortToast(RegisterActivity.this, "请输入正确的手机号");
                    }
                }else {
                    ToastUtil.showShortToast(RegisterActivity.this, "请输入正确的手机号");
                }
                break;
        }
    }

    private boolean isMobileNum(String mobiles) {
        Pattern p = Pattern.compile("^((1[3|4|5|6|7|8|9][0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    private void sendMessage(String phone){
        final ProgressDialog loadDialog = new ProgressDialog(this);
        loadDialog.setMessage("正在获取");
        loadDialog.setCancelable(false);
        loadDialog.show();

        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"verificationCode.do");
        requestParams.setConnectTimeout(60 * 1000);
        requestParams.addParameter("token", "");
        requestParams.addParameter("phone", phone);
        requestParams.addParameter("flag", 3);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                NoteUtil.logConnect(new RequestParams("登录onSuccess"), LoginActivity.this, "result:" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if (a==1) {
//                        finish();
                        ToastUtil.showShortToast(RegisterActivity.this, jsonObject.getString("message"));
                    } else {
                        ToastUtil.showShortToast(RegisterActivity.this, jsonObject.getString("message"));
                    }
                    ToastUtil.showShortToast(RegisterActivity.this, jsonObject.getString("message"));
                } catch (JSONException e) {
                    ToastUtil.showShortToast(RegisterActivity.this, "解析异常");
                } finally {
                    loadDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss();
                ToastUtil.showShortToast(RegisterActivity.this, "网络连接异常");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void register(String name,String phone,String code,String password){
        String cid = PushManager.getInstance().getClientid(this);
        final ProgressDialog loadDialog = new ProgressDialog(this);
        loadDialog.setMessage("正在获取");
        loadDialog.setCancelable(false);
        loadDialog.show();

        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"doRegisterMember.do");
        requestParams.setConnectTimeout(60 * 1000);
        requestParams.addParameter("username", name);
        requestParams.addParameter("mobile", phone);
        requestParams.addParameter("code", code);
        requestParams.addParameter("password", password);
        requestParams.addParameter("clientid", cid);
        requestParams.addParameter("invitationCode", Et_invite.getText().toString().trim());
        requestParams.addParameter("devicetype", "android");

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                NoteUtil.logConnect(new RequestParams("登录onSuccess"), LoginActivity.this, "result:" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if (a==1) {
                        saveLoginInfo(json);
                        ToastUtil.showShortToast(RegisterActivity.this, jsonObject.getString("message"));
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class).putExtra("status",0));

                    } else {
                        ToastUtil.showShortToast(RegisterActivity.this, jsonObject.getString("message"));
                    }
//                    ToastUtil.showShortToast(RegisterActivity.this, jsonObject.getString("message"));
                } catch (JSONException e) {
                    ToastUtil.showShortToast(RegisterActivity.this, "解析异常");
                } finally {
                    loadDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss();
                ToastUtil.showShortToast(RegisterActivity.this, "网络连接异常");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void saveLoginInfo(JSONObject jsonObject) throws JSONException {
        SPUtil.getLoginSharedPreferences().edit().putString("token", jsonObject.getString("token"))
                .putString("memberid", jsonObject.getString("memberid"))
                .putString("username", jsonObject.getString("username"))
                .putString("nickname", jsonObject.getString("nickname"))
                .putString("getuistatus", jsonObject.getInt("getuistatus")+"")
//                .putString("modulecode", jsonObject.getString("modulecode"))
//                .putString("modulename", jsonObject.getString("modulename"))
//                .putString("typename",jsonObject.getString("typename"))
//                .putInt("type",jsonObject.getInt("type"))
//                .putString("startdate",jsonObject.getString("startdate"))
////                .putString("teacherid",jsonObject.getString("teacherid"))
//                .putString("enddate",jsonObject.getString("enddate"))
//                .putInt("membertype",jsonObject.getInt("membertype"))
//                .putInt("tsort",jsonObject.getInt("tsort"))
                .commit();
    }

}
