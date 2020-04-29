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

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.Et_password_new)
    EditText Et_password_new;
    @BindView(R.id.Et_password)
    EditText Et_password;

    private String phone="",code="";

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_password;
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
        phone=getIntent().getStringExtra("phone");
        code=getIntent().getStringExtra("code");
    }

    @OnClick({R.id.Bt_login})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Bt_login:
                if(TextUtils.isEmpty(Et_password.getText())){
                    ToastUtil.showShortToast(ChangePasswordActivity.this,"请输入密码");
                }else {
                    if(TextUtils.isEmpty(Et_password_new.getText())){
                        ToastUtil.showShortToast(ChangePasswordActivity.this,"请输入新密码");
                    }else {
                        if(Et_password.getText().toString().trim().equals(Et_password_new.getText().toString().trim())) {
                            login( Et_password_new.getText().toString());
                        }else {
                            ToastUtil.showShortToast(ChangePasswordActivity.this,"两次输入密码不一致");
                        }
                    }
                }
                break;
        }
    }

    private void login(String passWord){
        final ProgressDialog loadDialog = new ProgressDialog(this);
        loadDialog.setMessage("正在修改");
        loadDialog.setCancelable(false);
        loadDialog.show();

        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"setupNewPass.do");
        requestParams.setConnectTimeout(60 * 1000);
//        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("mobile", phone);
        requestParams.addParameter("popoutCode", code);
        requestParams.addParameter("newPassword", passWord);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                NoteUtil.logConnect(new RequestParams("登录onSuccess"), LoginActivity.this, "result:" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if (a==1) {
                        ToastUtil.showShortToast(ChangePasswordActivity.this,"设置成功");
                        startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                        finish();
                    } else {
                    }

                } catch (JSONException e) {
                    ToastUtil.showShortToast(ChangePasswordActivity.this, "解析异常");
                } finally {
                    loadDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss();
                ToastUtil.showShortToast(ChangePasswordActivity.this, "网络连接异常");
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
