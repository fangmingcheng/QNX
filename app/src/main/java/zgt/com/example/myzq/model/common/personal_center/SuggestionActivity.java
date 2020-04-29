package zgt.com.example.myzq.model.common.personal_center;

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

public class SuggestionActivity extends BaseActivity {

    @BindView(R.id.Et_content)
    EditText Et_content;
    @BindView(R.id.Et_contacts)
    EditText Et_contacts;

    @Override
    public int getLayoutId() {
        return R.layout.activity_suggestion;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        StatusBarUtil.statusBarLightMode(this);
    }

    @OnClick({R.id.Bt_commit,R.id.Iv_back})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Bt_commit:
              if(TextUtils.isEmpty(Et_content.getText().toString().trim())){
                  ToastUtil.showShortToast(SuggestionActivity.this,"请填写投诉建议内容");
              }else {
                  if(TextUtils.isEmpty(Et_contacts.getText().toString().trim())){
                      ToastUtil.showShortToast(SuggestionActivity.this,"请填写联系方式");
                  }else {
                      getData(Et_content.getText().toString().trim(),Et_contacts.getText().toString().trim());
                  }
              }
//                cursorAnim(1);
                break;
            case R.id.Iv_back:
                finish();
                break;

        }
    }

    private void getData(String content,String contacts){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"complain.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("content", content);
        requestParams.addParameter("contactinformation", contacts);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    String msg=jsonObject.getString("message");
                    if (a==1) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(SuggestionActivity.this, msg);
                                Et_content.setText("");
                                Et_contacts.setText("");
                            }
                        });
                    } else if(a==-1){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(SuggestionActivity.this, LoginActivity.class));
                                finish();
//                                if(!TextUtils.isEmpty(SPUtil.getToken())) {
//                                    new AlertDialog.Builder(CoursewareDetailActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    startActivity(new Intent().setClass(CoursewareDetailActivity.this, LoginActivity.class));
//                                                    finish();
//                                                    dialog.dismiss();
//                                                }
//                                            }).create().show();
//                                }else {
//                                    startActivity(new Intent().setClass(CoursewareDetailActivity.this, LoginActivity.class));
//                                    finish();
//                                }
                            }
                        });

                    }else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(SuggestionActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(SuggestionActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(SuggestionActivity.this, "网络连接异常");
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
}
