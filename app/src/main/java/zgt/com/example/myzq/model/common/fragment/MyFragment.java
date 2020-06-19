package zgt.com.example.myzq.model.common.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseFragment;
import zgt.com.example.myzq.bean.Person_center;
import zgt.com.example.myzq.model.common.custom_view.MyImageView;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.model.common.personal_center.AboutSystemActivity;
import zgt.com.example.myzq.model.common.personal_center.Change_PasswordActivity;
import zgt.com.example.myzq.model.common.personal_center.My_msgActivity;
import zgt.com.example.myzq.model.common.personal_center.Personal_centerActivity;
import zgt.com.example.myzq.model.common.personal_center.Purchase_recordActivity;
import zgt.com.example.myzq.model.common.personal_center.SuggestionActivity;
import zgt.com.example.myzq.model.common.personal_center.basic.MyBasicInformationActivity;
import zgt.com.example.myzq.model.common.personal_center.mycourse.MyCoursesActivity;
import zgt.com.example.myzq.utils.CleanDataUtils;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends BaseFragment {

    @BindView(R.id.Iv_head)
    MyImageView Iv_head;//头像

    @BindView(R.id.Tv_name)
    TextView Tv_name;//姓名
    @BindView(R.id.Tv_member_time)
    TextView Tv_member_time;//购买记录数量

    @BindView(R.id.Tv_phnoe)
    TextView Tv_phnoe;//电话


    @BindView(R.id.Tv_size)
    TextView Tv_size;//电话

//    @BindView(R.id.switch2)
//    Switch switch2;//
//
//    @BindView(R.id.switch_system)
//    Switch switch_system;//

    private ProgressDialog loadDialog ;
    private Person_center person_center;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(getActivity());//黑色
        try {
            String totalCacheSize = CleanDataUtils.getTotalCacheSize(getContext());
            Tv_size.setText(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }).start();
    }

    public MyFragment() {
        // Required empty public constructor
    }

    @OnClick({R.id.Iv_customer,R.id.Rl_personal,R.id.Rl_change_password,R.id.Rl_purchase,R.id.Rl_order,R.id.Rl_message,R.id.Rl_cache,R.id.Rl_about,R.id.Li_follow,R.id.Ll_my_course,R.id.Rl_basic})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_customer://客服
                startActivity(new Intent().setClass(getActivity(), AboutSystemActivity.class));
                break;
            case R.id.Rl_purchase://购买记录
                startActivity(new Intent().setClass(getActivity(), Purchase_recordActivity.class));
                break;
            case R.id.Rl_personal://个人资料
                startActivity(new Intent().setClass(getActivity(), Personal_centerActivity.class));
                break;
            case R.id.Rl_basic://基本资料
                startActivity(new Intent().setClass(getActivity(), MyBasicInformationActivity.class).putExtra("status",1));
                break;
            case R.id.Rl_change_password://修改密码
                startActivity(new Intent().setClass(getActivity(), Change_PasswordActivity.class));
                break;
//            case R.id.Rl_test://风险评测
//                startActivity(new Intent().setClass(getActivity(), RiskTestActivity.class).putExtra("status",1));
//                break;
            case R.id.Rl_order://投诉建议
                startActivity(new Intent().setClass(getActivity(), SuggestionActivity.class));
                break;
            case R.id.Rl_message://消息通知
                startActivity(new Intent().setClass(getActivity(), My_msgActivity.class).putExtra("status",person_center.getGetuistatus()));
                break;
            case R.id.Rl_cache://缓存
                try {
                    loadDialog = new ProgressDialog(getActivity());
                    loadDialog.setMessage("正在清理缓存……");
                    loadDialog.setCancelable(false);
                    loadDialog.show();
                    CleanDataUtils.clearAllCache(getContext());
                    String size = CleanDataUtils.getTotalCacheSize(getContext());
                    Tv_size.setText(size);
                    if(loadDialog.isShowing()){
                        loadDialog.cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.Rl_about://关于
                startActivity(new Intent().setClass(getActivity(), AboutSystemActivity.class));
                break;

            case R.id.Li_follow:
//                startActivity(new Intent().setClass(getActivity(), MyconcernActivity.class));
                break;
            case R.id.Ll_my_course:
                startActivity(new Intent().setClass(getActivity(), MyCoursesActivity.class));
                break;
        }
    }
    private void setData(){
        if(TextUtils.isEmpty(person_center.getHeadimg())){
            Iv_head.setImageResource(R.drawable.replace);
        }else {
            Iv_head.setImageURL(person_center.getHeadimg());
        }
        Tv_name.setText(person_center.getNickname());
        Tv_phnoe.setText("手机号："+person_center.getMobile());
        Tv_member_time.setText(person_center.getOrderNum()+"");
    }

    private void setStatus(int status){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"updateGetuistatus.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        requestParams.addParameter("getuistatus", status);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        if(getActivity()==null){
                            return;
                        }

                    } else if(a==-1){
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//
                                startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
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
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"personalCenter.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        person_center=new Person_center();
                        person_center.setCity(json.getString("city"));
                        person_center.setTruename(json.getString("truename"));
                        person_center.setUsername(json.getString("username"));
                        person_center.setProvincename(json.getString("provincename"));
                        person_center.setHeadimg(json.getString("headimg"));
                        person_center.setCityname(json.getString("cityname"));
                        person_center.setOrderNum(json.getInt("orderNum"));
                        person_center.setEmail(json.getString("email"));
//                        person_center.setEnddate(json.getString("enddate"));
                        person_center.setMobile(json.getString("mobile"));
                        person_center.setMysign(json.getString("mysign"));
                        person_center.setNickname(json.getString("nickname"));
                        person_center.setSex(json.getInt("sex"));
//                        person_center.setStartdate(json.getString("startdate"));
//                        person_center.setTypename(json.getString("typename"));
                        person_center.setGetuistatus(json.getInt("getuistatus"));
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               setData();
                            }
                        });
                    } else if(a==-1){
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//
                                startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
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
