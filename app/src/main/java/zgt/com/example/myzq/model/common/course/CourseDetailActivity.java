package zgt.com.example.myzq.model.common.course;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.tencent.trtc.TRTCCloudDef;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.MyApp;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.classes.CourseCatalog;
import zgt.com.example.myzq.bean.classes.CourseContent;
import zgt.com.example.myzq.bean.classes.CourseDetail;
import zgt.com.example.myzq.model.common.course.fragment.CourseCatalogFragment;
import zgt.com.example.myzq.model.common.course.fragment.CourseContentFragment;
import zgt.com.example.myzq.model.common.course.fragment.CourseDetailFragment;
import zgt.com.example.myzq.model.common.custom_view.MyJzvdStd;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.model.common.order.ZBOrderDetailActivity;
import zgt.com.example.myzq.model.common.personal_center.RiskTestActivity;
import zgt.com.example.myzq.model.common.personal_center.basic.MyBasicInformationActivity;
import zgt.com.example.myzq.model.common.trtc.TRTCMainActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class CourseDetailActivity extends BaseActivity {

    @BindView(R.id.jz_video)
    MyJzvdStd jz_video;
    @BindView(R.id.Tv_detail)
    TextView Tv_detail;
    @BindView(R.id.Tv_catalog)
    TextView Tv_catalog;
    @BindView(R.id.Tv_education)
    TextView Tv_education;
//    @BindView(R.id.myviewpager)
//    ViewPager myviewpager;

    @BindView(R.id.fragment_container)
    FrameLayout fragment_container;

    @BindView(R.id.Ll_buy)
    LinearLayout Ll_buy;

    CourseDetailFragment courseDetailFragment;
    CourseCatalogFragment courseCatalogFragment;
    CourseContentFragment courseContentFragment;


    private String  uuid;
    private String fileUrl;
    private int index=1;
    private String contentid;

    //
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction transaction;


    private CourseDetail courseDetail;
    private CourseCatalog courseCatalog;
    private CourseContent courseContent;

    private List<CourseCatalog> courseCatalogs = new ArrayList<>();
    private List<CourseContent> courseContents = new ArrayList<>();


    public static NoLeakHandler handler ;


    public static class NoLeakHandler extends Handler{
        private WeakReference<CourseDetailActivity> mActivity;

        public NoLeakHandler(CourseDetailActivity activity){
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CourseDetailActivity activity=mActivity.get();
            switch (msg.what){
                case 2:
                    Bundle bundle = msg.getData();
                    int code = bundle.getInt("code");
                    String uuid = bundle.getString("uuid");
                    if(code == 2){
//                       Bundle bundle1= new Bundle();
                        activity.setData( uuid, code);

                    }else if(code == 3){
                        activity.setData( uuid, code);
                    }else if(code == 1){

                    }
                    break;
            }
        }
    }
    public void setData(String string,int status){
        if(status == 2){//视频
            if(jz_video!=null){
                fileUrl=string;
                jz_video.setUp(string, "");
                jz_video.startVideo();
            }
        }else if(status == 3){//课件
            if( Tv_detail!=null  && Tv_catalog!=null  && Tv_education != null){
                MyApp.fileUrl=string;
//                fileUrl = string;
                Tv_detail.setTextColor(Color.parseColor("#333333"));
                Tv_catalog.setTextColor(Color.parseColor("#333333"));
                Tv_education.setTextColor(Color.parseColor("#FFAE00"));

                transaction = fm.beginTransaction();
                if (courseContentFragment == null) {
                    courseContentFragment = new CourseContentFragment();
                    transaction.add(R.id.fragment_container,courseContentFragment);
                }
                hideFragment(transaction);
                transaction.show(courseContentFragment);
                transaction.commit();
            }
        }

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_course_detail;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        StatusBarUtil.statusBarLightMode(this);
        StatusBarUtil.setLightMode(this);

        uuid=getIntent().getStringExtra("uuid");
        index=getIntent().getIntExtra("index",1);
        if(index == 2){
            contentid=getIntent().getStringExtra("contentid");
            if(!TextUtils.isEmpty(contentid)){
                getData(contentid,"");
            }
        }
        // 默认第一页
        // 初始按钮颜色
        Tv_detail.setTextColor(Color.parseColor("#FFAE00"));
        handler = new NoLeakHandler(this);

    }

    @Override
    public void initToolBar() {

    }



    @Override
    protected void onResume() {
        getData();

        super.onResume();
        jz_video.goOnPlayOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        jz_video.goOnPlayOnPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        jz_video.clearFocus();
        jz_video.onStateAutoComplete();
//        if(courseware!=null) {
//            jz_video.clearSavedProgress(this, SPUtil.getServerAddress().substring(0, (SPUtil.getServerAddress().length() - 5)) + courseware.getVideourl());
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(!TextUtils.isEmpty(fileUrl)) {
            jz_video.resetAllVideos();
        }else {
        }
    }

    public List<CourseCatalog> getcourseCatalogList(){
        return courseCatalogs;
    }

    public List<CourseContent> getCourseContentList(){
        return courseContents;
    }

    public CourseDetail getCourseDetail(){
        return courseDetail;
    }


    public String getFile(){
        return fileUrl;
    }


    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
//            homeFragment = new HomeFragment();
//            transaction.replace(R.id.fragment_container, homeFragment);
//            transaction.commit();
        if(index ==1){
            Tv_detail.setTextColor(Color.parseColor("#FFAE00"));
            Tv_catalog.setTextColor(Color.parseColor("#333333"));
            Tv_education.setTextColor(Color.parseColor("#333333"));
            if (courseDetailFragment == null) {
                courseDetailFragment = new CourseDetailFragment();
                transaction.add(R.id.fragment_container,courseDetailFragment);
            }
            hideFragment(transaction);
            transaction.show(courseDetailFragment);
        }else if(index == 2){
            Tv_detail.setTextColor(Color.parseColor("#333333"));
            Tv_catalog.setTextColor(Color.parseColor("#FFAE00"));
            Tv_education.setTextColor(Color.parseColor("#333333"));
            if (courseCatalogFragment == null) {
                courseCatalogFragment = new CourseCatalogFragment();
                transaction.add(R.id.fragment_container,courseCatalogFragment);
            }
            hideFragment(transaction);
            transaction.show(courseCatalogFragment);
        }
            transaction.commit();

            if(!TextUtils.isEmpty(getFirstData())){
                getData(getFirstData());
            }

        Glide.with(CourseDetailActivity.this).load(courseDetail.getPicpath()).into(jz_video.thumbImageView);

        if(courseDetail.getIscharge()==0){
            Ll_buy.setVisibility(View.GONE);
        }else {
            if(courseDetail.getIsbuy()==0){
                Ll_buy.setVisibility(View.VISIBLE);
            }else {
                Ll_buy.setVisibility(View.GONE);
            }
//            if(courseDetail.getPricelimit()==0){
//                if(courseDetail.getIsbuy()==1){
//                    Ll_buy.setVisibility(View.GONE);
//                }else {
//                    Ll_buy.setVisibility(View.VISIBLE);
//                }
//            }else {
//                Ll_buy.setVisibility(View.VISIBLE);
//            }
        }

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

                        Intent liveintent = new Intent(CourseDetailActivity.this, TRTCMainActivity.class);
                        liveintent.putExtra("sdk_app_id",sdkAppId);
                        liveintent.putExtra("room_id",roomId);
                        liveintent.putExtra("user_id",userId);
                        liveintent.putExtra(TRTCMainActivity.KEY_APP_SCENE, TRTCCloudDef.TRTC_APP_SCENE_LIVE);
                        liveintent.putExtra("user_sig",userSig);

//                        Intent courseIntent = new Intent().setClass(CourseDetailActivity.this, CourseDetailActivity.class).putExtra("uuid",fileId);
//                        Intent[] intents = {courseIntent,liveintent};
                       startActivity(liveintent);
//                        Message message=  CourseDetailActivity.handler.obtainMessage();

//                        if(status==2){
//                            current = position;
//                            notifyDataSetChanged();
//                        }

                    } else if(a==-1){
                        startActivity(new Intent().setClass(CourseDetailActivity.this, LoginActivity.class));
                        finish();
//                       runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
////;
//                              finish();
//                            }
//                        });

                    }else if(a==2){
                        Toast.makeText(CourseDetailActivity.this, msg, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(CourseDetailActivity.this, "数据获取异常", Toast.LENGTH_SHORT);


                } finally {

                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                ToastUtil.showShortToast( CourseDetailActivity.this, "网络连接异常");

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
    }

    private String getFirstData(){
        for (int i=0;i<courseCatalogs.size();i++){
            if(courseCatalogs.get(i).getIsfee()==0){
                for (int j=0;j<courseCatalogs.get(i).getList().size();j++){
                    if(courseCatalogs.get(i).getList().get(j).getFlag()==1&&courseCatalogs.get(i).getList().get(j).getKtype()==2){
                        return  courseCatalogs.get(i).getList().get(j).getUuid();
                    }
                    if(courseCatalogs.get(i).getList().get(j).getFlag()==1&&courseCatalogs.get(i).getList().get(j).getKtype()==1&&courseCatalogs.get(i).getList().get(j).getLivestatus()==2){

                        return  courseCatalogs.get(i).getList().get(j).getUuid();
                    }
                }
            }
        }
        return "";
    }

    private  void getData(String uuid){
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
                        String fileurl = json.getString("fileurl");
                        String picUrl = json.getString("coverpicpath");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Glide.with(CourseDetailActivity.this).load(picUrl).into(jz_video.thumbImageView);
                                fileUrl=fileurl;
                                jz_video.setUp(fileurl,"");
                            }
                        });
//                        if(status==2){
//                            current = position;
//                            notifyDataSetChanged();
//                        }
//                       runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
////                                setData(fileurl,status);
//                            }
//                        });
                    } else if(a==-1){

//                       runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
////
//                                startActivity(new Intent().setClass(CourseDetailActivity.this,LoginActivity.class));
//                              finish();
//                            }
//                        });

                    }else if(a==2){
                        Toast.makeText(CourseDetailActivity.this, msg, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(CourseDetailActivity.this, "数据获取异常", Toast.LENGTH_SHORT);


                } finally {

                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                ToastUtil.showShortToast( CourseDetailActivity.this, "网络连接异常");

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
    }


    // 设置按钮颜色
    public void resetButtonColor() {
        Tv_detail.setTextColor(Color.parseColor("#333333"));
        Tv_catalog.setTextColor(Color.parseColor("#333333"));
        Tv_education.setTextColor(Color.parseColor("#333333"));
    }

    private void hideFragment(FragmentTransaction transaction){
        if (courseDetailFragment != null) {
            transaction.hide(courseDetailFragment);
        }
        if (courseCatalogFragment != null) {
            transaction.hide(courseCatalogFragment);
        }
        if (courseContentFragment != null) {
            transaction.hide(courseContentFragment);
        }

    }



    @OnClick({R.id.Tv_detail,R.id.Tv_catalog,R.id.Tv_education,R.id.Tv_commit})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Tv_commit:
                intoOrder();

                break;
            case R.id.Tv_detail:
                index=1;
                Tv_detail.setTextColor(Color.parseColor("#FFAE00"));
                Tv_catalog.setTextColor(Color.parseColor("#333333"));
                Tv_education.setTextColor(Color.parseColor("#333333"));

                transaction = fm.beginTransaction();
                if (courseDetailFragment == null) {
                    courseDetailFragment = new CourseDetailFragment();
                    transaction.add(R.id.fragment_container,courseDetailFragment);
                }
                hideFragment(transaction);
                transaction.show(courseDetailFragment);
                transaction.commit();
//                myviewpager.setCurrentItem(0);

//                cursorAnim(1);
                break;
            case R.id.Tv_catalog:
                index=2;
                Tv_detail.setTextColor(Color.parseColor("#333333"));
                Tv_catalog.setTextColor(Color.parseColor("#FFAE00"));
                Tv_education.setTextColor(Color.parseColor("#333333"));

                transaction = fm.beginTransaction();
                if (courseCatalogFragment == null) {
                    courseCatalogFragment = new CourseCatalogFragment();
                    transaction.add(R.id.fragment_container,courseCatalogFragment);
                }
                hideFragment(transaction);
                transaction.show(courseCatalogFragment);
                transaction.commit();
//                cursorAnim(1);
                break;
            case R.id.Tv_education:
                index=3;
                Tv_detail.setTextColor(Color.parseColor("#333333"));
                Tv_catalog.setTextColor(Color.parseColor("#333333"));
                Tv_education.setTextColor(Color.parseColor("#FFAE00"));

                transaction = fm.beginTransaction();
                if (courseContentFragment == null) {
                    courseContentFragment = new CourseContentFragment();
                    transaction.add(R.id.fragment_container,courseContentFragment);
                }
                hideFragment(transaction);
                transaction.show(courseContentFragment);
                transaction.commit();
//                cursorAnim(1);
                break;

        }
    }

    private void commonDialog(int status,String title,String content) {

        new android.app.AlertDialog.Builder(CourseDetailActivity.this).setTitle(title).setMessage(content).setCancelable(false)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).setPositiveButton("立即前往", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(status==2){
                    startActivity(new Intent().setClass(CourseDetailActivity.this, MyBasicInformationActivity.class).putExtra("fileid",courseDetail.getUuid()).putExtra("index",1).putExtra("type","1"));
                }else if(status==3){
                    startActivity(new Intent().setClass(CourseDetailActivity.this, RiskTestActivity.class).putExtra("fileid",courseDetail.getUuid()).putExtra("index",1).putExtra("type","1"));
                }else if(status==4){
                    startActivity(new Intent().setClass(CourseDetailActivity.this, RiskTestActivity.class).putExtra("fileid",courseDetail.getUuid()).putExtra("index",1).putExtra("type","1"));
                }
                dialog.dismiss();
            }
        }).show();

    }

    private void commonDialog(String title,String content) {
        new android.app.AlertDialog.Builder(CourseDetailActivity.this).setTitle(title).setMessage(content).setCancelable(false)
                .setNegativeButton("重新评测", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent().setClass(CourseDetailActivity.this, RiskTestActivity.class).putExtra("fileid",courseDetail.getUuid()).putExtra("index",1).putExtra("type","1"));
                        dialog.dismiss();
                    }
                }).setPositiveButton("前往购买", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent().setClass(CourseDetailActivity.this, ZBOrderDetailActivity.class).putExtra("fileid",courseDetail.getUuid()).putExtra("index",1).putExtra("type","1"));
                dialog.dismiss();
            }
        }).show();
    }

    private void intoOrder(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"checkMemberInformation0518.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("fileid", courseDetail.getUuid());
        requestParams.addParameter("producttype", "1");
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        commonDialog("您已完成风险评测","是否前往重新评测，或者直接购买");
                    } else if(a==2){
                        commonDialog(2,"您的个人信息不完整请您完善","是否前往完善");
                    } else if(a==3){
                        commonDialog(3,"没有风控记录，请您前往填写风控记录","是否前往填写");
                    } else if(a==4){
                        commonDialog(4," C1(最低类别),不能购买","是否前往重新评测");
                    } else if(a==5){
                        ToastUtil.showShortToast(CourseDetailActivity.this,"您已购买过该课程，无需重新购买");
                    }else if(a==6){
                        ToastUtil.showShortToast(CourseDetailActivity.this,"您未绑定手机号码，请您先绑定手机号码");
                    } else if(a==7){
                        ToastUtil.showShortToast(CourseDetailActivity.this,"您有订单未确认，请联系客服");
                    } else if(a==8){
                        ToastUtil.showShortToast(CourseDetailActivity.this,"请勿重复下单");
                    }
                    else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(CourseDetailActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {

                    }
                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(CourseDetailActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(CourseDetailActivity.this, "网络连接异常");
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
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"zgFileDetail.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("fileid", uuid);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        courseContents.clear();
                        courseCatalogs.clear();
                        JSONObject json=jsonObject.getJSONObject("data");
                        courseDetail = new CourseDetail();
                        courseDetail.setUuid(uuid);
                        courseDetail.setTitle(json.getString("title"));
                        courseDetail.setDescription(json.getString("description"));
                        courseDetail.setIsbuy(json.getInt("isbuy"));
                        courseDetail.setIscharge(json.getInt("ischarge"));
                        courseDetail.setLecturer(json.getString("lecturer"));
                        courseDetail.setPrice(json.getDouble("price"));
                        courseDetail.setPicpath(json.getString("picpath"));
                        courseDetail.setPricelimit(json.getInt("pricelimit"));
                        courseDetail.setRealprice(json.getDouble("realprice"));
                        courseDetail.setLiveCount(json.getLong("livecount"));
                        courseDetail.setFileCount(json.getLong("filecount"));
                        courseDetail.setVideoCount(json.getLong("videocount"));
                        courseDetail.setAudioCount(json.getLong("audioCount"));
                        JSONArray jsonArray=json.getJSONArray("menulist");

                        for (int i=0;i<jsonArray.length();i++){
                            courseCatalog=new CourseCatalog();
                            courseContents=new ArrayList<>();
                            if(i<3){
                                courseCatalog.setOpen(true);
                            }else {
                                courseCatalog.setOpen(false);
                            }
                            courseCatalog.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            courseCatalog.setClick(jsonArray.getJSONObject(i).getInt("click"));
                            courseCatalog.setFileid(jsonArray.getJSONObject(i).getString("fileid"));
                            courseCatalog.setIsfee(jsonArray.getJSONObject(i).getInt("isfee"));
                            courseCatalog.setName(jsonArray.getJSONObject(i).getString("name"));
//                            courseCatalog.setTeacherid(jsonArray.getJSONObject(i).getString("teacherid"));
//                            courseCatalog.setTeachername(jsonArray.getJSONObject(i).getString("teachername"));

                            JSONArray array = jsonArray.getJSONObject(i).getJSONArray("conentlist");
                            for (int j=0;j<array.length();j++){
                                courseContent=new CourseContent();
                                courseContent.setUuid(array.getJSONObject(j).getString("uuid"));
                                courseContent.setMenuid(array.getJSONObject(j).getString("menuid"));
                                courseContent.setKname(array.getJSONObject(j).getString("kname"));
                                courseContent.setKtype(array.getJSONObject(j).getInt("ktype"));
                                courseContent.setLivetime(array.getJSONObject(j).getString("livetime"));
                                courseContent.setTeachername(array.getJSONObject(j).getString("teachername"));
                                courseContent.setTeacherid(array.getJSONObject(j).getString("teacherid"));
                                courseContent.setVideoduration(array.getJSONObject(j).getString("videoduration"));
                                courseContent.setLivestatus(array.getJSONObject(j).getInt("livestatus"));
                                courseContent.setFlag(array.getJSONObject(j).getInt("flag"));
                                courseContent.setClick(array.getJSONObject(j).getInt("click"));
                                courseContents.add(courseContent);
                            }
                            courseCatalog.setList(courseContents);
                            courseCatalogs.add(courseCatalog);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               setDefaultFragment();
                            }
                        });
                    } else if(a==-1){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(CourseDetailActivity.this, LoginActivity.class));
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
                        final String msg=jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(CourseDetailActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(CourseDetailActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(CourseDetailActivity.this, "网络连接异常");
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

    private void setmVideoView(){
//        if(list1.size()>0) {
//            if(list1.get(0).getIsfee()==0) {
//                jz_video.setUp("http://byrobot-xld-private.oss-cn-qingdao.aliyuncs.com/files/8d07ee4e-3625-4304-b608-443cad3d7bee.jpg?Expires=1897498504&OSSAccessKeyId=LTAIV4yGOWdfJ9AP&Signature=Q%2BG7mBjXRo1IzLKTkabiNfbph90%3D", "");
//            }else {
//                ToastUtil.showShortToast(CourseDetailActivity.this,"该章节需要付费观看");
//            }
//            Glide.with(this).load(SPUtil.getServerAddress().substring(0,(SPUtil.getServerAddress().length()-5))+list1.get(0).getPicpath()).into(jz_video.thumbImageView);
//        }


//        Glide.with(this).load(SPUtil.getServerAddress().substring(0,(SPUtil.getServerAddress().length()-5))+courseware.getPicpath()).into(jz_video.thumbImageView);
//
//        list = new ArrayList<Fragment>();
//        list.add(new CourseDetailFragment());
//        list.add(new CourseCatalogFragment());
//        list.add(new CourseContentFragment());
//        adapter = new MyfragmentViewpageAdapter(fm, list);
//        myviewpager.setAdapter(adapter);
//        // viewpage监听事件，重写onPageSelected()方法，实现左右滑动页面
//        myviewpager.setOnPageChangeListener(this);
    }

    @Override
    public void onBackPressed() {
        MyApp.fileUrl="";
        super.onBackPressed();
    }





}
