package zgt.com.example.myzq;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.xutils.x;

import zgt.com.example.myzq.bean.HttpResult;
import zgt.com.example.myzq.model.common.receive.ShowNotificationReceiver;
import zgt.com.example.myzq.utils.Log;


/**
 * Created by ThinkPad on 2019/5/7.
 */

public class MyApp extends Application {

    private static MyApp mInstance;
    public static boolean isLive=false;
    private static DemoHandler handler;
    public static int status;
    private NotificationManager mNManager;
    Bitmap LargeBitmap = null;

    public static String fileUrl;

    public static HttpResult httpResult=null;

    private int NOTIFICATION_ID=0;

    //支付模式payModel
    public static String payModel = "";

    //
    public static String success_url = "";

    public static String orderId = "";

    public static  int index = 0;

    private WindowManager.LayoutParams wmParams=new WindowManager.LayoutParams();
    public WindowManager.LayoutParams getMywmParams(){
        return wmParams;
    }

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();

    @Override
    public void onCreate() {
        super.onCreate();
//        CrashListener.getInstance().init();
        mInstance = this;
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        LitePal.initialize(this);
        if (handler == null) {
            handler = new DemoHandler();
        }
    }



    public static MyApp getInstance() {
        return mInstance;
    }
    public static void sendMessage(Message msg) {
        handler.sendMessage(msg);
    }

    public  class DemoHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    try {
                        String uuid ="";
                        String url ="";
                        String fileid ="";
                        String contentid ="";
                        JSONObject json=new JSONObject((String)msg.obj);
                        status=json.getInt("msgtype");
                        String title=json.getString("title");
                        String body=json.getString("body");
                        JSONObject jsonObject=json.getJSONObject("parmstr");
                        if(status == 2){
                             uuid=jsonObject.getString("uuid");
                             url=jsonObject.getString("url");
                        }else if(status == 1){
                            fileid=jsonObject.getString("fileid");
                            contentid=jsonObject.getString("contentid");
                        }
                        showNotification(mInstance.getApplicationContext(),title,body,status,uuid,url,fileid,contentid);
//            if(status==1){
//                Intent intent = new Intent(context, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    break;
            }
        }
    }

    private  void showNotification(Context context, String title, String content, int start_status,String uuid,String url,String fileid ,String contentid){
        //获得通知消息管理类对象
        mNManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//获取大图标
//        LargeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img_circle_logo);
//        Intent intent = new Intent(context,MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Intent broadcastIntent = new Intent(context, ShowNotificationReceiver.class);
        broadcastIntent.putExtra("status",start_status+"");
        broadcastIntent.putExtra("uuid",uuid);
        broadcastIntent.putExtra("url",url);
        broadcastIntent.putExtra("fileid",fileid);
        broadcastIntent.putExtra("contentid",contentid);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //设置图片,通知标题,发送时间,提示方式等属性

        //高版本需要渠道
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            //只在Android O之上需要渠道，这里的第一个参数要和下面的channelId一样
            String channelId = "default";
            String channelName = "默认通知";
            mNManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
            Log.e("MyApp","我执行了");
        }

        Notification notification = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .build();
        Log.e("MyApp","通知执行了");
        mNManager.notify(NOTIFICATION_ID++,notification);
    }

}
