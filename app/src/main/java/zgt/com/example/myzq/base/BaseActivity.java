package zgt.com.example.myzq.base;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.igexin.sdk.PushManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import zgt.com.example.myzq.MyApp;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.model.common.service.DemoIntentService;
import zgt.com.example.myzq.model.common.service.DemoPushService;
import zgt.com.example.myzq.utils.Log;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.StatusBarUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder bind;
    public static List<BaseActivity> activitylist = new ArrayList<>();

    // DemoPushService.class 自定义服务名称, 核心服务
    private Class userPushService = DemoPushService.class;
    private static final String TAG = "GetuiSdkDemo";
    private static final int REQUEST_PERMISSION = 0;
    // SDK服务是否启动.
    private boolean isServiceRunning = true;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.getClass().getSimpleName() + " onCreate");
        //设置布局内容
        setContentView(getLayoutId());
        activitylist.add(this);
        //初始化黄油刀控件绑定框架
        bind = ButterKnife.bind(this);
        //初始化控件
        initViews(savedInstanceState);
        //初始化ToolBar
        initToolBar();
        StatusBarUtil.statusBarLightMode(this);

        context = this;
        isServiceRunning = true;

//        MyApp.demoActivity = this;

        PackageManager pkgManager = getPackageManager();
        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission = pkgManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission = pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
            requestPermission();
        } else {
            PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
            isServiceRunning = true;
        }
        // 注册 intentService 后 PushDemoReceiver 无效, sdk 会使用 DemoIntentService 传递数据,
        // AndroidManifest 对应保留一个即可(如果注册 DemoIntentService, 可以去掉 PushDemoReceiver, 如果注册了
        // IntentService, 必须在 AndroidManifest 中声明)
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);

        // cpu 架构
        Log.d(TAG, "cpu arch = " + (Build.VERSION.SDK_INT < 21 ? Build.CPU_ABI : Build.SUPPORTED_ABIS[0]));

        // 检查 so 是否存在
        File file = new File(this.getApplicationInfo().nativeLibraryDir + File.separator + "libgetuiext2.so");
        Log.e(TAG, "libgetuiext2.so exist = " + file.exists());

    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
            } else {
                Log.e(TAG, "We highly recommend that you need to grant the special permissions before initializing the SDK, otherwise some "
                        + "functions will not work");
                PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    // 一键关闭所有Activity
    public static void finishAll() {
        // for//for each//it
        Iterator<BaseActivity> it = activitylist.iterator();
        while (it.hasNext()) {
            it.next().finish();
        }
    }




    /**
     * 设置布局layout
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化views
     *
     * @param savedInstanceState
     */
    public abstract void initViews(Bundle savedInstanceState);

    /**
     * 初始化toolbar
     */
    public abstract void initToolBar();

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(this.getClass().getSimpleName() + " onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(this.getClass().getSimpleName() + " onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!MyApp.isLive){
            //app 从后台唤醒，进入前台
            addOrdelMemberOnline(1);
            MyApp.isLive=true;
            Log.d(this.getClass().getSimpleName() + "我上线了");
        }
        Log.d(this.getClass().getSimpleName() + " onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(this.getClass().getSimpleName() + " onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            //app 进入后台
            addOrdelMemberOnline(2);
            //全局变量isActive = false 记录当前已经进入后台
            MyApp.isLive=false;
            Log.d(this.getClass().getSimpleName() + "我下线了");
        }
        Log.d(this.getClass().getSimpleName() + " onStop");
    }

    @Override
    protected void onDestroy() {
//        Log.d("GetuiSdkDemo", "onDestroy()");
//        MyApp.payloadData.delete(0, MyApp.payloadData.length());
        bind.unbind();
        activitylist.remove(this);
        super.onDestroy();
        Log.d(this.getClass().getSimpleName() + " onDestroy");
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    private void addOrdelMemberOnline(int type){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"addOrdelMemberOnline.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("type", type);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    final String msg=jsonObject.getString("message");
//                    ToastUtil.showShortToast(BaseActivity.this, msg);
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(BaseActivity.this, LoginActivity.class));
                                finish();
//                                if(!TextUtils.isEmpty(SPUtil.getToken())) {
//                                    new AlertDialog.Builder(BaseActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    startActivity(new Intent().setClass(BaseActivity.this, LoginActivity.class));
//                                                    finish();
//                                                    dialog.dismiss();
//                                                }
//                                            }).create().show();
//                                }else {
//                                    startActivity(new Intent().setClass(BaseActivity.this, LoginActivity.class));
//                                    finish();
//                                }
                            }
                        });

                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(BaseActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(BaseActivity.this, getString(R.string.login_parse_exc));
                        }
                    });

                } finally {
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ToastUtil.showShortToast(BaseActivity.this, "网络连接异常");
//                    }
//                });
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
