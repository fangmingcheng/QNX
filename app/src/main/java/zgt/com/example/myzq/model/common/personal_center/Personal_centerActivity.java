package zgt.com.example.myzq.model.common.personal_center;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.MyApp;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.Personal;
import zgt.com.example.myzq.model.common.custom_view.MyImageView;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.model.common.permission.PermissionUtils;
import zgt.com.example.myzq.model.common.permission.request.IRequestPermissions;
import zgt.com.example.myzq.model.common.permission.request.RequestPermissions;
import zgt.com.example.myzq.model.common.permission.requestresult.IRequestPermissionsResult;
import zgt.com.example.myzq.model.common.permission.requestresult.RequestPermissionsResultSetApp;
import zgt.com.example.myzq.utils.FileProviderUtils;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.SystemProgramUtils;
import zgt.com.example.myzq.utils.ToastUtil;


public class Personal_centerActivity extends BaseActivity {

    @BindView(R.id.Tv_name)
    TextView Tv_name;
    @BindView(R.id.Iv_head)
    MyImageView Iv_head;
    @BindView(R.id.Tv_gender)
    TextView Tv_gender;
    @BindView(R.id.Tv_phone)
    TextView Tv_phone;


    private Personal personal;
    private String img_src;
    public static final int SELECT_PHOTO=1;
    private int yourChoice=-1,index1=0;
    private  String[] items = { "保密","男","女" };
    private File file;

    IRequestPermissions requestPermissions = RequestPermissions.getInstance();//动态权限请求
    IRequestPermissionsResult requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();//动态权限请求结果处理


    private static final int WRITE_PERMISSION = 0x01;


    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_center;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        StatusBarUtil.statusBarLightMode(this);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                getData();
//            }
//        }).start();
//        requestWritePermission();
    }

    @OnClick({R.id.Rl_head,R.id.Rl_name,R.id.Rl_phone,R.id.Iv_customer,R.id.Tv_quit,R.id.Ll_gender})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Rl_head:
                creataDailog();
//                selectImg();
                break;
            case R.id.Rl_name:
                startActivity(new Intent().setClass(this,ChangeNameActivity.class).putExtra("name",personal.getNickname()));
                break;
            case R.id.Rl_phone:
                startActivity(new Intent().setClass(this,BindingPhoneActivity.class));
                break;
            case R.id.Iv_customer:
                finish();
                break;
            case R.id.Ll_gender:
                showSingleChoiceDialog(Tv_gender,items,index1);
                break;
            case R.id.Tv_quit:
                quitProgram();
                break;
        }
    }

    private void creataDailog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(Task_DetailActivity.this);
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view = LayoutInflater.from(Personal_centerActivity.this).inflate(R.layout.check_dialog_layout, null);
        //    设置我们自己定义的布局文件作为弹出框的Content
//        builder.setView(view);
        final TextView Tv_take_photo = (TextView) view.findViewById(R.id.Tv_take_photo);
        final TextView Tv_choose_photo = (TextView) view.findViewById(R.id.Tv_choose_photo);
        final TextView Tv_cancel = (TextView) view.findViewById(R.id.Tv_cancel);
        final Dialog dialog = new Dialog(Personal_centerActivity.this, R.style.style_dialog);
        dialog.setContentView(view);
        dialog.show();
        Tv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!requestPermissions()){
                    ToastUtil.showShortToast(Personal_centerActivity.this,"请允许访问手机存储空间权限和相机权限");
                    return;
                }
                file=new File("/mnt/sdcard/tupian.jpg");
                SystemProgramUtils.paizhao(Personal_centerActivity.this, file,1);
                dialog.cancel();
            }
        });
        Tv_choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!requestPermissions()){
                    ToastUtil.showShortToast(Personal_centerActivity.this,"请允许访问手机存储空间权限和相机权限");
                    return;
                }
                SystemProgramUtils.zhaopian(Personal_centerActivity.this,1);
                dialog.cancel();
            }
        });

        Tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
//        window.setWindowAnimations(R.style.dialog_animation);
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private void showSingleChoiceDialog(TextView tv, String[] items, int index){
//        final String[] items = { "我是1","我是2","我是3","我是4" };
        yourChoice = -1;
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(Personal_centerActivity.this);
        singleChoiceDialog.setTitle("请选择");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, index,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourChoice = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(yourChoice!=-1) {
                            index1=yourChoice;
                            saveSex(index1);

                        }
                    }
                });
        singleChoiceDialog.show();
    }

    public void selectImg() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, SELECT_PHOTO);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }).start();
    }

    private void saveSex(int index){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"updateSex.do");
        requestParams.setConnectTimeout(60 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("sex", index);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                NoteUtil.logConnect(new RequestParams("登录onSuccess"), LoginActivity.this, "result:" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    ToastUtil.showShortToast(Personal_centerActivity.this,jsonObject.getString("message") );
                    if (a==1) {
                        Tv_gender.setText(items[index1]);
                    } else if(a==-1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(Personal_centerActivity.this,LoginActivity.class));
                                finish();
                            }
                        });
//                        new AlertDialog.Builder(Personal_centerActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        startActivity(new Intent().setClass(Personal_centerActivity.this,LoginActivity.class));
//                                        finish();
//                                        dialog.dismiss();
//                                    }
//                                }).create().show();
                    }else {

                    }

                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(Personal_centerActivity.this, "解析异常");
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
                        ToastUtil.showShortToast(Personal_centerActivity.this, "网络连接异常");
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
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"aboutme.do");
        requestParams.setConnectTimeout(60 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                NoteUtil.logConnect(new RequestParams("登录onSuccess"), LoginActivity.this, "result:" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if (a==1) {
                        personal=new Personal();
                        personal.setCity(json.getString("city"));
                        personal.setCityname(json.getString("cityname"));
                        personal.setEmail(json.getString("email"));
                        personal.setHeadimg(json.getString("headimg"));
                        personal.setMobile(json.getString("mobile"));
                        personal.setMysign(json.getString("mysign"));
                        personal.setNickname(json.getString("nickname"));
                        personal.setProvince(json.getString("province"));
                        personal.setProvincename(json.getString("provincename"));
                        personal.setSex(json.getInt("sex"));
                        personal.setUsername(json.getString("username"));
                        personal.setTruename(json.getString("truename"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setData();
                            }
                        });
                    } else if(a==-1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(Personal_centerActivity.this,LoginActivity.class));
                                finish();
                            }
                        });
//                        new AlertDialog.Builder(Personal_centerActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        startActivity(new Intent().setClass(Personal_centerActivity.this,LoginActivity.class));
//                                        finish();
//                                        dialog.dismiss();
//                                    }
//                                }).create().show();
                    }else {
                    }

                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(Personal_centerActivity.this, "解析异常");
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
                        ToastUtil.showShortToast(Personal_centerActivity.this, "网络连接异常");
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

    private void quitProgram(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"memberLogout.do");
        requestParams.setConnectTimeout(60 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                NoteUtil.logConnect(new RequestParams("登录onSuccess"), LoginActivity.this, "result:" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SPUtil.getLoginSharedPreferences().edit().putString("token", "")
                                        .putString("memberid", "")
                                        .putString("username", "")
                                        .putString("nickname", "")
                                        .putString("modulecode", "")
                                        .putString("modulename","")
                                        .putString("typename","")
                                        .putInt("type",0)
                                        .putString("startdate","")
                                        .putString("teacherid","")
                                        .putString("enddate","")
                                        .putInt("membertype",0)
                                        .putInt("tsort",0)
                                        .commit();
                                MyApp.httpResult=null;
                                finishAll();
                                startActivity(new Intent().setClass(Personal_centerActivity.this,LoginActivity.class));
                            }
                        });
                    } else if(a==-1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(Personal_centerActivity.this,LoginActivity.class));
                                finish();
                            }
                        });
//                        new AlertDialog.Builder(Personal_centerActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        startActivity(new Intent().setClass(Personal_centerActivity.this,LoginActivity.class));
//                                        finish();
//                                        dialog.dismiss();
//                                    }
//                                }).create().show();
                    }else {
                    }

                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(Personal_centerActivity.this, "解析异常");
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
                        ToastUtil.showShortToast(Personal_centerActivity.this, "网络连接异常");
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

    private void setData(){
        if(TextUtils.isEmpty(personal.getHeadimg())){
            Iv_head.setImageResource(R.drawable.replace);
        }else {
            Iv_head.setImageURL(personal.getHeadimg());
        }

        Tv_name.setText(personal.getNickname());
        if(TextUtils.isEmpty(personal.getMobile())) {
            Tv_phone.setText("未绑定");
        }else {
            Tv_phone.setText(personal.getMobile());
        }
        if(personal.getSex()==0){
            Tv_gender.setText("保密");
            index1=0;
        }else if(personal.getSex()==1) {
            Tv_gender.setText("男");
            index1=1;
        }else if(personal.getSex()==2){
            Tv_gender.setText("女");
            index1=2;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        Uri filtUri;
        File outputFile = new File("/mnt/sdcard/tupian_out.jpg");//裁切后输出的图片
        switch (requestCode) {
            case SystemProgramUtils.REQUEST_CODE_PAIZHAO:
                //拍照完成，进行图片裁切
                File file = new File("/mnt/sdcard/tupian.jpg");
                filtUri = FileProviderUtils.uriFromFile(Personal_centerActivity.this, file);
                SystemProgramUtils.Caiqie(Personal_centerActivity.this, filtUri, outputFile,1);
                //不进行裁剪直接显示
                break;
            case SystemProgramUtils.REQUEST_CODE_ZHAOPIAN:
                //相册选择图片完毕，进行图片裁切
                if (data == null ||  data.getData()==null) {
                    return;
                }
                filtUri = data.getData();
                SystemProgramUtils.Caiqie(Personal_centerActivity.this, filtUri, outputFile,1);

                break;
            case SystemProgramUtils.REQUEST_CODE_CAIQIE:
                //图片裁切完成，显示裁切后的图片
                try {
                    Uri uri = Uri.fromFile(outputFile);
//                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//                    Log.i("wechat", "压缩前图片的大小"+ (bitmap.getByteCount() / 1024/ 1024) + "M宽度为"+ bitmap.getWidth() + "高度为"+ bitmap.getHeight());
//                    Bitmap bit=Bitmap.createScaledBitmap(bitmap, 150, 150, true);
//                    Log.i("wechat", "压缩后图片的大小"+ (bit.getByteCount() / 1024) + "KB宽度为" + bit.getWidth() + "高度为"+ bit.getHeight());
                    send(outputFile);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                break;
        }
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case SELECT_PHOTO:
//                switch (resultCode) {
//                    case RESULT_OK:
//                        Uri uri = data.getData();
//                        img_src = uri.getPath();//这是本机的图片路径
//                        ContentResolver cr =getContentResolver();
//                        try {
//                            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
//                        /* 将Bitmap设定到ImageView */
////                            Iv_head.setImageBitmap(bitmap);
////                            Iv_head.setImageBitmap(ImageUtil.decodeSampledBitmapFromFilePath(img_src,100,100));
//                            String[] proj = {MediaStore.Images.Media.DATA};
//                            CursorLoader loader = new CursorLoader(getContext(), uri, proj, null, null, null);
//                            Cursor cursor = loader.loadInBackground();
//                            if (cursor != null) {
//                                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                                cursor.moveToFirst();
//                                img_src = cursor.getString(column_index);//图片实际路径
//                                send();
//
//                            }
//                            cursor.close();
//
//                        } catch (FileNotFoundException e) {
//                            Log.e("Exception", e.getMessage(), e);
//                        }
//
//                        break;
//                }
//                break;
//        }
    }

    private void send(File files){
        //要传递给服务器的json格式参数
        JSONObject json = new JSONObject();
        try {
            json.put("token", SPUtil.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //构建RequestParams对象，传入请求的服务器地址URL
        RequestParams params = new RequestParams(SPUtil.getServerAddress()+"uploadHeading.do");
        params.addParameter("token", SPUtil.getToken());
        params.setAsJsonContent(true);
        List<KeyValue> list = new ArrayList<>();
        list.add(new KeyValue("file", files));
        list.add(new KeyValue("parameters", json.toString()));
        MultipartBody body = new MultipartBody(list, "UTF-8");
        params.setRequestBody(body);
        x.http().post(params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                LogUtil.e("请求结果：" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        //加载图片
                        JSONObject json=jsonObject.getJSONObject("data");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Iv_head.setImageURL(json.getString("url"));
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        ToastUtil.showShortToast(Personal_centerActivity.this,jsonObject.getString("message"));
//                        Glide.with(Personal_centerActivity.this).load(img_src).into(Iv_head);
//                        Iv_head.setImageBitmap(ImageUtil.decodeSampledBitmapFromFilePath(img_src,100,100));
                    } else if(a==-1){
                        new AlertDialog.Builder(Personal_centerActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent().setClass(Personal_centerActivity.this,LoginActivity.class));
                                        finish();
                                        dialog.dismiss();
                                    }
                                }).create().show();
                    } else{
                        ToastUtil.showShortToast(Personal_centerActivity.this,jsonObject.getString("message"));
                    }
                } catch (JSONException e){
                    ToastUtil.showShortToast(Personal_centerActivity.this, "解析异常");
                } finally {

                }
            }

            @Override
            public void onFinished() {
                //上传完成
            }

            @Override
            public void onCancelled(CancelledException cex) {
                //取消上传
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //上传失败
//                LogUtil.e("请求失败：" + ex.toString());
            }
        });
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        if(requestCode == WRITE_PERMISSION){
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.d(LOG_TAG, "Write Permission Failed");
//                Toast.makeText(this, "You must allow permission write external storage to your mobile device.", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//    }
//    private void requestWritePermission(){
//        if(ContextCompat.checkSelfPermission(Personal_centerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);
//        }
//    }

    //请求权限
    private boolean requestPermissions(){
        //需要请求的权限
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
        //开始请求权限
        return requestPermissions.requestPermissions(
                this,
                permissions,
                PermissionUtils.ResultCode1);
    }

}
