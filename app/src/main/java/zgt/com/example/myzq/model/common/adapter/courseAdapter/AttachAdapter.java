package zgt.com.example.myzq.model.common.adapter.courseAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import zgt.com.example.myzq.bean.classes.CourseContent;
import zgt.com.example.myzq.model.common.course.CourseDetailActivity;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.model.common.trtc.TRTCMainActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;


/**
 * Created by ThinkPad on 2019/3/29.
 */

public class AttachAdapter extends MyBaseAdapter<CourseContent> {
    private int current = -1;
    private Context context;

    public AttachAdapter(Context context){
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
            convertView = inflater.inflate(R.layout.item_attach, null);
            viewHolder = new ViewHolder();
            viewHolder.Ll_file = convertView.findViewById(R.id.Ll_file);
            viewHolder.Iv_image_file = convertView.findViewById(R.id.Iv_image_file);
            viewHolder.Tv_title = convertView.findViewById(R.id.Tv_title);
            viewHolder.Tv_time = convertView.findViewById(R.id.Tv_time);
            viewHolder.Tv_live = convertView.findViewById(R.id.Tv_live);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."))
        CourseContent  courseContent = getItem(position);
//        String[] strings=s.split("\\\\");
        viewHolder.Tv_title.setText(courseContent.getKname());

        if(courseContent.getKtype()==1){
            if(courseContent.getLivestatus()==1){
                viewHolder.Tv_time.setText(courseContent.getLivetime()+"   主讲:"+courseContent.getTeachername());
                viewHolder.Tv_live.setText("【直播中】");
                viewHolder.Iv_image_file.setImageResource(R.mipmap.ic_zhengzhibo);
                viewHolder.Tv_title.setTextColor(Color.parseColor("#23B7FD"));
                viewHolder.Tv_time.setTextColor(Color.parseColor("#23B7FD"));
            }else if(courseContent.getLivestatus()==0){
                viewHolder.Tv_live.setText("【预告】");
                viewHolder.Tv_live.setTextColor(Color.parseColor("#ffae00"));
                viewHolder.Tv_time.setText(courseContent.getLivetime()+"   主讲:"+courseContent.getTeachername());
                viewHolder.Iv_image_file.setImageResource(R.mipmap.ic_nozhibao);
                viewHolder.Tv_title.setTextColor(Color.parseColor("#23B7FD"));
                viewHolder.Tv_time.setTextColor(Color.parseColor("#23B7FD"));
            } else {
                if(TextUtils.isEmpty(courseContent.getVideoduration())){
                    viewHolder.Tv_time.setText("主讲:"+courseContent.getTeachername());
                }else {
                    viewHolder.Tv_time.setText(courseContent.getVideoduration()+"   主讲:"+courseContent.getTeachername());
                }
                if(courseContent.getFlag()==0){
                    viewHolder.Tv_live.setText("【已结束】");
                    viewHolder.Tv_live.setTextColor(Color.parseColor("#d9d9d9"));
                    viewHolder.Iv_image_file.setImageResource(R.mipmap.ic_nozhibao);
                    viewHolder.Tv_title.setTextColor(Color.parseColor("#d9d9d9"));
                    viewHolder.Tv_time.setTextColor(Color.parseColor("#d9d9d9"));
                }else if(courseContent.getFlag()==1){
                    viewHolder.Tv_live.setText("【录播】");
                    viewHolder.Tv_live.setTextColor(Color.parseColor("#333333"));
                    if(current==position){
                        viewHolder.Iv_image_file.setImageResource(R.mipmap.ic_zhibo);
                        viewHolder.Tv_title.setTextColor(Color.parseColor("#FFAE00"));
                        viewHolder.Tv_time.setTextColor(Color.parseColor("#FFAE00"));
                    }else {
                        viewHolder.Iv_image_file.setImageResource(R.mipmap.ic_zhibo);
                        viewHolder.Tv_title.setTextColor(Color.parseColor("#333333"));
                        viewHolder.Tv_time.setTextColor(Color.parseColor("#333333"));
                    }

                }
            }
        }else if(courseContent.getKtype()==2){
            viewHolder.Tv_time.setVisibility(View.VISIBLE);
            if(TextUtils.isEmpty(courseContent.getVideoduration())){
                viewHolder.Tv_time.setText("主讲:"+courseContent.getTeachername());
            }else {
                viewHolder.Tv_time.setText(courseContent.getVideoduration()+"   主讲:"+courseContent.getTeachername());
            }
            if(courseContent.getFlag()==0){
                viewHolder.Iv_image_file.setImageResource(R.mipmap.ic_noshipin);
                viewHolder.Tv_title.setTextColor(Color.parseColor("#d9d9d9"));
                viewHolder.Tv_time.setTextColor(Color.parseColor("#d9d9d9"));
            }else if(courseContent.getFlag()==1){
                if(current==position){
                    viewHolder.Iv_image_file.setImageResource(R.mipmap.ic_shipin);
                    viewHolder.Tv_title.setTextColor(Color.parseColor("#FFAE00"));
                    viewHolder.Tv_time.setTextColor(Color.parseColor("#FFAE00"));
                }else {
                    viewHolder.Iv_image_file.setImageResource(R.mipmap.ic_shipin);
                    viewHolder.Tv_title.setTextColor(Color.parseColor("#333333"));
                    viewHolder.Tv_time.setTextColor(Color.parseColor("#333333"));
                }

            }

        }else if(courseContent.getKtype()==3){
            viewHolder.Tv_time.setVisibility(View.GONE);
            if(courseContent.getFlag()==0){
                viewHolder.Iv_image_file.setImageResource(R.mipmap.ic_nokejian);
                viewHolder.Tv_title.setTextColor(Color.parseColor("#d9d9d9"));
//                viewHolder.Tv_time.setTextColor(Color.parseColor("#d9d9d9"));
            }else if(courseContent.getFlag()==1){
                viewHolder.Iv_image_file.setImageResource(R.mipmap.ic_kejian);
                viewHolder.Tv_title.setTextColor(Color.parseColor("#333333"));
//                viewHolder.Tv_time.setTextColor(Color.parseColor("#333333"));
            }
        }else if(courseContent.getKtype()==4){
            viewHolder.Tv_time.setVisibility(View.VISIBLE);
            if(TextUtils.isEmpty(courseContent.getVideoduration())){
                viewHolder.Tv_time.setText("主讲:"+courseContent.getTeachername());
            }else {
                viewHolder.Tv_time.setText(courseContent.getVideoduration()+"   主讲:"+courseContent.getTeachername());
            }
            if(courseContent.getFlag()==0){
                viewHolder.Iv_image_file.setImageResource(R.mipmap.ic_audio4);
                viewHolder.Tv_title.setTextColor(Color.parseColor("#d9d9d9"));
                viewHolder.Tv_time.setTextColor(Color.parseColor("#d9d9d9"));
            }else if(courseContent.getFlag()==1){
                if(current==position){
                    viewHolder.Iv_image_file.setImageResource(R.mipmap.ic_audio2);
                    viewHolder.Tv_title.setTextColor(Color.parseColor("#FFAE00"));
                    viewHolder.Tv_time.setTextColor(Color.parseColor("#FFAE00"));
                }else {
                    viewHolder.Iv_image_file.setImageResource(R.mipmap.ic_audio1);
                    viewHolder.Tv_title.setTextColor(Color.parseColor("#333333"));
                    viewHolder.Tv_time.setTextColor(Color.parseColor("#333333"));
                }

            }

        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(courseContent.getKtype()==1){//直播
                    if(courseContent.getLivestatus()==1){
                        //进入正在直播界面
                        getData(courseContent.getUuid(),1,position);
//                        context.startActivity(new Intent().setClass(context, WebViewActivity.class).putExtra("url",courseContent.getUuid()));
                    }else if(courseContent.getLivestatus()==2){
                        if(courseContent.getFlag()==1){
                            getData(courseContent.getUuid(),2,position);

                        }
                    }
                }else if(courseContent.getKtype()==2){//视频
                    if(courseContent.getFlag()==0){
                    }else {
                        getData(courseContent.getUuid(),2,position);
                        //获取资源

                    }
                }else if(courseContent.getKtype()==3){
                    {//课件
                        if(courseContent.getFlag()==0){

                        }else {
                            getData(courseContent.getUuid(),3,position);
                            //获取资源
                        }
                    }
                }else if(courseContent.getKtype()==4){
                    {//课件
                        if(courseContent.getFlag()==0){

                        }else {
                            getData(courseContent.getUuid(),2,position);
                            //获取资源
                        }
                    }
                }
//                context.startActivity(new Intent().setClass(context, WebViewActivity.class).putExtra("url",s));
            }
        });
        return convertView;
    }

    class ViewHolder {
        LinearLayout Ll_file;
        ImageView Iv_image_file;
        TextView Tv_title,Tv_time,Tv_live;
    }

    private  void getData(String uuid,int status,int position){
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
//                    if(TextUtils.isEmpty(msg)){
//
//                    }else {
//                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//                    }
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        int type= json.getInt("type");
                        Bundle bundle = new Bundle();
                        if(type == 1){
                            String coverpicpath= json.getString("coverpicpath");
                            String userSig= json.getString("userSig");
                            String userId= json.getString("userId");
                            int roomId= json.getInt("roomId");
                            int sdkAppId= json.getInt("sdkAppId");

                            Intent intent = new Intent(context, TRTCMainActivity.class);
                            intent.putExtra("sdk_app_id",sdkAppId);
                            intent.putExtra("room_id",roomId);
                            intent.putExtra("user_id",userId);
                            intent.putExtra(TRTCMainActivity.KEY_APP_SCENE, TRTCCloudDef.TRTC_APP_SCENE_LIVE);
                            intent.putExtra("user_sig",userSig);
                            context.startActivity(intent);

                        }else if(type == 2){
                            String fileUrl = json.getString("fileurl");
                            String coverpicpath= json.getString("coverpicpath");
                            bundle.putInt("code",status);
                            bundle.putString("coverpicpath",coverpicpath);
                            bundle.putString("uuid",fileUrl);
                            Message message = CourseDetailActivity.handler.obtainMessage();
                            message.what=2;
                            message.setData(bundle);
                            message.sendToTarget();
                        }

//                        Message message=  CourseDetailActivity.handler.obtainMessage();

//                        if(status==2){
//                            current = position;
//                            notifyDataSetChanged();
//                        }

                    } else if(a==-1){
                        context.startActivity(new Intent().setClass(context, LoginActivity.class));


                    }else if(a>1){
                        if(TextUtils.isEmpty(msg)){

                        }else {
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        }

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
