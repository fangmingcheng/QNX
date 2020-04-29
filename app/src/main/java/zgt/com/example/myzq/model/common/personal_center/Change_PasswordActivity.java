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

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;


public class Change_PasswordActivity extends BaseActivity {

    @BindView(R.id.Et_password)
    EditText Et_password;
    @BindView(R.id.Et_password_new)
    EditText Et_password_new;
    @BindView(R.id.Et_password_again)
    EditText Et_password_again;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_password2;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        StatusBarUtil.statusBarLightMode(this);
    }

    @OnClick({R.id.Iv_back,R.id.Tv_save})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
                finish();
                break;
            case R.id.Tv_save:
                if(TextUtils.isEmpty(Et_password.getText())){
                    ToastUtil.showShortToast(Change_PasswordActivity.this,"请输入原密码");
                }else {
                   if(TextUtils.isEmpty(Et_password_new.getText())){
                       ToastUtil.showShortToast(Change_PasswordActivity.this,"请输入新密码");
                   }else {
                       if(Et_password_new.getText().toString().equals(Et_password_again.getText().toString())){
                           saveName(Et_password.getText().toString(),Et_password_new.getText().toString());
                       }else {
                           ToastUtil.showShortToast(Change_PasswordActivity.this,"请确认新密码是否一致");
                       }
                   }
                }
                break;
        }
    }

    private void saveName(String oldpwd,String newpwd){
        final ProgressDialog loadDialog = new ProgressDialog(this);
        loadDialog.setMessage("正在保存");
        loadDialog.setCancelable(false);
        loadDialog.show();

        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"modifyPwd.do");
        requestParams.setConnectTimeout(60 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("oldpwd", oldpwd);
        requestParams.addParameter("newpwd", newpwd);

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
                    } else if(a==-1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(Change_PasswordActivity.this,LoginActivity.class));
                                finish();
                            }
                        });
                    }
                    ToastUtil.showShortToast(Change_PasswordActivity.this, jsonObject.getString("message"));
                } catch (JSONException e) {
                    ToastUtil.showShortToast(Change_PasswordActivity.this, "解析异常");
                } finally {
                    loadDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss();
                ToastUtil.showShortToast(Change_PasswordActivity.this, "网络连接异常");
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
