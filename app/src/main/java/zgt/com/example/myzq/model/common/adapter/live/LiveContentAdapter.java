package zgt.com.example.myzq.model.common.adapter.live;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.trtc.TRTCCloudDef;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.MyBaseAdapter;
import zgt.com.example.myzq.bean.live.LiveItems;
import zgt.com.example.myzq.model.common.course.CourseDetailActivity;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.model.common.trtc.TRTCMainActivity;
import zgt.com.example.myzq.utils.Log;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;


/**
 * Created by ThinkPad on 2019/3/29.
 */

public class LiveContentAdapter extends MyBaseAdapter<LiveItems> {
    private int current = -1;
    private Context context;

    public LiveContentAdapter(Context context){
        super(context);
        this.context=context;
    }

    /**
     * 设置当前选择的项。
     * @param current 当前位置
     */
    public void setCurrent(int current) {
        this.current = current;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_live_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.Iv_btn = convertView.findViewById(R.id.Iv_btn);
            viewHolder.Tv_title = convertView.findViewById(R.id.Tv_title);
            viewHolder.Tv_time = convertView.findViewById(R.id.Tv_time);
            viewHolder.Tv_teacher = convertView.findViewById(R.id.Tv_teacher);
//            ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,ListView.LayoutParams.WRAP_CONTENT);//设置宽度和高度
//            convertView.setLayoutParams(params);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        LiveItems  live = getItem(position);
//        String[] strings=s.split("\\\\");
        if(live.getLivetime().length()>10){
            viewHolder.Tv_time.setText(live.getLivetime().substring(11,16));
        }
        viewHolder.Tv_title.setText(live.getKname());
        viewHolder.Tv_teacher.setText("主讲："+live.getTeachername());
        if(live.getLivestatus()==1){
            viewHolder.Iv_btn.setImageResource(R.mipmap.img_zhibo);
        }else if(live.getLivestatus()==0){
            viewHolder.Iv_btn.setImageResource(R.mipmap.img_yugao);
        }
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();

        Log.e(""+height);
        ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,height*2/15);//设置宽度和高度
        convertView.setLayoutParams(params);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(live.getLivestatus() == 1) {
                    getData(live.getUuid(),live.getFileid());

//                    context.startActivity(new Intent().setClass(context, CourseDetailActivity.class).putExtra("uuid", live.getFileid()));
                }

            }
        });
        return convertView;
    }

    class ViewHolder {

        ImageView Iv_btn;
        TextView Tv_time,Tv_title,Tv_teacher;
    }

    private  void getData(String uuid,String fileId){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"getFileContentByFileid.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("contentid", uuid);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    String msg=jsonObject.getString("message");
//                    Toast.makeText(CourseDetailActivity.this, msg, Toast.LENGTH_SHORT);
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        int type= json.getInt("type");

                        String userSig= json.getString("userSig");
                        String userId= json.getString("userId");
                        int roomId= json.getInt("roomId");
                        int sdkAppId= json.getInt("sdkAppId");

                        Intent liveintent = new Intent(context, TRTCMainActivity.class);
                        liveintent.putExtra("sdk_app_id",sdkAppId);
                        liveintent.putExtra("room_id",roomId);
                        liveintent.putExtra("user_id",userId);
                        liveintent.putExtra(TRTCMainActivity.KEY_APP_SCENE, TRTCCloudDef.TRTC_APP_SCENE_LIVE);
                        liveintent.putExtra("user_sig",userSig);

                        Intent courseIntent = new Intent().setClass(context, CourseDetailActivity.class).putExtra("uuid",fileId);
                        Intent[] intents = {courseIntent,liveintent};
                        context.startActivities(intents);
//                        Message message=  CourseDetailActivity.handler.obtainMessage();

//                        if(status==2){
//                            current = position;
//                            notifyDataSetChanged();
//                        }

                    } else if(a==-1){
                        context.startActivity(new Intent().setClass(context, LoginActivity.class));
//                       runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
////
                        context.startActivity(new Intent().setClass(context, LoginActivity.class));
//                              finish();
//                            }
//                        });

                    }else if(a==2){
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "数据获取异常", Toast.LENGTH_SHORT);


                } finally {

                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                ToastUtil.showShortToast( context, "网络连接异常");

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
