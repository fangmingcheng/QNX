package zgt.com.example.myzq.model.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.HttpResult;
import zgt.com.example.myzq.bean.Review;
import zgt.com.example.myzq.model.common.fragment.ClassListFragment;
import zgt.com.example.myzq.model.common.fragment.HomeFragment;
import zgt.com.example.myzq.model.common.fragment.InformationFragment;
import zgt.com.example.myzq.model.common.fragment.MyFragment;
import zgt.com.example.myzq.model.common.fragment.QuotationFragment;
import zgt.com.example.myzq.model.common.home.ReviewDetailActivity;
import zgt.com.example.myzq.utils.CommonUtil;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;



public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener{

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    @BindView(R.id.fragment_container)
    FrameLayout mFrameLayout;

    HomeFragment homeFragment;
    MyFragment myFragment;
    QuotationFragment quotationFragment;
    InformationFragment informationFragment;
    ClassListFragment classListFragment;

    private static HttpResult result;
    private long exitTime;
    private int index=0;
    private int currentIndex;

    private static final String HOMEPAGE_FRAGMENT_KEY = "homepageFragment";
    private static final String QUOTATION_FRAGMENT_KEY = "quotationFragment";
    private static final String INFORMATION_FRAGMENT_KEY = "informationFragment";
    private static final String CLASSROOM_FRAGMENT_KEY = "classListFragment";
    private static final String MY_FRAGMENT_KEY = "myFragment";

    private List<Review> list = new ArrayList<>();

    int status = 0;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initToolBar() {

    }


    @Override
    protected void onResume() {
        refreshToken();
        clientReview();
        super.onResume();
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        //得到当前界面的装饰视图
//        if(Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            //设置让应用主题内容占据状态栏和导航栏
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            //设置状态栏和导航栏颜色为透明
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
////            getWindow().setNavigationBarColor(Color.TRANSPARENT);
//        }
        if(savedInstanceState != null){
            homeFragment = (HomeFragment) getSupportFragmentManager().getFragment(savedInstanceState, HOMEPAGE_FRAGMENT_KEY);
            informationFragment = (InformationFragment) getSupportFragmentManager().getFragment(savedInstanceState, INFORMATION_FRAGMENT_KEY);
            myFragment = (MyFragment) getSupportFragmentManager().getFragment(savedInstanceState, MY_FRAGMENT_KEY);
            classListFragment = (ClassListFragment) getSupportFragmentManager().getFragment(savedInstanceState, CLASSROOM_FRAGMENT_KEY);
            quotationFragment=(QuotationFragment) getSupportFragmentManager().getFragment(savedInstanceState, QUOTATION_FRAGMENT_KEY);
//            savedInstanceState.remove("android:support:fragments");
//            savedInstanceState.remove("android:fragments");
        }
        status = getIntent().getIntExtra("status",0);
//        StatusBarUtil.statusBarLightMode(this);
        setDefaultFragment();
        InitNavigationBar();
        checkUpdate();
        if(!SPUtil.isRead_1()) {
            creataDailog();
        }

    }

    private void creataDailog(){
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.my_dialog_layout, null);
        final TextView Tv_content = (TextView) view.findViewById(R.id.Tv_content);
        final TextView Tv_disagree = (TextView) view.findViewById(R.id.Tv_disagree);
        final TextView Tv_agree = (TextView) view.findViewById(R.id.Tv_agree);
        final Dialog dialog = new Dialog(MainActivity.this, R.style.style_dialog);

        Tv_content.setMovementMethod(LinkMovementMethod.getInstance());
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();

        Tv_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDisAgreeDialog();
                dialog.cancel();
            }
        });

        Tv_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.getConfigSharedPreferences().edit().putBoolean("isRead_1", true).commit();
                dialog.cancel();
            }
        });

//        Window window = dialog.getWindow();
////        window.setWindowAnimations(R.style.dialog_animation);
//        window.getDecorView().setPadding(0, 0, 0, 0);
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = 600;//WindowManager.LayoutParams.MATCH_PARENT
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(lp);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        Log.e("TAG",lp.height+"");
        lp.width = (int)(display.getWidth()*0.9); //设置宽度
        lp.height =  (int)(display.getHeight()*0.6);
        Log.e("TAG",lp.height+"");
        Log.e("TAG",display.getHeight()+"");
        dialog.getWindow().setAttributes(lp);
    }

    private void createDisAgreeDialog(){
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.disagree_dialog_layout, null);
        final TextView Tv_content = (TextView) view.findViewById(R.id.Tv_content);
        final TextView Tv_disagree = (TextView) view.findViewById(R.id.Tv_disagree);
        final TextView Tv_agree = (TextView) view.findViewById(R.id.Tv_agree);
        final Dialog dialog = new Dialog(MainActivity.this, R.style.style_dialog);
        Tv_content.setMovementMethod(LinkMovementMethod.getInstance());
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();

        Tv_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finishAll();
               dialog.cancel();
            }
        });

        Tv_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.getConfigSharedPreferences().edit().putBoolean("isRead_1", true).commit();
                dialog.cancel();
            }
        });
    }


    private void checkUpdate() {
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress() + "getNewVersion.do");
        requestParams.setConnectTimeout(60 * 1000);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if(a==1){
                        JSONObject json=jsonObject.getJSONObject("data");
                        int client=json.getInt("client");
                        if(client==1){
                            String url=json.getString("url");
                            String ver=json.getString("ver");
                            int currentCode = CommonUtil.getVersionCode();
                            int serverCode =json.getInt("code");
                            int isforced =json.getInt("isforced");
                            if(serverCode>currentCode){
                                Gxdialogs(url,ver,isforced);
                            }
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }

    protected void Gxdialogs(String url,String ver,int index) {
        if(index ==1){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("版本更新")
                    .setMessage("发现新的app版本，请及时更新")
                    .setCancelable(false)
                    .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downLoadNewApk(url, "正在下载"+ver+"版本",ver);
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }else {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("版本更新")
                    .setMessage("发现新的app版本，请及时更新")
                    .setCancelable(false)
                    .setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downLoadNewApk(url, "正在下载"+ver+"版本",ver);
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }

    }

    private void downLoadNewApk(String apkUrl, String msg,String ver) {
        final ProgressDialog downloadApkDialog = new ProgressDialog(this);
        downloadApkDialog.setTitle("正在更新");
        downloadApkDialog.setMessage(msg);
        downloadApkDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        downloadApkDialog.setCancelable(false);
        downloadApkDialog.show();
        RequestParams requestParams = new RequestParams(apkUrl);
        requestParams.setSaveFilePath(Environment.getExternalStorageDirectory() + "/update/QNX_"+ver+".apk");
        requestParams.setAutoRename(false);
        x.http().post(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
                //网络请求之前回调
            }
            @Override
            public void onStarted() {
                //网络请求开始的时候回调
            }
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                //下载的时候不断回调的方法
                if (isDownloading) {
                    int progress = (int) ((current*100 )/ total);
                    downloadApkDialog.setProgress(progress);
                }
            }
            @Override
            public void onSuccess(File result) {
                //apk下载完成后，调用系统的安装方法
                downloadApkDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //判断是否是AndroidN以及更高的版本
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
//
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(MainActivity.this, "zgt.com.example.myzq.fileProvider", result);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");

                } else {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
                }
                SPUtil.getConfigSharedPreferences().edit().putBoolean("isRead", false).commit();
                startActivity(intent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                downloadApkDialog.dismiss();
                ToastUtil.showShortToast(MainActivity.this, ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (homeFragment != null) {
            getSupportFragmentManager().putFragment(outState, INFORMATION_FRAGMENT_KEY, homeFragment);
        }
        if (informationFragment != null) {
            getSupportFragmentManager().putFragment(outState, INFORMATION_FRAGMENT_KEY, informationFragment);
        }
        if (myFragment != null) {
            getSupportFragmentManager().putFragment(outState, MY_FRAGMENT_KEY, myFragment);
        }
        if (classListFragment != null) {
            getSupportFragmentManager().putFragment(outState, CLASSROOM_FRAGMENT_KEY, classListFragment);
        }
        if (quotationFragment != null) {
            getSupportFragmentManager().putFragment(outState, QUOTATION_FRAGMENT_KEY, quotationFragment);
        }

    }

    private void InitNavigationBar() {
        TextBadgeItem badgeItem = new TextBadgeItem ();
        badgeItem.setHideOnSelect(false)
                .setText("10")
                .setBorderWidth(0);
        ShapeBadgeItem mShapeBadgeItem = new ShapeBadgeItem();
        mShapeBadgeItem.setGravity(Gravity.BOTTOM )
                .setHideOnSelect(false);
        mBottomNavigationBar.setTabSelectedListener(this);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.nav_home, "首页").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.btn_zx, "资讯").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.btn_kt, "课堂").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.btn_hq, "行情").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.btn_wd, "我的").setActiveColorResource(R.color.colorPrimary))
                .setFirstSelectedPosition(index)
                .initialise();
        setBottomNavigationItem(6,21);
    }

    private void setBottomNavigationItem(int space, int imgLen) {
        float contentLen = 36;
        Class barClass = mBottomNavigationBar.getClass();
        Field[] fields = barClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            if (field.getName().equals("mTabContainer")) {
                try { //反射得到 mTabContainer
                    LinearLayout mTabContainer = (LinearLayout) field.get(mBottomNavigationBar);
                    for (int j = 0; j < mTabContainer.getChildCount(); j++) {
                        //获取到容器内的各个 Tab
                        View view = mTabContainer.getChildAt(j);
                        view.setPadding(0,15,0,0);
                        //获取到Tab内的各个显示控件
                        // 获取到Tab内的文字控件
                        TextView labelView = (TextView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title);
                        //计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
                        labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (Math.sqrt(2) * (contentLen - imgLen - space)));
                        //获取到Tab内的图像控件
                        ImageView iconView = (ImageView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
                        //设置图片参数，其中，MethodUtils.dip2px()：换算dp值
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) DensityUtil.dip2px( imgLen), (int) DensityUtil.dip2px(imgLen));
                        params.gravity = Gravity.CENTER;
                        iconView.setLayoutParams(params);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onTabSelected(int position) {
        Log.d("onTabSelected", "onTabSelected: " + position);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                currentIndex=0;
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fragment_container,homeFragment);
                }
                hideFragment(transaction);
                transaction.show(homeFragment);

                break;
            case 1:
                currentIndex=1;
                if (informationFragment == null) {
                    informationFragment = new InformationFragment();
                    transaction.add(R.id.fragment_container,informationFragment);
                }
                hideFragment(transaction);
                transaction.show(informationFragment);
                break;
            case 2:
                currentIndex=2;
                if (classListFragment == null) {
                    classListFragment = new ClassListFragment();
                    transaction.add(R.id.fragment_container,classListFragment);
                }
                hideFragment(transaction);
                transaction.show(classListFragment);
                break;
            case 3:
                currentIndex=3;
                if (quotationFragment == null) {
                    quotationFragment = new QuotationFragment();
                    transaction.add(R.id.fragment_container,quotationFragment);
                }
                hideFragment(transaction);
                transaction.show(quotationFragment);
                break;
            case 4:
                currentIndex=4;
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    transaction.add(R.id.fragment_container,myFragment);
                }
                hideFragment(transaction);
                transaction.show(myFragment);
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction){
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (informationFragment != null) {
           transaction.hide(informationFragment);
        }
        if (classListFragment != null) {
            transaction.hide(classListFragment);
        }
        if (quotationFragment != null) {
            transaction.hide(quotationFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    @Override
    public void onTabUnselected(int position) {
        Log.d("onTabUnselected", "onTabUnselected: " + position);
    }

    @Override
    public void onTabReselected(int position) {
        Log.d("onTabReselected", "onTabReselected: " + position);
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    /**
     * 双击退出App
     */
    public void exitApp() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            ToastUtil.showShortToast(this, "再按一次退出");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    private void clientReview(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"returnVisit.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        list.clear();
                        JSONObject json=jsonObject.getJSONObject("data");
                        JSONArray jsonArray=json.getJSONArray("listp");
                        for (int i=0;i<jsonArray.length();i++){
                            Review review = new Review();
                            review.setName(jsonArray.getJSONObject(i).getString("name"));
                            review.setUrl(jsonArray.getJSONObject(i).getString("url"));
                            list.add(review);
                        }
                        if(list.size()>0){
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("客户回访")
                                    .setMessage("您有一份客户回访，请查阅")
                                    .setCancelable(false)
                                    .setPositiveButton("立即查阅", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent().setClass(MainActivity.this, ReviewDetailActivity.class).putExtra("list",(Serializable) list).putExtra("index",0));
                                            dialog.dismiss();
                                        }
                                    })
                                    .create().show();
                        }
                    } else if(a==-1){

                    }else {

                    }
                } catch (JSONException e) {

                } finally {

                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ToastUtil.showShortToast(MainActivity.this, "网络连接异常");
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

    private void refreshToken(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"refreshToken.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {

                    } else if(a==-1){

                    }else {

                    }
                } catch (JSONException e) {

                } finally {

                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ToastUtil.showShortToast(MainActivity.this, "网络连接异常");
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


    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if(result==null) {

            if(status == 0){
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fragment_container,homeFragment);
                }
                hideFragment(transaction);
                transaction.show(homeFragment);
                transaction.commit();
                index=0;
                currentIndex=0;
            }else if(status == 1){
                if (informationFragment == null) {
                    informationFragment = new InformationFragment();
                    transaction.add(R.id.fragment_container,informationFragment);
                }
                hideFragment(transaction);
                transaction.show(informationFragment);
                transaction.commit();
                index=1;
                currentIndex=1;
            }else if(status == 2){
                if (classListFragment == null) {
                    classListFragment = new ClassListFragment();
                    transaction.add(R.id.fragment_container,classListFragment);
                }
                hideFragment(transaction);
                transaction.show(classListFragment);
                transaction.commit();
                index=2;
                currentIndex=2;
            }
//            homeFragment = new HomeFragment();
//            transaction.replace(R.id.fragment_container, homeFragment);
//            transaction.commit();


        }else {
//            quotationFragment = new QuotationFragment();
//            transaction.replace(R.id.fragment_container, quotationFragment);
//            transaction.commit();
            if (quotationFragment == null) {
                quotationFragment = new QuotationFragment();
                transaction.add(R.id.fragment_container,quotationFragment);
            }
            hideFragment(transaction);
            transaction.show(quotationFragment).commit();
            index=3;
            currentIndex=3;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(currentIndex==3){
            quotationFragment.clickBack(keyCode, event);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public HttpResult getUrl(){
        return result;
    }
    public void setResult(){
         result=null;
    }
}
