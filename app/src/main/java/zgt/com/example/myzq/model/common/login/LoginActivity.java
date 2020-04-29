package zgt.com.example.myzq.model.common.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.MyApp;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.HttpResult;
import zgt.com.example.myzq.model.common.MainActivity;
import zgt.com.example.myzq.utils.Log;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.Et_name)
    EditText Et_name;
    @BindView(R.id.Et_password)
    EditText Et_password;


    private HttpResult httpResult;


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        httpResult=(HttpResult) getIntent().getSerializableExtra("result");
        MyApp.httpResult=httpResult;
        Log.e("LoginActivity",""+httpResult);
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

    @Override
    public void initToolBar() {

    }

    @OnClick({R.id.Bt_login,R.id.Tv_password,R.id.Tv_register})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Bt_login:
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                if(TextUtils.isEmpty(Et_name.getText())){
                    ToastUtil.showShortToast(LoginActivity.this,"请输入用户名");
                }else {
                    if(TextUtils.isEmpty(Et_password.getText())){
                        ToastUtil.showShortToast(LoginActivity.this,"请输入密码");
                    }else {
                        login(Et_name.getText().toString(),Et_password.getText().toString());
                    }
                }
                break;
            case R.id.Tv_password:
                startActivity(new Intent().setClass(this,Next_stepActivity.class));
                break;
            case R.id.Tv_register:
                startActivity(new Intent().setClass(this,RegisterActivity.class));
                break;
        }
    }


    private void login(String useName,String passWord){
        String cid = PushManager.getInstance().getClientid(this);
        Log.d("TAG", getResources().getString(R.string.show_cid) + cid);
        final ProgressDialog loadDialog = new ProgressDialog(this);
        loadDialog.setMessage("正在登陆");
        loadDialog.setCancelable(false);
        loadDialog.show();

        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"memberLogin.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("username", useName);
        requestParams.addParameter("password", passWord);
        requestParams.addParameter("clientid", cid);
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
                        if(result==null) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("status",0));
                        }else {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("result",httpResult).putExtra("status",0));
                        }
                        finish();
                    } else {
                    }
                    ToastUtil.showShortToast(LoginActivity.this, jsonObject.getString("message"));
                } catch (JSONException e) {
                    ToastUtil.showShortToast(LoginActivity.this, "解析异常");
                } finally {
                    loadDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss();
                ToastUtil.showShortToast(LoginActivity.this, "网络连接异常");
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
//                .putString("email", jsonObject.getString("email"))
                .putString("getuistatus", jsonObject.getInt("getuistatus")+"")
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
