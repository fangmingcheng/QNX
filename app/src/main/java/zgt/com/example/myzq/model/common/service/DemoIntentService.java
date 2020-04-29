package zgt.com.example.myzq.model.common.service;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.BindAliasCmdMessage;
import com.igexin.sdk.message.FeedbackCmdMessage;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.igexin.sdk.message.SetTagCmdMessage;
import com.igexin.sdk.message.UnBindAliasCmdMessage;

import zgt.com.example.myzq.MyApp;
import zgt.com.example.myzq.R;


/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class DemoIntentService extends GTIntentService {

    private static final String TAG = "GetuiSdkDemo";

    /**
     * 为了观察透传数据变化.
     */
    private static int cnt;

    private NotificationManager mNManager;
    Bitmap LargeBitmap = null;

    public DemoIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        Log.d(TAG, "onReceiveServicePid -> " + pid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();

        // 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
        Log.d(TAG, "call sendFeedbackMessage = " + (result ? "success" : "failed"));

        Log.d(TAG, "onReceiveMessageData -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nmessageid = " + messageid + "\npkg = " + pkg
                + "\ncid = " + cid);

        if (payload == null) {
            Log.e(TAG, "receiver payload = null");
        } else {
            String data = new String(payload);
            Log.d(TAG, "receiver payload = " + data);

            // 测试消息为了观察数据变化
            if (data.equals(getResources().getString(R.string.push_transmission_data))) {
                data = data + "-" + cnt;
                cnt++;
            }
//            chooseAction(context,data);
            sendMessage(data, 0);
        }

        Log.d(TAG, "----------------------------------------------------------------------------------------------");
    }

//    private void chooseAction(Context context,String data){
//        try {
//            JSONObject json=new JSONObject(data);
//            int status=json.getInt("msgtype");
//            String title=json.getString("title");
//            String body=json.getString("body");
//            showNotification(context,title,body);
////            if(status==1){
////                Intent intent = new Intent(context, MainActivity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                context.startActivity(intent);
////            }
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//    }
//
//    private void showNotification(Context context,String title,String content){
//        //获得通知消息管理类对象
//        mNManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
////获取大图标
//        LargeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img_circle_logo);
//        Intent intent = new Intent(context,NotificationActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//
//        //设置图片,通知标题,发送时间,提示方式等属性
//        Notification.Builder builder = new Notification.Builder(context);
//        builder.setContentTitle(title)  //标题
//                .setContentText(content)   //内容
////                    .setSubText("--北极熊很Happy～")     //内容下面的一小段文字
////                    .setTicker("收到北极熊也有微笑发来的的消息～")      //收到信息后状态栏显示的文字信息
//                .setWhen(System.currentTimeMillis())    //系统显示时间
//                .setSmallIcon(R.mipmap.ic_launcher)     //收到信息后状态栏显示的小图标
//                .setLargeIcon(LargeBitmap)      //大图标
//                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)    //设置默认的三色灯与振动器
//                .setDefaults(Notification.DEFAULT_SOUND)    //设置系统的提示音
//                .setAutoCancel(true);       //设置点击后取消Notification
//        builder.setContentIntent(pendingIntent);    //
//    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);

        sendMessage(clientid, 1);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Log.d(TAG, "onReceiveOnlineState -> " + (online ? "online" : "offline"));
    }


    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.d(TAG, "onReceiveCommandResult -> " + cmdMessage);

        int action = cmdMessage.getAction();

        if (action == PushConsts.SET_TAG_RESULT) {
            setTagResult((SetTagCmdMessage) cmdMessage);
        } else if(action == PushConsts.BIND_ALIAS_RESULT) {
            bindAliasResult((BindAliasCmdMessage) cmdMessage);
        } else if (action == PushConsts.UNBIND_ALIAS_RESULT) {
            unbindAliasResult((UnBindAliasCmdMessage) cmdMessage);
        } else if ((action == PushConsts.THIRDPART_FEEDBACK)) {
            feedbackResult((FeedbackCmdMessage) cmdMessage);
        }
    }


        @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage message) {

            Log.d(TAG, "onNotificationMessageArrived -> " + "appid = " + message.getAppid() + "\ntaskid = " + message.getTaskId() + "\nmessageid = "
                    + message.getMessageId() + "\npkg = " + message.getPkgName() + "\ncid = " + message.getClientId() + "\ntitle = "
                    + message.getTitle() + "\ncontent = " + message.getContent());
//            //获得通知消息管理类对象
//            mNManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
////获取大图标
//            LargeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img_circle_logo);
//            Intent intent = new Intent(context,NotificationActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//
//            //设置图片,通知标题,发送时间,提示方式等属性
//            Notification.Builder builder = new Notification.Builder(context);
//            builder.setContentTitle( message.getTitle())  //标题
//                    .setContentText( message.getContent())   //内容
////                    .setSubText("--北极熊很Happy～")     //内容下面的一小段文字
////                    .setTicker("收到北极熊也有微笑发来的的消息～")      //收到信息后状态栏显示的文字信息
//                    .setWhen(System.currentTimeMillis())    //系统显示时间
//                    .setSmallIcon(R.mipmap.ic_launcher)     //收到信息后状态栏显示的小图标
//                    .setLargeIcon(LargeBitmap)      //大图标
//                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)    //设置默认的三色灯与振动器
//                    .setDefaults(Notification.DEFAULT_SOUND)    //设置系统的提示音
//                    .setAutoCancel(true);       //设置点击后取消Notification
//            builder.setContentIntent(pendingIntent);    //


    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage message) {

        Log.d(TAG, "onNotificationMessageClicked -> " + "appid = " + message.getAppid() + "\ntaskid = " + message.getTaskId() + "\nmessageid = "
                + message.getMessageId() + "\npkg = " + message.getPkgName() + "\ncid = " + message.getClientId() + "\ntitle = "
                + message.getTitle() + "\ncontent = " + message.getContent());
    }



//    /**
//     * 个推透传创建通知栏
//     *
//     * @param title
//     * @param subtitle
//     */
//    private void addNotification(String title, String subtitle, MixPushMessage message) {
//        //显示不重复通知
//        int requestCode = (int) System.currentTimeMillis();
//
//        Intent broadcastIntent = new Intent(this, GeTuiNotificationClickReceiver.class);
//        broadcastIntent.putExtra("message", message);
//        PendingIntent pendingIntent = PendingIntent.
//                getBroadcast(this, requestCode, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setWhen(System.currentTimeMillis())
//                .setContentTitle(title)
//                .setContentText(subtitle)
//                .setDefaults(Notification.DEFAULT_LIGHTS)
//                //.setVibrate(new long[]{0, 300, 300, 300})
//                //设置点击通知跳转页面后，通知消失
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent);
//        //获取app工程中的图片资源
//        int logoId = getApplicationContext().getResources().getIdentifier(getIconName(getApplicationContext()), "mipmap",
//                getApplicationContext().getPackageName());
//        builder.setSmallIcon(logoId);
//        NotificationManager manager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
//        manager.notify(requestCode, builder.build());
//    }

//    /**
//     * 静态广播接收器-通知栏点击
//     */
//    public class GeTuiNotificationClickReceiver extends BroadcastReceiver {
//        public GeTuiNotificationClickReceiver() {
//        }
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            GeTuiManager.sMixMessageProvider.onNotificationMessageClicked(context, (MixPushMessage) intent.getSerializableExtra("message"));
//        }
//    }



//    /**
//     * 获取主工程mipmap下的资源文件名
//     */
//    public static String getIconName(Context mContext) {
//        String value = "";
//        try {
//            ApplicationInfo appInfo = mContext.getPackageManager().
//                    getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
//            value = appInfo.metaData.getString("OEM_ICON");
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        back value;
//    }





    private void setTagResult(SetTagCmdMessage setTagCmdMsg) {
        String sn = setTagCmdMsg.getSn();
        String code = setTagCmdMsg.getCode();

        int text = R.string.add_tag_unknown_exception;
        switch (Integer.valueOf(code)) {
            case PushConsts.SETTAG_SUCCESS:
                text = R.string.add_tag_success;
                break;

            case PushConsts.SETTAG_ERROR_COUNT:
                text = R.string.add_tag_error_count;
                break;

            case PushConsts.SETTAG_ERROR_FREQUENCY:
                text = R.string.add_tag_error_frequency;
                break;

            case PushConsts.SETTAG_ERROR_REPEAT:
                text = R.string.add_tag_error_repeat;
                break;

            case PushConsts.SETTAG_ERROR_UNBIND:
                text = R.string.add_tag_error_unbind;
                break;

            case PushConsts.SETTAG_ERROR_EXCEPTION:
                text = R.string.add_tag_unknown_exception;
                break;

            case PushConsts.SETTAG_ERROR_NULL:
                text = R.string.add_tag_error_null;
                break;

            case PushConsts.SETTAG_NOTONLINE:
                text = R.string.add_tag_error_not_online;
                break;

            case PushConsts.SETTAG_IN_BLACKLIST:
                text = R.string.add_tag_error_black_list;
                break;

            case PushConsts.SETTAG_NUM_EXCEED:
                text = R.string.add_tag_error_exceed;
                break;

            default:
                break;
        }

        Log.d(TAG, "settag result sn = " + sn + ", code = " + code + ", text = " + getResources().getString(text));
    }

    private void bindAliasResult(BindAliasCmdMessage bindAliasCmdMessage) {
        String sn = bindAliasCmdMessage.getSn();
        String code = bindAliasCmdMessage.getCode();

        int text = R.string.bind_alias_unknown_exception;
        switch (Integer.valueOf(code)) {
            case PushConsts.BIND_ALIAS_SUCCESS:
                text = R.string.bind_alias_success;
                break;
            case PushConsts.ALIAS_ERROR_FREQUENCY:
                text = R.string.bind_alias_error_frequency;
                break;
            case PushConsts.ALIAS_OPERATE_PARAM_ERROR:
                text = R.string.bind_alias_error_param_error;
                break;
            case PushConsts.ALIAS_REQUEST_FILTER:
                text = R.string.bind_alias_error_request_filter;
                break;
            case PushConsts.ALIAS_OPERATE_ALIAS_FAILED:
                text = R.string.bind_alias_unknown_exception;
                break;
            case PushConsts.ALIAS_CID_LOST:
                text = R.string.bind_alias_error_cid_lost;
                break;
            case PushConsts.ALIAS_CONNECT_LOST:
                text = R.string.bind_alias_error_connect_lost;
                break;
            case PushConsts.ALIAS_INVALID:
                text = R.string.bind_alias_error_alias_invalid;
                break;
            case PushConsts.ALIAS_SN_INVALID:
                text = R.string.bind_alias_error_sn_invalid;
                break;
            default:
                break;

        }

        Log.d(TAG, "bindAlias result sn = " + sn + ", code = " + code + ", text = " + getResources().getString(text));

    }

    private void unbindAliasResult(UnBindAliasCmdMessage unBindAliasCmdMessage) {
        String sn = unBindAliasCmdMessage.getSn();
        String code = unBindAliasCmdMessage.getCode();

        int text = R.string.unbind_alias_unknown_exception;
        switch (Integer.valueOf(code)) {
            case PushConsts.UNBIND_ALIAS_SUCCESS:
                text = R.string.unbind_alias_success;
                break;
            case PushConsts.ALIAS_ERROR_FREQUENCY:
                text = R.string.unbind_alias_error_frequency;
                break;
            case PushConsts.ALIAS_OPERATE_PARAM_ERROR:
                text = R.string.unbind_alias_error_param_error;
                break;
            case PushConsts.ALIAS_REQUEST_FILTER:
                text = R.string.unbind_alias_error_request_filter;
                break;
            case PushConsts.ALIAS_OPERATE_ALIAS_FAILED:
                text = R.string.unbind_alias_unknown_exception;
                break;
            case PushConsts.ALIAS_CID_LOST:
                text = R.string.unbind_alias_error_cid_lost;
                break;
            case PushConsts.ALIAS_CONNECT_LOST:
                text = R.string.unbind_alias_error_connect_lost;
                break;
            case PushConsts.ALIAS_INVALID:
                text = R.string.unbind_alias_error_alias_invalid;
                break;
            case PushConsts.ALIAS_SN_INVALID:
                text = R.string.unbind_alias_error_sn_invalid;
                break;
            default:
                break;

        }

        Log.d(TAG, "unbindAlias result sn = " + sn + ", code = " + code + ", text = " + getResources().getString(text));

    }


    private void feedbackResult(FeedbackCmdMessage feedbackCmdMsg) {
        String appid = feedbackCmdMsg.getAppid();
        String taskid = feedbackCmdMsg.getTaskId();
        String actionid = feedbackCmdMsg.getActionId();
        String result = feedbackCmdMsg.getResult();
        long timestamp = feedbackCmdMsg.getTimeStamp();
        String cid = feedbackCmdMsg.getClientId();

        Log.d(TAG, "onReceiveCommandResult -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nactionid = " + actionid + "\nresult = " + result
                + "\ncid = " + cid + "\ntimestamp = " + timestamp);
    }

    private void sendMessage(String data, int what) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = data;
        MyApp.sendMessage(msg);
    }
}
