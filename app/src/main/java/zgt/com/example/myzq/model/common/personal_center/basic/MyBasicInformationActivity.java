package zgt.com.example.myzq.model.common.personal_center.basic;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.basic.BasicInformation;
import zgt.com.example.myzq.bean.basic.Occupation;
import zgt.com.example.myzq.model.common.custom_view.MyPicImageView;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.model.common.order.ZBOrderDetailActivity;
import zgt.com.example.myzq.model.common.permission.PermissionUtils;
import zgt.com.example.myzq.model.common.permission.request.IRequestPermissions;
import zgt.com.example.myzq.model.common.permission.request.RequestPermissions;
import zgt.com.example.myzq.model.common.permission.requestresult.IRequestPermissionsResult;
import zgt.com.example.myzq.model.common.permission.requestresult.RequestPermissionsResultSetApp;
import zgt.com.example.myzq.model.common.personal_center.RiskTestActivity;
import zgt.com.example.myzq.utils.BitmapUtil;
import zgt.com.example.myzq.utils.FileUtil;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.StatusBarUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class MyBasicInformationActivity extends BaseActivity {

    @BindView(R.id.Et_name)
    EditText Et_name;//姓名
//    @BindView(R.id.Et_phone)
//    EditText Et_phone;//电话
    @BindView(R.id.Et_id_card)
    EditText Et_id_card;//身份证

    @BindView(R.id.Tv_type)
    TextView Tv_type;//
    @BindView(R.id.Tv_Occupation)
    TextView Tv_Occupation;//
//    @BindView(R.id.Tv_Education)
//    TextView Tv_Education;//
    @BindView(R.id.Tv_record)
    TextView Tv_record;//

    @BindView(R.id.Iv_picture_other)
    MyPicImageView Iv_picture_other;//

    @BindView(R.id.Iv_picture)
    MyPicImageView Iv_picture;//

    @BindView(R.id.Iv_front)
    ImageView Iv_front;//

    @BindView(R.id.Tv_front)
    TextView Tv_front;//

    @BindView(R.id.Iv_other)
    ImageView Iv_other;//

    @BindView(R.id.Tv_other)
    TextView Tv_other;//

    @BindView(R.id.Bt_save)
    Button Bt_save;//Bt_save

    @BindView(R.id.Ll_front)
    LinearLayout Ll_front;//

    @BindView(R.id.Ll_other_side)
    LinearLayout Ll_other_side;//

    private int status;
//    private CourseDetail courseDetail;


    private List<Occupation> occupationList = new ArrayList<>();//职业
    private List<Occupation> educationList = new ArrayList<>();//学历
    private List<Occupation> creditrecordList = new ArrayList<>();//诚信记录
    private List<Occupation> idtypeList = new ArrayList<>();//证件类型

    public static final int REQUEST_CODE_PAIZHAO = 1;
    public static final int REQUEST1_CODE_PAIZHAO = 2;
    public static final int REQUEST_CODE_ZHAOPIAN = 3;
    public static final int REQUEST2_CODE_ZHAOPIAN = 4;

    private File file,file1;

    private int yourChoice=-1,index1=0,index2=0,index3=0,index4=0;

    IRequestPermissions requestPermissions = RequestPermissions.getInstance();//动态权限请求
    IRequestPermissionsResult requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();//动态权限请求结果处理

    private BasicInformation basicInformation;


    private Occupation occupation;

    private String mTempPhotoPath;
    private Uri imageUri;


    private int index;
    private String type,fileid;

    boolean isSave=false;

    @Override
    public void initToolBar() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_basic_information;

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.statusBarLightMode(this);
        status = getIntent().getIntExtra("status",0);
        fileid = getIntent().getStringExtra("fileid");
        type = getIntent().getStringExtra("type");
        index = getIntent().getIntExtra("index",0);
//        status = getIntent().getIntExtra("status",0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }).start();
    }

    private void initData(BasicInformation basicInformation){
        if(basicInformation !=null){
            Et_name.setText(basicInformation.getTruename());
            Et_id_card.setText(basicInformation.getIdcardno());
//            Et_phone.setText(basicInformation.getTel());

            Tv_type.setText(basicInformation.getIdtype());
//            Tv_Education.setText(basicInformation.getEducation());
            Tv_Occupation.setText(basicInformation.getOccupation());
            Tv_record.setText(basicInformation.getCreditrecord());

            if(TextUtils.isEmpty(basicInformation.getIdcardfrontpath())){

            }else {
                Iv_picture.setVisibility(View.VISIBLE);
                Iv_front.setVisibility(View.GONE);
                Tv_front.setVisibility(View.GONE);
                Iv_picture.setImageURL(basicInformation.getIdcardfrontpath());
            }

            if(TextUtils.isEmpty(basicInformation.getIdcardbackpath())){

            }else {
                Iv_picture_other.setVisibility(View.VISIBLE);
                Iv_other.setVisibility(View.GONE);
                Tv_other.setVisibility(View.GONE);
                Iv_picture_other.setImageURL(basicInformation.getIdcardbackpath());
            }

            if(basicInformation.getIsmodifyInformation()==1){
                Et_name.setFocusable(false);
                Et_id_card.setFocusable(false);
//                Et_phone.setFocusable(false);
                Tv_type.setClickable(false);
                Tv_Occupation.setClickable(false);
//                Tv_Education.setClickable(false);
                Tv_record.setClickable(false);
                Ll_front.setClickable(false);
                Ll_other_side.setClickable(false);
                Bt_save.setText("编辑");
                isSave = true;
            }
        }
    }

    @OnClick({R.id.Bt_save,R.id.Iv_back,R.id.Ll_front,R.id.Ll_other_side,R.id.Tv_type,R.id.Tv_Occupation,R.id.Tv_record})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Bt_save:
//                if(isSave){
//                    ToastUtil.showShortToast(MyBasicInformationActivity.this,"您已保存基本信息，不能再次提交");
//                }else {
                if("保存".equals(Bt_save.getText().toString().trim())) {
                    if (isSave) {
                        commitData1();
                    } else {
                        if (TextUtils.isEmpty(Et_id_card.getText()) || TextUtils.isEmpty(Et_name.getText())
                                || TextUtils.isEmpty(Tv_type.getText()) || TextUtils.isEmpty(Tv_Occupation.getText())
                                || TextUtils.isEmpty(Tv_record.getText())
                                || file == null || file1 == null) {
                            ToastUtil.showShortToast(MyBasicInformationActivity.this, "请检查数据是否填写完整");
                            return;

                        }
                        commitData();
                    }
                }else if("编辑".equals(Bt_save.getText().toString().trim())){
                    Bt_save.setText("保存");
                    Et_name.setTextColor(getResources().getColor(R.color.colorPrimary));
                    Et_id_card.setTextColor(getResources().getColor(R.color.colorPrimary));
//                    Et_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
                    Tv_type.setTextColor(getResources().getColor(R.color.colorPrimary));

                    Tv_Occupation.setClickable(true);
//                    Tv_Education.setClickable(true);
                    Tv_record.setClickable(true);
//                    Et_phone.setFocusable(true);
                }
//                }

                break;
            case R.id.Iv_back:
                finish();
                break;
            case R.id.Ll_front:
                creataDailog(1);
                break;
            case R.id.Ll_other_side:
                creataDailog(2);
                break;
            case R.id.Tv_type:
                showSingleChoiceDialog(Tv_type,idtypeList,index4,4);
                break;
            case R.id.Tv_Occupation:
                showSingleChoiceDialog(Tv_Occupation,occupationList,index1,1);
                break;
//            case R.id.Tv_Education:
//                showSingleChoiceDialog(Tv_Education,educationList,index2,2);
//                break;
            case R.id.Tv_record:
                showSingleChoiceDialog(Tv_record,creditrecordList,index3,3);
                break;
        }
    }

    private void showSingleChoiceDialog(TextView tv, List<Occupation > stringList, int index,int status ){

//        final String[] items = { "我是1","我是2","我是3","我是4" };
        String[] items = new String[stringList.size()];
        for(int i=0 ;i<stringList.size();i++){
            items[i] = stringList.get(i).text;
        }
        yourChoice=-1;
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(MyBasicInformationActivity.this);
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
                            tv.setText(items[yourChoice]);
                            tv.setTextColor(Color.parseColor("#333333"));
                            if(status == 1){
                                index1=yourChoice;
                            }else if(status == 2){
                                index2=yourChoice;
                            } else if(status == 3){
                                index3=yourChoice;
                            }else {
                                index4=yourChoice;
                            }

                        }else {
                            tv.setText(items[index]);
                            tv.setTextColor(Color.parseColor("#333333"));
                        }
                    }
                });
        singleChoiceDialog.show();
    }

    private void creataDailog(int status){
//        AlertDialog.Builder builder = new AlertDialog.Builder(Task_DetailActivity.this);
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view = LayoutInflater.from(MyBasicInformationActivity.this).inflate(R.layout.check_dialog_layout, null);
        //    设置我们自己定义的布局文件作为弹出框的Content
//        builder.setView(view);
        final TextView Tv_take_photo = (TextView) view.findViewById(R.id.Tv_take_photo);
        final TextView Tv_choose_photo = (TextView) view.findViewById(R.id.Tv_choose_photo);
        final TextView Tv_cancel = (TextView) view.findViewById(R.id.Tv_cancel);
        final Dialog dialog = new Dialog(MyBasicInformationActivity.this, R.style.style_dialog);
        dialog.setContentView(view);
        dialog.show();
        Tv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!requestPermissions()){
                    ToastUtil.showShortToast(MyBasicInformationActivity.this,"请允许访问手机存储空间权限和相机权限");
                    return;
                }
                takePhoto(status);
//                file=new File("/mnt/sdcard/tupian.jpg");
//                SystemProgramUtils.paizhao(MyBasicInformationActivity.this, file,status);
                dialog.cancel();
            }
        });
        Tv_choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!requestPermissions()){
                    ToastUtil.showShortToast(MyBasicInformationActivity.this,"请允许访问手机存储空间权限和相机权限");
                    return;
                }
                choosePhoto(status);

//                SystemProgramUtils.zhaopian(MyBasicInformationActivity.this,status);
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

    private void choosePhoto(int status) {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        if(status == 1){
            startActivityForResult(intentToPickPic, REQUEST_CODE_PAIZHAO);
        }else {
            startActivityForResult(intentToPickPic, REQUEST1_CODE_PAIZHAO);
        }

    }



    private void takePhoto(int status) {
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File file = new File(getExternalCacheDir()+System.currentTimeMillis(), System.currentTimeMillis() + ".png");
        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + System.currentTimeMillis() + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File photoFile ;
        if(status == 1){
            photoFile = new File(fileDir, "photo.jpeg");

        }else {
            photoFile = new File(fileDir, "photo1.jpeg");
        }

        mTempPhotoPath = photoFile.getAbsolutePath();
        imageUri = FileProvider.getUriForFile(this,"zgt.com.example.myzq.fileProvider", photoFile);
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        if(status == 1){
            startActivityForResult(intentToTakePhoto, REQUEST_CODE_ZHAOPIAN);
        }else {
            startActivityForResult(intentToTakePhoto, REQUEST2_CODE_ZHAOPIAN);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        Uri uri;
        String filePath;

        File fileDir = new File(Environment.getExternalStorageDirectory()+"/copypic/"+System.currentTimeMillis());

        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File photoFile ;
//        if(status == 1){
            photoFile = new File(fileDir, "photo.jpeg");

//        }else {
//            photoFile = new File(fileDir, "photo1.jpeg");
//        }
        String path1 =photoFile.getAbsolutePath();
//        switch (requestCode) {
            switch (requestCode) {
                case REQUEST_CODE_PAIZHAO:
                     uri = data.getData();
                     filePath = FileUtil.getFilePathByUri(this, uri);
                     copyFile(filePath,path1);
                    if (!TextUtils.isEmpty(filePath)) {
                        //将照片显示在 ivImage上
//                        Bitmap bitmap = getLoacalBitmap(filePath); //从本地取图片(在cdcard中获取)  //
//                        Iv_front .setImageBitmap(bitmap); //设置Bitma
                        Iv_picture.setVisibility(View.VISIBLE);
                        Iv_front.setVisibility(View.GONE);
                        Tv_front.setVisibility(View.GONE);
                        file = new File(path1);
                        if(file.length()<1024*20){
                            ToastUtil.showShortToast(MyBasicInformationActivity.this,"您所提交的照片尺寸太小，请重新上传");
                            return;
                        }
//                        Log.e("SIZE",file.length()+"");
//                        file = new File(BitmapUtil.compressImage(path1,10));
//                        Log.e("SIZE",file.length()+"");
//                        file = new File(BitmapUtil.compressImage(path1,30));
//                        Log.e("SIZE",file.length()+"");
//                        file = new File(BitmapUtil.compressImage(path1,50));
//                        Log.e("SIZE",file.length()+"");
//                        file = new File(BitmapUtil.compressImage(path1,70));
//                        Log.e("SIZE",file.length()+"");
//                        file = new File(BitmapUtil.compressImage(path1,90));
//                        Log.e("SIZE",file.length()+"");
//                        file = new File(BitmapUtil.compressImage(path1,100));
//                        Log.e("SIZE",file.length()+"");
                        if(file != null ){
                            if(file.length()>1024*1024*10){
                                file = new File(BitmapUtil.compressImage(path1,50));
                            }else if(file.length()>1024*1024*1){
                                file = new File(BitmapUtil.compressImage(path1,100));
                            }
                        }
//                        file = new File(BitmapUtil.compressImage(filePath));
                        Glide.with(this).load(path1).into(Iv_picture);
                    }
                    break;
                case REQUEST1_CODE_PAIZHAO:
                    uri = data.getData();
                    filePath = FileUtil.getFilePathByUri(this, uri);

                    if (!TextUtils.isEmpty(filePath)) {
                        //将照片显示在 ivImage上
                        copyFile(filePath,path1);
                        Iv_picture_other.setVisibility(View.VISIBLE);
                        Tv_other.setVisibility(View.GONE);
                        Iv_other.setVisibility(View.GONE);
                        file1 = new File(path1);
                        if(file.length()<1024*20){
                            ToastUtil.showShortToast(MyBasicInformationActivity.this,"您所提交的照片尺寸太小，请重新上传");
                            return;
                        }
                        if(file1 != null ){
                            if(file1.length()>1024*1024*10){
                                file1 = new File(BitmapUtil.compressImage(path1,50));
                            }else if(file1.length()>1024*1024*1){
                                file1 = new File(BitmapUtil.compressImage(path1,100));
                            }
                        }
                        Glide.with(this).load(path1).into(Iv_picture_other);
                    }
                    break;
                case REQUEST_CODE_ZHAOPIAN:
//                    uri = data.getData();
//                    filePath = FileUtil.getFilePathByUri(this, uri);
                    Iv_picture.setVisibility(View.VISIBLE);
                    Iv_front.setVisibility(View.GONE);
                    Tv_front.setVisibility(View.GONE);
                    file = new File(mTempPhotoPath);

                    if(file != null ){
                        if(file.length()>1024*1024*10){
                            file = new File(BitmapUtil.compressImage(mTempPhotoPath,50));
                        }else if(file.length()>1024*1024*1){
                            file = new File(BitmapUtil.compressImage(mTempPhotoPath,100));
                        }
                    }
//                    Log.e("TAG",file.length()+"");
//                    file = new File(BitmapUtil.compressImage(mTempPhotoPath,5));
//                    Log.e("TAG1",file.length()+"");
                    Glide.with(this).load(mTempPhotoPath).into(Iv_picture);
                    break;
                case REQUEST2_CODE_ZHAOPIAN:
//                    uri = data.getData();
//                    filePath = FileUtil.getFilePathByUri(this, uri);
                    Iv_picture_other.setVisibility(View.VISIBLE);
                    Tv_other.setVisibility(View.GONE);
                    Iv_other.setVisibility(View.GONE);
                    file1 = new File(mTempPhotoPath);

                    if(file1 != null ){
                        if(file1.length()>1024*1024*10){
                            file1 = new File(BitmapUtil.compressImage(mTempPhotoPath,50));
                        }else if(file1.length()>1024*1024*1){
                            file1 = new File(BitmapUtil.compressImage(mTempPhotoPath,100));
                        }
                    }
//                    file1 = new File(BitmapUtil.compressImage(mTempPhotoPath));
                    Glide.with(this).load(mTempPhotoPath).into(Iv_picture_other);
                    break;

            }

    }

// public  void vopyFile(File fromFile, File toFile){
//          try {
//            FileInputStream fosfrom = new FileInputStream(fromFile);
//            FileOutputStream fosto = new FileOutputStream(toFile);
//            byte bt[] = new byte[1024*1024];
//            int c;
//            while((c = fosfrom.read(bt)) > 0){
//                fosto.write(bt, 0, c);
//            }
//            fosfrom.close();
//            fosto.close();
//        }catch (FileNotFoundException e){
//            e.printStackTrace();
//            Log.i("复制文件异常", e.toString());
//        }catch (IOException e){
//            Log.i("复制文件异常", e.toString());
//        }
//   }

    //进行复制的函数
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }





    private void commonDialog(int status,String title,String content) {
        new android.app.AlertDialog.Builder(MyBasicInformationActivity.this).setTitle(title).setMessage(content).setCancelable(false)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("立即前往", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 if(status==3){
                    startActivity(new Intent().setClass(MyBasicInformationActivity.this, RiskTestActivity.class).putExtra("type",type).putExtra("fileid",fileid).putExtra("index",index));
                     MyBasicInformationActivity.this.finish();
                }else if(status==4){
                    startActivity(new Intent().setClass(MyBasicInformationActivity.this, RiskTestActivity.class).putExtra("type",type).putExtra("fileid",fileid).putExtra("index",index));
                     MyBasicInformationActivity.this.finish();
                }
                dialog.dismiss();
            }
        }).show();

    }

    private void commonDialog(String title,String content) {

        new android.app.AlertDialog.Builder(MyBasicInformationActivity.this).setTitle(title).setMessage(content).setCancelable(false)
                .setNegativeButton("重新评测", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent().setClass(MyBasicInformationActivity.this, RiskTestActivity.class).putExtra("fileid",fileid).putExtra("index",index).putExtra("type",type));
                        MyBasicInformationActivity.this.finish();
                        dialog.dismiss();
                    }
                }).setPositiveButton("前往购买", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent().setClass(MyBasicInformationActivity.this, ZBOrderDetailActivity.class).putExtra("fileid",fileid).putExtra("index",index).putExtra("type",type));
                MyBasicInformationActivity.this.finish();
                dialog.dismiss();
            }
        }).show();

    }

    private void intoOrder(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"checkMemberInformation.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("fileid", fileid);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        commonDialog("您已完成风险评测","是否前往重新评测，或者直接购买");
                    } else if(a==3){
                        commonDialog(3,"没有风控记录，请您前往填写风控记录","是否前往填写");
                    } else if(a==4){
                        commonDialog(4," C1(最低类别),不能购买","是否前往重新评测");
                    }else if(a==5){
                        ToastUtil.showShortToast(MyBasicInformationActivity.this,"您已购买过该课程，无需重新购买");
                    }else if(a==6){
                        ToastUtil.showShortToast(MyBasicInformationActivity.this,"您未绑定手机号码，请您先绑定手机号码");
                    } else if(a==7){
                        ToastUtil.showShortToast(MyBasicInformationActivity.this,"您有订单未确认，请联系客服");
                    } else if(a==8){
                        ToastUtil.showShortToast(MyBasicInformationActivity.this,"请勿重复下单");
                    }
                    else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(MyBasicInformationActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {

                    }
                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(MyBasicInformationActivity.this, getString(R.string.login_parse_exc));
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
                        ToastUtil.showShortToast(MyBasicInformationActivity.this, "连接服务器失败");
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
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress() + "getMemberInformation.do");
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.setConnectTimeout(60 * 1000);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if (a==1) {
                        basicInformation = new BasicInformation();
                        basicInformation.setCreditrecord(json.getString("creditrecord"));
                        basicInformation.setEducation(json.getString("education"));
                        basicInformation.setIdcardbackpath(json.getString("idcardbackpath"));
                        basicInformation.setIdcardfrontpath(json.getString("idcardfrontpath"));
                        basicInformation.setIdcardno(json.getString("idcardno"));
                        basicInformation.setIdtype(json.getString("idtype"));
                        basicInformation.setOccupation(json.getString("occupation"));
                        basicInformation.setTel(json.getString("tel"));
                        basicInformation.setTruename(json.getString("truename"));
                        basicInformation.setIsmodifyInformation(json.getInt("isauthentication"));
                        JSONArray array = json.getJSONArray("creditrecordList");
                        for(int i = 0;i<array.length(); i++){
                            occupation= new Occupation();
                            occupation.id=array.getJSONObject(i).getString("id");
                            occupation.itemcode=array.getJSONObject(i).getString("itemcode");
                            occupation.text=array.getJSONObject(i).getString("text");
                            creditrecordList.add(occupation);
                        }

                        JSONArray array1 = json.getJSONArray("occupationList");
                        for(int i = 0;i<array1.length(); i++){
                            occupation= new Occupation();
                            occupation.id=array1.getJSONObject(i).getString("id");
                            occupation.itemcode=array1.getJSONObject(i).getString("itemcode");
                            occupation.text=array1.getJSONObject(i).getString("text");
                            occupationList.add(occupation);
                        }

                        JSONArray array2 = json.getJSONArray("idtypeList");
                        for(int i = 0;i<array2.length(); i++){
                            occupation= new Occupation();
                            occupation.id=array2.getJSONObject(i).getString("id");
                            occupation.itemcode=array2.getJSONObject(i).getString("itemcode");
                            occupation.text=array2.getJSONObject(i).getString("text");
                            idtypeList.add(occupation);
                        }

                        JSONArray array3 = json.getJSONArray("educationList");
                        for(int i = 0;i<array3.length(); i++){
                            occupation= new Occupation();
                            occupation.id=array3.getJSONObject(i).getString("id");
                            occupation.itemcode=array3.getJSONObject(i).getString("itemcode");
                            occupation.text=array3.getJSONObject(i).getString("text");
                            educationList.add(occupation);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initData(basicInformation);
                            }
                        });
                    } else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(MyBasicInformationActivity.this, LoginActivity.class));
                                finish();
//                                if(TextUtils.isEmpty(SPUtil.getToken())){
//                                    startActivity(new Intent().setClass(DetailActivity.this,LoginActivity.class));
//                                    finish();
//                                }else {
//                                    new AlertDialog.Builder(DetailActivity.this).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    startActivity(new Intent().setClass(DetailActivity.this,LoginActivity.class));
//                                                    finish();
//                                                    dialog.dismiss();
//                                                }
//                                            }).create().show();
//                                }
                            }
                        });
                    }

                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(MyBasicInformationActivity.this, "解析异常");
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
                        ToastUtil.showShortToast(MyBasicInformationActivity.this, "连接服务器失败");
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

    private void commitData(){
        final ProgressDialog loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("正在提交...");
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        JSONObject json = new JSONObject();
        try {
            json.put("token", SPUtil.getToken());
            json.put("idcard",Et_id_card.getText().toString().trim());
            json.put("truename",  Et_name.getText().toString().trim());
//            json.put("tel", Et_phone.getText().toString().trim());
            json.put("occupation", Tv_Occupation.getText().toString());
            json.put("education",  Tv_Occupation.getText().toString());
            json.put("creditrecord", Tv_record.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress() + "saveMemberInformation.do");
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("idcard", Et_id_card.getText().toString().trim());
        requestParams.addParameter("truename", Et_name.getText().toString().trim());
//        requestParams.addParameter("tel", Et_phone.getText().toString().trim());
        requestParams.addParameter("occupation", Tv_Occupation.getText().toString());
//        requestParams.addParameter("education", Tv_Education.getText().toString());
        requestParams.addParameter("creditrecord", Tv_record.getText().toString());
        requestParams.addParameter("idtype", Tv_type.getText().toString());
//        requestParams.addParameter("idcardfront", file);
//        requestParams.addParameter("idcardback", file1);
        requestParams.setConnectTimeout(60 * 1000);

        requestParams.setAsJsonContent(true);
        List<KeyValue> list = new ArrayList<>();
        list.add(new KeyValue("idcardfront", file));
        list.add(new KeyValue("idcardback", file1));
        list.add(new KeyValue("parameters", json.toString()));
        MultipartBody body = new MultipartBody(list, "UTF-8");
        requestParams.setRequestBody(body);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if (a==1) {
                        ToastUtil.showShortToast(MyBasicInformationActivity.this, "提交成功！");
                        if(status == 1){
                            finish();
                        }else {
                            intoOrder();
                        }
                    }  else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(MyBasicInformationActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {
                        ToastUtil.showShortToast(MyBasicInformationActivity.this, jsonObject.getString("message"));
                    }

                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(MyBasicInformationActivity.this, "解析异常");
                        }
                    });

                } finally {
                    if(loadingDialog.isShowing()){
                        loadingDialog.dismiss();
                    }

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(loadingDialog.isShowing()){
                            loadingDialog.dismiss();
                        }
                        ToastUtil.showShortToast(MyBasicInformationActivity.this, "网络连接异常");
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

    private void commitData1(){
        final ProgressDialog loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("正在提交...");
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress() + "saveMemberInformation.do");
        requestParams.addParameter("token", SPUtil.getToken());
//        requestParams.addParameter("tel", Et_phone.getText().toString().trim());
        requestParams.addParameter("occupation", Tv_Occupation.getText().toString());
//        requestParams.addParameter("education", Tv_Education.getText().toString());
        requestParams.addParameter("creditrecord", Tv_record.getText().toString());

//        requestParams.addParameter("idcardfront", file);
//        requestParams.addParameter("idcardback", file1);
        requestParams.setConnectTimeout(60 * 1000);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    JSONObject json=jsonObject.getJSONObject("data");
                    if (a==1) {
                        ToastUtil.showShortToast(MyBasicInformationActivity.this, "提交成功！");
                        if(status == 1){
                            finish();
                        }else{
                            intoOrder();
                        }
                    }  else if(a==-1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(MyBasicInformationActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {
                        ToastUtil.showShortToast(MyBasicInformationActivity.this, jsonObject.getString("message"));
                    }

                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(MyBasicInformationActivity.this, "解析异常");
                        }
                    });

                } finally {
                    if(loadingDialog.isShowing()){
                        loadingDialog.dismiss();
                    }

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(loadingDialog.isShowing()){
                            loadingDialog.dismiss();
                        }
                        ToastUtil.showShortToast(MyBasicInformationActivity.this, "连接服务器失败");
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
