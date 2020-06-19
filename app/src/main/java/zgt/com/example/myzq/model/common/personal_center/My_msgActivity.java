package zgt.com.example.myzq.model.common.personal_center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.MessageList;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.StatusBarUtil;
import zgt.com.example.myzq.utils.ToastUtil;


public class My_msgActivity extends BaseActivity {

    @BindView(R.id.switch2)
    Switch switch2;//

    @BindView(R.id.switch_system)
    Switch switch_system;//

    private int status;

    private MessageList messageList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_msg;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.statusBarLightMode(this);
        status = getIntent().getIntExtra("status",0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }).start();

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switch_system.isChecked()){
                    if(isChecked){
                        setStatus(0);
                    }else {
                        setStatus(2);
                    }
                }else {
                    if(isChecked){
                        setStatus(1);
                    }else {
                        setStatus(3);
                    }
                }

//                if(!buttonView.isPressed()){ // 每次 setChecked 时会触发onCheckedChanged 监听回调，而有时我们在设置setChecked后不想去自动触发 onCheckedChanged 里的具体操作, 即想屏蔽掉onCheckedChanged;加上此判断
//
//                    back;
//
//                }

            }

        });

        switch_system.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(switch2.isChecked()){
                    if(isChecked){
                        setStatus(0);
                    }else {
                        setStatus(1);
                    }
                }else {
                    if(isChecked){
                        setStatus(2);
                    }else {
                        setStatus(3);
                    }
                }
            }

        });


        if(status==0){
            switch2.setChecked(true);
            switch_system.setChecked(true);
        }else if(status==1){
            switch2.setChecked(true);
            switch_system.setChecked(false);
        }
        else if(status==2){
            switch2.setChecked(false);
            switch_system.setChecked(true);
        }
        else if(status==3){
            switch2.setChecked(false);
            switch_system.setChecked(false);
        }
    }
    private void setData(){

    }

    @OnClick({R.id.Rl_personal,R.id.Iv_back})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
                finish();
                break;
            case R.id.Rl_personal://消息通知
                startActivity(new Intent().setClass(My_msgActivity.this, MessageActivity.class));
                break;

        }
    }

    private void setStatus(int status){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"updateGetuistatus.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("getuistatus", status);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {


                    } else if(a==-1){

                      runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//
                                startActivity(new Intent().setClass(My_msgActivity.this,LoginActivity.class));
                              finish();
                            }
                        });
                    }else {

                        final String msg=jsonObject.getString("message");
                       runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(My_msgActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {

                  runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(My_msgActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(My_msgActivity.this, "网络连接异常");
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

    private void getData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"sysMsgList.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("page", 1);
        requestParams.addParameter("rows", 20);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        JSONArray jsonArray=json.getJSONArray("items");
                        for (int i=0;i<jsonArray.length();i++){
                            messageList=new MessageList();
                            messageList.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            messageList.setUrl(jsonArray.getJSONObject(i).getString("url"));
                            messageList.setMsgtitle(jsonArray.getJSONObject(i).getString("msgtitle"));
                            messageList.setCreatetime(jsonArray.getJSONObject(i).getString("createtime"));
                            messageList.setSummary(jsonArray.getJSONObject(i).getString("summary"));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setData();
                            }
                        });
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(My_msgActivity.this,LoginActivity.class));
                                finish();
//                                new AlertDialog.Builder(My_msgActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                startActivity(new Intent().setClass(My_msgActivity.this,LoginActivity.class));
//                                                finish();
//                                                dialog.dismiss();
//                                            }
//                                        }).create().show();
                            }
                        });
                    }else {
                        final String msg=jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(My_msgActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(My_msgActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(My_msgActivity.this, "网络连接异常");
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
