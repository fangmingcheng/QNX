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

public class ChangeNameActivity extends BaseActivity {

    @BindView(R.id.Et_name)
    EditText Et_name;

    String name;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_name;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        StatusBarUtil.statusBarLightMode(this);
        name=getIntent().getStringExtra("name");
        Et_name.setText(name);
    }

    @Override
    public void initToolBar() {

    }

    @OnClick({R.id.Iv_back,R.id.Iv_cancel,R.id.Bt_save})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
               finish();
                break;
            case R.id.Iv_cancel:
                Et_name.setText("");
                break;
            case R.id.Bt_save:
                if(TextUtils.isEmpty(Et_name.getText())){
                    ToastUtil.showShortToast(ChangeNameActivity.this,"请输入昵称");
                }else {
                    if(Et_name.getText().toString().trim().matches("^[\\u4e00-\\u9fa5_a-zA-Z0-9]+$")){
                        if(Et_name.getText().toString().trim().length()>=2&&Et_name.getText().toString().trim().length()<=12) {
                            saveName(Et_name.getText().toString().trim());
                        }else {
                            ToastUtil.showShortToast(ChangeNameActivity.this,"昵称长度要求在2-12位之内");
                        }
                    }else {
                        ToastUtil.showShortToast(ChangeNameActivity.this,"昵称输入不合法");
                    }

                }
                break;
        }
    }

    private void saveName(String name){
        final ProgressDialog loadDialog = new ProgressDialog(this);
        loadDialog.setMessage("正在保存");
        loadDialog.setCancelable(false);
        loadDialog.show();

        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"updateNickName.do");
        requestParams.setConnectTimeout(60 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("nickname", name);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                NoteUtil.logConnect(new RequestParams("登录onSuccess"), LoginActivity.this, "result:" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    ToastUtil.showShortToast(ChangeNameActivity.this, jsonObject.getString("message"));
                    if (a==1) {
                        finish();
                    }else if(a==-1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(ChangeNameActivity.this,LoginActivity.class));
                                finish();
                            }
                        });
                    }else {

                    }
                } catch (JSONException e) {
                    ToastUtil.showShortToast(ChangeNameActivity.this, "解析异常");
                } finally {
                    loadDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss();
                ToastUtil.showShortToast(ChangeNameActivity.this, "网络连接异常");
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
