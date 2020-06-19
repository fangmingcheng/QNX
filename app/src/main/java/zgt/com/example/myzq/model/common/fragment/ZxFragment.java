package zgt.com.example.myzq.model.common.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseFragment;
import zgt.com.example.myzq.bean.stock.ZXStock;
import zgt.com.example.myzq.model.common.adapter.stock.ZXstockAdapter;
import zgt.com.example.myzq.model.common.home.h5.H5Activity;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.SpaceItemDecoration;
import zgt.com.example.myzq.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZxFragment extends BaseFragment {

    @BindView(R.id.Tv_SS)
    TextView Tv_SS;
    @BindView(R.id.Tv_increase)
    TextView Tv_increase;
    @BindView(R.id.Tv_index)
    TextView Tv_index;

    @BindView(R.id.Tv_SZ)
    TextView Tv_SZ;
    @BindView(R.id.Tv_increase_sz)
    TextView Tv_increase_sz;
    @BindView(R.id.Tv_index_sz)
    TextView Tv_index_sz;

    @BindView(R.id.Tv_CYB)
    TextView Tv_CYB;
    @BindView(R.id.Tv_increase_cyb)
    TextView Tv_increase_cyb;
    @BindView(R.id.Tv_index_cyb)
    TextView Tv_index_cyb;

    @BindView(R.id.Tv_cancle)
    TextView Tv_cancle;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    @BindView(R.id.Ll_no_zixuan)
    LinearLayout Ll_no_zixuan;

    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.image1)
    ImageView image1;


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private LinearLayoutManager linearLayoutManager;
    private ZXstockAdapter adapter;

    private List<ZXStock> list = new ArrayList<>();
    private List<ZXStock> zxStockList = new ArrayList<>();
    private ZXStock zxStock;

    private List<String> list1 = new ArrayList<>();
//    String[] strings ;
    String stocks = "";



    private int status = 0;//1为按最新价降序，2为最新价升序，3为涨幅降序，4为涨幅升序


    private Timer mTimer;

    public ZxFragment() {
        // Required empty public constructor
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
        }else {
//            getZXStock();
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_zx;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        initRecyclerview();
        getToken();
//        mTimer = new Timer();
//        mTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                getData();
//            }
//        }, 3*1000, 3*1000);


    }

    private void initRecyclerview(){
        linearLayoutManager = new LinearLayoutManager(getActivity());
        //调整RecyclerView的排列方向
//        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        //设置item间距，30dp
        recyclerview.addItemDecoration(new SpaceItemDecoration(10));
        adapter=new ZXstockAdapter(getActivity(),list);
        recyclerview.setAdapter(adapter);
        recyclerview.setNestedScrollingEnabled(false);//NestedScrollView嵌套RecyclerView卡顿解决办法：
        adapter.setOnItemClickListener(new ZXstockAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                int position = recyclerview.getChildAdapterPosition(view);
                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url","https://ze3oy5f3q.lightyy.com/index.html#/quote?code="+list.get(position).getStockcode()));
//                startActivity(new Intent().setClass(getActivity(), CourseDetailActivity.class).putExtra("uuid",recommendedtodays.get(position).getUuid()).putExtra("index",1));
            }

            @Override
            public void ondelect(View view) {
                int position = recyclerview.getChildAdapterPosition(view);
                createDisAgreeDialog(position);
            }
        });
    }

    private void createDisAgreeDialog(int index){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.disagree_dialog_zx, null);
        final TextView Tv_content = (TextView) view.findViewById(R.id.Tv_content);
        final TextView Tv_disagree = (TextView) view.findViewById(R.id.Tv_disagree);
        final TextView Tv_agree = (TextView) view.findViewById(R.id.Tv_agree);
        final Dialog dialog = new Dialog(getActivity(), R.style.style_dialog);
        Tv_content.setMovementMethod(LinkMovementMethod.getInstance());
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();

        Tv_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        Tv_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(index);
                dialog.cancel();
            }
        });
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        Log.e("TAG",lp.height+"");
        lp.width = (int)(display.getWidth()*0.7); //设置宽度
//        lp.height =  (int)(display.getHeight()*0.5);
        Log.e("TAG",lp.height+"");
        Log.e("TAG",display.getHeight()+"");
        dialog.getWindow().setAttributes(lp);
    }

    private void sort(int status){
        if(status == 1){
            Collections.sort(list, new Comparator<ZXStock>() {
                public int compare(ZXStock o1, ZXStock o2) {

                    // 按照学生的年龄进行降序排列
                    if (o1.getPrice() > o2.getPrice()) {
                        return -1;
                    }
                    if (o1.getPrice() == o2.getPrice()) {
                        return 0;
                    }
                    return 1;
                }
            });
        }else if(status ==2){
            Collections.sort(list, new Comparator<ZXStock>() {
                public int compare(ZXStock o1, ZXStock o2) {

                    // 按照学生的年龄进行降序排列
                    if (o1.getPrice() > o2.getPrice()) {
                        return 1;
                    }
                    if (o1.getPrice() == o2.getPrice()) {
                        return 0;
                    }
                    return -1;
                }
            });
        }else if(status ==3){
            Collections.sort(list, new Comparator<ZXStock>() {
                public int compare(ZXStock o1, ZXStock o2) {

                    // 按照学生的年龄进行降序排列
                    if (o1.getChg() > o2.getChg()) {
                        return -1;
                    }
                    if (o1.getChg() == o2.getChg()) {
                        return 0;
                    }
                    return 1;
                }
            });
        } else if(status ==4){
            Collections.sort(list, new Comparator<ZXStock>() {
                public int compare(ZXStock o1, ZXStock o2) {

                    // 按照学生的年龄进行降序排列
                    if (o1.getChg() > o2.getChg()) {
                        return 1;
                    }
                    if (o1.getChg() == o2.getChg()) {
                        return 0;
                    }
                    return -1;
                }
            });
        }

    }



    @OnClick({R.id.Ll_SS,R.id.Ll_SZ,R.id.Ll_CYB,R.id.Tv_add,R.id.Rl_chg,R.id.Rl_price,R.id.Tv_cancle,R.id.Bt_add})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.Ll_SS://上证
                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url","https://ze3oy5f3q.lightyy.com/index.html#/quote?code=000001.SS"));

                break;
            case R.id.Ll_SZ://深证
                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url","https://ze3oy5f3q.lightyy.com/index.html#/quote?code=399001.SZ"));

                break;
            case R.id.Ll_CYB://创业板
                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url","https://ze3oy5f3q.lightyy.com/index.html#/quote?code=399006.SZ"));
//                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url", "https://je7o2az1x.lightyy.com/index.html?p=hsjy_1189&h=0&tg=_blank").putExtra("title", "数据选股"));
                break;
//            case R.id.Ll_zx://资讯
//                startActivity(new Intent().setClass(getActivity(), InformationActivity.class));
////                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url", "https://je7o2az1x.lightyy.com/index.html?p=hsjy_1189&h=0&tg=_blank").putExtra("title", "数据选股"));
//                break;
//            case R.id.Ll_yb://研报
//                startActivity(new Intent().setClass(getActivity(), ReseaRchreportListActivity.class));
//                break;
            case R.id.Tv_add://添加自选
                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url","https://ze3oy5f3q.lightyy.com/#/search"));
                break;
            case R.id.Bt_add://添加自选
                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url","https://ze3oy5f3q.lightyy.com/#/search"));
//                startActivity(new Intent().setClass(getActivity(), H5Activity.class).putExtra("url", "https://je7o2az1x.lightyy.com/index.html?p=hsjy_1189&h=0&tg=_blank").putExtra("title", "数据选股"));
                break;
            case R.id.Rl_price:
                Tv_cancle.setText("取消排序");
                Tv_cancle.setTextColor(Color.parseColor("#037bff"));
                if(status != 1){
                    status = 1;
                    sort(status);
                    image.setImageResource(R.mipmap.btn_shang);
                    image1.setImageResource(R.mipmap.btn_wu);
                }else {
                    status = 2;
                    sort(status);
                    image.setImageResource(R.mipmap.btn_xia);
                    image1.setImageResource(R.mipmap.btn_wu);
                }

                adapter.notifyDataSetChanged();
                break;
            case R.id.Rl_chg:
                Tv_cancle.setText("取消排序");
                Tv_cancle.setTextColor(Color.parseColor("#037bff"));
                if(status != 3){
                    status = 3;
                    sort(status);
                    image.setImageResource(R.mipmap.btn_wu);
                    image1.setImageResource(R.mipmap.btn_shang);
                }else {
                    status = 4;
                    sort(status);
                    image.setImageResource(R.mipmap.btn_wu);
                    image1.setImageResource(R.mipmap.btn_xia);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.Tv_cancle:
                if("取消排序".equals(Tv_cancle.getText().toString())){
                    status =0;
                    Tv_cancle.setText("我的自选");
                    Tv_cancle.setTextColor(Color.parseColor("#333333"));
                    sort(0);
                    image1.setImageResource(R.mipmap.btn_wu);
                    image.setImageResource(R.mipmap.btn_wu);
                    list.clear();
                    getStockData();
                }

//                sort();
//                adapter.notifyDataSetChanged();
                break;

        }
    }

    private void getToken(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"getHstoken.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        JSONObject json = jsonObject.getJSONObject("data");
                        SPUtil.getLoginSharedPreferences().edit().putString("access_token", json.getString("hstoken")).commit();
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTimer = new Timer();
                                mTimer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        getData();
                                    }
                                }, 0, 5*1000);
                            }
                        });

                    } else if(a==-1){
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        });

                    }else {
                        if(getActivity()==null){
                            return;
                        }
                        final String msg=jsonObject.getString("message");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(getActivity(), msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    if(getActivity()==null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            ToastUtil.showShortToast(getActivity(), getString(R.string.login_parse_exc));
                        }
                    });
                } finally {
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(getActivity()==null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(getActivity(), "网络连接异常");
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
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"GetZiXuan.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        stocks="";
                        list1.clear();
//                        list.clear();
                        JSONObject json = jsonObject.getJSONObject("data");
                        String stkLabels= json.getString("stkLabels");
                        String[] strings  = stkLabels.split("\\|");
                        for (int i =0 ;i<strings.length ;i++){
                            if(TextUtils.isEmpty(strings[i])){
                                continue;
                            }
                            list1.add(strings[i]);
                            stocks+=","+strings[i];
                        }
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mTimer = new Timer();
//                                mTimer.schedule(new TimerTask() {
//                                    @Override
//                                    public void run() {
                                        getStockData();
//                                    }
//                                }, 3*1000, 3*1000);

                            }
                        });
                    } else if(a==-1){
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        });

                    }else {
                        if(getActivity()==null){
                            return;
                        }
                        final String msg=jsonObject.getString("message");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(getActivity(), msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    if(getActivity()==null){
                        return;
                    }
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
////                            ToastUtil.showShortToast(getActivity(), getString(R.string.login_parse_exc));
//                        }
//                    });
                } finally {
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(getActivity()==null){
                    return;
                }
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ToastUtil.showShortToast(getActivity(), "网络连接异常");
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

    private void getZXStock(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"GetZiXuan.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        stocks="";
                        list1.clear();
                        JSONObject json = jsonObject.getJSONObject("data");
                        String stkLabels= json.getString("stkLabels");
                        String[] strings = stkLabels.split("\\|");
                        for (int i =0 ;i<strings.length ;i++){
                            list1.add(strings[i]);
                            stocks+=","+strings[i];
                        }
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    } else if(a==-1){
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        });

                    }else {
                        if(getActivity()==null){
                            return;
                        }
                        final String msg=jsonObject.getString("message");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(getActivity(), msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    if(getActivity()==null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            ToastUtil.showShortToast(getActivity(), getString(R.string.login_parse_exc));
                        }
                    });
                } finally {
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(getActivity()==null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(getActivity(), "网络连接异常");
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



    private void getStockData(){
        RequestParams requestParams = new RequestParams("https://open.hscloud.cn/quote/v1/real");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addHeader("Authorization","Bearer "+SPUtil.getAccess_Token());
        requestParams.addParameter("en_prod_code", "000001.SS,399001.SZ,399006.SZ"+stocks);
        requestParams.addParameter("fields", "last_px,px_change_rate,prod_name");
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject json = jsonObject.getJSONObject("data");
                    JSONObject  jsonObject1 =json.getJSONObject("snapshot");
                    JSONArray array = jsonObject1.getJSONArray("000001.SS");
                    Tv_SS.setText(array.get(4)+"");
                    Tv_index.setText(array.get(2)+"");
                    if((double)array.get(3)>0.0){
                        Tv_increase.setText("+"+array.get(3)+"%");
                        Tv_increase.setTextColor(Color.parseColor("#FF4444"));
                        Tv_index.setTextColor(Color.parseColor("#FF4444"));
                    }else if((double)array.get(3)==0.0){
                        Tv_increase.setText(array.get(3)+"%");
                        Tv_increase.setTextColor(Color.parseColor("#333333"));
                        Tv_index.setTextColor(Color.parseColor("#333333"));
                    }else {
                        Tv_increase.setText("-"+array.get(3)+"%");
                        Tv_increase.setTextColor(Color.parseColor("#169433"));
                        Tv_index.setTextColor(Color.parseColor("#169433"));
                    }


                    JSONArray array1 = jsonObject1.getJSONArray("399001.SZ");
                    Tv_SZ.setText(array1.get(4)+"");
                    Tv_index_sz.setText(array1.get(2)+"");
                    if((double)array1.get(3)>0.0){
                        Tv_increase_sz.setText("+"+array1.get(3)+"%");
                        Tv_increase_sz.setTextColor(Color.parseColor("#FF4444"));
                        Tv_index_sz.setTextColor(Color.parseColor("#FF4444"));
                    }else if((double)array1.get(3)==0.0){
                        Tv_increase_sz.setText(array1.get(3)+"%");
                        Tv_increase_sz.setTextColor(Color.parseColor("#333333"));
                        Tv_index_sz.setTextColor(Color.parseColor("#333333"));
                    }else {
                        Tv_increase_sz.setText("-"+array1.get(3)+"%");
                        Tv_increase_sz.setTextColor(Color.parseColor("#169433"));
                        Tv_index_sz.setTextColor(Color.parseColor("#169433"));
                    }

                    JSONArray array3 = jsonObject1.getJSONArray("399006.SZ");
                    Tv_CYB.setText(array3.get(4)+"");
                    Tv_index_cyb.setText(array3.get(2)+"");
                    if((double)array3.get(3)>0.0){
                        Tv_increase_cyb.setText("+"+array3.get(3)+"%");
                        Tv_increase_cyb.setTextColor(Color.parseColor("#FF4444"));
                        Tv_index_cyb.setTextColor(Color.parseColor("#FF4444"));
                    }else if((double)array3.get(3)==0.0){
                        Tv_increase_cyb.setText(array3.get(3)+"%");
                        Tv_increase_cyb.setTextColor(Color.parseColor("#333333"));
                        Tv_index_cyb.setTextColor(Color.parseColor("#333333"));
                    }else {
                        Tv_increase_cyb.setText("-"+array3.get(3)+"%");
                        Tv_increase_cyb.setTextColor(Color.parseColor("#169433"));
                        Tv_index_cyb.setTextColor(Color.parseColor("#169433"));
                    }
                    list.clear();
                    for (int j=0;j<list1.size();j++){
                        JSONArray array2 = jsonObject1.getJSONArray(list1.get(j));

                        zxStock = new ZXStock();
                        zxStock.setStockcode(list1.get(j));
//                        for (int i=0;i<list.size();i++){
//                            if(list1.get(j).equals(list.get(i).getStockcode())){
//                                list.remove(i);
//                                break;
//                            }
//                        }
                        zxStock.setPrice((double)array2.get(2));
                        zxStock.setChg((double)array2.get(3));
                        zxStock.setStockname(array2.get(4).toString());
                        list.add(zxStock);
                    }

                    if(getActivity()==null){
                            return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(list.size()>0){
                                    scrollView.setVisibility(View.VISIBLE);
                                    Ll_no_zixuan.setVisibility(View.GONE);
                                    sort(status);
                                    adapter.notifyDataSetChanged();
                                }else {
                                    scrollView.setVisibility(View.GONE);
                                    Ll_no_zixuan.setVisibility(View.VISIBLE);
                                }


                            }
                        });

                } catch (JSONException e) {
                    if(getActivity()==null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(getActivity(), getString(R.string.login_parse_exc));
                        }
                    });
                } finally {
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(getActivity()==null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getToken();
//                        ToastUtil.showShortToast(getActivity(), "网络连接异常");
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

    private void deleteData(int index){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"DelZiXuan.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("stkLabel", list.get(index).getStockcode());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                     String msg=jsonObject.getString("message");
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        ToastUtil.showShortToast(getActivity(), msg);
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mTimer.cancel();
//                                mTimer = null;
//                                getZXStock();
                                list.remove(index);
                                adapter.notifyDataSetChanged();

                            }
                        });
                    } else if(a==-1){
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        });

                    }else {
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(getActivity(), msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    if(getActivity()==null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            ToastUtil.showShortToast(getActivity(), getString(R.string.login_parse_exc));
                        }
                    });
                } finally {
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(getActivity()==null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(getActivity(), "网络连接异常");
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
